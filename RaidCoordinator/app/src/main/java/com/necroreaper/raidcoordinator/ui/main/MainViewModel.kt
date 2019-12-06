package com.necroreaper.raidcoordinator.ui.main

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import android.util.LruCache
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.absoluteValue

class MainViewModel : ViewModel() {

    private var allGyms = listOf<Gym>()
    private lateinit var db: FirebaseFirestore
    lateinit var curUser: FirebaseUser
    private var gyms = MutableLiveData<List<Gym>>()
    private var raids = MutableLiveData<List<Raids>>()
    private var raidsToGymMap = HashMap<String,HashSet<Raids>>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mainActivity: MainActivity

    private var raidsAttending = HashSet<Raids>()
    private var location = MutableLiveData<Location>()
    fun init(user: FirebaseUser, activity: MainActivity) {
        db = FirebaseFirestore.getInstance()
        if (db == null) {
            Log.d("Error", "XXX FirebaseFirestore is null!")
        }
        curUser = user
        Log.d("username", user.displayName)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mainActivity = activity
    }

    fun setLocation(location: Location){
        this.location.postValue(location)
    }

    fun getLocation(): LiveData<Location>{
        return location
    }

    private fun nearby(gymGeoPoint: GeoPoint, distance: Double): Boolean{

        if (location.value != null) {
            return distanceTo(gymGeoPoint) < distance * 1000
        }

        return false
    }
    private fun distanceTo(gymGeoPoint: GeoPoint): Float{
        var gymLocation = Location("")
        gymLocation.latitude = gymGeoPoint.latitude
        gymLocation.longitude = gymGeoPoint.longitude
        if (location.value != null) {
            return location.value!!.distanceTo(gymLocation)
        }
        return 0f
    }
    fun observeGyms(): LiveData<List<Gym>> {
        return gyms
    }

    fun observeRaids(): LiveData<List<Raids>>{
        return raids
    }
    fun getGyms(){
        db.collection("gyms").get().addOnSuccessListener { document ->
            if (document != null) {
                allGyms = document.mapNotNull {
                    it.toObject(Gym::class.java)
                }
                getGymsNearby()
            } else {
                Log.d("Error", "No such document")
            }
        }.addOnFailureListener { exception ->
            Log.d("Error", "get failed with ", exception)
        }
        getRaids()
    }
    fun getGymsNearby(){
        var nearbyGym = allGyms.filter{
            nearby(it.location!!, 1.0)
        }
        nearbyGym.sortedBy {
            distanceTo(it.location!!)
        }
        gyms.postValue(nearbyGym)
    }

    fun getRaids(specificGym: Gym? = null){
        var collection: Query = db.collection("raids")
        if (specificGym != null)
            collection = collection.whereEqualTo("gym", specificGym.name)
        collection.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.w("Error", "listen:error", firebaseFirestoreException)
                return@addSnapshotListener
            }
            Log.d("Success", "fetch ${querySnapshot!!.documents.size}")
            raids.value = querySnapshot.documents.mapNotNull {
                it.toObject(Raids::class.java)
            }
            mapToGyms()
        }
    }

    private fun mapToGyms() {
        raidsToGymMap.clear()
        raids.value?.forEach {
            if(it.time!!.toDate().after(Date())) {
                var newRaids = setOf(it)
                if (raidsToGymMap.containsKey(it.gym)) {
                    newRaids = raidsToGymMap.get(it.gym)!!
                    newRaids.add(it)
                }
                raidsToGymMap.set(it.gym!!, newRaids.toHashSet())
            }
            raidsAttending.forEach {attending ->
                if (attending == it) {
                    mainActivity.createNotification(it)
                }
            }

        }
    }

    fun getRaidCount(specificGym: Gym): Int {
        var gymRaids = raidsToGymMap.get(specificGym.name)
        return if (gymRaids == null)  0 else gymRaids.size
    }

    fun getGymsRaids(specificGym: Gym): List<Raids>{
        var gymRaids = raidsToGymMap.get(specificGym.name)
        return if (gymRaids == null)  listOf() else gymRaids.toList().asReversed()
    }

    fun submitRaids(gym: Gym, tier: Int, time: Timestamp){
        val players = listOf(curUser.displayName!!)
        val data = hashMapOf(
            "gym" to gym.name,
            "tier" to tier,
            "time" to time,
            "players" to players
        )

        db.collection("raids")
            .add(data)
            .addOnSuccessListener { documentReference ->
                Log.d("Success", "DocumentSnapshot written with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Error", "Error adding document", e)
            }
        raidsAttending.add(Raids(gym.name, players, tier, time))
    }
    fun updatePlayers(raid: Raids){
        db.collection("raids").whereEqualTo("gym", raid.gym)
                                                         .whereEqualTo("time",raid.time)
                                                         .whereEqualTo("tier",raid.tier).get().addOnSuccessListener {
                var playersList = it.documents[0].get("players") as List<String>
                var playersSet = playersList.toHashSet()
                playersSet.add(curUser.displayName!!)
                var data = hashMapOf("players" to playersSet.toList())
                var document = db.collection("raids").document(it.documents[0].id)
                document.set(data, SetOptions.merge())
                raidsAttending.add(raid)
            }

    }


}

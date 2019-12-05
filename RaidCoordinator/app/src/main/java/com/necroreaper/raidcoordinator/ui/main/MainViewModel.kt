package com.necroreaper.raidcoordinator.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids

class MainViewModel : ViewModel() {
    private var nearbyGyms = MutableLiveData<List<Gym>>()

    private lateinit var db: FirebaseFirestore
    private lateinit var curUser: FirebaseUser
    private var gyms = MutableLiveData<List<Gym>>()
    private var raids = MutableLiveData<List<Raids>>()
    private var raidsToGymMap = HashMap<String,HashSet<Raids>>()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mainActivity: MainActivity
    private val LOCATION_REQUEST_CODE = 101

    fun init(user: FirebaseUser, activity: MainActivity) {
        db = FirebaseFirestore.getInstance()
        if (db == null) {
            Log.d("Error", "XXX FirebaseFirestore is null!")
        }
        curUser = user
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity)
        mainActivity = activity
    }


    private fun getNearbyGyms(distance: Double): List<GeoPoint>{
        val lat = 0.0144927536231884
        val lon = 0.0181818181818182

        var latitude = .0
        var longitude = .0
        val permission = ContextCompat.checkSelfPermission(mainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mainActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location : Location? ->
                longitude = location!!.longitude
                latitude = location!!.latitude
            }

        val lowerLat = latitude - (lat * distance)
        val lowerLon = longitude - (lon * distance)

        val upperLat = latitude + (lat * distance)
        val upperLon = longitude + (lon * distance)

        val lowerGeo = GeoPoint(lowerLat, lowerLon)
        val upperGeo = GeoPoint(upperLat, upperLon)
        return listOf(lowerGeo, upperGeo)
    }

    fun observeGyms(): LiveData<List<Gym>> {
        return gyms
    }
    fun getGyms(): LiveData<List<Gym>>{
        val nearbyBounds = getNearbyGyms(1.0)

        db.collection("gyms").whereGreaterThan("location", nearbyBounds[0])
                .get().addOnSuccessListener { document ->
            if (document != null) {
                gyms.value = document.mapNotNull {
                    it.toObject(Gym::class.java)
                }
            } else {
                Log.d("Error", "No such document")
            }
        }.addOnFailureListener { exception ->
                Log.d("Error", "get failed with ", exception)
            }
        getRaids()
        return nearbyGyms
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
            if (specificGym == null)
                mapToGyms()
        }
    }

    private fun mapToGyms() {
        raidsToGymMap = HashMap()
        raids.value?.forEach {
            var newRaids = setOf(it)
            if(raidsToGymMap.containsKey(it.gym)) {
                newRaids = raidsToGymMap.get(it.gym)!!
                newRaids.add(it)
            }
            raidsToGymMap.set(it.gym!!, newRaids.toHashSet())
        }
    }

    fun getRaidCount(specificGym: Gym): Int {
        var gymRaids = raidsToGymMap.get(specificGym.name)
        return if (gymRaids == null)  0 else gymRaids.size
    }

    fun getGymsRaids(specificGym: Gym): List<Raids>{
        var gymRaids = raidsToGymMap.get(specificGym.name)
        Log.d("test", "test")
        gymRaids?.forEach{
            Log.d("test", it.gym)
            Log.d("test", it.tier.toString())
        }
        return if (gymRaids == null)  listOf() else gymRaids.toList()
    }
}

package com.necroreaper.raidcoordinator

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids
import com.necroreaper.raidcoordinator.ui.main.*
import android.net.Uri
import android.content.pm.PackageManager
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.NotificationCompat
import com.necroreaper.raidcoordinator.stringConverters.DateConverter
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 10
    private lateinit var viewModel: MainViewModel
    private val CHANNEL_ID = "19134"
    private val CHANNEL_NAME = "PoGoRaidCoordinator"
    private lateinit var mLocationManager: LocationManager
    private val LOCATION_REQUEST_CODE = 101
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Sets up the authentication
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val mUser = FirebaseAuth.getInstance().currentUser


        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        mUser?.let{
            viewModel.init(it, this)
            if (savedInstanceState == null) {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, MainFragment.newInstance())
                        .commitNow()
            }
            viewModel.getGyms()
        } ?: startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE
            )
        }
        mLocationManager =  getSystemService(LOCATION_SERVICE) as LocationManager

        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 6000L,
            100.0f, mLocationListener)


    }

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            viewModel.setLocation(location)
        }
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    fun setGymInstance(gym: Gym){
        supportFragmentManager.beginTransaction().replace(R.id.container, GymFragment(gym))
            .addToBackStack(null)
            .commit()
    }

    fun setRaidInstance(raid: Raids, gym: Gym){
        supportFragmentManager.beginTransaction().replace(R.id.container, RaidFragment(raid, gym))
            .addToBackStack(null)
            .commit()
    }

    fun setProfile(item: MenuItem): Boolean{
        supportFragmentManager.beginTransaction().replace(R.id.container, UserFragment())
            .addToBackStack(null)
            .commit()
        return true
    }


    //https://stackoverflow.com/questions/6560345/suppressing-google-maps-intent-selection-dialog
    fun setLocationInstance(gym: Gym){
        var gmmIntentUri = Uri.parse("geo:${gym.location!!.latitude},${gym.location!!.longitude}?q=${gym.location!!.latitude},${gym.location!!.longitude}(${gym.name})")
        var mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                viewModel.init(user!!, this)
                viewModel.getGyms()
                supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
    fun createNotification(raid: Raids) {
        //https://stackoverflow.com/questions/47409256/what-is-notification-channel-idnotifications-not-work-in-api-27
        val contentTitle = "Raid Coordinator"
        val message = "There are now ${raid.players!!.size} at ${raid.gym} during ${DateConverter.convertDate(raid.time!!)}"
        val mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = "Notification for change in user"
            mNotificationManager.createNotificationChannel(channel)
        }
        val mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(contentTitle) // title for notification
            .setContentText(message)// message for notification
            .setAutoCancel(true) // clear notification after click
        val intent = Intent(applicationContext, MainActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        mBuilder.setContentIntent(pi)
        mNotificationManager.notify(0, mBuilder.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }
}

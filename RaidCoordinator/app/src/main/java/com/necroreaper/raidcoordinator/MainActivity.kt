package com.necroreaper.raidcoordinator

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids
import com.necroreaper.raidcoordinator.ui.main.*
import android.net.Uri
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 10
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
        // Sets up the authentication
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        val mUser = FirebaseAuth.getInstance().currentUser

        mUser?.let{
            //do your stuff here
        } ?: startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)

        viewModel = ViewModelProviders.of(this)[MainViewModel::class.java]
        viewModel.init(mUser!!, this)
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
    //https://stackoverflow.com/questions/6560345/suppressing-google-maps-intent-selection-dialog
    fun setLocationInstance(gym: Gym){
        val intent = Intent(
            android.content.Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?daddr=0,0%20(Imaginary%20Place)&dirflg=r")
        )
        if (isAppInstalled("com.google.android.apps.maps")) {
            intent.setClassName(
                "com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity"
            )
        }
        startActivity(intent)
    }
    private fun isAppInstalled(uri: String): Boolean {
        val pm = applicationContext.packageManager
        var app_installed = false
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
            app_installed = true
        } catch (e: PackageManager.NameNotFoundException) {
            app_installed = false
        }

        return app_installed
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }

}

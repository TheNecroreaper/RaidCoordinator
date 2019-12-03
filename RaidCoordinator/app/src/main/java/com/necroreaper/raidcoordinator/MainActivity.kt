package com.necroreaper.raidcoordinator

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids
import com.necroreaper.raidcoordinator.gymDatabase.DatabaseHelper
import com.necroreaper.raidcoordinator.ui.main.GymFragment
import com.necroreaper.raidcoordinator.ui.main.MainFragment
import com.necroreaper.raidcoordinator.ui.main.RaidFragment
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 10


    private lateinit var gymDb: SQLiteDatabase
    private lateinit var dbHelper: DatabaseHelper
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


        //Sets up the sqlite database

        dbHelper = DatabaseHelper(this)
        try {
            dbHelper.createDatabase()
        } catch (e: IOException) {
            Log.e("DB", "Fail to create database")
        }
        gymDb = dbHelper.readableDatabase
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

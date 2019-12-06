package com.necroreaper.raidcoordinator.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.UserProfileChangeRequest
import com.necroreaper.raidcoordinator.R
import kotlinx.android.synthetic.main.profile.*

class UserFragment : Fragment(){

    private lateinit var viewModel: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")


        return inflater.inflate(R.layout.profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //Log.d("test",viewModel.curUser.displayName)
        displayName.setText(viewModel.curUser.displayName)
        saveBT.setOnClickListener{
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(displayName.text.toString())
                .build()
            viewModel.curUser?.updateProfile(profileUpdates)
                ?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        displayName.setText(viewModel.curUser.displayName)
                    }
                }
        }
        returnBT.setOnClickListener{
            activity?.onBackPressed()
        }
        signOutBT.setOnClickListener{
            AuthUI.getInstance()
                .signOut(context!!)
                .addOnCompleteListener {
                    val providers = arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build())
                    startActivityForResult(
                        AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                        101)
                }
        }
    }

}
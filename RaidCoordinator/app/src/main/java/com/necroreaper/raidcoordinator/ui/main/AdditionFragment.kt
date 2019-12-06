package com.necroreaper.raidcoordinator.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.Timestamp

import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.R
import kotlinx.android.synthetic.main.raid_addition.*
import java.util.*


class AdditionFragment(gym: Gym) : Fragment() {


    private lateinit var viewModel: MainViewModel
    private var currentGym = gym
    private var tier = 1
    private var hour = Date().hours
    private var minutes = Date().minutes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        return inflater.inflate(R.layout.raid_addition, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        tier = 1
        hour = Date().hours
        minutes = Date().minutes
        var oneBT: Button = tierOne
        var twoBT: Button = tierTwo
        var threeBT: Button = tierThree
        var fourBT: Button = tierFour
        var fiveBT: Button = tierFive
        backArrowAddition.setOnClickListener{
            activity?.onBackPressed()
        }
        oneBT.setOnClickListener{
            tier = 1
        }
        twoBT.setOnClickListener{
            tier = 2
        }
        threeBT.setOnClickListener{
            tier = 3
        }
        fourBT.setOnClickListener{
            tier = 4
        }
        fiveBT.setOnClickListener{
            tier = 5
        }
        raidTimePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            hour = hourOfDay
            minutes = minute
        }
        raidSubmit.setOnClickListener{
            var sameTime = false

            viewModel.getGymsRaids(currentGym).forEach {
                var raidDate = it.time!!.toDate()
                Log.d(raidDate.hours.toString(), raidDate.minutes.toString())
                Log.d(hour.toString(),minutes.toString())
                sameTime = sameTime || (raidDate.hours == hour && raidDate.minutes == minutes && it.tier == tier)
            }
            if (sameTime) {
                Toast.makeText(context, "Raid time already available", Toast.LENGTH_SHORT).show()
                Log.d("test","test")
            }
            else{
                var raidDate = Date()
                raidDate.hours = hour
                raidDate.minutes = minutes
                var raidTimestamp = Timestamp(raidDate)
                viewModel.submitRaids(currentGym, tier, raidTimestamp)
                activity?.onBackPressed()
            }

        }
    }

}
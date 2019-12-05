package com.necroreaper.raidcoordinator.ui.main

import android.app.Dialog
import android.app.DialogFragment
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.adapters.RaidListAdapter
import kotlinx.android.synthetic.main.gym_instance.*
import java.util.*

class GymFragment(gym: Gym) : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var raidAdapter: RaidListAdapter
    private var currentGym = gym

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        raidAdapter = RaidListAdapter(viewModel, activity as MainActivity, currentGym)



        return inflater.inflate(R.layout.gym_instance, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backArrow.setOnClickListener {
            activity?.onBackPressed()
        }
        directionsButton.setOnClickListener {
            (activity as MainActivity).setLocationInstance(currentGym)
        }
        gymName.text = currentGym.name
        raidRecycler.adapter = raidAdapter
        raidRecycler.layoutManager = LinearLayoutManager(context)
        raidAdapter.submitList(viewModel.getGymsRaids(currentGym))
    }
//    fun showTimePickerDialog(v: View) {
//        TimePickerFragment().show(activity!!.supportFragmentManager, "timePicker")
//    }
}
class TimePickerFragment : DialogFragment(), TimePickerDialog.OnTimeSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
    }
}
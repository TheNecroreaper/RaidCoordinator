package com.necroreaper.raidcoordinator.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.dataTypes.Raids
import com.necroreaper.raidcoordinator.adapters.PlayerListAdapter
import com.necroreaper.raidcoordinator.stringConverters.DateConverter
import kotlinx.android.synthetic.main.gym_instance.*
import kotlinx.android.synthetic.main.raid_instance.*

class RaidFragment(raid: Raids, gym: Gym) : Fragment() {


    private lateinit var viewModel: MainViewModel
    private lateinit var playerAdapter: PlayerListAdapter
    private var currentRaid = raid
    private var currentGym = gym

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        playerAdapter = PlayerListAdapter(viewModel)



        return inflater.inflate(R.layout.raid_instance, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        backArrowRaid.setOnClickListener {
            activity?.onBackPressed()
        }
        raidGymName.text = currentGym.name
        directionsButtonRaid.setOnClickListener {
            (activity as MainActivity).setLocationInstance(currentGym)
        }
        raidTime.text = DateConverter.convertDate(currentRaid.time!!)
        playerRecycler.adapter = playerAdapter
        playerRecycler.layoutManager = LinearLayoutManager(context)

        playerAdapter.submitList(currentRaid.players!!.toList())
    }
}
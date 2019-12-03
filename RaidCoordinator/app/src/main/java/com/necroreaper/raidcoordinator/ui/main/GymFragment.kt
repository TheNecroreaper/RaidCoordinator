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
import com.necroreaper.raidcoordinator.adapters.RaidListAdapter
import kotlinx.android.synthetic.main.gym_instance.*

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
        gymName.text = currentGym.name
        raidRecycler.adapter = raidAdapter
        raidRecycler.layoutManager = LinearLayoutManager(context)

        raidAdapter.submitList(currentGym.events.toList())
    }
}
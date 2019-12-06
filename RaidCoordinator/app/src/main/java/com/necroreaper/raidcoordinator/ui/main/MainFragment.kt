package com.necroreaper.raidcoordinator.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.adapters.GymListAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import kotlin.collections.HashSet

class MainFragment : Fragment() {


    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var gymAdapter: GymListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel = activity?.run {
            ViewModelProviders.of(this)[MainViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        gymAdapter = GymListAdapter(viewModel, activity as MainActivity)


        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        gymRecycler.adapter = gymAdapter
        gymRecycler.layoutManager = LinearLayoutManager(context)

        viewModel.getLocation().observe(this, Observer {
            viewModel.getGymsNearby()
        })
        gymSwipeRefresh.setOnRefreshListener {
            viewModel.getGymsNearby()
            gymSwipeRefresh.isRefreshing = false
        }
        viewModel.observeRaids().observe(this, Observer {
            gymAdapter.submitList(viewModel.observeGyms().value)
        })
        viewModel.observeGyms().observe(this, Observer {
            gymAdapter.submitList(it)
        })
        //gymAdapter.submitList(tempList)
    }
}

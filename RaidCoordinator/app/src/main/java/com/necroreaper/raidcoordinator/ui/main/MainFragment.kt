package com.necroreaper.raidcoordinator.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.necroreaper.raidcoordinator.Gym
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.Raids
import com.necroreaper.raidcoordinator.adapters.GymListAdapter
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*
import kotlin.collections.HashSet

class MainFragment : Fragment() {

    var setOnePlayers: Set<String> = setOf("player1", "player2")
    var setTwoPlayers: Set<String> = setOf()
    var setThreePlayers: Set<String> = setOf("testPlayers")
    var setFourPlayers: Set<String> = setOf("")

    var setOneRaids: Set<Raids> = setOf(
        Raids(HashSet(setOnePlayers),
            1,
            Date()
        ),
        Raids(HashSet(setTwoPlayers),
            2,
            Date()
        )
    )
    var setTwoRaids: Set<Raids> = setOf()
    var setThreeRaids: Set<Raids> = setOf(
        Raids(HashSet(setThreePlayers),
            1,
            Date()
        ),
        Raids(HashSet(setFourPlayers),
            2,
            Date()
        )
    )
    var setFourRaids: Set<Raids> = setOf(
        Raids(HashSet(setThreePlayers),
            1,
            Date()
        )
    )

    var tempList: List<Gym> = listOf(
        Gym("Gym1", "Location1", HashSet(setOneRaids), 0),
        Gym("Gym2", "Location2", HashSet(setTwoRaids), 0),
        Gym("Gym3", "Location3", HashSet(setThreeRaids), 0),
        Gym("Gym4", "Location4", HashSet(setFourRaids), 0)
    )


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

        gymAdapter = GymListAdapter(viewModel)


        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        gymRecycler.adapter = gymAdapter
        gymRecycler.layoutManager = LinearLayoutManager(context)
        gymAdapter.submitList(tempList)
    }

}

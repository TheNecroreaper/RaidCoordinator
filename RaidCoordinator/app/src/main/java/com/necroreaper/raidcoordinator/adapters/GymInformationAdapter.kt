package com.necroreaper.raidcoordinator.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.necroreaper.raidcoordinator.Gym
import com.necroreaper.raidcoordinator.ui.main.MainViewModel

class GymListAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Gym, GymListAdapter.VH>(GymDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class GymDiff : DiffUtil.ItemCallback<Gym>() {

        override fun areItemsTheSame(oldItem: Gym, newItem: Gym): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Gym, newItem: Gym): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.location == newItem.location
                    && oldItem.events == newItem.events
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(gym: Gym){

        }
    }
}
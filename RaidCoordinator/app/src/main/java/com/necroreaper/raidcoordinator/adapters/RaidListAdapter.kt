package com.necroreaper.raidcoordinator.adapters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.necroreaper.raidcoordinator.Raids
import com.necroreaper.raidcoordinator.ui.main.MainViewModel

class RaidListAdapter(private val viewModel: MainViewModel)
    : ListAdapter<Raids, RaidListAdapter.VH>(RaidDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class RaidDiff : DiffUtil.ItemCallback<Raids>() {

        override fun areItemsTheSame(oldItem: Raids, newItem: Raids): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: Raids, newItem: Raids): Boolean {
            return oldItem.time == newItem.time
                    && oldItem.tier == newItem.tier
                    && oldItem.players == newItem.players
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {
        fun bind(raid: Raids){

        }
    }
}
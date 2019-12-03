package com.necroreaper.raidcoordinator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.ui.main.MainViewModel

class PlayerListAdapter(private val viewModel: MainViewModel)
    : ListAdapter<String, PlayerListAdapter.VH>(PlayerDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.player, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.getItem(position))
    }

    class PlayerDiff : DiffUtil.ItemCallback<String>() {

        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {

        private val nameTV = v.findViewById<TextView>(R.id.name)

        fun bind(playerName: String){
            nameTV.text = playerName
        }
    }
}
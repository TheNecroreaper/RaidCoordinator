package com.necroreaper.raidcoordinator.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.ui.main.MainViewModel

class GymListAdapter(private val viewModel: MainViewModel, private val activity: MainActivity)
    : ListAdapter<Gym, GymListAdapter.VH>(GymDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.gym, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.getItem(position))
    }

    class GymDiff : DiffUtil.ItemCallback<Gym>() {

        override fun areItemsTheSame(oldItem: Gym, newItem: Gym): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Gym, newItem: Gym): Boolean {
            return oldItem.name == newItem.name
                    && oldItem.location == newItem.location
        }
    }

    inner class VH(v: View) : RecyclerView.ViewHolder(v) {

        private val nameTV = v.findViewById<TextView>(R.id.name)
        private val locationBT = v.findViewById<ImageButton>(R.id.location)
        private val eventCountTV = v.findViewById<TextView>(R.id.eventCount)
        private val layout = v.findViewById<ConstraintLayout>(R.id.gymInstance)

        fun bind(gym: Gym){
            nameTV.text = gym.name
            locationBT.setOnClickListener{
                activity.setLocationInstance(gym)
            }
            eventCountTV.text = viewModel.getRaidCount(gym).toString()
            layout.setOnClickListener{
                activity.setGymInstance(gym)
            }
        }
    }
}
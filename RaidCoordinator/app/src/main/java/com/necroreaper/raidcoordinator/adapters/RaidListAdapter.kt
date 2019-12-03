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
import com.necroreaper.raidcoordinator.MainActivity
import com.necroreaper.raidcoordinator.R
import com.necroreaper.raidcoordinator.dataTypes.Gym
import com.necroreaper.raidcoordinator.dataTypes.Raids
import com.necroreaper.raidcoordinator.stringConverters.DateConverter
import com.necroreaper.raidcoordinator.ui.main.MainViewModel
import java.util.*

class RaidListAdapter(private val viewModel: MainViewModel, private val activity: MainActivity, private val gym: Gym)
    : ListAdapter<Raids, RaidListAdapter.VH>(RaidDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.raid, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(this.getItem(position), gym)
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
        private val tierTV = v.findViewById<TextView>(R.id.tier)
        private val timeTV = v.findViewById<TextView>(R.id.time)
        private val playerCountTV = v.findViewById<TextView>(R.id.playerCount)
        private val goingBT = v.findViewById<ImageButton>(R.id.going)
        private val layout = v.findViewById<ConstraintLayout>(R.id.raidInstance)
//        private val timeLeftTV = v.findViewById<TextView>(R.id.timeLeft)
        fun bind(raid: Raids, gym: Gym){
            tierTV.text = raid.tier.toString()
            timeTV.text = DateConverter.convertDate(raid.time)
//            timeLeftTV.text = timeCompare(raid.time)
            playerCountTV.text = raid.players.size.toString()
            layout.setOnClickListener{
                activity.setRaidInstance(raid, gym)
            }
            goingBT.setOnClickListener{
                //layout.setBackgroundColor()
            }
        }
    }



//    private fun timeCompare (date: Date): String {
//        var curTime = cur
//        var hourDiff =
//    }
}
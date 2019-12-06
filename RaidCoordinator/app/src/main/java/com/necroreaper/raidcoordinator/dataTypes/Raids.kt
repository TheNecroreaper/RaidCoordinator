package com.necroreaper.raidcoordinator.dataTypes

import com.google.firebase.Timestamp
import java.util.*
import kotlin.collections.HashSet

data class Raids(
    val gym: String? = null,
    val players: List<String>? = null,
    val tier: Int? = null,
    val time: Timestamp? = null
){
    override fun equals(other: Any?): Boolean {
        if (other is Raids )
        {
            return gym == other.gym && tier == other.tier && time == other.time
        }
        else
            return false
    }
}
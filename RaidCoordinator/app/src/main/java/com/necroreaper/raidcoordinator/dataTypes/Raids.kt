package com.necroreaper.raidcoordinator.dataTypes

import java.util.*
import kotlin.collections.HashSet

data class Raids (
    val players: HashSet<String>,
    val tier: Int,
    val time: Date
){

}
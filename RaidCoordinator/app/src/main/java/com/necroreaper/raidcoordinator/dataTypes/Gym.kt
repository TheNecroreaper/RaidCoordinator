package com.necroreaper.raidcoordinator.dataTypes

import kotlin.collections.HashSet


data class Gym (
    val name: String,
//    val location: String,
    val longitude: Double,
    val latitude: Double,
    val events: HashSet<Raids>,
    val id: Int
) {

}
package com.necroreaper.raidcoordinator

import java.util.*
import kotlin.collections.HashSet


data class Gym (
    val name: String,
    val location: String,
    val events: HashSet<Raids>,
    val id: Int
) {

}
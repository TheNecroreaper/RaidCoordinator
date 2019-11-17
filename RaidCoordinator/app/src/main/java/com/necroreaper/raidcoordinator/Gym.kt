package com.necroreaper.raidcoordinator

data class Gym (
    val name: String,
    val location: String,
    val events: HashSet<Raids>,
    val id: Int
) {

}
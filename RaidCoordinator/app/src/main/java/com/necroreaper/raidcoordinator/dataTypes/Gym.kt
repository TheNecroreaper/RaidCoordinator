package com.necroreaper.raidcoordinator.dataTypes

import com.google.firebase.firestore.GeoPoint


data class Gym (
    val name: String? = null,
    val location: GeoPoint? = null,
    val id: Int? = null
) {

}
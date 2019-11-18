package com.necroreaper.raidcoordinator.ui.main

import androidx.lifecycle.*
import com.necroreaper.raidcoordinator.Gym

class MainViewModel : ViewModel() {
    private var nearbyGyms = MutableLiveData<List<Gym>>()

    var username: String = "TempName"

    fun getGyms(): LiveData<List<Gym>>{
        return nearbyGyms
    }


}

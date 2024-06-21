package com.neisha.trashhub.view.Pickup

import android.health.connect.datatypes.ExerciseRoute
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MapsViewModel : ViewModel() {
    private val _location = MutableLiveData<ExerciseRoute.Location>()
    val location: LiveData<ExerciseRoute.Location> get() = _location

    fun setLocation(location: ExerciseRoute.Location) {
        _location.value = location
    }
}
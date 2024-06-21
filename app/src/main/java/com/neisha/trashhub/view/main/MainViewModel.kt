package com.neisha.trashhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neisha.trashhub.R
import com.neisha.trashhub.view.adapter.EducationAdapter

class MainViewModel : ViewModel() {

    private val _educationContent = MutableLiveData<List<EducationAdapter.EducationItem>>()
    val educationContent: LiveData<List<EducationAdapter.EducationItem>> get() = _educationContent

    init {
        loadEducationContent()
    }

    private fun loadEducationContent() {
        _educationContent.value = listOf(
            EducationAdapter.EducationItem(
                "Trash Detection Fitur Application",
                "Selamat datang di fitur Deteksi Jenis Sampah",
                R.drawable.image_detectioon
            ),
            EducationAdapter.EducationItem(
                "Trash PickUp Fitur Application",
                "Selamat datang di fitur Trash PickUp!",
                R.drawable.image_pickup
            ),
            EducationAdapter.EducationItem(
                "Trash Article Fitur Application",
                "Selamat datang di fitur Trash Article",
                R.drawable.image_articles
            )
        )
    }
}

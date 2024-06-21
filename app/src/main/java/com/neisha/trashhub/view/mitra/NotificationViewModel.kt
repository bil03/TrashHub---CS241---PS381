package com.neisha.trashhub.view.mitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {

    private val _notificationMessage = MutableLiveData<String>()
    val notificationMessage: LiveData<String>
        get() = _notificationMessage

    init {
        // Load notification details from a repository or any data source
        // Here, I'm setting a dummy message for demonstration
        _notificationMessage.value = "Ada orderan pickUp dari Daffa Satria."
    }
}
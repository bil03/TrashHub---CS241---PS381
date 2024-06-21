package com.neisha.trashhub.view.Profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.pref.UserModel
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    fun getSession(): LiveData<UserModel?> {
        return repository.getSession().asLiveData()
    }

    fun logout(onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                repository.logout()
                onComplete.invoke()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

package com.neisha.trashhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.pref.UserModel
import com.neisha.trashhub.data.pref.response.LoginResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = userRepository.login(email, password)
                _loginResult.value = Result.success(response)
            } catch (e: Exception) {
                _loginResult.value = Result.failure(e)
            } finally {
                _loading.value = false
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            try {
                userRepository.saveSession(user)
            } catch (e: Exception) {
                // Handle error if necessary
            }
        }
    }

    fun getSession(): LiveData<Result<UserModel?>> {
        val resultLiveData = MutableLiveData<Result<UserModel?>>()
        viewModelScope.launch {
            try {
                userRepository.getSession().collect { user ->
                    resultLiveData.value = Result.success(user)
                }
            } catch (e: Exception) {
                resultLiveData.value = Result.failure(e)
            }
        }
        return resultLiveData
    }
}

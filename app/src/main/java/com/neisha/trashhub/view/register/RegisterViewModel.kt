package com.neisha.trashhub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.neisha.trashhub.data.UserRepository
import com.neisha.trashhub.data.pref.response.RegisterErrorResponse
import com.neisha.trashhub.data.pref.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class RegisterViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    fun register(name: String, email: String, password: String, confirmPassword: String, phone: String, mitra: String = "", onResult: (RegisterResponse) -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                val response = userRepository.register(name, email, password, confirmPassword, phone, mitra)
                onResult(response)
            } catch (e: IOException) {
                onResult(RegisterResponse(true, "Network error. Please check your connection.", null))
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, RegisterErrorResponse::class.java)
                val errorMessage = errorBody.message
                onResult(RegisterResponse(true, errorMessage ?: "Unknown error", null))
            } finally {
                _loading.value = false
            }
        }
    }
}

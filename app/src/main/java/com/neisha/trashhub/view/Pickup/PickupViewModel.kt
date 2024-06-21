package com.neisha.trashhub.view.Pickup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.neisha.trashhub.view.response.CreatePickupResponse
import com.neisha.trashhub.view.response.CreateResult

class PickupViewModel : ViewModel() {

    private val _weight = MutableLiveData<String>()
    val weight: LiveData<String> get() = _weight

    private val _phoneNumber = MutableLiveData<String>()
    val phoneNumber: LiveData<String> get() = _phoneNumber

    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    private val _additionalInfo = MutableLiveData<String>()
    val additionalInfo: LiveData<String> get() = _additionalInfo

    private val _apiResponse = MutableLiveData<CreatePickupResponse>()
    val apiResponse: LiveData<CreatePickupResponse> get() = _apiResponse

    fun setWeight(weight: String) {
        _weight.value = weight
    }

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun setAdditionalInfo(additionalInfo: String) {
        _additionalInfo.value = additionalInfo
    }

    fun createPickup() {
        // Simulating an API call with dummy data for demonstration purposes
        val response = CreatePickupResponse(
            createResult = CreateResult(
                photo = "photo_url",
                weight = _weight.value,
                description = _additionalInfo.value,
                lon = "longitude",
                userId = "user_id",
                mitraId = "mitra_id",
                pickupDate = "pickup_date",
                notifUser = "notification_user",
                notifMitra = "notification_mitra",
                id = "id",
                pickupTime = "pickup_time",
                lat = "latitude",
                status = "status"
            ),
            error = false,
            message = "Pickup created successfully"
        )

        _apiResponse.postValue(response)
    }
}

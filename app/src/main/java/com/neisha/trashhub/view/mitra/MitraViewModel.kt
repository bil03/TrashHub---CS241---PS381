package com.neisha.trashhub.view.mitra

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MitraViewModel : ViewModel() {

    private val _order = MutableLiveData<Order>()
    val order: LiveData<Order>
        get() = _order

    init {
        // Load order details from a repository or any data source
        // Here, I'm setting a dummy order for demonstration
        val dummyOrder = Order(
            "Jl. Andam Dewi No. 16",
            "Jl. Andam Dewi No.16, Kubu Marapalam, Kec. Padang Tim., Kota Padang, Sumatera Barat 25142",
            "Daffa Satria",
            "08123456789",
            "Sampah botol plastik.",
            "3Kg"
        )
        _order.value = dummyOrder
    }

    fun rejectOrder() {
        // Logic to reject order
    }

    fun acceptOrder(date: String, time: String) {
        // Logic to accept order with provided date and time
    }
}
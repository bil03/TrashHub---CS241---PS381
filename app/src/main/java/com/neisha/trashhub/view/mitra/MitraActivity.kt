package com.neisha.trashhub.view.mitra

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.neisha.trashhub.R
import com.neisha.trashhub.databinding.ActivityMitraBinding

class MitraActivity : AppCompatActivity() {

    private lateinit var mitraViewModel: MitraViewModel
    private lateinit var binding: ActivityMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mitraViewModel = ViewModelProvider(this).get(MitraViewModel::class.java)

        binding.imgBack.setOnClickListener { finish() }

        binding.btnReject.setOnClickListener {
            mitraViewModel.rejectOrder()
            Toast.makeText(this, "Order rejected", Toast.LENGTH_SHORT).show()
        }

        binding.btnAccept.setOnClickListener {
            val date = binding.etPickupDate.text.toString()
            val time = binding.etPickupTime.text.toString()
            mitraViewModel.acceptOrder(date, time)
            Toast.makeText(this, "Order accepted", Toast.LENGTH_SHORT).show()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        mitraViewModel.order.observe(this, { order ->
            order?.let {
                binding.tvAddress.text = it.address
                binding.tvFullAddress.text = it.fullAddress
                binding.tvOrderedBy.text = it.orderedBy
                binding.tvPhoneNumber.text = it.phoneNumber
                binding.tvDescription.text = it.description
                binding.tvWeight.text = it.weight
            }
        })
    }
}
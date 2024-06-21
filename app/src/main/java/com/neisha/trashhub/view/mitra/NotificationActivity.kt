package com.neisha.trashhub.view.mitra

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.neisha.trashhub.R
import com.neisha.trashhub.databinding.NotificationMitraBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var notificationViewModel: NotificationViewModel
    private lateinit var binding: NotificationMitraBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NotificationMitraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)

        binding.tvCheckOrder.setOnClickListener {
            val intent = Intent(this, MitraActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        notificationViewModel.notificationMessage.observe(this, { message ->
            message?.let {
                binding.tvOrderMessage.text = it
            }
        })
    }
}
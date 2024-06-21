package com.neisha.trashhub.view.splash

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.neisha.trashhub.R
import com.neisha.trashhub.view.register.register_mitra.RegisterMitraActivity
import com.neisha.trashhub.view.register.register_user.RegisterUserActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val btnUser = findViewById<Button>(R.id.btn_login)
        val btnMitra = findViewById<Button>(R.id.btn_register)

        btnUser.setOnClickListener {
            val intent = Intent(this, RegisterUserActivity::class.java)
            startActivity(intent)
        }

        btnMitra.setOnClickListener {
            val intent = Intent(this, RegisterMitraActivity::class.java)
            startActivity(intent)
        }
    }
}

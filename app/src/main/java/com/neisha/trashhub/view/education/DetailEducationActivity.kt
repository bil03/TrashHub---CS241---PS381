package com.neisha.trashhub.view.education

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.neisha.trashhub.R
import com.neisha.trashhub.view.adapter.EducationAdapter

class DetailEducationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_education)

        val educationItem = intent.getParcelableExtra<EducationAdapter.EducationItem>("educationItem")

        if (educationItem != null) {
            findViewById<TextView>(R.id.detailTitleTextView).text = educationItem.title
            findViewById<TextView>(R.id.detailDescriptionTextView).text = educationItem.description

            val detailImageView = findViewById<ImageView>(R.id.detailImageView)
            detailImageView.setImageResource(educationItem.imageResId)
        } else {
            // Handle jika objek educationItem null
            // Contoh: Menampilkan pesan kesalahan
            findViewById<TextView>(R.id.detailTitleTextView).text = "Data tidak tersedia"
        }
    }
}

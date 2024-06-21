package com.neisha.trashhub.view.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neisha.trashhub.R

class EducationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val educationImage: ImageView = view.findViewById(R.id.educationImage)
    val educationTitle: TextView = view.findViewById(R.id.educationTitle)
    val educationText: TextView = view.findViewById(R.id.educationText)
}

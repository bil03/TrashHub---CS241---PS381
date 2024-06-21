package com.neisha.trashhub.view.adapter

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neisha.trashhub.R

class EducationAdapter(private var educationContent: List<EducationItem>, private val clickListener: EducationItemClickListener) : RecyclerView.Adapter<EducationViewHolder>() {

    data class EducationItem(
        val title: String,
        val description: String,
        val imageResId: Int
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readInt()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(title)
            parcel.writeString(description)
            parcel.writeInt(imageResId)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<EducationItem> {
            override fun createFromParcel(parcel: Parcel): EducationItem {
                return EducationItem(parcel)
            }

            override fun newArray(size: Int): Array<EducationItem?> {
                return arrayOfNulls(size)
            }
        }
    }

    private var itemClickListener: ((EducationItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.education_item, parent, false)
        return EducationViewHolder(view)
    }

    override fun onBindViewHolder(holder: EducationViewHolder, position: Int) {
        val item = educationContent[position]
        holder.educationImage.setImageResource(item.imageResId)
        holder.educationTitle.text = item.title
        holder.educationText.text = item.description.take(50)
        holder.itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }


    override fun getItemCount() = educationContent.size

    fun updateData(newContent: List<EducationItem>) {
        educationContent = newContent
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (EducationItem) -> Unit) {
        itemClickListener = listener
    }

    interface EducationItemClickListener {
        fun onEducationItemClicked(educationItem: EducationItem)
    }
}

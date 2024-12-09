package com.angad.adminblinkitclone.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.angad.adminblinkitclone.databinding.ItemViewImageSelectionBinding

class AdapterSelectedImage(private val imageUris: ArrayList<Uri>):RecyclerView.Adapter<AdapterSelectedImage.SelectedImageViewHolder>() {
    class SelectedImageViewHolder(val binding: ItemViewImageSelectionBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedImageViewHolder {
        val view =  ItemViewImageSelectionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedImageViewHolder(view)
    }

    override fun getItemCount(): Int {
        return imageUris.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: SelectedImageViewHolder, position: Int) {
    //    Getting the image Uri and store in the image variable
        val image = imageUris[position]
        holder.binding.apply {
            ivImage.setImageURI(image)
        }
    //    When user click the X icon close the image
        holder.binding.closeButton.setOnClickListener {
            if (position < imageUris.size){
                imageUris.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

}
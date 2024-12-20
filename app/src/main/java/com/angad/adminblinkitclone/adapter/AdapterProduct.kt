package com.angad.adminblinkitclone.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.angad.adminblinkitclone.FilteringProducts
import com.angad.adminblinkitclone.databinding.ItemViewProductBinding
import com.angad.adminblinkitclone.model.Product
import com.denzcoskun.imageslider.models.SlideModel

class AdapterProduct(val onEditButtonClicked: (Product) -> Unit)
    : RecyclerView.Adapter<AdapterProduct.ProductViewHolder>(), Filterable {

    class ProductViewHolder(val binding: ItemViewProductBinding):RecyclerView.ViewHolder(binding.root){

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.productRandomId == newItem.productRandomId
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = ItemViewProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = differ.currentList[position]

        holder.binding.apply {
            //    Creating a arrayList for storing the image
            val imageList = ArrayList<SlideModel>()

            //    Accessing the product image
            val productImage = product.productImageUris

            for (i in 0 until productImage?.size!!){
                //    Adding the image
                imageList.add(SlideModel(product.productImageUris!![i].toString()))
            }

            imageSlider.setImageList(imageList)
            tvProductTitle.text = product.productTitle
            val quantity = product.productQuantity.toString() + product.productUnit
            tvProductQuantity.text = quantity
            tvProductPrice.text = "₹" + product.productPrice.toString()
        }
    //    On click edit button open new layout to edit the product
        holder.itemView.setOnClickListener {
            onEditButtonClicked(product)
        }
    }

    private val filter: FilteringProducts? = null
    var originalProduct = ArrayList<Product>()
    override fun getFilter(): Filter {
        if (filter == null){
            return FilteringProducts(this, originalProduct)
        }
        return filter
    }
}
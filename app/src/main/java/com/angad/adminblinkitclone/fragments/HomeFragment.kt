package com.angad.adminblinkitclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.angad.adminblinkitclone.Constants
import com.angad.adminblinkitclone.adapter.AdapterProduct
import com.angad.adminblinkitclone.adapter.CategoriesAdapter
import com.angad.adminblinkitclone.databinding.FragmentHomeBinding
import com.angad.adminblinkitclone.model.Categories
import com.angad.adminblinkitclone.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

//    Creating an instance of binding
    private lateinit var binding: FragmentHomeBinding

//    Initialised the viewModel
    private val viewModel: AdminViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
    //    Initialised the binding
        binding = FragmentHomeBinding.inflate(layoutInflater)

    //    Calling the function that set all category item
        setCategories()

    //    calling the function that get all the product data
        getAllTheProducts("All")


        return  binding.root
    }

    private fun setCategories() {
        val categoryList = ArrayList<Categories>()

        for (i in 0 until Constants.allProductsCategoryIcon.size){
            categoryList.add(Categories(Constants.allProductCategory[i], Constants.allProductsCategoryIcon[i]))
        }
        binding.rvCategories.adapter = CategoriesAdapter(categoryList, ::onCategoryClicked)
    }


    private fun getAllTheProducts(category: String) {
        lifecycleScope.launch {

        //    Initially showing the visibility of shimmer effect
            binding.shimmerViewContainer.visibility = View.VISIBLE

            viewModel.fetchAllTheProducts(category).collect{

            //    If category is empty then hide the recyclerview and show the required text
                if (it.isEmpty()){
                    binding.rvProducts.visibility = View.GONE
                    binding.tvText.visibility = View.VISIBLE
                } else {
                    binding.rvProducts.visibility = View.VISIBLE
                    binding.tvText.visibility = View.GONE
                }

            //    Creating an object of adapter class
                val adapterProduct = AdapterProduct()

            //    Set the adapter to the recyclerview
                binding.rvProducts.adapter = adapterProduct

            //    After setting the adapter to the recyclerview we pass differ list
                adapterProduct.differ.submitList(it)

            //    After loaded the data hide the shimmer effect
                binding.shimmerViewContainer.visibility = View.GONE
            }
        }
    }

    private fun onCategoryClicked(categories: Categories){
        getAllTheProducts(categories.category)
    }
}
package com.angad.adminblinkitclone.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.angad.adminblinkitclone.Constants
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.adapter.AdapterProduct
import com.angad.adminblinkitclone.adapter.CategoriesAdapter
import com.angad.adminblinkitclone.databinding.EditProductLayoutBinding
import com.angad.adminblinkitclone.databinding.FragmentHomeBinding
import com.angad.adminblinkitclone.model.Categories
import com.angad.adminblinkitclone.model.Product
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
                val adapterProduct = AdapterProduct(::onEditButtonClicked)

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

//    Function that perform the functionality to edit the product details
    private fun onEditButtonClicked(product: Product){
        val editProduct = EditProductLayoutBinding.inflate(LayoutInflater.from(requireContext()))
        editProduct.apply {
            etProductTitle.setText(product.productTitle)
            etProductQuantity.setText(product.productQuantity.toString())
            etProductUnit.setText(product.productUnit)
            etProductPrice.setText(product.productPrice.toString())
            etProductStock.setText(product.productStock.toString())
            etProductCategory.setText(product.productCategory)
            etProductType.setText(product.productType)
        }
    //    Creating an object of AlertDialog
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(editProduct.root)
            .create()
        alertDialog.show()

        editProduct.btnEdit.setOnClickListener {
             editProduct.etProductTitle.isEnabled = true
             editProduct.etProductQuantity.isEnabled = true
             editProduct.etProductUnit.isEnabled = true
             editProduct.etProductPrice.isEnabled = true
             editProduct.etProductStock.isEnabled = true
             editProduct.etProductCategory.isEnabled = true
             editProduct.etProductType.isEnabled = true
        }
    //    Calling the function that set auto complete textview
        setAutoCompleteTextViews(editProduct)

    //    Function that save product details  after edited
        editProduct.btnSave.setOnClickListener {

            lifecycleScope.launch {

                val title = editProduct.etProductTitle.text.toString()
                val quantityText = editProduct.etProductQuantity.text.toString()
                val unit = editProduct.etProductUnit.text.toString()
                val priceText = editProduct.etProductPrice.text.toString()
                val stockText = editProduct.etProductStock.text.toString()
                val category = editProduct.etProductCategory.text.toString()
                val type = editProduct.etProductType.text.toString()

                // Safely parse numeric fields
                val quantity = quantityText.toIntOrNull()
                val price = priceText.toIntOrNull()
                val stock = stockText.toIntOrNull()

                // Validate all numeric fields
                if (quantity == null || price == null || stock == null) {
                    Toast.makeText(
                        requireContext(),
                        "Please enter valid numeric values for Quantity, Price, and Stock.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@launch
                }

                // Update the product only if all inputs are valid
                product.apply {
                    productTitle = title
                    productQuantity = quantity
                    productUnit = unit
                    productPrice = price
                    productStock = stock
                    productCategory = category
                    productType = type
                }


                //    Calling the function that save the edited product details
                viewModel.savingUpdateProducts(product)
            }

            Toast.makeText(requireContext(), "Saved changes!", Toast.LENGTH_SHORT).show()
        //    After save the product dismiss the product details
            alertDialog.dismiss()
        }
    }

//    Function that perform the functionality to pass the list
    private fun setAutoCompleteTextViews(editProduct: EditProductLayoutBinding) {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        editProduct.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }
}
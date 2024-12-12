package com.angad.adminblinkitclone.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.angad.adminblinkitclone.Constants
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.Utils
import com.angad.adminblinkitclone.activity.AdminMainActivity
import com.angad.adminblinkitclone.adapter.AdapterSelectedImage
import com.angad.adminblinkitclone.databinding.FragmentAddProductBinding
import com.angad.adminblinkitclone.model.Product
import com.angad.adminblinkitclone.viewmodels.AdminViewModel
import kotlinx.coroutines.launch

class AddProductFragment : Fragment() {

//    Creating an instance of binding
    private lateinit var binding: FragmentAddProductBinding

//    Initialised the viewModel
    private val viewModel: AdminViewModel by viewModels()

//    Creating an arrayList for storing the selected images
    private val imageUris: ArrayList<Uri> = arrayListOf()

//    For open the gallery to select the image
    private val selectedImage =  registerForActivityResult(ActivityResultContracts.GetMultipleContents()){ listOfUri ->
        //    define that the user choose 5 image at a time
        val fiveImages = listOfUri.take(5)
        imageUris.clear()
        imageUris.addAll(fiveImages)
        //    Initialised the adapter and passing the list of image to the adapter
        binding.rvProductImages.adapter = AdapterSelectedImage(imageUris)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

    //    Initialised the binding
        binding = FragmentAddProductBinding.inflate(layoutInflater)

    //    Calling the function that set auto complete textview
        setAutoCompleteTextViews()
    //    Calling the function that open gallery for image
        onImageButtonClicked()

    //    Calling the function to add product details
        onClickAddProductButton()

        return binding.root
    }


    private fun setAutoCompleteTextViews() {
        val units = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allUnitsOfProducts)
        val category = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductCategory)
        val productType = ArrayAdapter(requireContext(), R.layout.show_list, Constants.allProductType)

        binding.apply {
            etProductUnit.setAdapter(units)
            etProductCategory.setAdapter(category)
            etProductType.setAdapter(productType)
        }
    }

    private fun onImageButtonClicked() {
        binding.btnSelectImage.setOnClickListener {

        //    Launch for open the gallery for image
          selectedImage.launch("image/*")
        }
    }


    private fun onClickAddProductButton() {
        binding.btnAddProduct.setOnClickListener {
        //    Show loader
            Utils.showDialog(requireContext(), "Uploading images...")
        //    Accessing the value of all field
            val productTitle = binding.etProductTitle.text.toString()
            val productQuantity = binding.etProductQuantity.text.toString()
            val productUnit = binding.etProductUnit.text.toString()
            val productPrice = binding.etProductPrice.text.toString()
            val productStock = binding.etProductStock.text.toString()
            val productCategory = binding.etProductCategory.text.toString()
            val productType = binding.etProductType.text.toString()

            if (productTitle.isEmpty() || productQuantity.isEmpty() || productUnit.isEmpty() || productPrice.isEmpty()
                || productStock.isEmpty() || productCategory.isEmpty() || productType.isEmpty()){
                Utils.apply {
                    hideDialog()
                    Toast.makeText(context, "Please Fill all details...", Toast.LENGTH_SHORT).show()
                }
            } else if (imageUris.isEmpty()){
                Utils.apply {
                    hideDialog()
                    Toast.makeText(context, "Please upload some images...", Toast.LENGTH_SHORT).show()
                }
            } else {
                val product = Product(
                    productTitle = productTitle,
                    productQuantity = productQuantity.toInt(),
                    productUnit = productUnit,
                    productPrice = productPrice.toInt(),
                    productStock = productStock.toInt(),
                    productCategory = productCategory,
                    productType = productType,
                    itemCount = 0,
                    adminUid = Utils.getCurrentUserId(),
                //    Creating a random number
                    productRandomId = Utils.getRandomId()
                )
                saveImage(product)
            }
        }
    }

    private fun saveImage(product: Product) {
        viewModel.saveImageInDB(imageUris)
        lifecycleScope.launch {
            viewModel.isImagesUploaded.collect{
                if (it){
                    Utils.apply {
                        hideDialog()
                        Toast.makeText(context, "Image saved successfully", Toast.LENGTH_SHORT).show()
                    }
                    getUrls(product)
                }
            }
        }
    }

    private fun getUrls(product: Product) {

        Utils.showDialog(requireContext(), "Publishing product...")
        lifecycleScope.launch {
            viewModel.downloadedUrls.collect{ urls ->
                val convertedUrls = ArrayList<String?>(urls)
                product.productImageUris = convertedUrls
                saveProduct(product)
            }
        }
    }

    private fun saveProduct(product: Product) {
        viewModel.saveProduct(product)
        lifecycleScope.launch {
            viewModel.isProductSaved.collect{
                if (it){
                    Utils.hideDialog()
                    Toast.makeText(context, "Your product is live", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                }
            }
        }
    }

}
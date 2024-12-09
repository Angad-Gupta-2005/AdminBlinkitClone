package com.angad.adminblinkitclone.fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import com.angad.adminblinkitclone.Constants
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.adapter.AdapterSelectedImage
import com.angad.adminblinkitclone.databinding.FragmentAddProductBinding

class AddProductFragment : Fragment() {

//    Creating an instance of binding
    private lateinit var binding: FragmentAddProductBinding

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

}
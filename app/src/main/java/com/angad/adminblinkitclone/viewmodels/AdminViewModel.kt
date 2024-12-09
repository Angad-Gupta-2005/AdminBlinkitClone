package com.angad.adminblinkitclone.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.angad.adminblinkitclone.Utils
import com.angad.adminblinkitclone.model.Product
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.UUID

class AdminViewModel: ViewModel() {

//    Creating two stateflow for checking that the image is load to the firebase or not

    //    First check image loaded or not
    private val _isImagesUploaded = MutableStateFlow(false)
    var isImagesUploaded: StateFlow<Boolean> = _isImagesUploaded

    //    Second check url downloaded or not
    private val _downloadedUrls = MutableStateFlow<ArrayList<String>>(arrayListOf())
    var downloadedUrls: MutableStateFlow<ArrayList<String>> = _downloadedUrls

//    Creating a state flow for checking that our product is saved or not
    private val _isProductSaved = MutableStateFlow(false)
    var isProductSaved: StateFlow<Boolean> = _isProductSaved

//    Calling the function that upload the image into the firebase storage
    fun saveImageInDB(imageUri: ArrayList<Uri>){
        val downloadUrls = ArrayList<String>()

    //    Defining the path
        imageUri.forEach { uri ->
            val imageRef = FirebaseStorage.getInstance().reference.child(Utils.getCurrentUserId().toString())
                .child("images/").child(UUID.randomUUID().toString())
        //    Save the image into the firebase storage
            imageRef.putFile(uri).continueWithTask {
                imageRef.downloadUrl
            }.addOnCompleteListener { task ->
            //    After loading the image, download the image url
                val url = task.result
            //    store the image url into downloadUrls arrayList
                downloadUrls.add(url.toString())

                if (downloadUrls.size == imageUri.size){
                    _isImagesUploaded.value = true
                    _downloadedUrls.value = downloadUrls
                }
            }
        }
    }

    //    Calling the function that save the data into the firebase realtime database
    fun saveProduct(product: Product){

        //    Creating a node for adding all product
        FirebaseDatabase.getInstance("https://blinkit-clone-f610a-default-rtdb.asia-southeast1.firebasedatabase.app")
            .getReference("Admins")
            .child("AllProducts/${product.productRandomId}").setValue(product)
            .addOnSuccessListener {
                //    Creating a node for adding ProductCategory
                FirebaseDatabase.getInstance("https://blinkit-clone-f610a-default-rtdb.asia-southeast1.firebasedatabase.app")
                    .getReference("Admins")
                    .child("ProductCategory/${product.productRandomId}").setValue(product)
                    .addOnSuccessListener {
                        //    Creating a node for adding ProductType
                        FirebaseDatabase.getInstance("https://blinkit-clone-f610a-default-rtdb.asia-southeast1.firebasedatabase.app")
                            .getReference("Admins")
                            .child("ProductType/${product.productRandomId}").setValue(product)
                            .addOnSuccessListener {
                                _isProductSaved.value = true
                            }
                    }
            }
    }
}
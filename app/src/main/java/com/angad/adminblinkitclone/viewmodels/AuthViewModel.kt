package com.angad.adminblinkitclone.viewmodels

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.angad.adminblinkitclone.Users
import com.angad.adminblinkitclone.Utils
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.concurrent.TimeUnit

class AuthViewModel: ViewModel() {

    //    stateflow variable
    private val _verificationId = MutableStateFlow<String?>(null)

    //    state flow variable of type boolean
    private val _otpSent = MutableStateFlow(false)
    val optSent = _otpSent

    //    functionality after the user signIn successfully
    private val _isSignedInSuccessfully =MutableStateFlow(false)
    val isSignedInSuccessfully = _isSignedInSuccessfully

    private val _isCurrentUser = MutableStateFlow(false)
    val isCurrentUser = _isCurrentUser

    init {
        Utils.getAuthInstance().currentUser?.let {
            _isCurrentUser.value = true
        }
    }

    fun sendOTP(userNumber : String, activity: Activity){

        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

            override fun onVerificationFailed(e: FirebaseException) {}

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken,
            ) {
                _verificationId.value = verificationId
                _otpSent.value = true
            }
        }

        val options = PhoneAuthOptions.newBuilder(Utils.getAuthInstance())
            .setPhoneNumber("+91$userNumber") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    fun signInWithPhoneAuthCredential(otp: String, userNumber: String, user: Users) {
        val credential = PhoneAuthProvider.getCredential(_verificationId.value.toString(), otp)
        Utils.getAuthInstance().signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //    Save the user data into the firebase realtime database
                    FirebaseDatabase.getInstance("https://blinkit-clone-f610a-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("AllUsers").child("Users").child(user.uid!!).setValue(user)
                        .addOnSuccessListener {
                            // Toast.makeText( this, "Data save successfully", Toast.LENGTH_SHORT).show()
                            Log.d("Success", "signInWithPhoneAuthCredential: ")
                        }
                        .addOnFailureListener { e ->
                            // Toast.makeText(this, "Error ${e.message}", Toast.LENGTH_SHORT).show()
                            Log.e("Unsuccessful", "signInWithPhoneAuthCredential: ${e.message}" )
                        }
                    // Sign in success, update UI with the signed-in user's information
                    _isSignedInSuccessfully.value = true
                } else {
                    // Sign in failed, display a message and update the UI

                }
            }
    }

}
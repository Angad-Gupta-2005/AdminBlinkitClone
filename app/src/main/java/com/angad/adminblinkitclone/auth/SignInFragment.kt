package com.angad.adminblinkitclone.auth

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

    //    Initialised the binding and Inflate the layout for this fragment
        binding = FragmentSignInBinding.inflate(layoutInflater)

    //    Getting the user number and change the button color
        getUserNumber()

    //    on click continue button this function called
        onContinueButtonClick()

        return binding.root
    }

//    function to change the button color if user enter a valid user number
    private fun getUserNumber() {
        binding.etUserNumber.addTextChangedListener( object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val len = number?.length

                if (len == 10){
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.green))
                }
                else{
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grayish_blue))
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        } )
    }

    //    Functionality after the continue button clicked
    private fun onContinueButtonClick() {
        binding.btnContinue.setOnClickListener {
            val number = binding.etUserNumber.text.toString()
            if (number.isEmpty() || number.length != 10){
                Toast.makeText(context, "Please enter a valid phone number", Toast.LENGTH_SHORT).show()
            }
            else{
//                Jump signIn Fragment to OTP fragment and passing the number using the bundle
                val bundle = Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_signInFragment_to_OTPFragment, bundle)
            }
        }
    }

}
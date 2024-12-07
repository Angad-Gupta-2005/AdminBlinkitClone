package com.angad.adminblinkitclone.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.angad.adminblinkitclone.AdminMainActivity
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.databinding.FragmentSplashBinding
import com.angad.adminblinkitclone.viewmodels.AuthViewModel
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    //    Initialised the viewModel
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentSplashBinding.inflate(layoutInflater)

    //    Setting the status bar color
        setStatusBarColor()
        //        Use of Handler for splash screen
        Handler(Looper.getMainLooper()).postDelayed({
            lifecycleScope.launch {
                viewModel.isCurrentUser.collect{
                    if (it){
                        startActivity(Intent(requireActivity(), AdminMainActivity::class.java))
                        requireActivity().finish()
                    } else{
                        findNavController().navigate(R.id.action_splashFragment_to_signInFragment)
                    }
                }
            }
        }, 2000)
        return binding.root
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarColor() {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
    }

}
package com.angad.adminblinkitclone

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.angad.adminblinkitclone.databinding.ActivityAuthMainBinding

class AuthMainActivity : AppCompatActivity() {

//    Creating an instance of binding
    private lateinit var binding: ActivityAuthMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

    //    Initialised  the binding
        binding = ActivityAuthMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    //    Calling the function that set status bar color
        setStatusBarColor()
    }

    @Suppress("DEPRECATION")
    private fun setStatusBarColor() {
        window.statusBarColor = ContextCompat.getColor(this, R.color.yellow)
    }
}
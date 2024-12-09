package com.angad.adminblinkitclone.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.angad.adminblinkitclone.R
import com.angad.adminblinkitclone.databinding.ActivityAdminMainBinding
import com.angad.adminblinkitclone.fragments.AddProductFragment
import com.angad.adminblinkitclone.fragments.HomeFragment
import com.angad.adminblinkitclone.fragments.OrderFragment

class AdminMainActivity : AppCompatActivity() {

//    Creating an instance of binding
    private lateinit var binding: ActivityAdminMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAdminMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set default fragment
        replaceFragment(HomeFragment())

        // Handle navigation item clicks
        val bottomNavigationView = binding.bottomMenu
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> replaceFragment(HomeFragment())
                R.id.addProductFragment -> replaceFragment(AddProductFragment())
                R.id.orderFragment -> replaceFragment(OrderFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerView2, fragment)
            .commit()
    }
}
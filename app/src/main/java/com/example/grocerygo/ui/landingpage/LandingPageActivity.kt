package com.example.grocerygo.ui.landingpage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.grocerygo.databinding.ActivityLandingPageBinding
import com.example.grocerygo.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator

class LandingPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.materialButton.setOnClickListener {
            startActivity(Intent(this@LandingPageActivity, MainActivity::class.java))
            finishAffinity()
        }

        binding.viewpager.adapter = ViewPager2Adapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewpager) { _, _ ->
        }.attach()


    }
}
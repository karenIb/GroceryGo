package com.example.grocerygo.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.grocerygo.R
import com.example.grocerygo.ui.landingpage.LandingPageActivity
import com.example.grocerygo.ui.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val splashViewModel by lazy {
        SplashViewModel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launchWhenCreated {
            launch {
                delay(2000)
                navigate()
            }
        }
    }

    private suspend fun navigate() {
        if (!splashViewModel.isFirst()) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            splashViewModel.setIsFirst()
            startActivity(Intent(this, LandingPageActivity::class.java))
        }
        finishAffinity()
    }
}
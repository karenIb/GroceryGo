package com.example.grocerygo.ui.signin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.grocerygo.databinding.ActivitySignInBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.main.MainActivity
import com.example.grocerygo.ui.register.RegisterActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewModel = SignInViewModel()

        binding.btnSignUp.setOnClickListener {
            navigateToRegister()
        }

        binding.btnLogin.setOnClickListener { view ->

            var isError = false
            if (binding.etUserName.text.toString().isEmpty()) {
                binding.etUserName.error = "Please fill the username"
                isError = true
            } else {
                binding.etUserName.error = null
            }

            if (binding.etPassword.text.toString().isEmpty()) {
                isError = true
                binding.etPassword.error = "Please fill the password"
            } else {
                binding.etPassword.error = null
            }

            view.isEnabled = false

            if (isError) return@setOnClickListener
            lifecycleScope.launchWhenCreated {
                viewModel.login(
                    binding.etUserName.text.toString(),
                    binding.etPassword.text.toString()
                ).collect { resource ->

                    view.isEnabled = true

                    if (resource !is Resource.Loading) {
                        binding.progress.isVisible = false
                    }

                    when (resource) {
                        is Resource.Error -> {
                            Toast.makeText(
                                this@SignInActivity,
                                resource.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        is Resource.Loading -> {
                            binding.progress.isVisible = true

                        }

                        is Resource.Success -> {
                            viewModel.loginSuccess(resource.data?.token!!) {
                                viewModel.viewModelScope.launch {
                                    viewModel.getCart().collect{ cartRes ->

                                        if (cartRes !is Resource.Loading) {
                                            binding.progress.isVisible = false
                                        }
                                        when (cartRes) {
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    this@SignInActivity,
                                                    cartRes.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                binding.progress.isVisible = true
                                            }
                                            is Resource.NotStarted -> {}
                                            is Resource.Success -> {
                                                viewModel.setNumberInCarts(cartRes.data!!.cart!!.products.size)
                                                navigateToMain()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else -> {

                        }
                    }
                }
            }

        }
    }

    private fun navigateToMain() {
        finishAffinity()
        startActivity(Intent(this, MainActivity::class.java))
    }


    private fun navigateToRegister() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }
}
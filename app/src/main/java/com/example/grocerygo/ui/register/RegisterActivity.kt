package com.example.grocerygo.ui.register

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.grocerygo.data.PrefStore
import com.example.grocerygo.databinding.ActivityRegisterBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.main.MainActivity
import kotlinx.coroutines.cancel

class RegisterActivity : AppCompatActivity() {


    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        val viewModel = RegisterViewModel()

        binding.btnRegister.setOnClickListener { view ->
            var isErorr = false

            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val userName = binding.etUserName.text.toString()
            val password = binding.etPassword.text.toString()
            val confirmPassword = binding.etConfirmPassword.text.toString()
            val phoneNumber = binding.etPhoneNumber.text.toString()
            val email = binding.etEmail.text.toString()
            val address = binding.etAddress.text.toString()
            val dob = binding.etDOB.text.toString()

            binding.etFirstName.error = if (firstName.isEmpty()) {
                isErorr = true
                "Please fill first Name"
            } else {
                null
            }

            binding.etLastName.error = if (lastName.isEmpty()) {
                isErorr = true
                "Please fill last Name"
            } else {
                null
            }

            binding.etUserName.error = if (userName.isEmpty()) {
                isErorr = true

                "Please fill user name"
            } else {
                null
            }


            if (password.isEmpty()) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Please fill password", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (confirmPassword.isEmpty()) {
                isErorr = true

                Toast.makeText(
                    this@RegisterActivity,
                    "Please fill confirmPassword",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }


            if (confirmPassword != password) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Password don't match", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            if (phoneNumber.isEmpty()) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Please fill phoneNumber", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            if (email.isEmpty()) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Please fill email", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Invalid email format", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                isErorr = true

                Toast.makeText(this@RegisterActivity, "Please fill address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (dob.isEmpty()) {
                isErorr = true

                Toast.makeText(
                    this@RegisterActivity,
                    "Please fill date of birth",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (isErorr) return@setOnClickListener

            val gender =
                binding.etGender.findViewById<RadioButton>(binding.etGender.checkedRadioButtonId).tag as String

            lifecycleScope.launchWhenCreated {
                viewModel.register(
                    firstName,
                    lastName,
                    userName,
                    password,
                    phoneNumber,
                    email,
                    address,
                    dob,
                    gender.toInt()
                )
                    .collect { resource ->
                        view.isEnabled = true

                        if (resource !is Resource.Loading) {
                            binding.progress.isVisible = false
                        }

                        when (resource) {
                            is Resource.Error -> {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    resource.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Resource.Loading -> {
                                binding.progress.isVisible = true

                            }

                            is Resource.Success -> {
                                viewModel.onRegisterSuccess(resource.data!!)
                                navigateToMain()
                            }
                            else -> {
                            }
                        }
                    }
            }


        }

        binding.btnSet.setOnClickListener {
            DatePicker { year, month, dayOfMonth ->
                binding.etDOB.setText("$year-${month.toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}")
            }.show(supportFragmentManager, "datePicker")
        }

    }


    private fun navigateToMain() {
        finishAffinity()

        startActivity(Intent(this, MainActivity::class.java))
    }
}
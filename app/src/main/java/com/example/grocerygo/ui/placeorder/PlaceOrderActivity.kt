package com.example.grocerygo.ui.placeorder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ActivityPlaceOrderBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.register.RegisterViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class PlaceOrderActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityPlaceOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val placeOrderVM = PlaceOrderVM()


        binding.btnOrder.setOnClickListener {
            var isErorr = false

            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val userName = binding.etUserName.text.toString()
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



            if (phoneNumber.isEmpty()) {
                isErorr = true

                Toast.makeText(this@PlaceOrderActivity, "Please fill phoneNumber", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }


            if (email.isEmpty()) {
                isErorr = true

                Toast.makeText(this@PlaceOrderActivity, "Please fill email", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                isErorr = true

                Toast.makeText(this@PlaceOrderActivity, "Invalid email format", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (address.isEmpty()) {
                isErorr = true

                Toast.makeText(this@PlaceOrderActivity, "Please fill address", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (dob.isEmpty()) {
                isErorr = true

                Toast.makeText(
                    this@PlaceOrderActivity,
                    "Please fill date of birth",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (isErorr) return@setOnClickListener


            binding.apply {
                placeOrderVM.viewModelScope.launch {
                    placeOrderVM.placeOrder(
                        etFirstName.text.toString(),
                        etLastName.text.toString(),
                        etEmail.text.toString(),
                        etPhoneNumber.text.toString(),
                        etAddress.text.toString(),
                        3
                    ).collect{ resource ->
                        if (resource !is Resource.Loading) {
                            binding.progress.isVisible = false
                        }

                        when (resource) {
                            is Resource.Error -> {
                                Toast.makeText(
                                    this@PlaceOrderActivity,
                                    resource.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Resource.Loading -> {
                                binding.progress.isVisible = true
                            }

                            is Resource.Success -> {
                                placeOrderVM.resetCart()
                                Toast.makeText(this@PlaceOrderActivity,"Order Successfull", Toast.LENGTH_SHORT).show()
                                setResult(1)
                                finish()
                            }
                            else -> {}
                        }

                    }
                }


            }


        }

        lifecycleScope.launchWhenCreated {
            placeOrderVM.getProfile().collect{ resource->
                if (resource !is Resource.Loading) {
                    binding.progress.isVisible = false
                }

                when (resource) {
                    is Resource.Error -> {
                        Toast.makeText(
                            this@PlaceOrderActivity,
                            resource.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is Resource.Loading -> {
                        binding.progress.isVisible = true
                    }

                    is Resource.Success -> {
                        resource.data?.let {
                            binding.etFirstName.setText(it.profile.firstName)
                            binding.etLastName.setText(it.profile.lastName)
                            binding.etUserName.setText(it.profile.fullName)
                            binding.etDOB.setText(it.profile.dateOfBirth)
                            binding.etEmail.setText(it.profile.email)
                            binding.etAddress.setText(it.profile.address)
                            binding.etPhoneNumber.setText(it.profile.phoneNumber)
                        }
                    }
                    else -> {}
                }
            }
        }





    }
}
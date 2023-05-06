package com.example.grocerygo.ui.product

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ActivityProductBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.signin.SignInActivity
import kotlinx.coroutines.launch

class ProductActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@ProductActivity)
        binding.mt.setNavigationOnClickListener {
            finish()
        }

        if (intent.hasExtra("productID")) {
            getProduct(binding)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun getProduct(binding: ActivityProductBinding) {
        val productViewModel = ProductViewModel()
        binding.etRate.setText("")
        binding.appRate.rating = 0F
        lifecycleScope.launchWhenCreated {
            launch {
                productViewModel.getProductDetails(intent.getStringExtra("productID")!!).collect {
                    if (it !is Resource.Loading) {
                        binding.pr.isVisible = false
                    }
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(
                                this@ProductActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                        is Resource.Loading -> {
                            binding.pr.isVisible = true
                        }
                        is Resource.NotStarted -> {
                        }
                        is Resource.Success -> {
                            val details = it.data!!.product
                            binding.ivFavorite.setImageResource(
                                if (details.isFavorite) {
                                    R.drawable.favorite
                                } else {
                                    R.drawable.un_favorite
                                }
                            )
                            binding.tvName.text = details.name
                            binding.sdProductImage.setImageURI(details.imageURL)
                            binding.tvPrice.text = "${details.price} $"
                            if (details.discountedPrice != 0) {
                                binding.tvDiscounted.isVisible = true
                                binding.tvDiscounted.text = "${details.discountedPrice} $"
                                binding.line.isVisible = true
                            }

                            if (details.quantity == 0) {
                                binding.ivOutOfStock.isVisible = true
                            } else {
                                binding.btnAddToCard.isEnabled = true
                                if (!productViewModel.isLoggedIn()) {
                                    binding.btnAddToCard.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this@ProductActivity,
                                                SignInActivity::class.java
                                            )
                                        )
                                    }
                                    binding.btnSendReview.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this@ProductActivity,
                                                SignInActivity::class.java
                                            )
                                        )
                                    }

                                    binding.ivFavorite.setOnClickListener {
                                        startActivity(
                                            Intent(
                                                this@ProductActivity,
                                                SignInActivity::class.java
                                            )
                                        )
                                    }
                                } else {

                                    binding.appRate.setIsIndicator(false)

                                    binding.etRate.isEnabled = true

                                    binding.btnAddToCard.setOnClickListener {
                                        productViewModel.viewModelScope.launch {
                                            productViewModel.addToCart(details.id)
                                                .collect { cartRes ->
                                                    when (cartRes) {
                                                        is Resource.Error -> {
                                                            Toast.makeText(
                                                                this@ProductActivity,
                                                                cartRes.message,
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()
                                                        }
                                                        is Resource.Loading -> {
                                                            binding.pr.isVisible = true
                                                        }
                                                        is Resource.NotStarted -> {
                                                        }
                                                        is Resource.Success -> {
                                                            productViewModel.incrementCard()
                                                            Toast.makeText(
                                                                this@ProductActivity,
                                                                "Added Successfully",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                            finish()
                                                        }
                                                    }
                                                }
                                        }

                                    }
                                    binding.ivFavorite.setOnClickListener {
                                        if (details.isFavorite) {
                                            productViewModel.viewModelScope.launch {
                                                productViewModel.removeFavorite(details.id)
                                                    .collect { favRes ->
                                                        when (favRes) {
                                                            is Resource.Error -> {
                                                                Toast.makeText(
                                                                    this@ProductActivity,
                                                                    favRes.message,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()
                                                            }
                                                            is Resource.Loading -> {
                                                                binding.pr.isVisible = true
                                                            }
                                                            is Resource.NotStarted -> {
                                                            }
                                                            is Resource.Success -> {
                                                                productViewModel.decrementFavorite()
                                                                recreate()
                                                            }
                                                        }
                                                    }
                                            }
                                        } else {
                                            productViewModel.viewModelScope.launch {
                                                productViewModel.addFavorite(details.id)
                                                    .collect { favRes ->
                                                        when (favRes) {
                                                            is Resource.Error -> {
                                                                Toast.makeText(
                                                                    this@ProductActivity,
                                                                    favRes.message,
                                                                    Toast.LENGTH_SHORT
                                                                )
                                                                    .show()
                                                            }
                                                            is Resource.Loading -> {
                                                                binding.pr.isVisible = true
                                                            }
                                                            is Resource.NotStarted -> {
                                                            }
                                                            is Resource.Success -> {
                                                                productViewModel.incrementFavorite()
                                                                recreate()
                                                            }
                                                        }
                                                    }
                                            }
                                        }
                                    }

                                    binding.btnSendReview.setOnClickListener {
                                        if (!productViewModel.isLoggedIn()) {
                                            val intent = Intent(
                                                this@ProductActivity,
                                                SignInActivity::class.java
                                            )
                                            startActivity(intent)
                                        } else {
                                            if (binding.appRate.rating == 0F) {
                                                Toast.makeText(
                                                    this@ProductActivity,
                                                    "Please rate !!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                productViewModel.viewModelScope.launch {
                                                    productViewModel.addReview(
                                                        details.id,
                                                        binding.appRate.rating.toInt(),
                                                        binding.etRate.text.toString()
                                                    )
                                                        .collect { rateRes ->
                                                            when (rateRes) {
                                                                is Resource.Error -> {
                                                                    Toast.makeText(
                                                                        this@ProductActivity,
                                                                        rateRes.message,
                                                                        Toast.LENGTH_SHORT
                                                                    )
                                                                        .show()
                                                                }
                                                                is Resource.Loading -> {
                                                                    binding.pr.isVisible = true
                                                                }
                                                                is Resource.NotStarted -> {
                                                                }
                                                                is Resource.Success -> {
                                                                    recreate()
                                                                }
                                                            }
                                                        }
                                                }

                                            }
                                        }
                                    }
                                }

                            }

                            var sumStars = 0

                            for (review in details.reviews) {
                                sumStars += review.stars
                            }



                            if (details.reviews.isNotEmpty()) {
                                val averageStars = sumStars / details.reviews.size.toFloat()
                                binding.appCompatRatingBar.rating = averageStars
                                binding.recyclerView.adapter =
                                    ReviewsAdapter(details.reviews) { id ->

                                        productViewModel.viewModelScope.launch {
                                            productViewModel.removeReview(id.toString())
                                                .collect { revRes ->
                                                    when (revRes) {
                                                        is Resource.Error -> {
                                                            Toast.makeText(
                                                                this@ProductActivity,
                                                                revRes.message,
                                                                Toast.LENGTH_SHORT
                                                            )
                                                                .show()
                                                        }
                                                        is Resource.Loading -> {
                                                            binding.pr.isVisible = true
                                                        }
                                                        is Resource.NotStarted -> {
                                                        }
                                                        is Resource.Success -> {
                                                            recreate()
                                                        }
                                                    }
                                                }

                                        }

                                    }
                            } else {
                                binding.tvNoComments.isVisible = true
                                binding.recyclerView.isVisible = false
                            }


                        }
                    }
                }
            }

        }
    }
}
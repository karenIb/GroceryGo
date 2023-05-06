package com.example.grocerygo.ui.category

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.grocerygo.databinding.ActivityCategoryBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.home.SpacingItemDecorator
import com.example.grocerygo.ui.product.ProductActivity
import com.example.grocerygo.ui.signin.SignInActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvCategoryProducts.layoutManager = GridLayoutManager(this, 2)
        binding.rvCategoryProducts.addItemDecoration(SpacingItemDecorator(15))
        val viewModel = CategoryVM()
        binding.mt.setNavigationOnClickListener {
            finish()
        }
        if (intent.hasExtra("id")) {
            viewModel.viewModelScope.launch {
                binding.mt.title = intent.getStringExtra("name")

                viewModel.getCategoryProducts(intent.getStringExtra("id")!!.toInt()).collect {
                    if (it !is Resource.Loading) {
                        binding.pr.isVisible = false
                    }
                    when (it) {
                        is Resource.Error -> {
                            Toast.makeText(this@CategoryActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Loading -> {
                            binding.pr.isVisible = true
                        }
                        is Resource.NotStarted -> {}
                        is Resource.Success -> {
                            binding.rvCategoryProducts.adapter =
                                 ProductsAdapter(it.data!!.products, { producId ->
                                    val intent =
                                        Intent(this@CategoryActivity, ProductActivity::class.java)
                                    intent.putExtra("productID", producId.toString())
                                    startActivity(intent)
                                }, { position ,producId ->
                                    if (!viewModel.isLoggedIn()) {
                                        val intent = Intent(
                                            this@CategoryActivity,
                                            SignInActivity::class.java
                                        )
                                        startActivity(intent)
                                    } else {
                                        viewModel.viewModelScope.launch {
                                            if (!it.data.products[position].isFavorite) {
                                                viewModel.addFavorite(producId.toString()).collectLatest { resfav->
                                                    if (resfav !is Resource.Loading) {
                                                        binding.pr.isVisible = false
                                                    }
                                                    when (resfav) {
                                                        is Resource.Error -> {

                                                        }
                                                        is Resource.Loading -> {
                                                            binding.pr.isVisible = true
                                                        }
                                                        is Resource.NotStarted -> {}
                                                        is Resource.Success -> {
                                                            recreate()
                                                        }
                                                    }
                                                }
                                            }
                                            else {
                                                viewModel.unFavorite(producId.toString()).collectLatest { resfav->
                                                    if (resfav !is Resource.Loading) {
                                                        binding.pr.isVisible = false
                                                    }
                                                    when (resfav) {
                                                        is Resource.Error -> {

                                                        }
                                                        is Resource.Loading -> {
                                                            binding.pr.isVisible = true
                                                        }
                                                        is Resource.NotStarted -> {}
                                                        is Resource.Success -> {
                                                            recreate()
                                                        }
                                                    }
                                                }
                                            }

                                        }

                                    }
                                }, { producId ->

                                    if (!viewModel.isLoggedIn()) {
                                        val intent = Intent(
                                            this@CategoryActivity,
                                            SignInActivity::class.java
                                        )
                                        startActivity(intent)
                                    } else {
                                        viewModel.viewModelScope.launch {

                                            viewModel.addToCart(producId.toString()).collectLatest { cartRes->
                                                if (cartRes !is Resource.Loading) {
                                                    binding.pr.isVisible = false
                                                }
                                                when (cartRes) {
                                                    is Resource.Error -> {

                                                    }
                                                    is Resource.Loading -> {
                                                        binding.pr.isVisible = true
                                                    }
                                                    is Resource.NotStarted -> {}
                                                    is Resource.Success -> {
                                                        viewModel.incrementCard()
                                                        finish()
                                                    }
                                                }
                                            }
                                        }

                                    }

                                })
                        }
                    }
                }
            }
        }
    }
}
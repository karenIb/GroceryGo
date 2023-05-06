package com.example.grocerygo.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.databinding.ActivitySearchBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.product.ProductActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchActivity : AppCompatActivity() {

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val searchViewModel = SearchViewModel()
        binding.rvSearchResults.layoutManager = LinearLayoutManager(this@SearchActivity)

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                job?.cancel()
                job = lifecycleScope.launch {
                    searchViewModel.searchProduct(s.toString()).collect {
                        if (it !is Resource.Loading) {
                            binding.pr.isVisible = false
                        }
                        when (it) {
                            is Resource.Error -> {
                                Toast.makeText(
                                    this@SearchActivity,
                                    "${it.message}", Toast.LENGTH_SHORT
                                ).show()
                            }
                            is Resource.Loading -> {
                                binding.pr.isVisible = true
                            }
                            is Resource.NotStarted -> {
                            }
                            is Resource.Success -> {
                                binding.rvSearchResults.adapter = SearchAdapter(it.data!!.products,
                                    { productID ->
                                        val intent = Intent(
                                            this@SearchActivity,
                                            ProductActivity::class.java
                                        ).putExtra("productID", productID)
                                        startActivity(intent)
                                    },
                                    { categoryID ->

                                    })
                            }
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })
        binding.imageButton.setOnClickListener {
            finish()
        }

    }
}
package com.example.grocerygo.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.databinding.FragmentHomeBinding
import com.example.grocerygo.ui.category.CategoryActivity
import com.example.grocerygo.ui.main.MainViewModel
import com.example.grocerygo.ui.product.ProductActivity
import com.example.grocerygo.ui.search.SearchActivity
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]

        lifecycleScope.launch {
            viewModel.pageModelState.collect {
                if (it !is MainViewModel.Resource.Loading) {
                    binding.pr.isVisible = false
                }
                when (it) {
                    is MainViewModel.Resource.Error -> {
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }
                    MainViewModel.Resource.Loading -> {
                        binding.pr.isVisible = true
                    }
                    MainViewModel.Resource.NotStarted -> {}
                    is MainViewModel.Resource.Success -> {
                        val adapter = HomeAdapter(lifecycleScope, {product ->
                            val intent = Intent(requireContext(), ProductActivity::class.java)
                            intent.putExtra("productID", product.id)
                            startActivity(intent)
                        }, { category ->
                            val intent = Intent(requireContext(), CategoryActivity::class.java)
                            intent.putExtra("id", category.id)
                            intent.putExtra("name", category.name)
                            startActivity(intent)
                        })
                        binding.rv.adapter = adapter
                        binding.rv.layoutManager = LinearLayoutManager(requireContext())
                        adapter.setData(it.data!!)
                    }
                }
            }
        }

        binding.materialCardView.setOnClickListener {
            startActivity(Intent(requireContext(), SearchActivity::class.java))
        }
        super.onViewCreated(view, savedInstanceState)

    }


}
package com.example.grocerygo.ui.categories

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
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.databinding.FragmentCategoriesBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.main.MainViewModel
import com.example.grocerygo.ui.product.ProductActivity
import com.example.grocerygo.ui.signin.SignInActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ExploreFragment : Fragment() {

    private lateinit var maiNViewModel: MainViewModel
    private lateinit var fragmentCategoriesBinding: FragmentCategoriesBinding

    private var subCategoriesAdapter: SubCategoriesAdapter? = null


    private var pos = -1

    private var currentJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentCategoriesBinding = FragmentCategoriesBinding.inflate(layoutInflater)
        maiNViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        return fragmentCategoriesBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentCategoriesBinding.rvCategories.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {

            launch {
                maiNViewModel.categoriesState.collect {
                    val adapter = CategoriesAdapter(it) { category ->
                        val list = category.subcategories.map { sub ->
                            SubcategoryProducts(sub, emptyList())
                        }
                        maiNViewModel.subCateState.value = list
                    }
                    fragmentCategoriesBinding.rvCategories.adapter =
                        CategoriesAdapter(it) { category ->
                            val list = category.subcategories.map { sub ->
                                SubcategoryProducts(sub, emptyList())
                            }
                            maiNViewModel.subCateState.value = list
                        }
                    adapter.select(0)
                }
            }

            launch {
                maiNViewModel.subCateState.collect { subList ->
                    fragmentCategoriesBinding.rvSubcategories.layoutManager =
                        LinearLayoutManager(fragmentCategoriesBinding.root.context)
                    subCategoriesAdapter = SubCategoriesAdapter(subList,
                        { cats, position ->
                            currentJob?.cancel()
                            pos = position
                            currentJob = maiNViewModel.viewModelScope.launch {
                                maiNViewModel.getSubCategryProduct(cats.subcategory.id.toInt())
                                    .collect { res ->
                                        if (res !is Resource.Loading) {
                                            fragmentCategoriesBinding.pr.isVisible = false
                                        }
                                        when (res) {
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    requireActivity(),
                                                    res.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                fragmentCategoriesBinding.pr.isVisible = true
                                            }
                                            is Resource.NotStarted -> {}
                                            is Resource.Success -> {
                                                subCategoriesAdapter?.setProducts(
                                                    position,
                                                    res.data!!.products
                                                )
                                            }
                                        }
                                    }
                            }
                        },
                        {
                            val intent = Intent(requireContext(), ProductActivity::class.java)
                            intent.putExtra("productID", it.toString())
                            startActivity(intent)
                        },
                        { p, r, s, currentState ->
                            if (!maiNViewModel.isLoggedIn()) {
                                startActivity(Intent(requireContext(), SignInActivity::class.java))
                                return@SubCategoriesAdapter
                            }
                            if (currentState) {
                                maiNViewModel.viewModelScope.launch {
                                    maiNViewModel.unFavorite(s.toString()).collect { fav ->

                                        if (fav !is Resource.Loading) {
                                            fragmentCategoriesBinding.pr.isVisible = false
                                        }
                                        when (fav) {
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    fav.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                fragmentCategoriesBinding.pr.isVisible = true
                                            }
                                            is Resource.NotStarted -> {}
                                            is Resource.Success -> {
                                                subCategoriesAdapter?.setFavorite(p, r, false)

                                            }
                                        }
                                    }
                                }
                            } else {
                                maiNViewModel.viewModelScope.launch {
                                    maiNViewModel.addFavorite(s.toString()).collect { fav ->

                                        if (fav !is Resource.Loading) {
                                            fragmentCategoriesBinding.pr.isVisible = false
                                        }
                                        when (fav) {
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    fav.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                fragmentCategoriesBinding.pr.isVisible = true
                                            }
                                            is Resource.NotStarted -> {}
                                            is Resource.Success -> {
                                                subCategoriesAdapter?.setFavorite(p, r, true)
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        { productId ->
                            if (!maiNViewModel.isLoggedIn()) {
                                startActivity(Intent(requireContext(), SignInActivity::class.java))
                                return@SubCategoriesAdapter
                            }
                            maiNViewModel.viewModelScope.launch {
                                maiNViewModel.addToCart(productId = productId.toString())
                                    .collect { add ->
                                        if (add !is Resource.Loading) {
                                            fragmentCategoriesBinding.pr.isVisible = false
                                        }
                                        when (add) {
                                            is Resource.Error -> {
                                                Toast.makeText(
                                                    requireContext(),
                                                    add.message,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            is Resource.Loading -> {
                                                fragmentCategoriesBinding.pr.isVisible = true
                                            }
                                            is Resource.NotStarted -> {}
                                            is Resource.Success -> {
                                                maiNViewModel.incrementCard()
                                                Toast.makeText(
                                                    requireActivity(),
                                                    "Added Successfully",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                    }
                            }
                        })
                    fragmentCategoriesBinding.rvSubcategories.adapter = subCategoriesAdapter

                }
            }

        }
    }

}
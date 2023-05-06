package com.example.grocerygo.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ActivityMainBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.home.Categories
import com.example.grocerygo.ui.home.HomeModel
import com.example.grocerygo.ui.home.PromotionsModel
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val navController: NavController? by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        lifecycleScope.launchWhenCreated {
            viewModel.cardBadgeNum.collect{badgeN ->
                val badge = binding.bottomNav.getOrCreateBadge(R.id.ordersFragment)
                if (badgeN > 0) {
                    badge.isVisible = true
                    badge.number = badgeN
                } else {
                    badge.isVisible = false
                    badge.number = 0
                }
            }

        }
        lifecycleScope.launch {
            val list = arrayListOf<HomeModel<*>>()
            viewModel.getOffers().collect { offerRes ->
                when (offerRes) {
                    is Resource.Error -> {
                        viewModel.pageModelState.value =
                            MainViewModel.Resource.Error(offerRes.message ?: "")
                    }
                    is Resource.Loading -> {
                        viewModel.pageModelState.value = MainViewModel.Resource.Loading
                    }
                    is Resource.NotStarted -> {}
                    is Resource.Success -> {
                        list.add(PromotionsModel(offerRes.data!!.products))
                        viewModel.getCategories().collect { categoryRes ->
                            if (categoryRes !is Resource.Loading) {
                                binding.pr.isVisible = false
                            }
                            when (categoryRes) {
                                is Resource.Error -> {
                                    viewModel.pageModelState.value =
                                        MainViewModel.Resource.Error(offerRes.message ?: "")
                                }
                                is Resource.Loading -> {
                                }
                                is Resource.NotStarted -> {}
                                is Resource.Success -> {
                                    list.add(Categories(categoryRes.data!!.categories))
                                    viewModel.pageModelState.value =
                                        MainViewModel.Resource.Success(list)
                                    viewModel.categoriesState.value = categoryRes.data.categories


                                }
                            }
                        }
                    }
                }
            }
        }

        binding.bottomNav.setupWithNavController(navController!!)
    }


    override fun onResume() {
        super.onResume()
        viewModel.getCardBadgeNumber()

    }


}




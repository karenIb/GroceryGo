package com.example.grocerygo.ui.orders

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.grocerygo.R
import com.example.grocerygo.databinding.FragmentOrdersBinding
import com.example.grocerygo.network.Resource
import com.example.grocerygo.ui.main.MainViewModel
import com.example.grocerygo.ui.placeorder.PlaceOrderActivity
import kotlinx.coroutines.launch


class OrdersFragment : Fragment() {


    lateinit var binding:FragmentOrdersBinding
    lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOrdersBinding.inflate(LayoutInflater.from(requireContext()))
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        super.onViewCreated(view, savedInstanceState)
        binding.rvOrderFromCart.layoutManager = LinearLayoutManager(requireContext())

        if (viewModel.isLoggedIn()) {
            viewLifecycleOwner.lifecycleScope.launchWhenResumed {
                getCart()
            }

            binding.materialButton2.setOnClickListener {
                startActivity(Intent(requireContext(), PlaceOrderActivity::class.java))
            }
        }




    }

      fun getCart() {
          viewModel.viewModelScope.launch {
              viewModel.getCart().collect{
                  if (it !is Resource.Loading) {
                      binding.pr.isVisible = false
                  }
                  when (it) {
                      is Resource.Error -> {
                          Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                      }
                      is Resource.Loading -> {
                          binding.pr.isVisible = true
                      }
                      is Resource.NotStarted -> {}
                      is Resource.Success -> {
                          if (it.data!!.cart == null )
                              return@collect
                          binding.tvTotal.text = "${it.data!!.cart!!.totalPrice} $"
                          binding.rvOrderFromCart.adapter = OrdersAdapter(it.data!!,{id->
                              viewModel.viewModelScope.launch {
                                  viewModel.addToCart(id.toString()).collect {
                                      if (it !is Resource.Loading) {
                                          binding.pr.isVisible = false
                                      }
                                      when (it) {
                                          is Resource.Error -> {
                                              Toast.makeText(
                                                  requireContext(),
                                                  it.message,
                                                  Toast.LENGTH_SHORT
                                              ).show()
                                          }
                                          is Resource.Loading -> {
                                              binding.pr.isVisible = true
                                          }
                                          is Resource.NotStarted -> {}
                                          is Resource.Success -> {
                                              viewModel.viewModelScope.launch{
                                                  getCart()
                                              }
                                          }
                                      }
                                  }
                              }

                          },{id->
                              viewModel.viewModelScope.launch {
                                  viewModel.decrementCard(id.toString()).collect{
                                      if (it !is Resource.Loading) {
                                          binding.pr.isVisible = false
                                      }
                                      when (it) {
                                          is Resource.Error -> {
                                              Toast.makeText(
                                                  requireContext(),
                                                  it.message,
                                                  Toast.LENGTH_SHORT
                                              ).show()
                                          }
                                          is Resource.Loading -> {
                                              binding.pr.isVisible = true
                                          }
                                          is Resource.NotStarted -> {}
                                          is Resource.Success -> {
                                              viewModel.viewModelScope.launch{
                                                  getCart()
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
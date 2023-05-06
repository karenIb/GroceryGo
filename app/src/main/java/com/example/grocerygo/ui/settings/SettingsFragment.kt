package com.example.grocerygo.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.grocerygo.R
import com.example.grocerygo.databinding.FragmentSettingsBinding
import com.example.grocerygo.ui.main.MainViewModel
import com.example.grocerygo.ui.signin.SignInActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SettingsFragment : Fragment() {


    lateinit var binding:FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        binding.btnSignout.setOnClickListener {
            viewModel.logout()
            requireActivity().finishAffinity()
            startActivity(Intent(requireContext(), SignInActivity::class.java))
        }

        viewModel.viewModelScope.launch {
            viewModel.getCart().collect{

            }
        }

        binding.mcFavorite.setOnClickListener {
        }

        binding.mcProfile.setOnClickListener {
        }
    }


}
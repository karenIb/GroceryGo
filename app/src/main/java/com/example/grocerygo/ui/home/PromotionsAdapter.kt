package com.example.grocerygo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.databinding.ItemDealBinding
import com.example.grocerygo.network.Product

class PromotionsAdapter(val data:List<Product>, val onPromotionClicked:(Product) -> Unit): Adapter<PromotionsAdapter.PromotionItemViewHolder>() {

    inner class PromotionItemViewHolder(private val binding: ItemDealBinding):ViewHolder(binding.root) {
        fun bindData(product: Product) {
            binding.sd.setImageURI(product.imageURL)
            binding.root.setOnClickListener {
                onPromotionClicked(product)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionItemViewHolder {
        return PromotionItemViewHolder(ItemDealBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: PromotionItemViewHolder, position: Int) {
        holder.bindData(data[position])
    }
}
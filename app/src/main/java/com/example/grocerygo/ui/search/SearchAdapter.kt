package com.example.grocerygo.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.databinding.ItemSearchItemBinding
import com.example.grocerygo.network.Product

class SearchAdapter(
    private val products: List<Product>,
    private val onProductSelected: (String) -> Unit,
    private val onCategorySelected: (String) -> Unit
) : Adapter<SearchAdapter.SearchVH>() {

    inner class SearchVH(private val itemSearchItemBinding: ItemSearchItemBinding) :
        ViewHolder(itemSearchItemBinding.root) {
        fun bindCat(product: Product) {
            itemSearchItemBinding.sdproduct.setImageURI(product.actualImage())
            itemSearchItemBinding.ivOutOfStock.isVisible = product.quantity == 0
            itemSearchItemBinding.tvProductName.text = product.name
            itemSearchItemBinding.tvCategory.text = product.subcategory
            itemSearchItemBinding.mcResult.setOnClickListener {
                onProductSelected(product.id)
            }
            itemSearchItemBinding.tvCategory.setOnClickListener {
                onCategorySelected(product.subcategoryId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        return SearchVH(ItemSearchItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.bindCat(products[position])
    }
}
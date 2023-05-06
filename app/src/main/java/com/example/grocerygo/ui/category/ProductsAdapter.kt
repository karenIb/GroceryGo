package com.example.grocerygo.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ItemProductItemBinding
import com.example.grocerygo.network.Product

class ProductsAdapter(
    val products: List<Product>,
    val onProductSelected: (Int) -> Unit,
    val onFavorite: (Int,Int) -> Unit,
    val onProductAdded: (Int) -> Unit
) : Adapter<ProductsAdapter.ProductsVH>() {


    inner class ProductsVH(val binding: ItemProductItemBinding) : ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.sdTopCat.setImageURI(product.imageURL)
            binding.tvSdName.text = product.name
            binding.tvPrice.text = "${product.price} $"

            binding.btnAddToCard.setOnClickListener {
                onProductAdded(product.id.toInt())
            }

            binding.root.setOnClickListener {
                onProductSelected(product.id.toInt())
            }

            binding.ivFavorite.setOnClickListener {
                onFavorite(adapterPosition, product.id.toInt())
            }

            if (product.discountedPrice != product.price) {
                binding.tvDiscounted.text = "${product.discountedPrice} $"
                binding.line.isVisible = true
                binding.tvDiscounted.isVisible = true
            } else {
                binding.line.isVisible = false
                binding.tvDiscounted.isVisible = false

            }

            binding.ivFavorite.setImageResource(
                if (product.isFavorite)
                    R.drawable.favorite
            else
                R.drawable.un_favorite
                )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        return ProductsVH(ItemProductItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun favorite(position: Int) {
        products[position].isFavorite = !products[position].isFavorite
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        holder.bind(products[position])
    }


}
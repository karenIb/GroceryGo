package com.example.grocerygo.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.databinding.ItemTopCatBinding
import com.example.grocerygo.network.Category

class TopCategoriesAdapter(private val topCats:List<Category>, val onCategoryClicked:(Category) -> Unit): Adapter<TopCategoriesAdapter.TopCatVH>() {


    inner class TopCatVH(private val bind:ItemTopCatBinding):ViewHolder(bind.root) {

        fun bindCategory(category: Category) {
            bind.sdTopCat.setImageURI(category.imageURL)
            bind.tvSdName.text = category.name
            bind.root.setOnClickListener {
                onCategoryClicked(category)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopCatVH {
        return TopCatVH(ItemTopCatBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return topCats.size
    }

    override fun onBindViewHolder(holder: TopCatVH, position: Int) {
        holder.bindCategory(topCats[position])
    }
}
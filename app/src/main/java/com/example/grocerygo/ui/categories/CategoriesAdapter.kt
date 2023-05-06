package com.example.grocerygo.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ItemCategoriesBinding
import com.example.grocerygo.network.Category

class CategoriesAdapter(
    private val categories: List<Category>,
    private val onCategorySelected: (Category) -> Unit
) : Adapter<CategoriesAdapter.CategoriesVH>() {

    private var selectedITemIndex = 0


    fun select(position: Int) {
        if (position >= categories.size)
            return
        val previous = selectedITemIndex
        selectedITemIndex = position
        notifyItemChanged(previous)
        notifyItemChanged(selectedITemIndex)
        onCategorySelected(categories[position])
    }

    inner class CategoriesVH(private val itemCategoriesBinding: ItemCategoriesBinding) :
        ViewHolder(itemCategoriesBinding.root) {

        fun bindCategory(category: Category) {
            itemCategoriesBinding.tvCategoryName.text = category.name
            if (selectedITemIndex == adapterPosition) {
                itemCategoriesBinding.mcLine.isInvisible = false
                itemCategoriesBinding.tvCategoryName.setTextColor(
                    itemCategoriesBinding.root.context.getColor(
                        R.color.purple_700
                    )
                )
                itemCategoriesBinding.ll.setBackgroundColor(
                    itemCategoriesBinding.root.context.getColor(
                        R.color.pure_white
                    )
                )

            } else {
                itemCategoriesBinding.mcLine.isInvisible = true
                itemCategoriesBinding.ll.setBackgroundColor(
                    itemCategoriesBinding.root.context.getColor(
                        R.color.white
                    )
                )

                itemCategoriesBinding.tvCategoryName.setTextColor(
                    itemCategoriesBinding.root.context.getColor(
                        R.color.black
                    )
                )
            }

            itemCategoriesBinding.root.setOnClickListener {
                select(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesVH {
        return CategoriesVH(ItemCategoriesBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: CategoriesVH, position: Int) {
        holder.bindCategory(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }
}
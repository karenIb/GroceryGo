package com.example.grocerygo.ui.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.R
import com.example.grocerygo.databinding.ItemSubcategoryBinding
import com.example.grocerygo.network.Product
import com.example.grocerygo.ui.category.ProductsAdapter
import com.example.grocerygo.ui.home.SpacingItemDecorator

class SubCategoriesAdapter(
    private val subcategoryProducts: List<SubcategoryProducts>,
    private val onSubScatSelected: (SubcategoryProducts, Int) -> Unit,
    private val onProductSelected: (Int) -> Unit,
    private val onFavorite: (Int, Int, Int, Boolean) -> Unit,
    private val onItemAdded: (Int) -> Unit
) : Adapter<SubCategoriesAdapter.SubCategoriesVH>() {

    private var selected = -1

    fun select(position: Int) {
        if (selected == position) {
            selected = -1
            notifyItemChanged(position)
            return
        }

        val prev = selected
        selected = position
        notifyItemChanged(prev)
        notifyItemChanged(selected)
        onSubScatSelected(subcategoryProducts[position], position)
    }

    inner class SubCategoriesVH(private val itemSubcategoryBinding: ItemSubcategoryBinding) :
        ViewHolder(itemSubcategoryBinding.root) {

        fun bindItem(subcategoryProduct: SubcategoryProducts) {

            if (selected != adapterPosition) {
                itemSubcategoryBinding.rvProducts.isVisible = false
                itemSubcategoryBinding.tvSubTitle.setTextColor(
                    itemSubcategoryBinding.root.context.getColor(
                        R.color.black
                    )
                )
            } else {
                itemSubcategoryBinding.rvProducts.isVisible = true
                itemSubcategoryBinding.tvSubTitle.setTextColor(
                    itemSubcategoryBinding.root.context.getColor(
                        R.color.purple_700
                    )
                )
            }

            itemSubcategoryBinding.rvProducts.layoutManager =
                GridLayoutManager(itemSubcategoryBinding.root.context, 2)
            if (itemSubcategoryBinding.rvProducts.itemDecorationCount == 0) {
                itemSubcategoryBinding.rvProducts.addItemDecoration(SpacingItemDecorator(10))
            }
            itemSubcategoryBinding.rvProducts.adapter = ProductsAdapter(subcategoryProduct.products,
                {
                    onProductSelected(it)
                },
                { r, s ->
                    onFavorite(adapterPosition, r, s, subcategoryProduct.products[r].isFavorite)
                },
                {
                    onItemAdded(it)
                })

            itemSubcategoryBinding.tvSubTitle.text = subcategoryProduct.subcategory.name
            itemSubcategoryBinding.tvSubTitle.setOnClickListener {
                select(adapterPosition)
            }
        }
    }

    fun setProducts(position: Int, products: List<Product>) {
        subcategoryProducts[position].products = products
        notifyItemChanged(position)
    }

    fun setFavorite(parentPosition: Int, position: Int, isFavorite: Boolean) {
        subcategoryProducts[parentPosition].products[position].isFavorite = isFavorite
        notifyItemChanged(parentPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoriesVH {
        return SubCategoriesVH(ItemSubcategoryBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
        return subcategoryProducts.size
    }

    override fun onBindViewHolder(holder: SubCategoriesVH, position: Int) {
        holder.bindItem(subcategoryProducts[position])
    }
}
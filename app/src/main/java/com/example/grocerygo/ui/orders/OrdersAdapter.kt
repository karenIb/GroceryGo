package com.example.grocerygo.ui.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.databinding.ItemOrderBinding
import com.example.grocerygo.network.CartResponse
import com.example.grocerygo.network.Product

class OrdersAdapter(
    private val cart: CartResponse,
    val onIncrement: (Int) -> Unit,
    val onDecrement: (Int) -> Unit
) : Adapter<OrdersAdapter.OrdersVH>() {


    private val products = cart.cart!!.products


    inner class OrdersVH(private val itemOrderBinding: ItemOrderBinding) :
        ViewHolder(itemOrderBinding.root) {
        fun bindOrder(product: Product) {

            itemOrderBinding.tvName.text = product.name
            itemOrderBinding.textView6.text = "${(product.discountedPrice* product.pickedQuantity)} $"
            itemOrderBinding.tvQuantity.text = product.pickedQuantity.toString()

            itemOrderBinding.ivIncrement.isEnabled = product.quantity != 0

            itemOrderBinding.ivIncrement.setOnClickListener {
                onIncrement(product.id.toInt())
            }

            itemOrderBinding.ivDecrement.setOnClickListener {
                onDecrement(product.id.toInt())
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersVH {
        return OrdersVH(ItemOrderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OrdersVH, position: Int) {
        holder.bindOrder(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }


}
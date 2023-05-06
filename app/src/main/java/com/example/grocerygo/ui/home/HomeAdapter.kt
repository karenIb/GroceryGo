package com.example.grocerygo.ui.home

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.grocerygo.databinding.ItemCategoriesMainBinding
import com.example.grocerygo.databinding.ItemDealsBinding
import com.example.grocerygo.network.Category
import com.example.grocerygo.network.Product
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.delay
import java.lang.Math.abs

class HomeAdapter(
    val lifecycleScope: LifecycleCoroutineScope,
    val onPromotionClicked: (Product) -> Unit,
    val onCategoryClicked: (Category) -> Unit
) : Adapter<ViewHolder>() {

    private val data = ArrayList<HomeModel<*>>()

    fun setData(list: List<HomeModel<*>>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }


    inner class PromotionVH(private val binding: ItemDealsBinding) : ViewHolder(binding.root) {
        fun bindData(toBind: List<Product>) {

            binding.vpDeals.apply {
                clipChildren = false  // No clipping the left and right items
                clipToPadding =
                    false  // Show the viewpager in full width without clipping the padding
                offscreenPageLimit = 3  // Render the left and right items
                (getChildAt(0) as RecyclerView).overScrollMode =
                    RecyclerView.OVER_SCROLL_NEVER // Remove the scroll effect
            }
            val adapter = PromotionsAdapter(toBind) { product ->
                onPromotionClicked(product)
            }

            binding.vpDeals.adapter = adapter

            TabLayoutMediator(binding.tbL, binding.vpDeals) { _, _ ->
            }.attach()


            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer((40 * Resources.getSystem().displayMetrics.density).toInt()))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = (0.80f + r * 0.20f)
            }
            binding.vpDeals.setPageTransformer(compositePageTransformer)

            lifecycleScope.launchWhenResumed {
                infiniteScroll(binding.vpDeals, 0, toBind.size - 1)
            }
        }
    }

    inner class TopCategoriesVH(private val binding: ItemCategoriesMainBinding) :
        ViewHolder(binding.root) {
        fun bindData(toBind: List<Category>) {
            binding.rvTopCategories.layoutManager = GridLayoutManager(itemView.context, 2)
            binding.rvTopCategories.addItemDecoration(SpacingItemDecorator(15))
            binding.rvTopCategories.adapter = TopCategoriesAdapter(toBind) {
                onCategoryClicked(it)
            }

        }
    }

    private suspend fun infiniteScroll(pager: ViewPager2, currentIndex: Int, indexSize: Int) {
        if (indexSize <= 0)
            return
        delay(1500)
        if (currentIndex <= indexSize) {
            val nextItemIndex = currentIndex + 1
            pager.setCurrentItem(nextItemIndex, 500)
            infiniteScroll(pager, nextItemIndex, indexSize)
        } else {
            pager.setCurrentItem(0, 300)
            infiniteScroll(pager, 0, indexSize)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 0) {
            PromotionVH(ItemDealsBinding.inflate(LayoutInflater.from(parent.context)))
        } else {
            TopCategoriesVH(ItemCategoriesMainBinding.inflate(LayoutInflater.from(parent.context)))
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder is PromotionVH) {
            val model = data[position] as PromotionsModel
            holder.bindData(model.data)
        }

        if (holder is TopCategoriesVH) {
            val model = data[position] as Categories
            holder.bindData(model.data)
        }
    }


}

fun ViewPager2.setCurrentItem(
    item: Int,
    duration: Long,
    interpolator: TimeInterpolator = AccelerateDecelerateInterpolator(),
    pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
) {
    val pxToDrag: Int = pagePxWidth * (item - currentItem)
    val animator = ValueAnimator.ofInt(0, pxToDrag)
    var previousValue = 0
    animator.addUpdateListener { valueAnimator ->
        val currentValue = valueAnimator.animatedValue as Int
        val currentPxToDrag = (currentValue - previousValue).toFloat()
        fakeDragBy(-currentPxToDrag)
        previousValue = currentValue
    }
    animator.addListener(object : Animator.AnimatorListener {
        override fun onAnimationStart(animation: Animator) {
            beginFakeDrag()
        }

        override fun onAnimationEnd(animation: Animator) {
            endFakeDrag()
        }

        override fun onAnimationCancel(animation: Animator) { /* Ignored */
        }

        override fun onAnimationRepeat(animation: Animator) { /* Ignored */
        }
    })
    animator.interpolator = interpolator
    animator.duration = duration
    animator.start()
}
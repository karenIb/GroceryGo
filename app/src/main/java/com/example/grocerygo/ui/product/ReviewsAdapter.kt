package com.example.grocerygo.ui.product

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.grocerygo.databinding.ItemRatingBinding
import com.example.grocerygo.network.Review

class ReviewsAdapter(private val reviews:List<Review>,
                     private val onDeleteReview:(Int)->Unit):Adapter<ReviewsAdapter.ReviewVH>() {

    inner class ReviewVH(private val itemRatingBinding: ItemRatingBinding):ViewHolder(itemRatingBinding.root) {

        fun bindReview(review: Review) {
            itemRatingBinding.tvName.text = if (review.isMine)
                "Me"
            else
                review.customerName
            itemRatingBinding.tvDate.text = review.date
            itemRatingBinding.tvComment.text = review.comment
            itemRatingBinding.rbRating.rating = review.stars.toFloat()

            if (review.isMine) {
                itemRatingBinding.ivDelete.isVisible = true
                itemRatingBinding.ivDelete.setOnClickListener {
                    onDeleteReview(review.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewVH {
        return ReviewVH(ItemRatingBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount(): Int {
       return reviews.size
    }

    override fun onBindViewHolder(holder: ReviewVH, position: Int) {
        holder.bindReview(reviews[position])
    }
}
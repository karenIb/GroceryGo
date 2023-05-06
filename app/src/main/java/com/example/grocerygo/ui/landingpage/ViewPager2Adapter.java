package com.example.grocerygo.ui.landingpage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.grocerygo.R;

class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    // Array of images
    // Adding images from drawable folder
    private final int[] images = {R.drawable.onbaording_1, R.drawable.onbaording_2, R.drawable.onbaording_3};
    private final String[] titles = {"Discover", "Make the payment", "Enjoy your shopping"};
    private final String[] descs = {
            "Explore the top most grocery products sales",
            "Make the payment online",
            "Get high quality products for the best prices"
    };
    private final Context ctx;

    // Constructor of our ViewPager2Adapter class
    ViewPager2Adapter(Context ctx) {
        this.ctx = ctx;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.item_landing, parent, false);
        return new ViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.images.setImageResource(images[position]);
        holder.tvTitle.setText(titles[position]);
        holder.tvDetails.setText(descs[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView tvTitle;
        TextView tvDetails;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.imageView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDetails = itemView.findViewById(R.id.tv_details);
        }
    }
}
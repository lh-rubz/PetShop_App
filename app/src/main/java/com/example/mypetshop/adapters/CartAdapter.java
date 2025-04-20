package com.example.mypetshop.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypetshop.R;
import com.example.mypetshop.models.CartItem;
import com.example.mypetshop.utils.SharedPrefManager;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private CartListener listener;
    private SharedPrefManager sharedPrefManager;

    public interface CartListener {
        void onQuantityChanged(String productId, int newQuantity);
        void onItemRemoved(String productId);
    }

    public CartAdapter(List<CartItem> cartItems, CartListener listener, SharedPrefManager sharedPrefManager) {
        this.cartItems = cartItems;
        this.listener = listener;
        this.sharedPrefManager = sharedPrefManager;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        boolean isAvailable = sharedPrefManager.isProductAvailable(
                cartItem.getProduct().getId(),
                cartItem.getQuantity()
        );

        // Set item details
        holder.ivProductImage.setImageResource(cartItem.getProduct().getImageRes());
        holder.tvProductName.setText(cartItem.getProduct().getName());
        holder.tvProductPrice.setText(cartItem.getProduct().getPrice());
        holder.tvQuantity.setText(String.valueOf(cartItem.getQuantity()));
        holder.tvItemTotal.setText(String.format("$%.2f", cartItem.getTotalPrice()));

        // Visual treatment for out-of-stock items
        if (!isAvailable) {
            holder.itemView.setAlpha(0.5f);
            holder.tvOutOfStock.setVisibility(View.VISIBLE);
            // Disable quantity buttons for out-of-stock items
            holder.btnIncrease.setEnabled(false);
            holder.btnDecrease.setEnabled(false);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.tvOutOfStock.setVisibility(View.GONE);
            holder.btnIncrease.setEnabled(true);
            holder.btnDecrease.setEnabled(true);
        }

        // Set up quantity change listeners
        holder.btnIncrease.setOnClickListener(v -> {
            if (isAvailable) { // Only allow increasing if available
                int newQuantity = cartItem.getQuantity() + 1;
                // Check if the increased quantity is still available
                if (sharedPrefManager.isProductAvailable(cartItem.getProduct().getId(), newQuantity)) {
                    listener.onQuantityChanged(cartItem.getProduct().getId(), newQuantity);
                    cartItem.setQuantity(newQuantity);
                    holder.tvQuantity.setText(String.valueOf(newQuantity));
                    holder.tvItemTotal.setText(String.format("$%.2f", cartItem.getTotalPrice()));
                } else {
                    // Show message that requested quantity isn't available
                    holder.tvOutOfStock.setVisibility(View.VISIBLE);
                    holder.itemView.setAlpha(0.5f);
                    holder.btnIncrease.setEnabled(false);
                    holder.btnDecrease.setEnabled(false);
                }
            }
        });

        holder.btnDecrease.setOnClickListener(v -> {
            if (cartItem.getQuantity() > 1) {
                int newQuantity = cartItem.getQuantity() - 1;
                listener.onQuantityChanged(cartItem.getProduct().getId(), newQuantity);
                cartItem.setQuantity(newQuantity);
                holder.tvQuantity.setText(String.valueOf(newQuantity));
                holder.tvItemTotal.setText(String.format("$%.2f", cartItem.getTotalPrice()));
            } else {
                listener.onItemRemoved(cartItem.getProduct().getId());
                cartItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            listener.onItemRemoved(cartItem.getProduct().getId());
            cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, cartItems.size());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProductImage;
        TextView tvProductName, tvProductPrice, tvQuantity, tvItemTotal, tvOutOfStock;
        Button btnIncrease, btnDecrease, btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.iv_product_image);
            tvProductName = itemView.findViewById(R.id.tv_product_name);
            tvProductPrice = itemView.findViewById(R.id.tv_product_price);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvItemTotal = itemView.findViewById(R.id.tv_item_total);
            tvOutOfStock = itemView.findViewById(R.id.tv_out_of_stock);
            btnIncrease = itemView.findViewById(R.id.btn_increase);
            btnDecrease = itemView.findViewById(R.id.btn_decrease);
            btnRemove = itemView.findViewById(R.id.btn_remove);
        }
    }
}
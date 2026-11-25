package com.example.gearhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearhub.R;
import com.example.gearhub.model.CartItem;
import com.example.gearhub.model.Product;
import com.example.gearhub.util.HomeProductImages;
import com.example.gearhub.util.PriceFormatter;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter quản lý hiển thị danh sách CartItem trong CartActivity.
 * Cung cấp callback lắng nghe khi tăng, giảm số lượng hoặc xóa sản phẩm.
 */
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnCartItemChangeListener {
        void onQuantityChanged(CartItem item, int newQuantity);
        void onItemRemoved(CartItem item);
    }

    private final List<CartItem> cartItems = new ArrayList<>();
    private final OnCartItemChangeListener listener;

    public CartAdapter(OnCartItemChangeListener listener) {
        this.listener = listener;
    }

    public void submitList(List<CartItem> newItems) {
        cartItems.clear();
        if (newItems != null) {
            cartItems.addAll(newItems);
        }
        notifyDataSetChanged();
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
        CartItem item = cartItems.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgProduct;
        private final TextView txtBrand;
        private final TextView txtName;
        private final TextView txtPrice;
        private final TextView txtQuantity;
        private final MaterialButton btnMinus;
        private final MaterialButton btnPlus;
        private final ImageView btnRemove;

        CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgCartProduct);
            txtBrand = itemView.findViewById(R.id.txtCartBrand);
            txtName = itemView.findViewById(R.id.txtCartName);
            txtPrice = itemView.findViewById(R.id.txtCartPrice);
            txtQuantity = itemView.findViewById(R.id.txtCartQuantity);
            btnMinus = itemView.findViewById(R.id.btnCartMinus);
            btnPlus = itemView.findViewById(R.id.btnCartPlus);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }

        void bind(CartItem item, OnCartItemChangeListener listener) {
            Product product = item.getProduct();

            imgProduct.setImageResource(HomeProductImages.forProduct(product));
            txtBrand.setText(product.getBrand());
            txtName.setText(product.getName());
            txtPrice.setText(PriceFormatter.format(item.getTotalPrice()));
            txtQuantity.setText(String.valueOf(item.getQuantity()));

            btnMinus.setOnClickListener(v -> {
                if (listener != null) {
                    int currentQty = item.getQuantity();
                    if (currentQty > 1) {
                        listener.onQuantityChanged(item, currentQty - 1);
                    } else {
                        // Nếu bấm giảm khi số lượng là 1 thì hỏi xóa hoặc xóa luôn
                        listener.onItemRemoved(item);
                    }
                }
            });

            btnPlus.setOnClickListener(v -> {
                if (listener != null) {
                    int currentQty = item.getQuantity();
                    if (currentQty < 99) {
                        listener.onQuantityChanged(item, currentQty + 1);
                    }
                }
            });

            btnRemove.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemRemoved(item);
                }
            });
        }
    }
}

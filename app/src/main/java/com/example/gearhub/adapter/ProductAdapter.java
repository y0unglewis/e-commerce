package com.example.gearhub.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.gearhub.R;
import com.example.gearhub.model.Product;
import com.example.gearhub.util.DetailProductImages;
import com.example.gearhub.util.PriceFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Chuyển List<Product> thành các card trong lưới Home. */
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    private final List<Product> products = new ArrayList<>();
    private final OnProductClickListener listener;

    public ProductAdapter(OnProductClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> updated) {
        products.clear();
        products.addAll(updated);
        notifyDataSetChanged(); // Danh sách mẫu nhỏ; TV1 có thể chuyển sang DiffUtil sau.
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = products.get(position);
        holder.image.setImageResource(DetailProductImages.forProduct(product));
        holder.brand.setText(product.getBrand());
        holder.name.setText(product.getName());
        holder.rating.setText(String.format(Locale.getDefault(), "★ %.1f", product.getRating()));
        holder.price.setText(PriceFormatter.format(product.getPrice()));
        holder.originalPrice.setText(PriceFormatter.format(product.getOriginalPrice()));
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(view -> listener.onProductClick(product));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView brand, name, rating, price, originalPrice;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgProduct);
            brand = itemView.findViewById(R.id.txtBrand);
            name = itemView.findViewById(R.id.txtName);
            rating = itemView.findViewById(R.id.txtRating);
            price = itemView.findViewById(R.id.txtPrice);
            originalPrice = itemView.findViewById(R.id.txtOriginalPrice);
        }
    }
}

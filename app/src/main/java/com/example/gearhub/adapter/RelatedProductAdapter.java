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
import com.example.gearhub.util.HomeProductImages;
import com.example.gearhub.util.PriceFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/** Danh sách sản phẩm liên quan cuộn ngang tại màn Product Detail. */
public class RelatedProductAdapter extends RecyclerView.Adapter<RelatedProductAdapter.Holder> {
    public interface OnRelatedClickListener {
        void onRelatedClick(Product product);
    }

    private final List<Product> items = new ArrayList<>();
    private final OnRelatedClickListener listener;

    public RelatedProductAdapter(OnRelatedClickListener listener) {
        this.listener = listener;
    }

    public void setProducts(List<Product> products) {
        items.clear();
        items.addAll(products);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_related_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Product product = items.get(position);
        holder.image.setImageResource(HomeProductImages.forProduct(product));
        holder.image.setContentDescription(product.getName());
        holder.brand.setText(product.getBrand());
        holder.name.setText(product.getName());
        holder.rating.setText(String.format(Locale.getDefault(), "★ %.1f", product.getRating()));
        holder.price.setText(PriceFormatter.format(product.getPrice()));
        holder.originalPrice.setText(PriceFormatter.format(product.getOriginalPrice()));
        holder.originalPrice.setPaintFlags(holder.originalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(v -> listener.onRelatedClick(product));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        final ImageView image;
        final TextView brand, name, rating, price, originalPrice;

        Holder(@NonNull View view) {
            super(view);
            image = view.findViewById(R.id.imgRelatedProduct);
            brand = view.findViewById(R.id.txtRelatedBrand);
            name = view.findViewById(R.id.txtRelatedName);
            rating = view.findViewById(R.id.txtRelatedRating);
            price = view.findViewById(R.id.txtRelatedPrice);
            originalPrice = view.findViewById(R.id.txtRelatedOriginalPrice);
        }
    }
}

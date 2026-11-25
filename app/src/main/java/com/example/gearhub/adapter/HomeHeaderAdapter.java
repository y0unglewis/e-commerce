package com.example.gearhub.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gearhub.R;

/** Banner, tiêu đề và thông báo rỗng nằm ở đầu danh sách Home. */
public class HomeHeaderAdapter extends RecyclerView.Adapter<HomeHeaderAdapter.HeaderViewHolder> {
    private boolean empty;

    public void setEmpty(boolean empty) {
        if (this.empty == empty) return;
        this.empty = empty;
        notifyItemChanged(0);
    }

    @NonNull
    @Override
    public HeaderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HeaderViewHolder holder, int position) {
        holder.emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        final TextView emptyText;

        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            emptyText = itemView.findViewById(R.id.txtEmpty);
        }
    }
}

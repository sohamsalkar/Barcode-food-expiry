package com.example.soham_pc.whenexpires.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.soham_pc.whenexpires.database.entity.ProductEntity;
import com.example.soham_pc.whenexpires.R;
import com.example.soham_pc.whenexpires.utils.Methods;

import java.util.List;

public class ProductsRecyclerAdapter extends RecyclerView.Adapter<ProductsRecyclerAdapter.RecyclerViewHolder> {

    private final List<ProductEntity> products;
    Context context;

    public ProductsRecyclerAdapter(List<ProductEntity> products, Context context) {
        this.products = products;
        this.context = context;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecyclerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_recycer_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        ProductEntity product = products.get(position);
        holder.tvProductName.setText(product.getProductName());
        holder.tvCount.setText(product.getDaysRemaining());

        int days = (int) new Methods().getRemainingDays(product.getExpiryDate());
        String dayFormat = " day left to expire";
        if (days < 5) {

            if (days < 1) {
                holder.tvDayRem.setText("Expired");
                holder.tvDayRem.setBackgroundColor(context.getResources().getColor(R.color.light_grey));
            } else {
                if (days > 1) {
                    dayFormat = " days left to expire";
                }
                holder.tvDayRem.setText("" + new Methods().getRemainingDays(product.getExpiryDate()) + dayFormat);
                holder.tvDayRem.setBackgroundColor(context.getResources().getColor(R.color.orange));
            }
        } else {
            dayFormat = " days left to expire";
            holder.tvDayRem.setText("" + new Methods().getRemainingDays(product.getExpiryDate()) + " day (s) left to expire");
            holder.tvDayRem.setBackgroundColor(context.getResources().getColor(R.color.copia_green));
        }

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    /*public void addItems(List<BucketListModel> bucketListModelList) {
        this.bucketListModelList = bucketListModelList;
        notifyDataSetChanged();
    }*/

    static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvProductName;
        private final TextView tvDayRem;
        private final TextView tvCount;

        RecyclerViewHolder(View view) {
            super(view);
            tvProductName = view.findViewById(R.id.prodName);
            tvDayRem = view.findViewById(R.id.daysTv);
            tvCount = view.findViewById(R.id.tvCount);
        }
    }
}
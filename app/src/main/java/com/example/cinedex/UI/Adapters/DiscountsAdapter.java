package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cinedex.R;
import com.example.cinedex.Data.Models.Discount;

import java.util.List;

public class DiscountsAdapter extends RecyclerView.Adapter<DiscountsAdapter.VH> {

    private final Context context;
    private final List<Discount> list;

    public DiscountsAdapter(Context context, List<Discount> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_descuento_enhanced, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Discount d = list.get(position);
        if (holder.title != null) holder.title.setText(d.title);
        if (holder.subtitle != null) holder.subtitle.setText(d.subtitle);
        if (holder.validity != null) holder.validity.setText(d.validity);
        if (holder.image != null) Glide.with(context).load(d.imageUrl).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, subtitle, validity;
        ImageView image;
        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.item_disc_title);
            subtitle = v.findViewById(R.id.item_disc_subtitle);
            validity = v.findViewById(R.id.item_disc_validity);
            image = v.findViewById(R.id.item_disc_image);
        }
    }
}

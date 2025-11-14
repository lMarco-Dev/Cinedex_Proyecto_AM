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
import com.example.cinedex.Data.Models.Announcement;

import java.util.List;

public class AnnouncementsAdapter extends RecyclerView.Adapter<AnnouncementsAdapter.VH> {

    private final Context context;
    private final List<Announcement> list;

    public AnnouncementsAdapter(Context context, List<Announcement> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_anuncio_enhanced, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Announcement a = list.get(position);
        if (holder.title != null) holder.title.setText(a.title);
        if (holder.subtitle != null) holder.subtitle.setText(a.subtitle);
        if (holder.image != null) {
            Glide.with(context).load(a.imageUrl).centerCrop().into(holder.image);
        }
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView image;
        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.item_ann_title);
            subtitle = v.findViewById(R.id.item_ann_subtitle);
            image = v.findViewById(R.id.item_ann_image);
        }
    }
}

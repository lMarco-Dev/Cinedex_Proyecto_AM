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
import com.example.cinedex.Data.Models.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {

    private final Context context;
    private final List<News> list;

    public NewsAdapter(Context context, List<News> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_noticia_enhanced, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        News n = list.get(position);
        if (holder.title != null) holder.title.setText(n.title);
        if (holder.summary != null) holder.summary.setText(n.summary);
        if (holder.meta != null) holder.meta.setText(n.meta);
        if (holder.image != null) Glide.with(context).load(n.imageUrl).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() { return list.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView title, summary, meta;
        ImageView image;
        VH(@NonNull View v) {
            super(v);
            title = v.findViewById(R.id.item_news_title);
            summary = v.findViewById(R.id.item_news_summary);
            meta = v.findViewById(R.id.item_news_meta);
            image = v.findViewById(R.id.item_news_image);
        }
    }
}

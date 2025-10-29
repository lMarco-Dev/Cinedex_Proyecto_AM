package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cinedex.R;
import com.example.cinedex.Data.Models.Section;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.SectionViewHolder> {

    private List<Section> sectionList;
    private Context context;

    public MainAdapter(List<Section> sectionList, Context context) {
        this.sectionList = sectionList;
        this.context = context;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sectionList.get(position);

        holder.titleTextView.setText(section.getTitle());

        // 🚨 Configurar el RecyclerView HORIZONTAL
        holder.sectionRecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        );

        // Crear y asignar el adaptador de películas (el que ya tenías)
        MovieAdapter movieAdapter = new MovieAdapter(section.getMovieList(), context);
        holder.sectionRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    // Método para actualizar la lista de secciones
    public void setSections(List<Section> newSections) {
        this.sectionList = newSections;
        notifyDataSetChanged();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public RecyclerView sectionRecyclerView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.section_title);
            sectionRecyclerView = itemView.findViewById(R.id.section_recycler_view);
        }
    }
}
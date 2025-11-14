package com.example.cinedex.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinedex.Data.Models.DTOs.ResenaPublicaDto;
import com.example.cinedex.R;

import java.util.List;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {

    private List<ResenaPublicaDto> lista;

    public ResenaAdapter(List<ResenaPublicaDto> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ResenaPublicaDto r = lista.get(position);

        // Setear datos de la reseña
        holder.txtComentario.setText(r.getComentario());
        holder.txtPelicula.setText("Película: " + r.getTituloPelicula());
        holder.ratingBar.setRating(r.getCalificacion());
        holder.fecha.setText(r.getFecha());

        Glide.with(holder.itemView.getContext())
                .load(r.getPosterPeliculaURL())
                .placeholder(R.drawable.bg_poster_placeholder) // mientras carga
                .error(R.drawable.bg_poster_placeholder)
                .centerCrop()
                .into(holder.imgPoster);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtComentario, txtPelicula, fecha;
        RatingBar ratingBar;
        ImageView imgPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtComentario = itemView.findViewById(R.id.txtComentario);
            txtPelicula = itemView.findViewById(R.id.txtPelicula);
            fecha = itemView.findViewById(R.id.txtFecha);
            ratingBar = itemView.findViewById(R.id.ratingBarItem);
            imgPoster = itemView.findViewById(R.id.imgPoster); // ImageView del layout
        }
    }
}

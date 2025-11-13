package com.example.cinedex.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinedex.Data.Models.Reseña;
import com.example.cinedex.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {

    private List<Reseña> lista;

    public ResenaAdapter(List<Reseña> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ResenaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenaAdapter.ViewHolder holder, int position) {
        Reseña r = lista.get(position);
        holder.tvUser.setText(r.getUsuario() != null ? r.getUsuario().getNombreUsuario() : "Anon");
        holder.tvComment.setText(r.getReseñaTexto());
        holder.ratingBar.setRating(r.getPuntuacion());
        if (r.getFechaColeccion() != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.tvDate.setText(df.format(r.getFechaColeccion()));
        } else holder.tvDate.setText("");
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvUser, tvComment, tvDate;
        RatingBar ratingBar;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUser = itemView.findViewById(R.id.itemResenaUser);
            tvComment = itemView.findViewById(R.id.itemResenaComment);
            tvDate = itemView.findViewById(R.id.itemResenaDate);
            ratingBar = itemView.findViewById(R.id.itemResenaRating);
        }
    }

    // Para actualizar datos desde la Activity
    public void updateData(List<Reseña> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }
}


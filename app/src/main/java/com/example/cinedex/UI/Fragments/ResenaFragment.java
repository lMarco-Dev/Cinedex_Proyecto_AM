package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private Context ctx;

    public ResenaAdapter(List<Reseña> lista, Context ctx) {
        this.lista = lista;
        this.ctx = ctx;
    }

    @NonNull
    @Override
    public ResenaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenaAdapter.ViewHolder holder, int position) {
        Reseña r = lista.get(position);

        holder.itemNombre.setText(r.getUsuario() != null ? r.getUsuario().getNombreUsuario() : "Anónimo");
        String meta = (r.getPelicula() != null ? r.getPelicula().getTitle() : "Película") +
                " · " + r.getPuntuacion() + " ★";
        holder.itemMeta.setText(meta);

        holder.itemComentario.setText(r.getReseñaTexto() != null ? r.getReseñaTexto() : "");
        if (r.getFechaColeccion() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            // holder.itemFecha.setText(sdf.format(r.getFechaColeccion()));
        }

        // ubicación (puede ser null)
        if (r.getUsuario() != null) {
            // no hacemos nada con avatar por ahora
        }
        holder.itemUbicacion.setText(r.getFechaColeccion() != null ? "Fecha: " + r.getFechaColeccion().toString() : "");
        // si tienes campo de ubicacion en tu modelo, muéstralo
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void setData(List<Reseña> data) {
        this.lista = data;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemAvatar;
        TextView itemNombre, itemMeta, itemComentario, itemUbicacion;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemAvatar = itemView.findViewById(R.id.item_avatar);
            itemNombre = itemView.findViewById(R.id.item_nombre);
            itemMeta = itemView.findViewById(R.id.item_meta);
            itemComentario = itemView.findViewById(R.id.item_comentario);
            itemUbicacion = itemView.findViewById(R.id.item_ubicacion);
        }
    }
}

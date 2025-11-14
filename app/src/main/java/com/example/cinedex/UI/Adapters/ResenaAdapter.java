// Archivo: UI/Adapters/ResenaAdapter.java
package com.example.cinedex.UI.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinedex.Data.Models.Resena;
import com.example.cinedex.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ResenaAdapter extends RecyclerView.Adapter<ResenaAdapter.ViewHolder> {

    private List<Resena> lista;

    public ResenaAdapter(List<Resena> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public ResenaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Obtenemos el contexto del padre (el RecyclerView)
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_resena, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ResenaAdapter.ViewHolder holder, int position) {
        Resena r = lista.get(position);

        // --- CÓDIGO ACTUALIZADO PARA TU XML ---

        // 1. Poner nombre de usuario
        holder.itemNombre.setText(r.getUsuario() != null ? r.getUsuario().getNombreUsuario() : "Anónimo");

        // 2. Crear el texto de "meta" (ej. "Interstellar · 4.5 ★")
        // Asumo que tu modelo Reseña tiene getPelicula() y getPuntuacion()
        String tituloPelicula = (r.getPelicula() != null ? r.getPelicula().getTitle() : "Película");
        String meta = String.format(Locale.getDefault(), "%s · %.1f ★",
                tituloPelicula,
                r.getPuntuacion());
        holder.itemMeta.setText(meta);

        // 3. Poner comentario
        holder.itemComentario.setText(r.getReseñaTexto());

        // 4. Poner fecha (o ubicación)
        if (r.getFechaColeccion() != null) {
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            holder.itemUbicacion.setText("Fecha: " + df.format(r.getFechaColeccion()));
        } else {
            holder.itemUbicacion.setText("Fecha: N/A");
        }
    }

    @Override
    public int getItemCount() {
        // Comprobación para evitar NullPointerException si la lista no se ha cargado
        return (lista != null) ? lista.size() : 0;
    }

    // --- VIEWHOLDER ACTUALIZADO ---
    // Coincide con los IDs de tu item_resena.xml
    public static class ViewHolder extends RecyclerView.ViewHolder{
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

    // Para actualizar datos desde la Activity (o Fragment)
    public void updateData(List<Resena> nuevas) {
        this.lista = nuevas;
        notifyDataSetChanged();
    }
}
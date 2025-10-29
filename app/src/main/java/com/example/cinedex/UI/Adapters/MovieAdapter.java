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
import com.example.cinedex.Data.Models.Movie;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movieList;
    private Context context; // Contexto para usar Glide

    // Constructor que recibe la lista y el contexto
    public MovieAdapter(List<Movie> movieList, Context context) {
        this.movieList = movieList;
        this.context = context;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar el layout del ítem (item_movie.xml)
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movieList.get(position);

        holder.titleTextView.setText(movie.getTitle());
        // Puedes agregar más TextViews aquí si los tienes en el layout

        // Construir la URL completa de la imagen
        String imageUrl = "https://image.tmdb.org/t/p/w500/" + movie.getPosterPath();

        // Usar Glide para cargar la imagen en el ImageView
        Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background) // Placeholder
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    // Método para actualizar los datos después de la llamada a la API
    public void setMovies(List<Movie> newMovies) {
        this.movieList = newMovies;
        notifyDataSetChanged(); // Refrescar la vista
    }

    // Clase interna ViewHolder
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView posterImageView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            // Conectar las vistas del layout item_movie.xml
            titleTextView = itemView.findViewById(R.id.movie_title);
            posterImageView = itemView.findViewById(R.id.movie_poster);
        }
    }
}
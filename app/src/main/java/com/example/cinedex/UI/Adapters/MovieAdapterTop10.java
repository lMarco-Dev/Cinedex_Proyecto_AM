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
import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.R;
import java.util.List;

public class MovieAdapterTop10 extends RecyclerView.Adapter<MovieAdapterTop10.MovieTop10ViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MovieAdapterTop10(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    /* ===========================================================================
                                FABRICA LA TARJETA
       =========================================================================== */
    @NonNull
    @Override
    public MovieTop10ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Convierte el molde en una tarjeta vacia para que onBindViewHolder lo utilice
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_top10, parent, false);
        return new MovieTop10ViewHolder(view);
    }

    /* ===========================================================================
                                DECORADOR DE LA TARJETA
       =========================================================================== */
    @Override
    public void onBindViewHolder(@NonNull MovieTop10ViewHolder holder, int position) {
        Movie movie = movies.get(position); // -> Obtiene la pelicula en su posición

        // Poner el número (posición + 1)
        holder.rankNumber.setText(String.valueOf(position + 1));

        // Cargar imagen con Glide
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(context)
                .load(posterUrl)
                .into(holder.poster);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieTop10ViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView rankNumber;

        public MovieTop10ViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster_top10);
            rankNumber = itemView.findViewById(R.id.movie_rank_number);
        }
    }
}
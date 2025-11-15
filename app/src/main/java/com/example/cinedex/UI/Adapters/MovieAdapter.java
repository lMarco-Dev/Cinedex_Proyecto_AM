package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // Necesitarás Glide o Picasso
import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.R;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MovieAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }


    /* ===========================================================================
                                FABRICA LA TARJETA
       =========================================================================== */
    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false); //Crea la vista visual
        return new MovieViewHolder(view);
    }

    /* ===========================================================================
                                DECORADOR DE LA TARJETA
       =========================================================================== */
    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {

        //Obtenemos la pelicula para la posición.
        Movie movie = movies.get(position);

        //La rellenamos con lso datos.
        holder.title.setText(movie.getTitle());
        holder.voteCount.setText(movie.getVoteCount() + " Votos");

        // Cargar imagen con Glide
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        Glide.with(context)
                .load(posterUrl)
                .into(holder.poster);

        //Redireccionar
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int movieId = movie.getId();

                Bundle bundle = new Bundle();
                bundle.putInt("movieId", movieId);

                Navigation.findNavController(view).navigate(
                        R.id.action_homeFragment_to_movieDetailFragment,
                        bundle
                );
            }
        });
    }

    // Items en una lista
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // El ViewHolder
    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView poster;
        TextView title;
        TextView voteCount;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            poster = itemView.findViewById(R.id.movie_poster);
            title = itemView.findViewById(R.id.movie_title);
            voteCount = itemView.findViewById(R.id.movie_vote_count);
        }
    }
}
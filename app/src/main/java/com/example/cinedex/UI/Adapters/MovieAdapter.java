package com.example.cinedex.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
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

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.title.setText(movie.getTitle());
        holder.voteCount.setText(movie.getVoteCount() + " Votos");

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
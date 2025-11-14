package com.example.cinedex.UI.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.AnnouncementsAdapter;
import com.example.cinedex.UI.Adapters.DiscountsAdapter;
import com.example.cinedex.UI.Adapters.NewsAdapter;
import com.example.cinedex.Data.Models.Announcement;
import com.example.cinedex.Data.Models.Discount;
import com.example.cinedex.Data.Models.News;

import java.util.ArrayList;
import java.util.List;

public class AnunciosFragment extends Fragment {

    private RecyclerView rvAnuncios;
    private RecyclerView rvDescuentos;
    private RecyclerView rvNoticias;

    private TextView tvVerTodosAnuncios;
    private TextView tvTopTitle, tvTopGenreYear, tvTopScore;
    private RatingBar ratingTop;
    private CardView cardTopMovie;

    public AnunciosFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ly_anuncios, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        // Vistas
        rvAnuncios = view.findViewById(R.id.rv_anuncios);
        rvDescuentos = view.findViewById(R.id.rv_descuentos);
        rvNoticias = view.findViewById(R.id.rv_noticias);
        tvVerTodosAnuncios = view.findViewById(R.id.tv_ver_todos_anuncios);

        cardTopMovie = view.findViewById(R.id.card_top_movie);
        tvTopTitle = view.findViewById(R.id.tv_top_title);
        tvTopGenreYear = view.findViewById(R.id.tv_top_genre_year);
        tvTopScore = view.findViewById(R.id.tv_top_score);
        ratingTop = view.findViewById(R.id.rating_top);

        Context ctx = requireContext();

        // LayoutManagers
        if (rvAnuncios != null) rvAnuncios.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        if (rvDescuentos != null) rvDescuentos.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false));
        if (rvNoticias != null) rvNoticias.setLayoutManager(new LinearLayoutManager(ctx, LinearLayoutManager.VERTICAL, false));

        // Datos demo (reemplaza por llamadas a API)
        List<Announcement> anuncios = sampleAnnouncements();
        List<Discount> descuentos = sampleDiscounts();
        List<News> noticias = sampleNews();

        // Asignar adapters (usa las clases que te doy abajo)
        if (rvAnuncios != null) rvAnuncios.setAdapter(new AnnouncementsAdapter(ctx, anuncios));
        if (rvDescuentos != null) rvDescuentos.setAdapter(new DiscountsAdapter(ctx, descuentos));
        if (rvNoticias != null) rvNoticias.setAdapter(new NewsAdapter(ctx, noticias));

        // Top movie ejemplo
        if (tvTopTitle != null) tvTopTitle.setText("Galaxy Wars: El Renacer");
        if (tvTopGenreYear != null) tvTopGenreYear.setText("Ciencia ficción • 2025");
        if (ratingTop != null) ratingTop.setRating(5f);
        if (tvTopScore != null) tvTopScore.setText("5.0 (842 reseñas)");
        // si quieres imagen: ImageView iv = cardTopMovie.findViewById(R.id.img_top_poster); Glide.with(this).load(url).into(iv);

        if (tvVerTodosAnuncios != null) {
            tvVerTodosAnuncios.setOnClickListener(v -> {
                // navegación a lista completa de anuncios si la agregas
            });
        }
    }

    // --- Datos de ejemplo ---
    private List<Announcement> sampleAnnouncements() {
        List<Announcement> l = new ArrayList<>();
        l.add(new Announcement("Evento Noche Retro", "¡Función especial con 2x1!", "https://picsum.photos/800/400?random=1"));
        l.add(new Announcement("Maratón Sci-Fi", "Descuentos en entradas", "https://picsum.photos/800/400?random=2"));
        l.add(new Announcement("Estreno VIP", "Premiere exclusiva", "https://picsum.photos/800/400?random=3"));
        return l;
    }
    private List<Discount> sampleDiscounts() {
        List<Discount> l = new ArrayList<>();
        l.add(new Discount("Combo 2 + 1", "Ahorra 30% en palomitas", "https://picsum.photos/200/120?random=21", "Hasta 30/11"));
        l.add(new Discount("Descuento Estudiante", "20% con carnet", "https://picsum.photos/200/120?random=22", "Válido siempre"));
        return l;
    }
    private List<News> sampleNews() {
        List<News> l = new ArrayList<>();
        l.add(new News("Evento exclusivo en Cinedex", "Invitados y sorteos", "https://picsum.photos/300/200?random=31", "2h • Noticias"));
        l.add(new News("Nuevo sistema de reseñas", "Geolocalización en reseñas", "https://picsum.photos/300/200?random=32", "1d • Blog"));
        return l;
    }
}

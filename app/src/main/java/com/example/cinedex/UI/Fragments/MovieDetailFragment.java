package com.example.cinedex.UI.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.Data.Models.Reseña;
import com.example.cinedex.Data.Models.ReseñaRequest;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.Data.Network.TmdbClient;
import com.example.cinedex.Data.Network.TmdbApiService;
import com.example.cinedex.R;

import java.util.Locale; // Importa Locale para formatear texto
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements ResenaFragment.ResenaDialogListener {

    private final String TMDB_API_KEY = "f908b6414babca36cf721d90d6b85e1f";
    private int movieId;

    // Servicios de API
    private TmdbApiService apiService;
    private CineDexApiService cineDexApiService;

    // Vistas
    private ImageView detailBackdrop, detailPoster;
    private TextView detailTitle, detailDescription, detailMetadata;
    private RatingBar detailRating;
    private Button detailReviewButton;
    private Toolbar toolbar;
    private TextView detailTextRating, detailTextYear, detailTextRuntime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.movieId = getArguments().getInt("movieId");
        }

        //Inicializamos las API
        apiService = TmdbClient.getApiService();
        cineDexApiService = CineDexApiClient.getApiService();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ly_fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enlazar las vistas
        toolbar = view.findViewById(R.id.detail_toolbar);
        detailBackdrop = view.findViewById(R.id.detail_backdrop);
        detailPoster = view.findViewById(R.id.detail_poster);
        detailTitle = view.findViewById(R.id.detail_title);
        detailRating = view.findViewById(R.id.detail_rating);
        detailDescription = view.findViewById(R.id.detail_description);
        detailReviewButton = view.findViewById(R.id.detail_review_button);
        detailTextRating = view.findViewById(R.id.detail_text_rating);
        detailTextYear = view.findViewById(R.id.detail_text_year);
        detailTextRuntime = view.findViewById(R.id.detail_text_runtime);

        // --- Configuración del Botón "Atrás" ---
        setupToolbar();

        // Configurar el botón de reseña
        detailReviewButton.setOnClickListener(v -> {
            Log.d("DetailFragment", "Botón de reseña presionado");
            mostrarDialogResena();
        });

        // Cargar los datos
        fetchMovieDetails();
    }

    private void setupToolbar() {
        // Habilita la Toolbar
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false); // Oculta el título
        }

        // Acción de clic para la flecha de "Atrás"
        toolbar.setNavigationOnClickListener(v -> {
            // Esto navega hacia atrás en el NavGraph
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void fetchMovieDetails() {
        // --- ¡CAMBIO AQUÍ! ---
        // Debes pedir a la API que te dé también 'runtime' y 'release_date'
        // Esto se hace en la interfaz, pero para este código asumimos que ya lo hace
        Call<Movie> call = apiService.getMovieDetails(movieId, TMDB_API_KEY);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());
                } else {
                    detailDescription.setText("No se pudieron cargar los detalles.");
                }
            }
            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                detailDescription.setText("Error de conexión.");
            }
        });
    }

    private void updateUI(Movie movie) {
        detailTitle.setText(movie.getTitle());
        detailDescription.setText(movie.getOverview());

        float rating = (float) (movie.getVoteAverage() / 2.0);
        detailRating.setRating(rating);

        // Rating (ej. "8.3")
        detailTextRating.setText(String.format(Locale.US, "%.1f", movie.getVoteAverage()));

        // Año (ej. "2024")
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            detailTextYear.setText(movie.getReleaseDate().split("-")[0]);
        } else {
            detailTextYear.setText("N/A");
        }

        // Duración (ej. "120 min")
        if (movie.getRuntime() > 0) {
            detailTextRuntime.setText(movie.getRuntime() + " min");
        } else {
            detailTextRuntime.setText("N/A");
        }

        // Cargar imágenes
        String backdropUrl = "https://image.tmdb.org/t/p/w780" + movie.getBackdropPath();
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();

        if (getContext() != null) {
            Glide.with(getContext()).load(backdropUrl).into(detailBackdrop);
            Glide.with(getContext()).load(posterUrl).into(detailPoster);
        }
    }

    // ---- Metodos para mostrar la realización de la reseña -----
    private  void mostrarDialogResena() {

        // 1. Crear una instancia de diálogo
        ResenaFragment dialog = new ResenaFragment();

        // 2. Enganchamos el listener
        dialog.setResenaDialogListener(this);

        // 3. Muestra el diálogo
        dialog.show(getParentFragmentManager(), "ResenaDialog");
    }

    // ---- Metodos para mostrar la realización de la reseña -----
    @Override
    public void onResenaGuardada(String comentario, float puntaje) {

        Log.d("[DETAILFRAGMENT]", "Datos recibidos del diálogo: " + comentario + " / " + puntaje);

        // 1. Obtener los datos de sesion(Id y Token de usuario)
        int idUsuario = getUsuarioIdLogueado();
        String token = getAuthToken();

        // Validar usuario
        if(idUsuario == -1 || token.isEmpty()) {
            Toast.makeText(getContext(), "Error: No se encontró sesión de usuario.", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. Crear el objeto de solicitud
        ReseñaRequest request = new ReseñaRequest(idUsuario, this.movieId, comentario, puntaje);

        // 3. Enviar la reseña a tu API de CineDex
        enviarResena(token, request);
    }

    //
     // ---- Método que llama a Retrofit
    //
    private void enviarResena(String token, ReseñaRequest request) {

        Toast.makeText(getContext(), "Guardando reseña...", Toast.LENGTH_SHORT).show();

        // Prepara el token de autorización
        String authToken = "Bearer " + token;

        Call<Reseña> call = cineDexApiService.postResena(authToken, request);

        call.enqueue(new Callback<Reseña>() {
            @Override
            public void onResponse(Call<Reseña> call, Response<Reseña> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ¡Éxito!
                    Toast.makeText(getContext(), "¡Reseña guardada con éxito!", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getContext(), "Error al guardar la reseña.", Toast.LENGTH_SHORT).show();
                    Log.e("DetailFragment_API", "Error API: " + response.code() + " - " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Reseña> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión.", Toast.LENGTH_SHORT).show();
                Log.e("DetailFragment_API", "Fallo de red: " + t.getMessage());
            }
        });
    }

    //
    // ---- Método que Sesión de Usuario de obtención de Id y token
    //
    private int getUsuarioIdLogueado() {

        SharedPreferences prefs = getActivity().getSharedPreferences("CineDexPrefs", Context.MODE_PRIVATE);

        return  prefs.getInt("USER_ID", -1);
    }

    private String getAuthToken() {

        SharedPreferences prefs = getActivity().getSharedPreferences("CineDexPrefs", Context.MODE_PRIVATE);

        return prefs.getString("AUTH_TOKEN", "");
    }
}
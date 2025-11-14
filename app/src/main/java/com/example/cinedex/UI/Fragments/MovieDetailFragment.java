// Archivo: UI/Fragments/MovieDetailFragment.java
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

import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// --- CAMBIO 1: Implementa la interfaz del NUEVO diálogo ---
public class MovieDetailFragment extends Fragment implements ResenaDialogFragment.ResenaDialogListener {

    // ... (todas tus variables están bien: apiKey, movieId, apis, vistas, etc.) ...
    private final String TMDB_API_KEY = "f908b6414babca36cf721d90d6b85e1f";
    private int movieId;
    private TmdbApiService apiService;
    private CineDexApiService cineDexApiService;
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

        // --- Enlazar Vistas (Todo esto está bien) ---
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

        setupToolbar();

        detailReviewButton.setOnClickListener(v -> {
            Log.d("DetailFragment", "Botón de reseña presionado");
            mostrarDialogResena();
        });

        fetchMovieDetails();
    }

    // ... (setupToolbar, fetchMovieDetails, y updateUI están perfectos, no los toques) ...
    private void setupToolbar() {
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            activity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> {
            NavHostFragment.findNavController(this).popBackStack();
        });
    }

    private void fetchMovieDetails() {
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
        detailTextRating.setText(String.format(Locale.US, "%.1f", movie.getVoteAverage()));
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            detailTextYear.setText(movie.getReleaseDate().split("-")[0]);
        } else {
            detailTextYear.setText("N/A");
        }
        if (movie.getRuntime() > 0) {
            detailTextRuntime.setText(movie.getRuntime() + " min");
        } else {
            detailTextRuntime.setText("N/A");
        }
        String backdropUrl = "https://image.tmdb.org/t/p/w780" + movie.getBackdropPath();
        String posterUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterPath();
        if (getContext() != null) {
            Glide.with(getContext()).load(backdropUrl).into(detailBackdrop);
            Glide.with(getContext()).load(posterUrl).into(detailPoster);
        }
    }

    // --- CAMBIO 2: Llama a la NUEVA clase de diálogo ---
    private void mostrarDialogResena() {
        // 1. Crear una instancia de diálogo (del nuevo DialogFragment)
        ResenaDialogFragment dialog = new ResenaDialogFragment();

        // 2. Enganchamos el listener
        dialog.setResenaDialogListener(this);

        // 3. Muestra el diálogo (¡Ahora .show() sí funciona!)
        dialog.show(getParentFragmentManager(), "ResenaDialog");
    }

    // --- CAMBIO 3: Este método AHORA es llamado por ResenaDialogFragment ---
    @Override
    public void onResenaGuardada(String comentario, float puntaje) {
        Log.d("[DETAILFRAGMENT]", "Datos recibidos del diálogo: " + comentario + " / " + puntaje);

        // 1. Obtener los datos de sesion(Id y Token de usuario)
        int idUsuario = getUsuarioIdLogueado();
        String token = getAuthToken();

        // --- CAMBIO 4: Corrección de SharedPreferences (¡Error sutil!) ---
        // Debes usar las mismas keys que en tu Actividad_Login
        // int idUsuario = getUsuarioIdLogueado();
        // String token = getAuthToken(); // Aún no guardas token, lo dejaremos vacío

        if(idUsuario == -1) { // || token.isEmpty()) { -> Quitamos esto por ahora
            Toast.makeText(getContext(), "Error: No se encontró sesión de usuario.", Toast.LENGTH_LONG).show();
            return;
        }

        // 2. Crear el objeto de solicitud
        ReseñaRequest request = new ReseñaRequest(idUsuario, this.movieId, comentario, puntaje);

        // 3. Enviar la reseña a tu API de CineDex
        enviarResena(token, request); // 'token' aquí será "" (vacío), pero la llamada se hará
    }

    // ... (enviarResena está bien) ...
    private void enviarResena(String token, ReseñaRequest request) {
        Toast.makeText(getContext(), "Guardando reseña...", Toast.LENGTH_SHORT).show();
        String authToken = "Bearer " + token;
        Call<Reseña> call = cineDexApiService.postResena(authToken, request);
        call.enqueue(new Callback<Reseña>() {
            @Override
            public void onResponse(Call<Reseña> call, Response<Reseña> response) {
                if (response.isSuccessful() && response.body() != null) {
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

    // --- CAMBIO 5: Arreglar SharedPreferences ---
    // Estás buscando en "CineDexPrefs" con "USER_ID",
    // pero en Actividad_Login guardaste en "sesion_usuario" con "ID_USUARIO".
    private int getUsuarioIdLogueado() {
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        return  prefs.getInt("ID_USUARIO", -1);
    }

    private String getAuthToken() {
        // Aún no estás guardando esto en el Login, así que siempre devolverá ""
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        return prefs.getString("AUTH_TOKEN", "");
    }
}
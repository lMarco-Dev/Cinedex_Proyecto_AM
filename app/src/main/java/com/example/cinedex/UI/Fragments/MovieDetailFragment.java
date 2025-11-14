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
import com.example.cinedex.Data.Models.ReseñaRequest; // ¡Importa la clase actualizada!
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.Data.Network.TmdbClient;
import com.example.cinedex.Data.Network.TmdbApiService;
import com.example.cinedex.R;

import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

// Implementa la interfaz del DIÁLOGO
public class MovieDetailFragment extends Fragment implements ResenaDialogFragment.ResenaDialogListener {

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

    // Variable para guardar la película actual
    private Movie peliculaActual;

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

        setupToolbar();

        detailReviewButton.setOnClickListener(v -> {
            Log.d("DetailFragment", "Botón de reseña presionado");
            mostrarDialogResena();
        });

        fetchMovieDetails();
    }

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
        // Guardamos el objeto película
        this.peliculaActual = movie;

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

    // Llama al DIÁLOGO
    private void mostrarDialogResena() {
        ResenaDialogFragment dialog = new ResenaDialogFragment();
        dialog.setResenaDialogListener(this);
        dialog.show(getParentFragmentManager(), "ResenaDialog");
    }

    // --- ¡¡MÉTODO CORREGIDO!! ---
    @Override
    public void onResenaGuardada(String comentario, float puntaje) {
        Log.d("[DEBUG_RESEÑA]", "Datos recibidos del diálogo: Comentario=" + comentario + " | Puntaje=" + puntaje);

        int idUsuario = getUsuarioIdLogueado();
        String token = getAuthToken();

        // VALIDACIÓN 1: ¿Se cargó la película?
        if (this.peliculaActual == null) {
            Toast.makeText(getContext(), "Error: Datos de la película no cargados. Intenta de nuevo.", Toast.LENGTH_SHORT).show();
            return;
        }

        // VALIDACIÓN 2: ¿Está el usuario logueado?
        if(idUsuario == -1) {
            Toast.makeText(getContext(), "Error: No se encontró sesión de usuario.", Toast.LENGTH_LONG).show();
            return;
        }

        // VALIDACIÓN 3: ¿El puntaje es válido?
        if (puntaje < 0.5f) {
            Toast.makeText(getContext(), "Error: El puntaje no puede ser 0.", Toast.LENGTH_LONG).show();
            return;
        }

        // --- ¡¡AQUÍ ESTÁ LA LÍNEA CORREGIDA!! ---
        // Ahora le pasamos los 6 argumentos que el constructor espera
        ReseñaRequest request = new ReseñaRequest(
                idUsuario,
                this.peliculaActual.getId(),
                comentario,
                puntaje,
                this.peliculaActual.getTitle(),
                this.peliculaActual.getPosterPath()
        );

        // 3. Enviar la reseña a tu API de CineDex
        enviarResena(token, request);
    }


    // Método que llama a Retrofit (con el Logcat de depuración)
    private void enviarResena(String token, ReseñaRequest request) {
        Toast.makeText(getContext(), "Guardando reseña...", Toast.LENGTH_SHORT).show();
        String authToken = "Bearer " + token;

        Call<Reseña> call = cineDexApiService.postResena(authToken, request);

        call.enqueue(new Callback<Reseña>() {
            @Override
            public void onResponse(Call<Reseña> call, Response<Reseña> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_POST_RESEÑA", "¡Éxito! El body no es nulo. ID Creado: " + response.body().getIdReseña());
                    Toast.makeText(getContext(), "¡Reseña guardada con éxito!", Toast.LENGTH_LONG).show();
                } else {
                    String errorBody = "N/A";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {}

                    Log.e("API_POST_RESEÑA", "Error de API: " + response.code() + " - " + response.message() + " - " + errorBody);
                    Toast.makeText(getContext(), "Error de API: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Reseña> call, Throwable t) {
                Log.e("API_POST_RESEÑA", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(getContext(), "Error de CONEXIÓN", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Métodos de SharedPreferences (Corregidos para usar "sesion_usuario")
    private int getUsuarioIdLogueado() {
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        return  prefs.getInt("ID_USUARIO", -1);
    }

    private String getAuthToken() {
        SharedPreferences prefs = getActivity().getSharedPreferences("sesion_usuario", Context.MODE_PRIVATE);
        return prefs.getString("AUTH_TOKEN", "");
    }
}
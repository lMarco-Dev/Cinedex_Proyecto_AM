package com.example.cinedex.UI.Activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinedex.Data.Models.DTOs.MensajeRespuestaDto;
import com.example.cinedex.Data.Models.DTOs.ResenaPublicaDto;
import com.example.cinedex.Data.Models.DTOs.ResenaRequestDto;
import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.Data.Network.TmdbApiService;
import com.example.cinedex.Data.Network.TmdbClient;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.ResenaAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Resena extends AppCompatActivity {

    TextView tvNombreUsuario, tvCorreoUsuario, tvUbicacion, tvResumen;
    Spinner spinnerPeliculas;
    EditText etComentario;
    RatingBar ratingBar;
    Button btnUbicacion, btnPublicar;
    RecyclerView rvResenas;

    CineDexApiService apiService;
    TmdbApiService tmdbService;
    ResenaAdapter adapter;

    List<ResenaPublicaDto> resenas = new ArrayList<>();
    List<Movie> peliculasTMDB = new ArrayList<>();

    FusedLocationProviderClient fusedLocationClient;
    ActivityResultLauncher<String> solicitarPermiso;

    private final String TMDB_API_KEY = "908b6414babca36cf721d90d6b85e1f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_resena);

        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario);
        tvUbicacion = findViewById(R.id.tvUbicacion);
        tvResumen = findViewById(R.id.tvResumenEstrellas);
        spinnerPeliculas = findViewById(R.id.spinnerPeliculas);
        etComentario = findViewById(R.id.etComentario);
        ratingBar = findViewById(R.id.ratingBarPuntaje);
        btnUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnPublicar = findViewById(R.id.btnPublicarResena);
        rvResenas = findViewById(R.id.rvResenas);

        apiService = CineDexApiClient.getApiService();
        tmdbService = TmdbClient.getApiService();

        adapter = new ResenaAdapter(resenas);
        rvResenas.setLayoutManager(new LinearLayoutManager(this));
        rvResenas.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        solicitarPermiso = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) obtenerUltimaUbicacion();
                    else Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                }
        );

        cargarDatosUsuario();
        cargarPeliculasDesdeTMDB();
        cargarResenasDesdeApi();

        btnUbicacion.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                obtenerUltimaUbicacion();
            } else {
                solicitarPermiso.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });

        btnPublicar.setOnClickListener(v -> publicarResena());
    }

    private void cargarDatosUsuario() {
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        tvNombreUsuario.setText(prefs.getString("NOMBRE_USUARIO", "Usuario"));
        tvCorreoUsuario.setText(prefs.getString("EMAIL_USUARIO", "correo@ejemplo.com"));
    }

    private void cargarPeliculasDesdeTMDB() {
        tmdbService.getPopularMovies(TMDB_API_KEY)
                .enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                        if (!response.isSuccessful() || response.body() == null) {
                            Toast.makeText(Actividad_Resena.this, "Error al cargar TMDB", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        peliculasTMDB = response.body().getResults();
                        List<String> nombres = new ArrayList<>();
                        for (Movie m : peliculasTMDB) nombres.add(m.getTitle());

                        spinnerPeliculas.setAdapter(
                                new ArrayAdapter<>(Actividad_Resena.this,
                                        android.R.layout.simple_spinner_dropdown_item, nombres)
                        );
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        Toast.makeText(Actividad_Resena.this, "TMDB fallo: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void obtenerUltimaUbicacion() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        fusedLocationClient.getLastLocation().addOnSuccessListener(loc -> {
            if (loc != null)
                tvUbicacion.setText("Lat: " + loc.getLatitude() + "  Lon: " + loc.getLongitude());
        });
    }

    private void publicarResena() {
        Movie movieSeleccionada = peliculasTMDB.get(spinnerPeliculas.getSelectedItemPosition());
        String comentario = etComentario.getText().toString().trim();
        float puntaje = ratingBar.getRating();

        if (comentario.isEmpty() || puntaje == 0) {
            Toast.makeText(this, "Completa el comentario y el puntaje", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        int idUsuario = prefs.getInt("ID_USUARIO", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        ResenaRequestDto req = new ResenaRequestDto(
                idUsuario,
                movieSeleccionada.getId(),
                movieSeleccionada.getTitle(),
                movieSeleccionada.getPosterPath(),
                comentario,
                puntaje
        );

        apiService.postResena(req).enqueue(new Callback<MensajeRespuestaDto>() {
            @Override
            public void onResponse(Call<MensajeRespuestaDto> call, Response<MensajeRespuestaDto> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(Actividad_Resena.this, response.body().getMensaje(), Toast.LENGTH_SHORT).show();
                    etComentario.setText("");
                    ratingBar.setRating(0);
                    cargarResenasDesdeApi();
                } else {
                    Toast.makeText(Actividad_Resena.this, "Error API: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MensajeRespuestaDto> call, Throwable t) {
                Toast.makeText(Actividad_Resena.this, "Fallo de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarResenasDesdeApi() {
        apiService.getResenas().enqueue(new Callback<List<ResenaPublicaDto>>() {
            @Override
            public void onResponse(Call<List<ResenaPublicaDto>> call, Response<List<ResenaPublicaDto>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    Log.d("API_RESEÑAS", new Gson().toJson(response.body()));

                    resenas.clear();
                    resenas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    actualizarResumen();
                }
            }

            @Override
            public void onFailure(Call<List<ResenaPublicaDto>> call, Throwable t) {
            }
        });
    }

    private void actualizarResumen() {
        if (resenas.isEmpty()) {
            tvResumen.setText("0 reseñas • ⭐ 0.0");
            return;
        }

        float total = 0;
        for (ResenaPublicaDto r : resenas) total += r.getCalificacion();

        float promedio = total / resenas.size();
        tvResumen.setText(resenas.size() + " reseñas • ⭐ " + String.format("%.1f", promedio));
    }
}

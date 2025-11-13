package com.example.cinedex.UI.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RecyclerView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.cinedex.Data.Models.Movie;
import com.example.cinedex.Data.Models.Reseña;
import com.example.cinedex.Data.Models.ReseñaRequest;
import com.example.cinedex.Data.Network.CineDexApiClient;
import com.example.cinedex.Data.Network.CineDexApiService;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.ResenaAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Resena extends AppCompatActivity {

    TextView tvNombreUsuario, tvCorreoUsuario, tvUbicacion, tvResumen;
    Spinner spinnerPeliculas;
    EditText etGenero, etAnio, etComentario;
    RatingBar ratingBar;
    Button btnUbicacion, btnPublicar;
    RecyclerView rvResenas;

    CineDexApiService apiService;
    ResenaAdapter adapter;
    List<Reseña> reseñas = new ArrayList<>();
    List<Movie> peliculasSimuladas = new ArrayList<>(); // o carga desde TMDB

    // Ubicación
    FusedLocationProviderClient fusedLocationClient;
    double lastLat = 0, lastLon = 0;

    ActivityResultLauncher<String> solicitarPermiso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_resena);

        // vistas
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario);
        tvCorreoUsuario = findViewById(R.id.tvCorreoUsuario);
        tvUbicacion = findViewById(R.id.tvUbicacion);
        tvResumen = findViewById(R.id.tvResumenEstrellas);
        spinnerPeliculas = findViewById(R.id.spinnerPeliculas);
        etGenero = findViewById(R.id.etGenero);
        etAnio = findViewById(R.id.etAnio);
        etComentario = findViewById(R.id.etComentario);
        ratingBar = findViewById(R.id.ratingBarPuntaje);
        btnUbicacion = findViewById(R.id.btnObtenerUbicacion);
        btnPublicar = findViewById(R.id.btnPublicarResena);
        rvResenas = findViewById(R.id.rvResenas);

        // API
        apiService = CineDexApiClient.getApiService();

        // RecyclerView
        adapter = new ResenaAdapter(reseñas, this);
        rvResenas.setLayoutManager(new LinearLayoutManager(this));
        rvResenas.setAdapter(adapter);

        // FusedLocation
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // permiso launcher
        solicitarPermiso = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) obtenerUltimaUbicacion();
            else Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
        });

        // cargar datos de usuario en SharedPreferences (ejemplo)
        SharedPreferences prefs = getSharedPreferences("CineDexPrefs", MODE_PRIVATE);
        String nombre = prefs.getString("NOMBRE_USUARIO", "Usuario");
        String correo = prefs.getString("EMAIL", "correo@ejemplo.com");
        tvNombreUsuario.setText(nombre);
        tvCorreoUsuario.setText(correo);

        // Cargar lista películas (puedes reemplazar por llamada TMDB)
        cargarPeliculasSimuladas();

        btnUbicacion.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                obtenerUltimaUbicacion();
            } else {
                solicitarPermiso.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });

        btnPublicar.setOnClickListener(v -> publicarResena());

        // cargar reseñas desde API
        cargarResenasDesdeApi();
    }

    private void cargarPeliculasSimuladas(){
        // ejemplo simple, idealmente trae desde TMDB
        peliculasSimuladas.add(new Movie("Interstellar", "desc", "/path"));
        peliculasSimuladas.add(new Movie("Demon Slayer", "desc", "/path"));
        peliculasSimuladas.add(new Movie("Spirited Away", "desc", "/path"));

        List<String> nombres = new ArrayList<>();
        for (Movie m: peliculasSimuladas) nombres.add(m.getTitle());

        spinnerPeliculas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombres));
    }

    private void obtenerUltimaUbicacion() {
        try {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null) {
                            lastLat = location.getLatitude();
                            lastLon = location.getLongitude();
                            tvUbicacion.setText(String.format("Ubicación: %.5f, %.5f", lastLat, lastLon));
                        } else {
                            tvUbicacion.setText("Ubicación: No disponible");
                        }
                    })
                    .addOnFailureListener(e -> {
                        tvUbicacion.setText("Ubicación: Error");
                    });
        } catch (SecurityException ex){
            Toast.makeText(this, "Permiso de ubicación requerido", Toast.LENGTH_SHORT).show();
        }
    }

    private void publicarResena(){
        String peliculaSeleccionada = spinnerPeliculas.getSelectedItem() != null ? spinnerPeliculas.getSelectedItem().toString() : "";
        int idPelicula = 0; // si tu API necesita id, buscalo en tu lista de películas (simulada)
        // buscar id de la peliculaSimulada:
        for (Movie m: peliculasSimuladas) {
            if (m.getTitle().equals(peliculaSeleccionada)) {
                idPelicula = m.getId(); // si tu Movie no tiene id real, usa índice o adapta
                break;
            }
        }

        String comentario = etComentario.getText().toString().trim();
        float puntaje = ratingBar.getRating();

        if (comentario.isEmpty() || puntaje == 0.0f) {
            Toast.makeText(this, "Escribe un comentario y selecciona un puntaje", Toast.LENGTH_SHORT).show();
            return;
        }

        // crea request (usa tu modelo ReseñaRequest)
        int idUsuario = getSharedPreferences("CineDexPrefs", MODE_PRIVATE).getInt("USER_ID", -1);
        if (idUsuario == -1) {
            Toast.makeText(this, "Usuario no logueado", Toast.LENGTH_SHORT).show();
            return;
        }

        ReseñaRequest req = new ReseñaRequest(idUsuario, idPelicula, comentario, puntaje);

        // token de autorización (si aplica)
        String token = getSharedPreferences("CineDexPrefs", MODE_PRIVATE).getString("AUTH_TOKEN", "");
        String bearer = token.isEmpty() ? "" : "Bearer " + token;

        Call<Reseña> call = apiService.postResena(bearer, req);
        call.enqueue(new Callback<Reseña>() {
            @Override
            public void onResponse(Call<Reseña> call, Response<Reseña> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Añadir ubicación en el cliente (si tu API no la guarda)
                    // Actualizar lista
                    reseñas.add(0, response.body());
                    adapter.notifyItemInserted(0);
                    // actualizar resumen (simple)
                    actualizarResumen();
                    Toast.makeText(ActividadResena.this, "Reseña publicada", Toast.LENGTH_SHORT).show();
                    // limpiar formulario
                    etComentario.setText("");
                    ratingBar.setRating(0);
                } else {
                    Toast.makeText(ActividadResena.this, "Error al publicar (API)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Reseña> call, Throwable t) {
                Toast.makeText(ActividadResena.this, "Fallo de conexión: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cargarResenasDesdeApi(){
        apiService.getResenas().enqueue(new Callback<List<Reseña>>() {
            @Override
            public void onResponse(Call<List<Reseña>> call, Response<List<Reseña>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reseñas.clear();
                    reseñas.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    actualizarResumen();
                }
            }

            @Override
            public void onFailure(Call<List<Reseña>> call, Throwable t) { /* manejar */ }
        });
    }

    private void actualizarResumen(){
        int c1=0,c2=0,c3=0,c4=0,c5=0;
        for (Reseña r : reseñas){
            int v = Math.round(r.getPuntuacion());
            switch (v){
                case 1: c1++; break;
                case 2: c2++; break;
                case 3: c3++; break;
                case 4: c4++; break;
                case 5: c5++; break;
            }
        }
        tvResumen.setText(String.format("Resumen: ★5: %d  ★4: %d  ★3: %d  ★2: %d  ★1: %d", c5,c4,c3,c2,c1));
    }
}

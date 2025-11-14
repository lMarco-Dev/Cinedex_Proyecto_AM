// Archivo: UI/Activities/Actividad_Resena.java
package com.example.cinedex.UI.Activities;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log; // ¡Añadido para depuración!
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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView; // ¡¡Importante!!

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
import java.util.Locale; // ¡Importado!

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Actividad_Resena extends AppCompatActivity {

    TextView tvNombreUsuario, tvCorreoUsuario, tvUbicacion, tvResumen;
    Spinner spinnerPeliculas;
    EditText etGenero, etAnio, etComentario;
    RatingBar ratingBar;
    Button btnUbicacion, btnPublicar;
    // --- CORREGIDO: Declarado como RecyclerView ---
    RecyclerView rvResenas;

    CineDexApiService apiService;
    ResenaAdapter adapter;
    List<Reseña> reseñas = new ArrayList<>();
    List<Movie> peliculasSimuladas = new ArrayList<>();

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
        rvResenas = findViewById(R.id.rvResenas); // Esto ya coincide con tu XML

        apiService = CineDexApiClient.getApiService();

        // RecyclerView
        adapter = new ResenaAdapter(reseñas);
        rvResenas.setLayoutManager(new LinearLayoutManager(this));
        rvResenas.setAdapter(adapter);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        solicitarPermiso = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) obtenerUltimaUbicacion();
            else Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
        });

        // Cargar datos de usuario
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        String nombre = prefs.getString("NOMBRE_USUARIO", "Usuario");
        tvNombreUsuario.setText(nombre);
        tvCorreoUsuario.setText("correo@ejemplo.com"); // Placeholder

        // Cargar películas simuladas
        cargarPeliculasSimuladas();

        btnUbicacion.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                obtenerUltimaUbicacion();
            } else {
                solicitarPermiso.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            }
        });

        btnPublicar.setOnClickListener(v -> publicarResena());

        cargarResenasDesdeApi();
    }

    private void cargarPeliculasSimuladas(){
        // ¡OJO! Tu clase Movie.java real no tiene este constructor.
        // Esto solo funciona si AÑADES este constructor a tu Movie.java
        // public Movie(String title, String overview, String posterPath) { ... }
        // Para que esto funcione, tu Movie.java (que me pasaste) necesita un constructor
        // que asigne esos valores.
        //
        // Voy a asumir que tu constructor `public Movie(String, String, String)`
        // NO asigna el ID. Por lo tanto, movie.getId() devolverá 0.

        peliculasSimuladas.add(new Movie("Interstellar", "desc", "/path1"));
        peliculasSimuladas.add(new Movie("Demon Slayer", "desc", "/path2"));
        peliculasSimuladas.add(new Movie("Spirited Away", "desc", "/path3"));


        List<String> nombres = new ArrayList<>();
        for (Movie m: peliculasSimuladas) nombres.add(m.getTitle());

        spinnerPeliculas.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, nombres));
    }

    private void obtenerUltimaUbicacion() {
        // ... (tu código está bien) ...
    }

    // --- ¡¡MÉTODO CORREGIDO!! ---
    private void publicarResena(){

        // 1. Obtener los datos del formulario
        String peliculaSeleccionada = spinnerPeliculas.getSelectedItem() != null ? spinnerPeliculas.getSelectedItem().toString() : "";
        String comentario = etComentario.getText().toString().trim();
        float puntaje = ratingBar.getRating();

        // 2. Encontrar la película simulada que coincide con el título
        Movie movieSimuladaSeleccionada = null;
        for (Movie m : peliculasSimuladas) {
            if (m.getTitle().equals(peliculaSeleccionada)) {
                movieSimuladaSeleccionada = m;
                break;
            }
        }

        // 3. Validar los datos
        if (movieSimuladaSeleccionada == null) {
            Toast.makeText(this, "Error: No se pudo encontrar la película simulada", Toast.LENGTH_SHORT).show();
            return;
        }
        if (comentario.isEmpty() || puntaje < 0.5f) { // Validamos que el puntaje sea mayor a 0
            Toast.makeText(this, "Escribe un comentario y selecciona un puntaje", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. Obtener el ID de usuario
        SharedPreferences prefs = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        int idUsuario = prefs.getInt("ID_USUARIO", -1);

        if (idUsuario == -1) {
            Toast.makeText(this, "Usuario no logueado. Cierra sesión y vuelve a entrar.", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- ¡¡AQUÍ ESTÁ LA CORRECCIÓN!! ---
        // 5. Creamos el Request de 6 argumentos
        //    Usamos los datos de la película simulada
        ReseñaRequest req = new ReseñaRequest(
                idUsuario,
                movieSimuladaSeleccionada.getId(), // ¡OJO! Esto probablemente es 0
                comentario,
                puntaje,
                movieSimuladaSeleccionada.getTitle(), // ej. "Interstellar"
                movieSimuladaSeleccionada.getPosterPath() // ej. "/path1"
        );

        // --- ADVERTENCIA DE LÓGICA ---
        // El ID de la película (movieSimuladaSeleccionada.getId()) es 0 porque
        // tu constructor de simulación no le asigna uno.
        // Esto HARÁ QUE LA API FALLE (con 404 o 500)
        // Pero el código de Android AHORA SÍ COMPILA.
        Log.d("PublicarResena", "Enviando Reseña para Película ID: " + movieSimuladaSeleccionada.getId());


        String bearer = ""; // Token sigue vacío por ahora

        Call<Reseña> call = apiService.postResena(bearer, req);
        call.enqueue(new Callback<Reseña>() {
            @Override
            public void onResponse(Call<Reseña> call, Response<Reseña> response) {
                // Usamos el código de depuración de antes
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("API_POST_RESEÑA", "¡Éxito! El body no es nulo. ID Creado: " + response.body().getIdReseña());
                    Toast.makeText(Actividad_Resena.this, "¡Reseña guardada con éxito!", Toast.LENGTH_LONG).show();

                    // Actualizar UI
                    reseñas.add(0, response.body());
                    adapter.notifyItemInserted(0);
                    actualizarResumen();
                    etComentario.setText("");
                    ratingBar.setRating(0);

                } else {
                    String errorBody = "N/A";
                    try {
                        if (response.errorBody() != null) {
                            errorBody = response.errorBody().string();
                        }
                    } catch (Exception e) {}

                    Log.e("API_POST_RESEÑA", "Error de API: " + response.code() + " - " + response.message() + " - " + errorBody);
                    Toast.makeText(Actividad_Resena.this, "Error de API: " + response.code() + " - " + errorBody, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Reseña> call, Throwable t) {
                Log.e("API_POST_RESEÑA", "Fallo de conexión: " + t.getMessage());
                Toast.makeText(Actividad_Resena.this, "Error de CONEXIÓN", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void cargarResenasDesdeApi(){
        // ... (tu código está bien) ...
    }

    private void actualizarResumen(){
        // ... (tu código está bien) ...
    }
}
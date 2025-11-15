package com.example.cinedex.UI.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.cinedex.R;
import com.example.cinedex.UI.Adapters.MainAdapter;
import com.example.cinedex.Data.Models.MovieResponse;
import com.example.cinedex.Data.Models.Section;
import com.example.cinedex.Data.Models.SectionTop10;
import com.example.cinedex.Data.Network.TmdbClient;
import com.example.cinedex.Data.Network.TmdbApiService;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private final String TMDB_API_KEY = "f908b6414babca36cf721d90d6b85e1f";

    private RecyclerView mainRecyclerView;
    private MainAdapter mainAdapter; // -> Encarga de construir la lista
    private TmdbApiService apiService; // -> Retrofit para hablar ocn la API

    private int totalCalls = 5; // -> Cuantas veces se llama a la API
    private int callsCompleted = 0; // -> Contardor de llamadas

    /* ================================================================================
                         CAJAS VACIAS PARA GUARDAR LA LISTA
     ================================================================================ */
    private Section sectionPopulares = null;
    private Section sectionCartelera = null;
    private SectionTop10 sectionTop10 = null;
    private Section sectionEstrenos = null;
    private Section sectionTendencias = null;


    /* ================================================================================
                            CONSTRUCTOR DEL FRAGMENT
     ================================================================================ */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Utiliza ly_fragment_home como plantilla para mostrar
        View view = inflater.inflate(R.layout.ly_fragment_home, container, false);

        apiService = TmdbClient.getApiService(); // -> Prepara el mensajero
        mainRecyclerView = view.findViewById(R.id.recycler_popular_movies); // -> Encuentra la lista de peliculas vertical
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // -> Las organice

        // Inicializa el MainAdapter con una lista vacía de tipo 'Object'
        mainAdapter = new MainAdapter(new ArrayList<>(), getContext());
        mainRecyclerView.setAdapter(mainAdapter);

        // Inicia la carga
        fetchAllSections();
        return view;
    }


    /* ================================================================================
                             CARGAR TODO EL CONTENIDO DE LA API
     ================================================================================ */
    private void fetchAllSections() {

        // Reseteamos y indicamos donde guardaremos las cajas
        callsCompleted = 0;
        totalCalls = 5;
        sectionPopulares = null;
        sectionCartelera = null;
        sectionTop10 = null;
        sectionEstrenos = null;
        sectionTendencias = null;

        // 1. Cargar "Top 10" -> Indicamos que acceda a la API y busque TopRatedMovies
        apiService.getTopRatedMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si encontro los datos -> crea la sección TOP 10 HOY
                    sectionTop10 = new SectionTop10(
                            "TOP 10 HOY",
                            "Lo más visto en Perú",
                            response.body().getResults().subList(0, 10) // -> Toma solo los 10 primeros
                    );
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 2. Cargar "Populares" -> Indicamos que acceda a la API y busque PopularMovies
        apiService.getPopularMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Si encontro los datos -> crea la sección Peliculas populares
                    sectionPopulares = new Section("Películas Populares", response.body().getResults());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 3. Cargar "Próximos Estrenos"
        apiService.getUpcomingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionEstrenos = new Section("Próximos Estrenos", response.body().getResults());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 4. Cargar "En Cartelera"
        apiService.getNowPlayingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionCartelera = new Section("En Cartelera", response.body().getResults());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });

        // 5. Cargar "Tendencias de la Semana"
        apiService.getTrendingMovies(TMDB_API_KEY).enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    sectionTendencias = new Section("Tendencias de la Semana", response.body().getResults());
                }
                checkIfAllCallsAreDone(); // Llama al contador
            }
            @Override public void onFailure(Call<MovieResponse> call, Throwable t) { checkIfAllCallsAreDone(); }
        });
    }

    /* ================================================================================
                             SINCRONIZACIÓN DE LAS CAJAS
     ================================================================================ */
    private synchronized void checkIfAllCallsAreDone() {
        callsCompleted++;

        // Solo continuar si TODAS las llamadas (5) han terminado
        if (callsCompleted == totalCalls) {

            Log.d("HomeFragment", "Todas las " + totalCalls + " secciones cargadas. Construyendo UI.");

            // 1. Crear la lista final en el orden deseado
            List<Object> finalList = new ArrayList<>();

            if (sectionPopulares != null) {
                finalList.add(sectionPopulares);
            }
            if (sectionCartelera != null) {
                finalList.add(sectionCartelera);
            }

            if (sectionTop10 != null) {
                finalList.add(sectionTop10);
            }

            if (sectionEstrenos != null) {
                finalList.add(sectionEstrenos);
            }
            if (sectionTendencias != null) {
                finalList.add(sectionTendencias);
            }

            mainAdapter.setSections(finalList); // -> Llama al MainAdapter para que se encargue de dibujarlas
        }
    }
}
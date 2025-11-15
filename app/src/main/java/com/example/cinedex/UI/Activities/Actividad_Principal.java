package com.example.cinedex.UI.Activities;

import android.os.Bundle;
import android.view.View; // <-- No olvides este import

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.cinedex.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Actividad_Principal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ly_actividad_principal);

        /* ===================================================================
                                CONEXIÓN PRINCIPAL
          =================================================================== */
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment); // -> Es la ventana
        NavController navController = navHostFragment.getNavController(); // -> Es lo que decide que se muestra en la ventana

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        NavigationUI.setupWithNavController(bottomNavigationView, navController); // -> Conecta la barra de navegación con el controlador


        /* ===================================================================
                                BARRA DE NAVEGACIÓN
          =================================================================== */
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            //Obtenemos el ID
            int id = destination.getId();

            //Decidimos a que vistas la barra de navegación sera visible o no visible
            if(id == R.id.homeFragment || id == R.id.eventosFragment || id == R.id.profileFragment) {
                bottomNavigationView.setVisibility(View.VISIBLE);
            } else {
                bottomNavigationView.setVisibility(View.GONE);
            }
        });
    }
}
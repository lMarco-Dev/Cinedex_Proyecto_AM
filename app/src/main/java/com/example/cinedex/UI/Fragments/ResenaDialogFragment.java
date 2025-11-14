// Archivo: UI/Fragments/ResenaDialogFragment.java
package com.example.cinedex.UI.Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.cinedex.R;

public class ResenaDialogFragment extends DialogFragment {

    // 1. Interfaz para devolver datos a MovieDetailFragment
    public interface ResenaDialogListener {
        void onResenaGuardada(String comentario, float puntaje);
    }

    private ResenaDialogListener listener;
    private RatingBar ratingBar;
    private EditText etComentario;

    // 2. Método para que MovieDetailFragment se "enganche"
    public void setResenaDialogListener(ResenaDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // 3. Inflar el layout del diálogo
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ly_fragment_dialog_resena, null);

        // 4. Conectar las vistas del XML del diálogo
        ratingBar = view.findViewById(R.id.dialog_rating_bar);
        etComentario = view.findViewById(R.id.dialog_edit_text);
        Button btnGuardar = view.findViewById(R.id.dialog_button_guardar);
        Button btnCancelar = view.findViewById(R.id.dialog_button_cancelar);

        // 5. Configurar los clics de los botones
        btnGuardar.setOnClickListener(v -> {
            String comentario = etComentario.getText().toString().trim();
            float puntaje = ratingBar.getRating();

            if (comentario.isEmpty() || puntaje == 0.0f) {
                Toast.makeText(getContext(), "Por favor, añade un puntaje y comentario", Toast.LENGTH_SHORT).show();
            } else {
                // 6. Usar el listener para devolver los datos y cerrar
                if (listener != null) {
                    listener.onResenaGuardada(comentario, puntaje);
                }
                dismiss(); // Cerrar el diálogo
            }
        });

        btnCancelar.setOnClickListener(v -> {
            dismiss(); // Simplemente cerrar el diálogo
        });

        // 7. Construir el diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }
}
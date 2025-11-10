package com.example.cinedex.UI.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;

import com.example.cinedex.R;

public class ResenaFragment extends DialogFragment {

    public interface ResenaDialogListener{
        void onResenaGuardada(String comentario, float puntaje);
    }

    private ResenaDialogListener listener;

    private EditText etComentario;
    private RatingBar rbPuntaje;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        //Insertamos el layout de la reseña
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.ly_fragment_dialog_resena, null);

        //Conectamos la vista al layout
        etComentario = view.findViewById(R.id.dialog_edit_text);
        rbPuntaje = view.findViewById(R.id.dialog_rating_bar);
        Button btnCancelar = view.findViewById(R.id.dialog_button_cancelar);
        Button btnGuardar = view.findViewById(R.id.dialog_button_guardar);

        // Configuración de botones
        btnCancelar.setOnClickListener(v -> {
            dismiss();
        });

        btnGuardar.setOnClickListener(v -> {
            // 1. Obtener los datos
            String comentario = etComentario.getText().toString().trim();
            float puntaje = rbPuntaje.getRating();

            // 2. Validar datos
            if (comentario.isEmpty()) {
                etComentario.setError("El comentario no puede estar vacío");
                return;
            }
            if (puntaje == 0) {
                Toast.makeText(getContext(), "Por favor, selecciona un puntaje", Toast.LENGTH_SHORT).show();
                return;
            }

            // 3. Enviar los datos al MovieDetailFragment
            if(listener != null) {
                listener.onResenaGuardada(comentario, puntaje);
            }

            // 4. Cerramos la reseña
            dismiss();
        });

        // Crear el constructor del dialogo
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setView(view);

        return builder.create();
    }

    public void setResenaDialogListener(ResenaDialogListener listener) {
        this.listener = listener;
    }
}
package com.example.cinedex.Data.Access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {

    //Constantes para las tablas
    public static final String TABLA_USUARIO = "Usuario";
    public static final String ID_USUARIO = "idUsuario";
    public static final String COL_NOMBRE = "nombre";
    public static final String COL_CORREO = "correo";
    public static final String COL_CONTRASENA = "contraseña";

    // Sentencia SQL de Creación
    String tabla_User = "CREATE TABLE " + TABLA_USUARIO + " (" +
            ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOMBRE + " VARCHAR(100) NOT NULL, " +
            COL_CORREO + " VARCHAR(100) NOT NULL, " +
            COL_CONTRASENA + " VARCHAR(100) NOT NULL)";

    public BDHelper(@Nullable Context context,
                    @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla_User);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS Usuario");
        db.execSQL(tabla_User);
    }
}

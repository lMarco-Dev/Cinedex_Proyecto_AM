package com.example.cinedex.Data.Access;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BDHelper extends SQLiteOpenHelper {

    // --- Constantes para la Base de Datos ---
    private static final String NOMBRE_BD = "cinedex.db";
    private static final int VERSION_BD = 1;

    //Constantes para las tablas
    public static final String TABLA_USUARIO = "Usuario";
    public static final String ID_USUARIO = "idUsuario";
    public static final String COL_NOMBRE_USUARIO = "nombreUsuario";
    public static final String COL_EMAIL = "email";
    public static final String COL_CONTRASENA = "contrasena";
    public static final String COL_NOMBRES = "nombres";
    public static final String COL_APELLIDOS = "apellidos";
    public static final String COL_RANGO_ACTUAL = "idRangoActual";

    // Sentencia SQL de Creación
    String tabla_User = "CREATE TABLE " + TABLA_USUARIO + " (" +
            ID_USUARIO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_NOMBRE_USUARIO + " VARCHAR(100) NOT NULL, " +
            COL_EMAIL + " VARCHAR(100) NOT NULL, " +
            COL_CONTRASENA + " VARCHAR(100) NOT NULL, " +
            COL_NOMBRES + " VARCHAR(100) NOT NULL, " +
            COL_APELLIDOS + " VARCHAR(100) NOT NULL, " +
            COL_RANGO_ACTUAL + " INTEGER NOT NULL)";

    public BDHelper(@Nullable Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
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

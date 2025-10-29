package com.example.cinedex.Data.Access;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cinedex.Data.Models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DAOUsuario {

    //Datos base
    private String nombreBD;
    private int version;
    private BDHelper helper;

    //Constructor

    public DAOUsuario(Context contexto) {
        this.nombreBD = "BDUsuario";
        this.version = 1;
        Log.d("Estado","Inicio de DAOUsuario");
        this.helper = new BDHelper(contexto, nombreBD, null, version);
        Log.d("Estado","[BDHelper]: Inicializado Correctamente");
    }

    public String Insertar(Usuario _usuario) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        // USANDO CONSTANTES DE COLUMNA
        valores.put(BDHelper.COL_NOMBRE, _usuario.getNombre());
        valores.put(BDHelper.COL_CORREO, _usuario.getCorreo());
        valores.put(BDHelper.COL_CONTRASENA, _usuario.getContraseña());

        // USANDO CONSTANTE DE TABLA
        long fila = db.insert(BDHelper.TABLA_USUARIO,null,valores);
        Log.d("Estado","Fila Insertar: " + fila);
        db.close();

        if(fila > 0)
            return "OK";
        else
            return "[ERROR]: Registro invalido";
    }

    // Metodo LISTAR
    public List<Usuario> Listar(){
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        // USANDO CONSTANTE DE TABLA EN SENTENCIA SQL
        String sql = "SELECT * FROM " + BDHelper.TABLA_USUARIO;
        Cursor registros = db.rawQuery(sql,null);

        if(registros.moveToFirst()){
            do {
                // USANDO CONSTANTES PARA EL MAPEADO DE COLUMNAS (más seguro)
                String nombre = registros.getString(registros.getColumnIndexOrThrow(BDHelper.COL_NOMBRE));
                String correo = registros.getString(registros.getColumnIndexOrThrow(BDHelper.COL_CORREO));
                String contraseña = registros.getString(registros.getColumnIndexOrThrow(BDHelper.COL_CONTRASENA));

                Usuario u = new Usuario(nombre, correo, contraseña);
                lista.add(u);
            } while (registros.moveToNext());
        }
        registros.close();
        db.close();
        return lista;
    }

    // Metodo ELIMINAR
    public boolean Eliminar(int idUsuario){
        SQLiteDatabase db = helper.getWritableDatabase();

        // USANDO CONSTANTES DE TABLA Y COLUMNA
        int filas = db.delete(BDHelper.TABLA_USUARIO,
                BDHelper.ID_USUARIO + "=?",
                new String[]{String.valueOf(idUsuario)});
        db.close();
        return filas > 0;
    }

    // Metodo ACTUALIZAR
    public boolean Actualizar(Usuario u, int idUsuario) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        // USANDO CONSTANTES DE COLUMNA
        valores.put(BDHelper.COL_NOMBRE, u.getNombre());
        valores.put(BDHelper.COL_CORREO, u.getCorreo());
        valores.put(BDHelper.COL_CONTRASENA, u.getContraseña());

        int filas = db.update(BDHelper.TABLA_USUARIO,
                valores,
                BDHelper.ID_USUARIO + "=?",
                new String[]{String.valueOf(idUsuario)});
        db.close();

        return filas > 0;
    }
}

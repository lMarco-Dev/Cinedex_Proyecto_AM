package com.example.cinedex.Data.Access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.cinedex.Data.Models.Usuario;

import java.util.ArrayList;
import java.util.List;

public class DAOUsuario {

    private BDHelper helper;

    public DAOUsuario(Context context) {
        helper = new BDHelper(context);
        Log.d("Estado","[BDHelper]: Inicializado Correctamente");
    }

    // ✅ INSERTAR USUARIO LOCAL (solo después de confirmar con API)
    public boolean Insertar(Usuario _usuario) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(BDHelper.COL_NOMBRE_USUARIO, _usuario.getNombreUsuario());
        valores.put(BDHelper.COL_EMAIL, _usuario.getEmail());
        valores.put(BDHelper.COL_CONTRASENA, _usuario.getContraseña());
        valores.put(BDHelper.COL_NOMBRES, _usuario.getNombres());
        valores.put(BDHelper.COL_APELLIDOS, _usuario.getApellidos());
        valores.put(BDHelper.COL_RANGO_ACTUAL, _usuario.getIdRangoActual());

        long fila = db.insert(BDHelper.TABLA_USUARIO, null, valores);
        db.close();

        return fila > 0;
    }

    // ✅ LISTAR TODOS
    public List<Usuario> Listar(){
        List<Usuario> lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT * FROM " + BDHelper.TABLA_USUARIO;
        Cursor registros = db.rawQuery(sql,null);

        int idIndex = registros.getColumnIndexOrThrow(BDHelper.ID_USUARIO);
        int nombreUsuario = registros.getColumnIndexOrThrow(BDHelper.COL_NOMBRE_USUARIO);
        int email = registros.getColumnIndexOrThrow(BDHelper.COL_EMAIL);
        int contraseña = registros.getColumnIndexOrThrow(BDHelper.COL_CONTRASENA);
        int nombres = registros.getColumnIndexOrThrow(BDHelper.COL_NOMBRES);
        int apellidos = registros.getColumnIndexOrThrow(BDHelper.COL_APELLIDOS);
        int rango = registros.getColumnIndexOrThrow(BDHelper.COL_RANGO_ACTUAL);

        if(registros.moveToFirst()){
            do {
                Usuario u = new Usuario();
                u.setIdUsuario(registros.getInt(idIndex));
                u.setNombreUsuario(registros.getString(nombreUsuario));
                u.setEmail(registros.getString(email));
                u.setContraseña(registros.getString(contraseña));
                u.setNombres(registros.getString(nombres));
                u.setApellidos(registros.getString(apellidos));
                u.setIdRangoActual(registros.getInt(rango));

                lista.add(u);
            } while (registros.moveToNext());
        }
        registros.close();
        db.close();
        return lista;
    }

    // ✅ ELIMINAR
    public boolean Eliminar(int idUsuario){
        SQLiteDatabase db = helper.getWritableDatabase();
        int filas = db.delete(BDHelper.TABLA_USUARIO,
                BDHelper.ID_USUARIO + "=?",
                new String[]{String.valueOf(idUsuario)});
        db.close();
        return filas > 0;
    }

    // ✅ ACTUALIZAR
    public boolean Actualizar(Usuario u, int idUsuario) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues valores = new ContentValues();

        valores.put(BDHelper.COL_NOMBRE_USUARIO, u.getNombreUsuario());
        valores.put(BDHelper.COL_EMAIL, u.getEmail());
        valores.put(BDHelper.COL_CONTRASENA, u.getContraseña());
        valores.put(BDHelper.COL_NOMBRES, u.getNombres());
        valores.put(BDHelper.COL_APELLIDOS, u.getApellidos());
        valores.put(BDHelper.COL_RANGO_ACTUAL, u.getIdRangoActual());

        int filas = db.update(BDHelper.TABLA_USUARIO,
                valores,
                BDHelper.ID_USUARIO + "=?",
                new String[]{String.valueOf(idUsuario)});
        db.close();
        return filas > 0;
    }

    // ✅ Verificar si existe usuario (para evitar duplicar sesión local)
    public boolean ExisteUsuario(String nombreUsuario){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT 1 FROM " + BDHelper.TABLA_USUARIO + " WHERE " + BDHelper.COL_NOMBRE_USUARIO + " = ?",
                new String[]{nombreUsuario}
        );

        boolean existe = cursor.moveToFirst();
        cursor.close();
        db.close();
        return existe;
    }
}

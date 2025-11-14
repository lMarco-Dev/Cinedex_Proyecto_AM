package com.example.cinedex.Data.Access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.cinedex.Data.Models.Resena;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DAOResena {
    private BDHelper helper;

    public DAOResena(Context ctx){
        helper = new BDHelper(ctx);
    }

    public boolean InsertarLocal(int idUsuario, int idPelicula, String titulo, String genero,
                                 int anio, String sinopsis, String actores, String direccion,
                                 String comentario, float puntuacion, double lat, double lon) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("IdUsuario", idUsuario);
        cv.put("IdPelicula", idPelicula);
        cv.put("TituloPelicula", titulo);
        cv.put("Genero", genero);
        cv.put("Anio", anio);
        cv.put("Sinopsis", sinopsis);
        cv.put("Actores", actores);
        cv.put("Direccion", direccion);
        cv.put("Comentario", comentario);
        cv.put("Puntuacion", puntuacion);
        cv.put("Latitud", lat);
        cv.put("Longitud", lon);
        String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        cv.put("FechaRegistro", fecha);

        long fila = db.insert("ResenaLocal", null, cv);
        db.close();
        return fila > 0;
    }

    public List<Resena> ListarPorPelicula(int idPelicula){
        List<Resena> lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ResenaLocal WHERE IdPelicula = ? ORDER BY FechaRegistro DESC", new String[]{String.valueOf(idPelicula)});
        if(c.moveToFirst()){
            do{
                Resena r = new Resena();
                r.setIdReseña(c.getInt(c.getColumnIndexOrThrow("IdResena")));
                r.setIdUsuario(c.getInt(c.getColumnIndexOrThrow("IdUsuario")));
                r.setIdPelicula(c.getInt(c.getColumnIndexOrThrow("IdPelicula")));
                r.setReseñaTexto(c.getString(c.getColumnIndexOrThrow("Comentario")));
                r.setPuntuacion((float) c.getDouble(c.getColumnIndexOrThrow("Puntuacion")));
                // Puedes añadir setters para lat/lon si extiendes el modelo
                lista.add(r);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }

    public List<Resena> ListarPorUsuario(int idUsuario){
        List<Resena> lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM ResenaLocal WHERE IdUsuario = ? ORDER BY FechaRegistro DESC", new String[]{String.valueOf(idUsuario)});
        if(c.moveToFirst()){
            do{
                Resena r = new Resena();
                r.setIdReseña(c.getInt(c.getColumnIndexOrThrow("IdResena")));
                r.setIdUsuario(c.getInt(c.getColumnIndexOrThrow("IdUsuario")));
                r.setIdPelicula(c.getInt(c.getColumnIndexOrThrow("IdPelicula")));
                r.setReseñaTexto(c.getString(c.getColumnIndexOrThrow("Comentario")));
                r.setPuntuacion((float) c.getDouble(c.getColumnIndexOrThrow("Puntuacion")));
                lista.add(r);
            } while(c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }

    public int ContarPorPuntuacion(int idPelicula, int estrella){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM ResenaLocal WHERE IdPelicula = ? AND ROUND(Puntuacion) = ?",
                new String[]{String.valueOf(idPelicula), String.valueOf(estrella)});
        int count = 0;
        if(c.moveToFirst()) count = c.getInt(0);
        c.close();
        db.close();
        return count;
    }
}

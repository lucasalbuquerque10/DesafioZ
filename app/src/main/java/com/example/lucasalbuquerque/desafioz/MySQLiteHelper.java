package com.example.lucasalbuquerque.desafioz;

/**
 * Created by Lucas Albuquerque on 17/04/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MySQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "moviesManager";

    private static final String TABLE_MOVIES = "movies";

    private static final String KEY_ID = "id";
    private static final String KEY_MOVIE = "movie";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_MOVIES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_MOVIE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);

        onCreate(db);

    }

    public void addContact(Movie movie) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_MOVIE, movie.getJSONString());

        db.insert(TABLE_MOVIES, null, values);
        db.close();
    }

    public List<Movie> getAllContacts() {
        List<Movie> movieList = new ArrayList<Movie>();
        String selectQuery = "SELECT  * FROM " + TABLE_MOVIES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Movie movie = new Movie();
                movie.setId_bd(Integer.parseInt(cursor.getString(0)));
                movie.setJSONString(cursor.getString(1));
                movieList.add(movie);
            } while (cursor.moveToNext());
        }

        return movieList;
    }

    public void deleteContact(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MOVIES, KEY_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    Movie getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MOVIES, new String[]{KEY_ID,
                        KEY_MOVIE}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Movie movie = new Movie(cursor.getString(1));
        return movie;
    }
}


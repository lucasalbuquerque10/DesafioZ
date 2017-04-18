package com.example.lucasalbuquerque.desafioz;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView rated, released, runtime, genre, director, writer, actors, plot, language, country, awards, ratings, metascore, imdbRating, imdbVotes, imdbID, type, dvd, boxOffice, production, website;
    ImageView poster;
    Toolbar myToolbar;
    ImageButton button_save, button_delete;
    Movie movie;
    MySQLiteHelper db;
    String jsonString;
    int id_bd_movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movieObj");
        String saveOrDel = (String) intent.getSerializableExtra("saveOrDel");
        jsonString = (String) intent.getSerializableExtra("movieJson");
        if (intent.getSerializableExtra("id_bd_movie") != null) {
            id_bd_movie = (int) intent.getSerializableExtra("id_bd_movie");
        }


        db = new MySQLiteHelper(this);

        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(movie.getTitle() + " " + "(" + movie.getYear() + ")");
        myToolbar.setTitleTextColor(Color.WHITE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myToolbar.setNavigationIcon(getResources().getDrawable(android.R.drawable.ic_media_previous));

        button_save = (ImageButton) findViewById(R.id.button_save);
        button_delete = (ImageButton) findViewById(R.id.button_delete);
        if (saveOrDel.equalsIgnoreCase("del")) {
            button_delete.setVisibility(View.VISIBLE);
            button_save.setVisibility(View.INVISIBLE);
        } else {
            button_save.setVisibility(View.VISIBLE);
            button_delete.setVisibility(View.INVISIBLE);
        }
        poster = (ImageView) findViewById(R.id.poster);
        rated = (TextView) findViewById(R.id.rated);
        released = (TextView) findViewById(R.id.released);
        runtime = (TextView) findViewById(R.id.runtime);
        genre = (TextView) findViewById(R.id.genre);
        director = (TextView) findViewById(R.id.director);
        writer = (TextView) findViewById(R.id.writer);
        actors = (TextView) findViewById(R.id.actors);
        plot = (TextView) findViewById(R.id.plot);
        language = (TextView) findViewById(R.id.language);
        country = (TextView) findViewById(R.id.country);
        awards = (TextView) findViewById(R.id.awards);
        ratings = (TextView) findViewById(R.id.ratings);
        metascore = (TextView) findViewById(R.id.metascore);
        imdbRating = (TextView) findViewById(R.id.imdbRating);
        imdbVotes = (TextView) findViewById(R.id.imdbVotes);
        imdbID = (TextView) findViewById(R.id.imdbID);
        type = (TextView) findViewById(R.id.type);
        dvd = (TextView) findViewById(R.id.dvd);
        boxOffice = (TextView) findViewById(R.id.boxOffice);
        production = (TextView) findViewById(R.id.production);
        website = (TextView) findViewById(R.id.website);


        new DownloadImageTask((ImageView) findViewById(R.id.poster))
                .execute(movie.getPoster());
        rated.setText("Rated: " + movie.getRated());
        released.setText("Released: " + movie.getReleased());
        runtime.setText("Runtime: " + movie.getRuntime());
        genre.setText("Genre: " + movie.getGenre());
        writer.setText("Writer: " + movie.getWriter());
        actors.setText("Actors: " + movie.getActors());
        plot.setText("Plot: " + movie.getPlot());
        language.setText("Language: " + movie.getLanguage());
        country.setText("Country: " + movie.getCountry());
        awards.setText("Awards: " + movie.getAwards());
        ratings.setText("Ratings: " + movie.getSource() + " " + movie.getValue());
        metascore.setText("Metascore: " + movie.getMetascore());
        imdbRating.setText("imdbRating: " + movie.getImdbRating());
        imdbVotes.setText("imdbVotes: " + movie.getImdbVotes());
        imdbID.setText("imdbID: " + movie.getImdbID());
        type.setText("Type: " + movie.getType());
        dvd.setText("DVD: " + movie.getDvd());
        boxOffice.setText("BoxOffice: " + movie.getBoxOffice());
        production.setText("Production: " + movie.getProduction());
        website.setText("Website: " + movie.getWebsite());

        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.addContact(movie);
                List<Movie> movies = db.getAllContacts();
                for (Movie mv : movies) {
                    String log = "Id: " + mv.getId_bd() + " ,Name: " + mv.getJSONString();
                    Log.d("Name: ", log);
                }

                Intent myIntent = new Intent(DetailActivity.this, MainActivity.class);
                DetailActivity.this.startActivity(myIntent);

            }
        });

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.deleteContact(id_bd_movie);
                List<Movie> movies = db.getAllContacts();
                for (Movie mv : movies) {
                    String log = "Id: " + mv.getId_bd() + " ,Name: " + mv.getJSONString();
                    Log.d("Name: ", log);
                }

                Intent myIntent = new Intent(DetailActivity.this, MainActivity.class);
                DetailActivity.this.startActivity(myIntent);

            }
        });


    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView poster;

        public DownloadImageTask(ImageView bmImage) {
            this.poster = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                poster.setImageBitmap(result);
            } else {
                poster.setImageResource(R.drawable.noimage);
            }

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}

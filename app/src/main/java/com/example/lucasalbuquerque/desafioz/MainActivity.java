package com.example.lucasalbuquerque.desafioz;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Dialog dialog;
    ListView lista_filmes;
    EditText et_input;
    final Context context = this;
    TextView tv_semFilme;
    String parameter;
    private String TAG = MainActivity.class.getSimpleName();
    Movie movie;
    String response, jsonString;
    MySQLiteHelper db;
    int id_bd_movie;
    Toolbar myToolbar;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Meus Filmes");
        myToolbar.setTitleTextColor(Color.WHITE);

        db = new MySQLiteHelper(this);

        lista_filmes = (ListView) findViewById(R.id.lv_filmes);
        ImageButton button_search = (ImageButton) findViewById(R.id.button_search);
        tv_semFilme = (TextView) findViewById(R.id.tv_semFilme);


        List<Movie> movies = db.getAllContacts();
        ListArrayAdapter adapter = new ListArrayAdapter(this,
                R.layout.list_item, movies);
        lista_filmes.setAdapter(adapter);

        if (lista_filmes.getAdapter().getCount() == 0) {
            tv_semFilme.setVisibility(View.VISIBLE);
        } else {
            tv_semFilme.setVisibility(View.INVISIBLE);
        }
        lista_filmes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Movie movie = (Movie) parent.getItemAtPosition(position);
                id_bd_movie = movie.getId_bd();

                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(movie.getJSONString());

                    String title = jsonObj.getString("Title");
                    String year = jsonObj.getString("Year");
                    String rated = jsonObj.getString("Rated");
                    String released = jsonObj.getString("Released");
                    String runtime = jsonObj.getString("Runtime");
                    String genre = jsonObj.getString("Genre");
                    String director = jsonObj.getString("Director");
                    String writer = jsonObj.getString("Writer");
                    String actors = jsonObj.getString("Actors");
                    String plot = jsonObj.getString("Plot");
                    String language = jsonObj.getString("Language");
                    String country = jsonObj.getString("Country");
                    String awards = jsonObj.getString("Awards");
                    String poster = jsonObj.getString("Poster");
                    String metascore = jsonObj.getString("Metascore");
                    String imdbRating = jsonObj.getString("imdbRating");
                    String imdbVotes = jsonObj.getString("imdbVotes");
                    String imdbID = jsonObj.getString("imdbID");
                    String type = jsonObj.getString("Type");
                    String dvd = jsonObj.getString("DVD");
                    String boxOffice = jsonObj.getString("BoxOffice");
                    String production = jsonObj.getString("Production");
                    String website = jsonObj.getString("Website");

                    JSONArray ratings = jsonObj.getJSONArray("Ratings");
                    JSONObject d = ratings.getJSONObject(0);
                    String source = d.getString("Source");
                    String value = d.getString("Value");

                    movie = new Movie(movie.getJSONString(), title, year, rated, released, runtime, genre, director, writer, actors, plot, language, country, awards, poster, metascore, imdbRating, imdbVotes, imdbID, type, dvd, boxOffice, production, website, response, source, value);
                    Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                    myIntent.putExtra("movieObj", movie);
                    myIntent.putExtra("movieJson", movie.getJSONString());
                    myIntent.putExtra("saveOrDel", "del");
                    myIntent.putExtra("id_bd_movie", id_bd_movie);
                    MainActivity.this.startActivity(myIntent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        button_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom_dialog_search);
                dialog.setTitle("Title...");

                TextView text = (TextView) dialog.findViewById(R.id.text);
                et_input = (EditText) dialog.findViewById(R.id.et_input);

                Button btn_pesquisar = (Button) dialog.findViewById(R.id.btn_pesquisar);

                btn_pesquisar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!et_input.getText().toString().matches("")) {
                            dialog.dismiss();
                            parameter = et_input.getText().toString().replaceAll("\\s", "+");
                            new GetContacts().execute();
                        } else {
                            et_input.setError("Campo obrigatório");
                        }
                    }
                });
                dialog.show();
            }


        });

    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall("http://www.omdbapi.com/?t=" + parameter);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    jsonString = jsonObj.toString();
                    response = jsonObj.getString("Response");
                    if (response.equalsIgnoreCase("true")) {


                        String title = jsonObj.getString("Title");
                        String year = jsonObj.getString("Year");
                        String rated = jsonObj.getString("Rated");
                        String released = jsonObj.getString("Released");
                        String runtime = jsonObj.getString("Runtime");
                        String genre = jsonObj.getString("Genre");
                        String director = jsonObj.getString("Director");
                        String writer = jsonObj.getString("Writer");
                        String actors = jsonObj.getString("Actors");
                        String plot = jsonObj.getString("Plot");
                        String language = jsonObj.getString("Language");
                        String country = jsonObj.getString("Country");
                        String awards = jsonObj.getString("Awards");
                        String poster = jsonObj.getString("Poster");
                        String metascore = jsonObj.getString("Metascore");
                        String imdbRating = jsonObj.getString("imdbRating");
                        String imdbVotes = jsonObj.getString("imdbVotes");
                        String imdbID = jsonObj.getString("imdbID");
                        String type = jsonObj.getString("Type");
                        String dvd = jsonObj.getString("DVD");
                        String boxOffice = jsonObj.getString("BoxOffice");
                        String production = jsonObj.getString("Production");
                        String website = jsonObj.getString("Website");

                        JSONArray ratings = jsonObj.getJSONArray("Ratings");
                        JSONObject d = ratings.getJSONObject(0);
                        String source = d.getString("Source");
                        String value = d.getString("Value");

                        movie = new Movie(jsonString, title, year, rated, released, runtime, genre, director, writer, actors, plot, language, country, awards, poster, metascore, imdbRating, imdbVotes, imdbID, type, dvd, boxOffice, production, website, response, source, value);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            if (response.equalsIgnoreCase("true")) {

                Intent myIntent = new Intent(MainActivity.this, DetailActivity.class);
                myIntent.putExtra("movieObj", movie);
                myIntent.putExtra("movieJson", jsonString);
                myIntent.putExtra("saveOrDel", "save");
                MainActivity.this.startActivity(myIntent);
            } else {
                Toast.makeText(getApplicationContext(),
                        "Filme não encontrado!",
                        Toast.LENGTH_LONG)
                        .show();
            }

        }

    }


}


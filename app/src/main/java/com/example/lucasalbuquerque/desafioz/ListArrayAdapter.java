package com.example.lucasalbuquerque.desafioz;

/**
 * Created by Lucas Albuquerque on 17/04/2017.
 */


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.List;

public class ListArrayAdapter extends ArrayAdapter<Movie> {
    private Context context;
    private List<Movie> movies;

    public ListArrayAdapter(Context context, int i, List<Movie> movies) {
        super(context, R.layout.list_item, movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.list_text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_poster);
        try {
            JSONObject jsonObj = new JSONObject(movies.get(position).getJSONString());
            textView.setText(jsonObj.getString("Title") + " " + "(" + jsonObj.getString("Year") + ")");
            new DownloadImageTask((ImageView) rowView.findViewById(R.id.list_poster))
                    .execute(jsonObj.getString("Poster"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rowView;
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
}

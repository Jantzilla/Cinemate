package com.creativesourceapps.android.cinemate;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DetailActivity extends AppCompatActivity {

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public ArrayAdapter requestMovieData(String movieId, String endpoint) {
        String response;

        //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\

        final String MOVIES_API_KEY = "";

        //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\



        //Default Movies API endpoint
        String myUrl = "https://api.themoviedb.org/3/movie/" + movieId + "/" + endpoint + "?api_key=" + MOVIES_API_KEY;


        final ArrayList<String> listItem = new ArrayList<>();
        final ArrayList<String> clipNum = new ArrayList<>();
        listItem.clear();

        HttpRequest getRequest = new HttpRequest();

        if(isOnline()) {
            try {
                response = getRequest.execute(myUrl).get();

                JSONObject json = new JSONObject(response);
                JSONArray items = json.getJSONArray("results");

                for (int i = 0; i < items.length(); i++) {
                    JSONObject resultObject = items.getJSONObject(i);
                    if(endpoint.equals("videos")) {
                        listItem.add(resultObject.getString("key"));
                        clipNum.add(String.valueOf(i+1));
                    } else {
                        listItem.add(resultObject.getString("content"));
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e){
                e.printStackTrace();
                Toast.makeText(this,"Unable to get movie data. Make sure you have added your \"API KEY\" to MainActivity.java", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,"No Network Connection. Please reconnect and reload page.", Toast.LENGTH_LONG).show();
        }

        //MoviesAdapter moviesAdapter = new MoviesAdapter(this, listItem);

        if(endpoint.equals("videos")) {
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.trailer_list_item, R.id.tv_trailer_number, clipNum);

            ListView listView = findViewById(R.id.lv_trailers);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String item_clicked = listItem.get(position);

                    Uri uriUrl = Uri.parse("https://www.youtube.com/watch?v="+item_clicked);
                    Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
                    startActivity(launchBrowser);



                }
            });

            return adapter;

        } else {
            ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.review_list_item,R.id.tv_review_item, listItem);

            ListView listView = findViewById(R.id.lv_reviews);
            listView.setAdapter(adapter);

            return adapter;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_detail);
        final SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        final ContentValues values = new ContentValues();

        final Movie movie = getIntent().getParcelableExtra("parcel_data");
        final ImageView imageView = findViewById(R.id.imageView);
        final ImageView imageView2 = findViewById(R.id.imageView2);

        requestMovieData(String.valueOf(movie.id), "videos");
        requestMovieData(String.valueOf(movie.id), "reviews");

        if(sharedpreferences.getString("Favorite"+movie.id,"false").equals("true"))
            imageView2.setImageResource(android.R.drawable.btn_star_big_on);

        TextView title = findViewById(R.id.textView);
        TextView releaseDate = findViewById(R.id.textView2);
        TextView rating = findViewById(R.id.textView3);
        TextView description = findViewById(R.id.textView4);

        releaseDate.setText(movie.releaseDate);
        rating.setText(movie.voteAverage);
        title.setText(movie.title);
        description.setText(movie.overview);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+movie.poster).into(imageView);

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sharedpreferences.getString("Favorite"+movie.id,"false").equals("true")) {
                    imageView2.setImageResource(android.R.drawable.btn_star_big_off);
                    editor.putString("Favorite"+movie.id, "false");
                    editor.commit();

                    getContentResolver().delete(FavoritesContract.FAVORITE_URI, String.valueOf(movie.id), null);

                }
                else {
                    imageView2.setImageResource(android.R.drawable.btn_star_big_on);
                    editor.putString("Favorite"+movie.id, "true");
                    editor.commit();

                    values.put(FavoritesContract.COLUMN_MOVIE_NAME, movie.title.trim());
                    values.put(FavoritesContract.COLUMN_MOVIE_ID, movie.id);
                    values.put(FavoritesContract.COLUMN_MOVIE_IMAGE, movie.poster.trim());

                    //ContentResolver will access the Employee Content Provider
                    Uri newUri = getContentResolver().insert(FavoritesContract.FAVORITE_URI, values);

                    String newUserId = newUri.getLastPathSegment();

                    Toast.makeText(getApplicationContext(),"Successfully saved. New User ID is " + newUserId, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
}

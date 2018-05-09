package com.creativesourceapps.android.cinemate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void requestMovieData() {
        String response;
        SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);



                                //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\

                        final String MOVIES_API_KEY = "";

                                //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\


        //Default Movies API endpoint
        String myUrl = "https://api.themoviedb.org/3/movie/" + sharedpreferences.getString("SortBy", "popular") + "?api_key=" + MOVIES_API_KEY;


        final ArrayList<Movie> movies = new ArrayList<>();
        movies.clear();

        if(sharedpreferences.getString("SortBy", "popular").equals("favorites")) {

            String[] projection = {"image"};
            Cursor cursor = getContentResolver().query(FavoritesContract.FAVORITE_URI, projection,null,null,null);
            if(cursor.moveToFirst()) {
                do {
                    Movie movie = new Movie(0,"10/10","movie", cursor.getString(0),
                            "details","unknown");
                    movies.add(movie);

                }while(cursor.moveToNext());
            }
            cursor.close();

        } else {

            HttpRequest getRequest = new HttpRequest();

            if (isOnline()) {
                try {
                    response = getRequest.execute(myUrl).get();

                    JSONObject json = new JSONObject(response);
                    JSONArray items = json.getJSONArray("results");

                    for (int i = 0; i < items.length(); i++) {
                        JSONObject resultObject = items.getJSONObject(i);
                        Movie movie = new Movie(resultObject.getInt("id"),
                                resultObject.getString("vote_average") + "/10",
                                resultObject.getString("title"),
                                resultObject.getString("poster_path"),
                                resultObject.getString("overview"),
                                resultObject.getString("release_date"));
                        movies.add(movie);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to get movie data. Make sure you have added your \"API KEY\" to MainActivity.java", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "No Network Connection. Please reconnect and reload page.", Toast.LENGTH_LONG).show();
            }
        }


        MoviesAdapter moviesAdapter = new MoviesAdapter(this, movies);

        GridView gridView = findViewById(R.id.gridview_movie);
        gridView.setAdapter(moviesAdapter);

        if(!sharedpreferences.getString("SortBy", "popular").equals("favorites")) {

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    Movie item_clicked = movies.get(position);

                    Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                    intent.putExtra("parcel_data", item_clicked);
                    startActivity(intent);

                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

                requestMovieData();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        GridView gridView = findViewById(R.id.gridview_movie);

        switch (item.getItemId()) {
            case R.id.popular:
                editor.putString("SortBy", "popular");
                editor.apply();
                requestMovieData();
                break;

            case R.id.top_rated:
                editor.putString("SortBy", "top_rated");
                editor.apply();
                requestMovieData();
                break;

            case R.id.favorites:
                editor.putString("SortBy", "favorites");
                editor.apply();
                gridView.setOnItemClickListener(null);
                requestMovieData();
                break;
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.android_action_bar_spinner_menu, menu);
        return true;
    }
}

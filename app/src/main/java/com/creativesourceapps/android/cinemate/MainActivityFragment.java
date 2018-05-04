package com.creativesourceapps.android.cinemate;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class MainActivityFragment extends Fragment {

    private MoviesAdapter moviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ArrayList<Movie> movies = new ArrayList<Movie>();


        String moviesApiKey = "6e520b25b87418f51e3f5d6be319d4ae";

        //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\

        //Movies API endpoint
        String myUrl = "https://api.themoviedb.org/3/movie/popular?api_key="+moviesApiKey;

        String response;

        HttpRequest getRequest = new HttpRequest();

        try {
            response = getRequest.execute(myUrl).get();

            JSONObject json = new JSONObject(response);
            JSONArray items = json.getJSONArray("results");

            for (int i = 0; i < items.length(); i++) {
                JSONObject resultObject = items.getJSONObject(i);
                Movie movie = new Movie(resultObject.getString("title"),
                        resultObject.getString("overview"),
                        resultObject.getInt("id"),
                        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185/"+resultObject.getString("poster_path")));
                movies.add(movie);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        moviesAdapter = new MoviesAdapter(getActivity(), movies);

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(moviesAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Movie item_clicked = movies.get(position);

                Toast.makeText(getContext(), item_clicked.versionName, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra("parcel_data", (Parcelable) item_clicked);
                startActivity(intent);

            }
        });

        return rootView;
    }
}
package com.creativesourceapps.android.cinemate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class MainActivityFragment extends Fragment {

    private MoviesAdapter moviesAdapter;

    Movie[] movies = {
            new Movie("Cupcake", "1.5", R.drawable.ic_launcher_foreground),
            new Movie("Donut", "1.6", R.drawable.ic_launcher_foreground),
            new Movie("Eclair", "2.0-2.1", R.drawable.ic_launcher_foreground),
            new Movie("Froyo", "2.2-2.2.3", R.drawable.ic_launcher_foreground),
            new Movie("GingerBread", "2.3-2.3.7", R.drawable.ic_launcher_foreground),
            new Movie("Honeycomb", "3.0-3.2.6", R.drawable.ic_launcher_foreground),
            new Movie("Ice Cream Sandwich", "4.0-4.0.4", R.drawable.ic_launcher_foreground),
            new Movie("Jelly Bean", "4.1-4.3.1", R.drawable.ic_launcher_foreground),
            new Movie("KitKat", "4.4-4.4.4", R.drawable.ic_launcher_foreground),
            new Movie("Lollipop", "5.0-5.1.1", R.drawable.ic_launcher_foreground)
    };

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        moviesAdapter = new MoviesAdapter(getActivity(), Arrays.asList(movies));

        // Get a reference to the GridView, and attach this adapter to it.
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie);
        gridView.setAdapter(moviesAdapter);


        String moviesApiKey = "6e520b25b87418f51e3f5d6be319d4ae";

                //**\\**//**\\YOUR API KEY GOES HERE//**\\**//**\\

        //Movies API endpoint
        String myUrl = "https://api.themoviedb.org/3/movie/popular?api_key="+moviesApiKey;

        String response;

        HttpRequest getRequest = new HttpRequest();

        try {
            response = getRequest.execute(myUrl).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String item_clicked = parent.getItemAtPosition(position).toString();

                Toast.makeText(getContext(), item_clicked, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), DetailActivity.class);
                startActivity(intent);

            }
        });

        return rootView;
    }
}
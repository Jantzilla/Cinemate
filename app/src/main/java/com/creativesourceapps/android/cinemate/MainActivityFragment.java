package com.creativesourceapps.android.cinemate;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.Arrays;


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

        return rootView;
    }
}
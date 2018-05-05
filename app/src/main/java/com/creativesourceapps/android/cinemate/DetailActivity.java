package com.creativesourceapps.android.cinemate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Movie movie = getIntent().getParcelableExtra("parcel_data");
        ImageView imageView = findViewById(R.id.imageView);
        TextView title = findViewById(R.id.textView);
        TextView releaseDate = findViewById(R.id.textView2);
        TextView rating = findViewById(R.id.textView3);
        TextView description = findViewById(R.id.textView4);
        releaseDate.setText(movie.releaseDate);
        rating.setText(movie.voteAverage);
        title.setText(movie.title);
        description.setText(movie.overview);
        Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/"+movie.poster).into(imageView);
    }
}

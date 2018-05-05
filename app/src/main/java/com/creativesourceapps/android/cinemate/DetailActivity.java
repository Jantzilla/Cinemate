package com.creativesourceapps.android.cinemate;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final SharedPreferences sharedpreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();

        final Movie movie = getIntent().getParcelableExtra("parcel_data");
        final ImageView imageView = findViewById(R.id.imageView);
        final ImageView imageView2 = findViewById(R.id.imageView2);

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
                }
                else {
                    imageView2.setImageResource(android.R.drawable.btn_star_big_on);
                    editor.putString("Favorite"+movie.id, "true");
                    editor.commit();
                }
            }
        });
    }
}

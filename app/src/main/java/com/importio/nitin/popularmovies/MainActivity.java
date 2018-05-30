package com.importio.nitin.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.movieList_rv);
        RecyclerViewClickListener listener = new RecyclerViewClickListener() {
            @Override
            public void onClick(int position) {
                Log.d("Nitin", position + " clicked ");
            }
        };
        MainScreenAdapter adapter = new MainScreenAdapter(new ArrayList<MovieDetails>(), listener);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);
    }
}

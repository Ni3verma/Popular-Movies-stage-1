package com.importio.nitin.popularmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainScreenAdapter extends RecyclerView.Adapter<MainScreenAdapter.MyViewHolder>{

    ArrayList<MovieDetails> movieList;
    RecyclerViewClickListener mListener;
    MainScreenAdapter(ArrayList<MovieDetails> list, RecyclerViewClickListener listener){
        this.movieList = list;
        this.mListener=listener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_rv,parent,false);

        return new MyViewHolder(view,mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //MovieDetails movie=movieList.get(position);
        holder.poster.setImageResource(R.drawable.ic_launcher_background);
    }

    @Override
    public int getItemCount() {
        //return movieList.size();
        return 60;
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private RecyclerViewClickListener mListener;
        ImageView poster;
        public MyViewHolder(View itemView,RecyclerViewClickListener listener) {
            super(itemView);
            mListener=listener;
            poster=itemView.findViewById(R.id.movie_image);
            poster.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(getAdapterPosition());
        }
    }
}

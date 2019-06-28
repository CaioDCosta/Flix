package com.example.flix;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flix.models.Config;
import com.example.flix.models.Movie;

import org.parceler.Parcels;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private static final int FADING_LENGTH_LANDSCAPE = 30;
    private static final int FADING_LENGTH_PORTRAIT = 80;
    private static final int ROUNDED_RADIUS = 30;
    private static final int ROUND_MARGIN = 10;

    // List of movies
    ArrayList<Movie> movies;

    // Config for getting images
    Config config;

    // Rendering Context
    Context context;

    // RecyclerView for setting fade length
    RecyclerView rvMovies;

    public MovieAdapter(ArrayList<Movie> movies, RecyclerView rvMovies) {
        this.movies = movies;
        this.rvMovies = rvMovies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View movieView = inflater.inflate(R.layout.item_movie, viewGroup, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Movie movie = movies.get(i);
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());

        String imageUrl = null;
        // Orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if(isPortrait) {
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
            rvMovies.setFadingEdgeLength(FADING_LENGTH_PORTRAIT);
        }
        else {
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
            rvMovies.setFadingEdgeLength(FADING_LENGTH_LANDSCAPE);
        }

        ImageView imageView = isPortrait ? viewHolder.ivPosterImage : viewHolder.ivBackdropImage;
        int placeholderID = isPortrait ? R.drawable.flicks_movie_placeholder : R.drawable.flicks_backdrop_placeholder;

        Glide.with(context).load(imageUrl).bitmapTransform(new RoundedCornersTransformation(context, ROUNDED_RADIUS, ROUND_MARGIN))
                .placeholder(placeholderID).error(placeholderID).into(imageView);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Nullable @BindView(R.id.ivPosterImage) ImageView ivPosterImage;
        @Nullable @BindView(R.id.ivBackdropImage) ImageView ivBackdropImage;
        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvOverview) TextView tvOverview;

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            if (position != RecyclerView.NO_POSITION) {
                // Get movie at the current position
                Movie movie = movies.get(position);
                // Create intent for the new activity
                Intent intent = new Intent(context, MovieDetail.class);
                // Serialize the movie using parceler, use its short name as a key
                Bundle bundle = new Bundle(2);
                bundle.putParcelable(Movie.class.getSimpleName(), Parcels.wrap(movie));
                bundle.putParcelable(Config.class.getSimpleName(), Parcels.wrap(config));
                intent.putExtras(bundle);
                // Show the activity
                context.startActivity(intent);
            }
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }
    }

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }
}

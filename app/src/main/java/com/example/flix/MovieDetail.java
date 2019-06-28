package com.example.flix;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flix.models.Config;
import com.example.flix.models.Movie;

import org.parceler.Parcels;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetail extends AppCompatActivity {

	// the movie to display
	Movie movie;
	Config config;

	TextView tvTitle;
	TextView tvOverview;
	RatingBar rbVoteAverage;
	ImageView ivBackdropImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_movie_detail);
		// unwrap the movie passed in via intent, using its simple name as a key
		movie = (Movie) Parcels.unwrap(getIntent().getExtras().getParcelable(Movie.class.getSimpleName()));
		config = (Config) Parcels.unwrap(getIntent().getExtras().getParcelable(Config.class.getSimpleName()));
		Log.i("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

		tvTitle = (TextView) findViewById(R.id.tvTitle);
		tvOverview = (TextView) findViewById(R.id.tvOverview);
		rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
		ivBackdropImage = (ImageView) findViewById(R.id.ivBackdropImage);

		tvTitle.setText(movie.getTitle());
		tvOverview.setText(movie.getOverview());

		float voteAverage = movie.getVoteAverage().floatValue();
		rbVoteAverage.setRating(voteAverage > 0f ? voteAverage / 2f : 0f);

		String imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());

		// Radius and margin for rounded corners
		int radius = 30;
		int margin = 10;

		Glide.with(ivBackdropImage.getContext()).load(imageUrl).bitmapTransform(new RoundedCornersTransformation(ivBackdropImage.getContext(), radius, margin))
				.placeholder(R.drawable.flicks_backdrop_placeholder).error(R.drawable.flicks_backdrop_placeholder).into(ivBackdropImage);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.getWindow().setUiOptions(View.SYSTEM_UI_FLAG_FULLSCREEN);
	}
}

package com.example.flix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.flix.models.Config;
import com.example.flix.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieDetail extends AppCompatActivity {

	// Base URL
	public static final String API_BASE_URL = "https://api.themoviedb.org/3";

	// API Key Parameter
	public static final String API_KEY_PARAM = "api_key";

	// Activity error logging tag
	public static final String TAG = "MovieDetail";

	// the movie to display
	Movie movie;
	Config config;

	TextView tvTitle;
	TextView tvOverview;
	RatingBar rbVoteAverage;
	ImageView ivBackdropImage;
	String trailerKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);

		setContentView(R.layout.activity_movie_detail);
		// unwrap the movie passed in via intent, using its simple name as a key
		movie = (Movie) Parcels.unwrap(getIntent().getExtras().getParcelable(Movie.class.getSimpleName()));
		config = (Config) Parcels.unwrap(getIntent().getExtras().getParcelable(Config.class.getSimpleName()));
		Log.i("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));
		AsyncHttpClient client = new AsyncHttpClient();
		String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";
		RequestParams params = new RequestParams();
		params.put(API_KEY_PARAM, getString(R.string.api_key));
		client.get(url, params, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				try {
					JSONArray results = response.getJSONArray("results");
					for(int i = 0; i < results.length(); i++) {
						if(results.getJSONObject(i).getString("site").equals("YouTube")) {
							trailerKey = results.getJSONObject(0).getString("key");
							return;
						}
					}
					Toast.makeText(MovieDetail.this, "No YouTube video", Toast.LENGTH_LONG).show();
					findViewById(R.id.ivPlay).setVisibility(View.INVISIBLE);
				} catch (JSONException e) {
					logError("Failed to parse JSON",e,true);
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				logError("Failed to get data from videos endpoint", throwable,true);
			}
		});


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
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
	}

	public void onClickBackdrop(View v) {
		if(trailerKey == null) return;
		Intent intent = new Intent(v.getContext(), MovieTrailer.class);
		intent.putExtra("trailer_key", trailerKey);
		startActivity(intent);
	}

	private void logError(String message, Throwable error, boolean alertUser) {
		Log.e(TAG, message, error);
		if(alertUser) {
			Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
		}
	}
}

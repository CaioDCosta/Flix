package com.example.flix;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MovieTrailer extends YouTubeBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_trailer);

		final String videoId = getIntent().getStringExtra("trailer_key");

		// resolve the player view from the layout
		YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// initialize with API key stored in secrets.xml
		playerView.initialize(getString(R.string.google_api_key), new YouTubePlayer.OnInitializedListener() {
			@Override
			public void onInitializationSuccess(YouTubePlayer.Provider provider,
			                                    YouTubePlayer youTubePlayer, boolean b) {
				// do any work here to cue video, play video, etc.
				youTubePlayer.cueVideo(videoId);
				youTubePlayer.setFullscreen(true);
				youTubePlayer.play();
			}

			@Override
			public void onInitializationFailure(YouTubePlayer.Provider provider,
			                                    YouTubeInitializationResult youTubeInitializationResult) {
				// log the error
				Log.e("MovieTrailerActivity", "Error initializing YouTube player");
			}
		});
	}
}

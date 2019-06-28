package com.example.flix.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Movie {

	// Base URL
	public static final String API_BASE_URL = "https://api.themoviedb.org/3";

	// API Key Parameter
	public static final String API_KEY_PARAM = "api_key";

	// Values from API
	String title;
	String overview;
	String posterPath;
	String backdropPath;
	Double voteAverage;
	String id;

	// Included for Parceler
	public Movie() {
	}

	// Parse from JSON
	public Movie(JSONObject object) throws JSONException {
		title = object.getString("title");
		overview = object.getString("overview");
		posterPath = object.getString("poster_path");
		backdropPath = object.getString("backdrop_path");
		voteAverage = object.getDouble("vote_average");
		id = object.getString("id");
	}

	public String getTitle() {
		return title;
	}

	public String getOverview() {
		return overview;
	}

	public String getPosterPath() {
		return posterPath;
	}

	public String getBackdropPath() { return backdropPath; }

	public Double getVoteAverage() { return voteAverage; }

	public String getId() { return id; }
}

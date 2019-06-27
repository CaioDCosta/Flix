package com.example.flix.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Config {
    // Base image url
    String imageBaseUrl;

    // Poster size
    String posterSize;

    // Backdrop Size
    String backdropSize;

    public Config() {
    }

    public Config(JSONObject object) throws JSONException {
        JSONObject images = object.getJSONObject("images");
        // Parse image base url
        imageBaseUrl = images.getString("secure_base_url");

        // Parse poster size
        JSONArray posterSizeOptions = images.getJSONArray("poster_sizes");
        posterSize = posterSizeOptions.optString(3, "w342");

        // Parse backdrop size
        JSONArray backdropSizeOptions = images.getJSONArray("backdrop_sizes");
        backdropSize = backdropSizeOptions.optString(1, "w780");
    }

    // Return the image url for the given size and path
    public String getImageUrl(String size, String path) {
        return String.format("%s%s%s", imageBaseUrl, size, path);
    }

    public String getImageBaseUrl() {
        return imageBaseUrl;
    }

    public String getPosterSize() { return posterSize; }

    public String getBackdropSize() {
        return backdropSize;
    }
}

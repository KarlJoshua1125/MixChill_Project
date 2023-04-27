package com.example.loginform;

public class Track {
    private String trackId;
    private String trackName;
    private String youtubeLink;

    public Track() {

    }

    public Track(String trackId, String trackName, String youtubeLink) {
        this.trackId = trackId;
        this.trackName = trackName;
        this.youtubeLink = youtubeLink;
    }

    public String getTrackId() {
        return trackId;
    }

    public String getTrackName() {
        return trackName;
    }
    public String getYoutubeLink() {
        return youtubeLink;
    }
}
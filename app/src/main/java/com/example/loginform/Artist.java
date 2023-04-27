package com.example.loginform;

public class Artist {
    private String artistId;
    private String artistName;
    private String artistLocation;
    private String artistGenre;

    public Artist(){

    }

    public Artist(String artistId, String artistName,String artistLocation, String artistGenre ) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistLocation = artistLocation;
        this.artistGenre =artistGenre ;
    }

    public String getArtistId() {
        return artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getArtistLocation() {
        return artistLocation;
    }

    public String getArtistGenre() {
        return artistGenre;
    }
}

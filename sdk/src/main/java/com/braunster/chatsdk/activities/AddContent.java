package com.braunster.chatsdk.activities;

public class AddContent {
    private String artistId;
    private String artistName;
    private String artistGenre;

    public AddContent(){
        //this constructor is required
    }

    public AddContent(String artistId, String artistName, String artistGenre) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.artistGenre = artistGenre;
    }

    public String email() {
        return artistId;
    }

    public String lat() {
        return artistName;
    }

    public String lan() {
        return artistGenre;
    }
}
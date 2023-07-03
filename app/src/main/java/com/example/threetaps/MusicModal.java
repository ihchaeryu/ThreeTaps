package com.example.threetaps;

public class MusicModal {

    // variables
    private String fileName;
    private String artistName;

    // constructor
    public MusicModal(String fileName, String artistName) {
        this.fileName = fileName;
        this.artistName = artistName;
    }

    // getter
    public String getFileName() {
        return fileName;
    }

    public String getArtistName() {
        return artistName;
    }

    // setter
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}

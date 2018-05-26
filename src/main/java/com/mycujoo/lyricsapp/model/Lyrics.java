package com.mycujoo.lyricsapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Lyrics {

    private String lyrics;

    public Lyrics() {
    }

    public String getLyrics() {
        return lyrics;
    }

    public String setLyrics() {
        this.lyrics = lyrics;
        return lyrics;
    }

    @Override
    public String toString() {
        return "Lyrics{" + lyrics + '}';
    }

}

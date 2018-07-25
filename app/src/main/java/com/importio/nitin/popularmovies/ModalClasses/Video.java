package com.importio.nitin.popularmovies.ModalClasses;

public class Video {
    private String name;
    private String site;
    private String key;

    public Video(String name, String site, String key) {
        this.name = name;
        this.site = site;
        this.key = key;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

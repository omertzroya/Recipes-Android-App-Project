package com.example.recipesapp.models;


public class RecipeFavorite {
    private String name;
    private String link;

    public RecipeFavorite(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}

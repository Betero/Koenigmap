package com.example.koenigmap.Model;

public class CategoryModel {

    private String name;
    private int sets;
    private String url;

    public CategoryModel(){
        //for fb
    }

    public CategoryModel(String name, int sets, String url) {
        this.name = name;
        this.sets = sets;
        this.url = url;
    }

    public String getname() {
        return name;
    }

    public void setname(String imageUrl) {
        this.name = imageUrl;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

package fr.wildcodeschool.chantome.wildoldschool;

/**
 * Created by chantome on 27/10/2016.
 */

public class Categorie {
    private String name;

    public Categorie(){}

    public Categorie(String name){
        setName(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

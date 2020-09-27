package ley.jmclient.model;

import com.google.gson.annotations.Expose;

public class Category {

    @Expose
    public String id;

    @Expose
    public String name;


    @Override
    public String toString() {
        return name;
    }
}

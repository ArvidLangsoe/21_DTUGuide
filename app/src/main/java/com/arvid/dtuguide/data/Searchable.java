package com.arvid.dtuguide.data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.List;

/**
 * Created by peter on 1/8/2018.
 */

public abstract class Searchable implements Serializable, Comparable<Searchable>{
    private String name;
    private List<String> tags;

    public Searchable(){}

    public String getName() {
        return name;
    }

    public Searchable setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getTags() {
        return tags;
    }

    public Searchable setTags(List<String> tags) {
        this.tags = tags;
        return this;
    }

    public boolean equals(Searchable o){
        if(this.getName().equals(o.getName()))
            return true;
        else
            return false;
    }

    public abstract String getDescription();

    public String getType(){
        if(this.getClass().isAssignableFrom(LocationDTO.class))
            return "Location";
        else
            return "Person";
    }

    public int compareTo(@NonNull Searchable o) {
        return this.name.compareToIgnoreCase(o.name);
    }
}
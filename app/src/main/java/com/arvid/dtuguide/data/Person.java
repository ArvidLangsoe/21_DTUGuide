package com.arvid.dtuguide.data;

/**
 * Created by peter on 1/12/2018.
 */

public class Person extends Searchable {
    private String role;
    private LocationDTO room;
    private String email;
    private String description;
    private String pictureURL;

    public String toString(){
        return "Person name:"+getName()+", role:"+role+", email: "+email+", desc:"+description+", room:"+room.getName();
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public Person setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
        return this;
    }

    public String getRole() {
        return role;
    }

    public Person setdescription(String description){
        this.description = description;
        return this;
    }

    public Person setRole(String role) {
        this.role = role;
        return this;
    }

    public LocationDTO getRoom() {
        return room;
    }

    public Person setRoom(LocationDTO room) {
        this.room = room;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Person setEmail(String email) {
        this.email = email;
        return this;
    }

    @Override
    public String getDescription() {
        return description;
    }
}

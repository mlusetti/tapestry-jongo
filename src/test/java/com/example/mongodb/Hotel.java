package com.example.mongodb;

import com.google.code.morphia.annotations.Embedded;
import com.google.code.morphia.annotations.Id;
import org.bson.types.ObjectId;
import org.jongo.marshall.jackson.id.Id;

public class Hotel
{
    @Id
	private String id;

    private String name;
    private int stars;

    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }
}

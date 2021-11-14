package com.pwszit.singiel;

public class ItemModel {
    //private int ;
    private String name, age, city, image, userlikedid;



    public ItemModel(String image, String name, String age, String city, String userlikedid) {
        this.image = image;
        this.name = name;
        this.age = age;
        this.city = city;
        this.userlikedid = userlikedid;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }

    public String getUserlikedid() {
        return userlikedid;
    }
}
package com.example.bar;

public class User {
    public String fullname;
    public String email;
    public String image;
    String gender,phone,birthday;

    public User() {
    }

    public User (String fullname, String email) {
        this.fullname = fullname;
        this.email = email;
    }

    public User(String gender, String phone, String birthday) {
        this.gender = gender;
        this.phone = phone;
        this.birthday = birthday;
    }

    public User(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
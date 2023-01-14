package com.example.bar;

import android.net.Uri;

public class homemodel {

    String name, image, phone, place, style, facility, business, consumption;
    String food, sign, activity, km, people, review, user_id,user_name,user_image,key;
    int id, count, rating;
    boolean fav_btn;
    String date, time;
    String hour,min,year,month,day;
    String coupon_title,coupon_discount,coupon_serial_number,coupon_low_consumption,coupon_effective_date,coupon_from;
    String imagename;
    Uri image_review;
    String image_review0,image_review1,image_review2;
    String video_name,video_url,video_search;
    String menu1,menu2,menu3;

    public homemodel(String menu1, String menu2) {
        this.menu1 = menu1;
        this.menu2 = menu2;
    }

    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
    }

    public String getMenu2() {
        return menu2;
    }

    public void setMenu2(String menu2) {
        this.menu2 = menu2;
    }

    public homemodel(String name, String video_name, String video_url, String video_search) {
        this.name = name;
        this.video_name = video_name;
        this.video_url = video_url;
        this.video_search = video_search;
    }

    public String getVideo_name() {
        return video_name;
    }

    public void setVideo_name(String video_name) {
        this.video_name = video_name;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_search() {
        return video_search;
    }

    public void setVideo_search(String video_search) {
        this.video_search = video_search;
    }

    public homemodel(String imagename, Uri image_review) {
        this.imagename = imagename;
        this.image_review = image_review;
    }

    public homemodel(String image_review0, String image_review1, String image_review2) {
        this.image_review0 = image_review0;
        this.image_review1 = image_review1;
        this.image_review2 = image_review2;
    }

    public String getImage_review0() {
        return image_review0;
    }

    public void setImage_review0(String image_review0) {
        this.image_review0 = image_review0;
    }

    public String getImage_review1() {
        return image_review1;
    }

    public void setImage_review1(String image_review1) {
        this.image_review1 = image_review1;
    }

    public String getImage_review2() {
        return image_review2;
    }

    public void setImage_review2(String image_review2) {
        this.image_review2 = image_review2;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public Uri getImage_review() {
        return image_review;
    }

    public void setImage_review(Uri image_review) {
        this.image_review = image_review;
    }

    public homemodel() {
    }

    public homemodel(String coupon_from, String coupon_title, String coupon_discount, String coupon_serial_number, String coupon_low_consumption, String coupon_effective_date) {
        this.coupon_title = coupon_title;
        this.coupon_discount = coupon_discount;
        this.coupon_serial_number = coupon_serial_number;
        this.coupon_low_consumption = coupon_low_consumption;
        this.coupon_effective_date = coupon_effective_date;
        this.coupon_from = coupon_from;
    }

    public String getCoupon_title() {
        return coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        this.coupon_title = coupon_title;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getCoupon_serial_number() {
        return coupon_serial_number;
    }

    public void setCoupon_serial_number(String coupon_serial_number) {
        this.coupon_serial_number = coupon_serial_number;
    }

    public String getCoupon_low_consumption() {
        return coupon_low_consumption;
    }

    public void setCoupon_low_consumption(String coupon_low_consumption) {
        this.coupon_low_consumption = coupon_low_consumption;
    }

    public String getCoupon_effective_date() {
        return coupon_effective_date;
    }

    public void setCoupon_effective_date(String coupon_effective_date) {
        this.coupon_effective_date = coupon_effective_date;
    }

    public String getCoupon_from() {
        return coupon_from;
    }

    public void setCoupon_from(String coupon_from) {
        this.coupon_from = coupon_from;
    }

    public homemodel(String name, String image, String phone, String place, String style, String facility, String business, String consumption, String food, String sign, String activity, String km, String people, String review, String user_id, String user_name, String user_image, String key, int id, int count, int rating, boolean fav_btn, String date, String time, String hour, String min, String year, String month, String day) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.place = place;
        this.style = style;
        this.facility = facility;
        this.business = business;
        this.consumption = consumption;
        this.food = food;
        this.sign = sign;
        this.activity = activity;
        this.km = km;
        this.people = people;
        this.review = review;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_image = user_image;
        this.key = key;
        this.id = id;
        this.count = count;
        this.rating = rating;
        this.fav_btn = fav_btn;
        this.date = date;
        this.time = time;
        this.hour = hour;
        this.min = min;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public boolean isFav_btn() {
        return fav_btn;
    }

    public void setFav_btn(boolean fav_btn) {
        this.fav_btn = fav_btn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getConsumption() {
        return consumption;
    }

    public void setConsumption(String consumption) {
        this.consumption = consumption;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }
}

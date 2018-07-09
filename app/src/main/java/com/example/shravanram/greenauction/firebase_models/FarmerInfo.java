package com.example.shravanram.greenauction.firebase_models;

/**
 * Created by Shravan ram on 3/25/2018.
 */

public class FarmerInfo {

    public String fname="none";
    public String price="0",rating="0",phone;
    public FarmerInfo(){

    }
    public FarmerInfo(String fname,String price,String rating,String phone){
        this.fname=fname;
        this.price=price;
        this.rating=rating;
        this.phone=phone;

    }
    public void setPhone(String phone){
        this.phone=phone;
    }

    public String getPhone(){
        return phone;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getFname() {
        return fname;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {

        return price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
package com.example.shravanram.greenauction.firebase_models;
/**
 * Created by Shravan ram on 3/21/2018.
 */

public class AuctionCardView1 {
    public String prod, loc, deadline, weight;
    public int qty;
    public float initialbid;
    public String category;
   public String image;
   public float finalbid;
   public String winner;
    public AuctionCardView1(){

    }
    public AuctionCardView1(String prod,String loc,String deadline,String weight,int qty,
                            float initialbid,String category,String image,float finalbid,String winner){
        this.prod=prod;
        this.loc=loc;
        this.deadline=deadline;
        this.weight=weight;
        this.qty=qty;
        this.initialbid=initialbid;
        this.category=category;
        this.finalbid=finalbid;
        this.winner=winner;
       this.image=image;
    }
    public void setFinalbid(float finalbid){
        this.finalbid=finalbid;
    }
    public float getFinalbid(){
        return finalbid;
    }

    public void setWinner(String winner)
    {
        this.winner=winner;
    }

    public String getWinner(){
        return winner;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getLoc() {
        return loc;
    }

    public void setProd(String prod) {
        this.prod = prod;
    }

    public String getProd() {
        return prod;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public float getInitialbid() {
        return initialbid;
    }

    public void setInitialbid(float initialbid) {
        this.initialbid = initialbid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}

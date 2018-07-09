package com.example.shravanram.greenauction.firebase_models;
/**
 * Created by Shravan ram on 3/21/2018.
 */

public class MarketCardView {
    public int initialbid;
    public MarketCardView(){

    }
    public MarketCardView(int initialbid){
        this.initialbid=initialbid;
    }
    public int getInitialbid() {
        return initialbid;
    }

    public void setInitialbid(int initialbid) {
        this.initialbid = initialbid;
    }
}
package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shravanram.greenauction.FarmerSide.AllOngoingFarmerSide;
import com.example.shravanram.greenauction.FarmerSide.FarmerOngoing;
import com.example.shravanram.greenauction.FarmerSide.FarmerSideViewBidsInAuction;
import com.example.shravanram.greenauction.firebase_models.PersonInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class ReEnterBidForm extends AppCompatActivity {
    private FirebaseAuth fire;

    private DatabaseReference mDatabase;
  //  private EditText loc;
    private EditText price;
    private Button bidEnterbut;
    String nameOfFarmer,ratingOfFarmer,phoneOfFarmer;
    String e1[];
    String e2;
    String auctionId;
    String inibid;
    int initialBid;

    private ArrayList<String> AllBidIds=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_bid_form);
        fire=FirebaseAuth.getInstance();


        price = (EditText) findViewById(R.id.price);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        e1=fire.getCurrentUser().getEmail().toString().split("\\.");
        e2=e1[0];
        Intent i = getIntent();
        auctionId= i.getStringExtra("auctionSelected");
        bidEnterbut = (Button) findViewById(R.id.bidEnter);
        bidEnterbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String init_pri = price.getText().toString().trim();
                if(parseInt(init_pri)<initialBid){
                    Toast.makeText(getApplicationContext(),
                            "Your bid should be greater than "+initialBid,Toast.LENGTH_LONG).show();
                    return;
                }
                mDatabase.child("Bids").child(auctionId).child(e2).child("fname").setValue(nameOfFarmer);
                mDatabase.child("Bids").child(auctionId).child(e2).child("price").setValue(init_pri);
                mDatabase.child("Bids").child(auctionId).child(e2).child("rating").setValue(ratingOfFarmer);
                mDatabase.child("Bids").child(auctionId).child(e2).child("phone").setValue(phoneOfFarmer);
                //to avoid repertition of BidIds everytime we type or press enter bid price
                if(!AllBidIds.contains(auctionId)) {
                    mDatabase.child("Farmer").child(e2).child("BidID").push().setValue(auctionId);
                }


                startActivity(new Intent(getApplicationContext(),FarmerOngoing.class));
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is to give auction id
                DataSnapshot allBids=dataSnapshot.child("Farmer").child(e2).child("BidID");
                //gets all the farmer names in the bid
                Iterable<DataSnapshot> bids=allBids.getChildren();
                for(DataSnapshot var1:bids) {

                    AllBidIds.add(var1.getValue().toString());
                }
                nameOfFarmer=dataSnapshot.child("Farmer").child(e2).child("name").getValue().toString();
                ratingOfFarmer=dataSnapshot.child("Farmer").child(e2).child("rating").getValue().toString();
                phoneOfFarmer=dataSnapshot.child("Farmer").child(e2).child("phone").getValue().toString();

                inibid=dataSnapshot.child("auction").child(auctionId).child("initialbid").getValue().toString();
                initialBid=parseInt(inibid);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),FarmerOngoing.class);
        startActivity(newIntent);
    }

}

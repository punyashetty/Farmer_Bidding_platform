package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.shravanram.greenauction.firebase_models.FarmerInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ConsumerSideViewBidsInAuctionCompleted extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent;
    FirebaseDatabase database;
    DatabaseReference myRef;
    List<FarmerInfo> list;
    RecyclerView recycle;
    Button view;
    RecyclerAdapterConsumer recyclerAdapter;
  //  TextView emptyView;
    String AuctionSelected;
    private DatabaseReference mRef;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_side_view_bids_in_auction);
        drawerLayout = (DrawerLayout) findViewById(R.id.d1);
        relativeLayout=(RelativeLayout)findViewById(R.id.nothingToDisplay);
        //this nav_view in activity_dummy.xml

        //go to res >values>strings.xml and do the changes
        actionBar = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
        actionBar.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(actionBar);
        actionBar.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    intent = new Intent(getApplicationContext(), ConsumerHomeActivity.class);
                    startActivity(intent);

                }
                if (id == R.id.nav_profile) {
                    intent=new Intent(getApplicationContext(),ConsumerViewProfile.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_market_analysis) {
                    intent = new Intent(getApplicationContext(), SearchActivityForMarketAnalysis.class);
                    startActivity(intent);

                }

                if(id==R.id.nav_rate_farmer){
                    intent = new Intent(getApplicationContext(),FarmersToRate.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_logout) {
                    // firebaseAuth=FirebaseAuth.getInstance();
                    fire.signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }


                return true;
            }
        });
        recycle = (RecyclerView) findViewById(R.id.farmersideviewbids);
    //    emptyView=(TextView)findViewById(R.id.emptyView);
        Bundle obj=getIntent().getExtras();
        AuctionSelected=obj.getString("auctionClicked");
        mRef= FirebaseDatabase.getInstance().getReference().child("auction");

        database = FirebaseDatabase.getInstance();

        recycle.setHasFixedSize(true);
        recycle.setLayoutManager(new LinearLayoutManager(this));

        //AuctionSelected is what was clicked on prev page
        myRef = database.getReference().child("Bids").child(AuctionSelected);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list = new ArrayList<FarmerInfo>();
                Iterable<DataSnapshot> AllAuctions = dataSnapshot.getChildren();
                for (DataSnapshot dataSnapshot1 : AllAuctions) {

                    FarmerInfo value = dataSnapshot1.getValue(FarmerInfo.class);
                    Log.d("Key", dataSnapshot1.getValue().toString());

                    FarmerInfo fire = new FarmerInfo();
                    String fname = value.getFname();
                    // Log.d("name", fname);
                    String price = value.getPrice();
                    //                    Log.d("price", price);
                    String rating = value.getRating();
                    String phoneNumber=value.getPhone();
                    fire.setFname(fname);
                    fire.setPrice(price);
                    fire.setRating(rating);
                    fire.setPhone(phoneNumber);
                    list.add(fire);
                    if(list.isEmpty()){
                        recycle.setVisibility(View.GONE);
                        relativeLayout.setVisibility(View.VISIBLE);
                    }
                    else {
                        recycle.setVisibility(View.VISIBLE);
                        relativeLayout.setVisibility(View.GONE);
                    }
                    recyclerAdapter = new RecyclerAdapterConsumer(list, ConsumerSideViewBidsInAuctionCompleted.this,AuctionSelected);
                    recycle.setAdapter(recyclerAdapter);
                    recyclerAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return actionBar.onOptionsItemSelected(item);
    }
   /* @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),Com.class);
        startActivity(newIntent);
    }*/
}
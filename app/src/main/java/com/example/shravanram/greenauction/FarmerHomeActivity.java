package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.shravanram.greenauction.FarmerSide.AllOngoingFarmerSide;
import com.example.shravanram.greenauction.FarmerSide.FarmerOngoing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FarmerHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent;
    private DatabaseReference mRef= FirebaseDatabase.getInstance().getReference();
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;

    private Button viewAllAuctions,viewMyOngoingBids,viewMyCompletedBids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_farmer_profile);
        drawerLayout = (DrawerLayout) findViewById(R.id.d1);
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
                    intent = new Intent(getApplicationContext(), FarmerHomeActivity.class);
                    startActivity(intent);

                }
                if (id == R.id.nav_profile) {
                    intent=new Intent(getApplicationContext(),FarmerViewProfile.class);
                    startActivity(intent);
                }
                if (id == R.id.nav_market_analysis) {
                    intent = new Intent(getApplicationContext(), SearchActivityForMarketAnalysis.class);
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
       


               // buttonLogout.setOnClickListener(this);
                firebaseAuth = FirebaseAuth.getInstance();
                viewAllAuctions = (Button) findViewById(R.id.viewAllPortals);
                viewMyOngoingBids = (Button) findViewById(R.id.viewMyOngoingPortal);
                viewMyCompletedBids = (Button) findViewById(R.id.viewMyCompletedPortal);
                viewAllAuctions.setOnClickListener(this);
                viewMyOngoingBids.setOnClickListener(this);
                viewMyCompletedBids.setOnClickListener(this);
            }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return actionBar.onOptionsItemSelected(item);
    }
    @Override
    public void onClick(View view) {
        if(view==viewAllAuctions)
        {
            // finish();
            startActivity(new Intent(this,AllOngoingFarmerSide.class));

        }
        if(view==viewMyOngoingBids)
        {
            // finish();
             startActivity(new Intent(this,FarmerOngoing.class));
        }
       if(view==viewMyCompletedBids) {
         startActivity(new Intent(this,AuctionsWon.class));
       }
    }
    @Override
    public void onBackPressed(){
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent1);
    }
}













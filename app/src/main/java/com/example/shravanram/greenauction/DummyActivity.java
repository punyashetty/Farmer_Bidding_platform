package com.example.shravanram.greenauction;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;



public class DummyActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy);
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
                    intent1 = new Intent(getApplicationContext(),FarmerHomeActivity.class);
                    startActivity(intent1);

                }
                if (id == R.id.nav_profile) {
                    intent1 = new Intent(getApplicationContext(),FarmerViewProfile.class);
                    startActivity(intent1);
                }
                if (id == R.id.nav_market_analysis) {
                    intent1 = new Intent(getApplicationContext(), SearchActivityForMarketAnalysis.class);
                    startActivity(intent1);

                }

                if(id==R.id.nav_rate_farmer){
                    intent1 = new Intent(getApplicationContext(), FarmersToRate.class);
                    startActivity(intent1);
                }
                if (id == R.id.nav_logout) {
                    // firebaseAuth=FirebaseAuth.getInstance();
                    fire.signOut();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }


                return true;
            }
        });
    }
        @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return actionBar.onOptionsItemSelected(item);
    }
}

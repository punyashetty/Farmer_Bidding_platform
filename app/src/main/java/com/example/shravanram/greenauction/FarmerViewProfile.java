package com.example.shravanram.greenauction;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class FarmerViewProfile extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent;


    TextView name,location,phone,email;
    RatingBar ratingBar;
    String e1[];
    String nameOfPerson,loca,phoneOfPerson,rating;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_view_profile);
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
                    intent = new Intent(getApplicationContext(), FarmerViewProfile.class);
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

        name=(TextView)findViewById(R.id.Text);
        location=(TextView)findViewById(R.id.Text2);
        phone=(TextView)findViewById(R.id.Text3);
        email=(TextView)findViewById(R.id.Text4);
        ratingBar=(RatingBar)findViewById(R.id.rating_bar);

        mDatabase= FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                e1=fire.getCurrentUser().getEmail().toString().split("\\.");
                String e2=e1[0];

                nameOfPerson= dataSnapshot.child("Farmer").child(e2).child("name").getValue().toString();
                loca=dataSnapshot.child("Farmer").child(e2).child("city").getValue().toString();
                phoneOfPerson=dataSnapshot.child("Farmer").child(e2).child("phone").getValue().toString();
                rating=dataSnapshot.child("Farmer").child(e2).child("rating").getValue().toString();

                name.setText(nameOfPerson);
                location.setText(loca);
                phone.setText(phoneOfPerson);
                email.setText(fire.getCurrentUser().getEmail().toString());
                ratingBar.setRating(Float.parseFloat(rating));
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
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),FarmerHomeActivity.class);
        startActivity(newIntent);
    }
}

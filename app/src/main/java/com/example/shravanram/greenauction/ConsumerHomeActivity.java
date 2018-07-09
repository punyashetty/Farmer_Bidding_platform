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

import com.google.firebase.auth.FirebaseAuth;

public class ConsumerHomeActivity extends AppCompatActivity implements View.OnClickListener {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent;
    private FirebaseAuth firebaseAuth;
    private TextView textViewUserEmail;
    private Button createPortal;
    private Button viewOngoingPortal;
    private Button viewCompletedPortal;

    //private Button buttonLogout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consumer_profile);
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
                    intent = new Intent(getApplicationContext(), FarmersToRate.class);
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

       // buttonLogout = (Button) findViewById(R.id.ButtonLogout);
       // textViewUserEmail=(TextView) findViewById(R.id.userText);
        //buttonLogout.setOnClickListener(this);
       firebaseAuth=FirebaseAuth.getInstance();
        createPortal=(Button) findViewById(R.id.createPortal);
        viewOngoingPortal=(Button) findViewById(R.id.viewOngoingPortal);
        viewCompletedPortal=(Button)findViewById(R.id.viewCompletedPortal);

        createPortal.setOnClickListener(this);
        viewOngoingPortal.setOnClickListener(this);
       viewCompletedPortal.setOnClickListener(this);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        return actionBar.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view==createPortal)
        {
           // finish();
            startActivity(new Intent(this,PortalCreation.class));

        }
        if(view==viewOngoingPortal)
        {
           // finish();
           startActivity(new Intent(this,OngoingConsumerSide.class));
        }
       if(view==viewCompletedPortal){
            startActivity(new Intent(this,CompletedConsumerSide.class));
       }
    }
    @Override
    public void onBackPressed(){
        Intent intent1 = new Intent(Intent.ACTION_MAIN);
        intent1.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent1);
    }
}

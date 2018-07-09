package com.example.shravanram.greenauction;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shravanram.greenauction.FarmerSide.FarmerSideViewBidsInAuction;
import com.example.shravanram.greenauction.R;
import com.example.shravanram.greenauction.firebase_models.AuctionCardView2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Boolean.FALSE;

public class AuctionsWon extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    //private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent1;

    ProgressDialog progressDialog;
    private DatabaseReference tRef;
    private DatabaseReference mRef;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    public static int auctionSel;

    private RelativeLayout relativeLayout;
    String name="none";
    String emailno[];
    String winner="none";
    Calendar c = Calendar.getInstance();
    Date d1,d2;
    String t;

    private ArrayList<String> auctionsSelect=new ArrayList<String>();
    private ArrayList<String> auctionsOngoing=new ArrayList<String>();
    private RecyclerView ourlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_farmer);
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
        mRef= FirebaseDatabase.getInstance().getReference().child("auction");
        mRef.keepSynced(true);

        progressDialog = new ProgressDialog(this);

        ourlist=(RecyclerView)findViewById(R.id.auctions);
        ourlist.setHasFixedSize(true);
        //change this line if you make changes in chikoo.xml
        ourlist.setLayoutManager(new LinearLayoutManager(this));
        tRef = FirebaseDatabase.getInstance().getReference();
        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gives emailno as an array  as beta1@gmail and com as second element
                emailno=fire.getCurrentUser().getEmail().toString().split("\\.");
                String e2=emailno[0];//contains beta1@gmail
                DataSnapshot AuctionID=dataSnapshot.child("auction");
                Iterable<DataSnapshot> AllAuctions=AuctionID.getChildren();
                 name=dataSnapshot.child("Farmer").child(e2).child("name").getValue().toString();
                for(DataSnapshot var1:AllAuctions)
                {
                    String id=var1.getKey().toString();
                    Log.d("heyyy my id",""+id);
                    t = dataSnapshot.child("auction").child(id).child("deadline").getValue().toString();
                    winner = dataSnapshot.child("auction").child(id).child("winner").getValue().toString();
                    Log.d("deadline",""+t);
                    //selects all auctions of the current user ,can be ongoing or past
                    auctionsSelect.add(id);

                    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
                    String getCurrentDateTime = sdf.format(c.getTime());
                    try {
                        d1 = sdf.parse(getCurrentDateTime);
                        d2 = sdf.parse(t);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                    if (d1.after(d2)&& winner.equals(name) )

                    {

                        auctionsOngoing.add(id);
                        //ongoing auctions

                    }


                }
                if(auctionsOngoing.size()==0){
                    relativeLayout.setVisibility(View.VISIBLE);
                    ourlist.setVisibility(View.GONE);
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
    @Override
    protected void onStart(){
        progressDialog.setMessage("Loading your completed auctions which you won");
        progressDialog.show();
        if(auctionsOngoing.size()==0){
            progressDialog.dismiss();
        }
        super.onStart();
        FirebaseRecyclerAdapter<AuctionCardView2,BlogviewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter
                <AuctionCardView2, BlogviewHolder>(AuctionCardView2.class,R.layout.blog_row_card1,BlogviewHolder.class,mRef){
            @Override

            protected void populateViewHolder(BlogviewHolder viewHolder,AuctionCardView2 model,int position){
                progressDialog.dismiss();
                if(auctionsOngoing.contains(""+(position+1)))
                {
                //    emptyFlag=0;
                    //Log.d("pos",""+position);

                    auctionSel=position+1;
                    viewHolder.setProd(model.getProd());
                    viewHolder.setImage(getApplicationContext(),model.getImage());
                    viewHolder.setQty(model.getQty());
                    viewHolder.setDeadline(model.getDeadline());
                    viewHolder.setWeight(model.getWeight());
                    viewHolder.setLoc(model.getLoc());
                    viewHolder.setInitialBid(model.getInitialbid());
                    viewHolder.setCategory(model.getCategory());
                    viewHolder.setPosition();
                    viewHolder.setFinalBid(model.getFinalbid());

                }
                else
                {
                    viewHolder.setVisibility(FALSE);

                }

            }
            @Override
            public BlogviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BlogviewHolder viewHolder = super.onCreateViewHolder(parent, viewType);

                return viewHolder;
            }
        };
        ourlist.setAdapter(firebaseRecyclerAdapter);
    }
    public static class BlogviewHolder extends RecyclerView.ViewHolder {
        View mView;
        public  BlogviewHolder(View itemView){
            super(itemView);
            mView=itemView;

        }




        public void setProd(String prod){
            TextView produce=(TextView)mView.findViewById(R.id.t1);
            produce.setText(prod);
        }

        public void setQty(int qty){
            TextView quantity=(TextView)mView.findViewById(R.id.t2);
            quantity.setText(""+qty);
        }
        public void setDeadline(String ded){
            TextView dead=(TextView)mView.findViewById(R.id.t3);
            dead.setText("Date of auction:"+ded);
        }

        public void setWeight(String wt){
            TextView location=(TextView)mView.findViewById(R.id.t4);
            location.setText(wt);
        }
        public void setLoc(String loc){
            TextView location=(TextView)mView.findViewById(R.id.t5);
            location.setText("Location:"+loc);
        }

        public void setInitialBid(float initialBid){
            TextView bid=(TextView)mView.findViewById(R.id.t6);
            bid.setText("Initial Bid: "+initialBid);
        }
        public void setCategory(String cat){
            TextView cate=(TextView)mView.findViewById(R.id.t7);
            cate.setText("Category:"+cat);
        }
        public void setImage(Context ctx,String img) {
            ImageView imgVw = (ImageView) mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(img).into(imgVw);
        }
        public void setPosition()
        {
            TextView c=(TextView)mView.findViewById(R.id.t8);
            c.setText("Auction id: "+auctionSel);
        }
        public void setFinalBid(float finalBid){
            TextView bid1=(TextView)mView.findViewById(R.id.t9);
            bid1.setText("Final bid : "+finalBid);
        }
        public void setVisibility(boolean isVisible){
            RecyclerView.LayoutParams param = (RecyclerView.LayoutParams)itemView.getLayoutParams();

            itemView.setVisibility(View.GONE);
            param.height = 0;
            param.width = 0;

            itemView.setLayoutParams(param);
        }


    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),FarmerHomeActivity.class);
        startActivity(newIntent);
    }
}
package com.example.shravanram.greenauction.FarmerSide;

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

import com.example.shravanram.greenauction.FarmerHomeActivity;
import com.example.shravanram.greenauction.FarmerViewProfile;
import com.example.shravanram.greenauction.FarmersToRate;
import com.example.shravanram.greenauction.LoginActivity;
import com.example.shravanram.greenauction.R;
import com.example.shravanram.greenauction.SearchActivityForMarketAnalysis;
import com.example.shravanram.greenauction.firebase_models.FarmerAuctionCardView;
import com.example.shravanram.greenauction.firebase_models.FarmerInfo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.shravanram.greenauction.firebase_models.AuctionCardView1;
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

public class FarmerOngoing extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    Intent intent1;
    private RelativeLayout relativeLayout;
    ProgressDialog progressDialog;
    private DatabaseReference tRef;
    private DatabaseReference mRef;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    public static  int auctionSel=0;

    String emailno[];
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
        ourlist.setLayoutManager(new LinearLayoutManager(this));
        tRef = FirebaseDatabase.getInstance().getReference();
        tRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //gives emailno as an array  as beta1@gmail and com as second element
                emailno=fire.getCurrentUser().getEmail().toString().split("\\.");
                String e2=emailno[0];//contains beta1@gmail
                DataSnapshot BidID=dataSnapshot.child("Farmer").child(e2).child("BidID");
                Iterable<DataSnapshot> AllBids=BidID.getChildren();
                for(DataSnapshot var1:AllBids)
                {
                    String id=var1.getValue().toString();
                    Log.d("heyyy my id",""+id);
                    t = dataSnapshot.child("auction").child(id).child("deadline").getValue().toString();
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


                    if (d2.after(d1))

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
    protected void onStart(){
        progressDialog.setMessage("Loading your auctions");
        progressDialog.show();

        if(auctionsOngoing.size()==0){
            progressDialog.dismiss();
        }
        super.onStart();
        FirebaseRecyclerAdapter<FarmerAuctionCardView,BlogviewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<FarmerAuctionCardView,
                BlogviewHolder>(FarmerAuctionCardView.class, R.layout.blog_row_card,BlogviewHolder.class,mRef){
            @Override
            protected void populateViewHolder(BlogviewHolder viewHolder,FarmerAuctionCardView model,int position){
                progressDialog.dismiss();
                if(auctionsOngoing.contains(""+(position+1)))
                {

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

                }
                else
                {
                    viewHolder.setVisibility(FALSE);
                }

            }
            @Override
            public BlogviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                BlogviewHolder viewHolder = super.onCreateViewHolder(parent, viewType);
                viewHolder.setOnClickListener(new BlogviewHolder.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        TextView c=(TextView)view.findViewById(R.id.t8);
                        Toast.makeText(getApplicationContext(), "Item clicked at "+ c.getText() , Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), MyFarmerSideViewBidsInAuction.class);
                        i.putExtra("auctionClicked",c.getText());
                        startActivity(i);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toast.makeText(getApplicationContext(), "Item long clicked at " + position, Toast.LENGTH_SHORT).show();
                    }
                });
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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());

                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mClickListener.onItemLongClick(v, getAdapterPosition());
                    return true;
                }
            });
        }
        private BlogviewHolder.ClickListener mClickListener;

        //Interface to send callbacks...
        public interface ClickListener{
            public void onItemClick(View view, int position);
            public void onItemLongClick(View view, int position);
        }

        public void setOnClickListener(BlogviewHolder.ClickListener clickListener){
            mClickListener = clickListener;
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
            Calendar c1 = Calendar.getInstance();
            long elapsedDays=0,elapsedHours=0,elapsedMinutes=0;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
            String getCurrentDateTime = simpleDateFormat.format(c1.getTime());
            try {
                Date date1 = simpleDateFormat.parse(ded);
                Date date2 = simpleDateFormat.parse(getCurrentDateTime);
                //ongoing means its even after today's date
                long different = date1.getTime() - date2.getTime();


                //long secondsInMilli = 1000;
                long minutesInMilli = 1000 * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                elapsedDays = different / daysInMilli;
                different = different % daysInMilli;

                elapsedHours = different / hoursInMilli;
                different = different % hoursInMilli;

                elapsedMinutes = different / minutesInMilli;
                different = different % minutesInMilli;

                //long elapsedSeconds = different / secondsInMilli;


            } catch (ParseException e) {
                e.printStackTrace();
            }

            TextView dead=(TextView)mView.findViewById(R.id.t3);
            dead.setText("Time left:"+elapsedDays+"days "+elapsedHours+"hrs "+elapsedMinutes+"mins");
        }

        public void setWeight(String wt){
            TextView location=(TextView)mView.findViewById(R.id.t4);
            location.setText(wt);
        }
        public void setLoc(String loc){
            TextView location=(TextView)mView.findViewById(R.id.t5);
            location.setText(loc);
        }

        public void setInitialBid(float initialBid){
            TextView bid=(TextView)mView.findViewById(R.id.t6);
            bid.setText(""+initialBid);
        }
        public void setCategory(String cat){
            TextView cate=(TextView)mView.findViewById(R.id.t7);
            cate.setText(cat);
        }

        public void setPosition()
        {
            TextView c=(TextView)mView.findViewById(R.id.t8);
            c.setText(""+auctionSel);
        }
        public void setImage(Context ctx, String img) {
            ImageView imgVw = (ImageView) mView.findViewById(R.id.post_img);
            Picasso.with(ctx).load(img).into(imgVw);
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

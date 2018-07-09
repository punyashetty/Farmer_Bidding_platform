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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.example.shravanram.greenauction.firebase_models.AuctionCardView1;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.shravanram.greenauction.CompletedConsumerSide.auctionSel;
import static java.lang.Boolean.FALSE;

public class FarmersToRate extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBar;
    // private FirebaseAuth fire=FirebaseAuth.getInstance();
    Intent intent;
    private RelativeLayout relativeLayout;
    ProgressDialog progressDialog;
    private DatabaseReference tRef;
    private DatabaseReference mRef;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    String rated="";
    //String farmerTorate;

    String emailno[];

    String t;

    private ArrayList<String> auctionsSelect=new ArrayList<String>();

    private RecyclerView ourlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmers_to_rate);
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
                emailno = fire.getCurrentUser().getEmail().toString().split("\\.");
                String e2 = emailno[0];//contains beta1@gmail
                DataSnapshot AuctionID = dataSnapshot.child("Consumer").child(e2).child("AuctionID");
                Iterable<DataSnapshot> AllAuctions = AuctionID.getChildren();
                for (DataSnapshot var1 : AllAuctions) {
                    String id = var1.getValue().toString();
                    String chosen=dataSnapshot.child("auction").child(id).child("chosen").getValue().toString();
            //        rated=dataSnapshot.child("auction").child(id).child("rated").getValue().toString();

                    if(chosen.contains("yes")) {
                        auctionsSelect.add(id);
                    }

                }
                if(auctionsSelect.size()==0){
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
        progressDialog.setMessage("Loading the farmers to rate");
        progressDialog.show();
        if(auctionsSelect.size()==0){
            progressDialog.dismiss();
        }
        super.onStart();

        FirebaseRecyclerAdapter<AuctionCardView1,BlogviewHolder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<AuctionCardView1,
                BlogviewHolder>(AuctionCardView1.class,R.layout.blog_row_rate,BlogviewHolder.class,mRef){
            @Override

            protected void populateViewHolder(BlogviewHolder viewHolder,AuctionCardView1 model,int position){
                progressDialog.dismiss();
                if(auctionsSelect.contains(""+(position+1)))
                {

                    //Log.d("pos",""+position);
                    auctionSel=position+1;
                    viewHolder.setProd(model.getProd());
                    viewHolder.setImage(getApplicationContext(),model.getImage());
                    viewHolder.setQty(model.getQty());
                    viewHolder.setWeight(model.getWeight());
                    viewHolder.setInitialBid(model.getInitialbid());
                    viewHolder.setFinalBid(model.getFinalbid());
                    viewHolder.setWinner(model.getWinner());
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
                        TextView t5=(TextView)view.findViewById(R.id.t5);
                        Toast.makeText(getApplicationContext(), "Rating farmer at" + t5.getText(), Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), RatingFarmer.class);
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

        public void setWeight(String wt){
            TextView location=(TextView)mView.findViewById(R.id.t4);
            location.setText(wt);
        }
        public void setWinner(String winner1){
            TextView winnerText =(TextView)mView.findViewById(R.id.t5);

            winnerText.setText("Winner is: "+winner1);
        }
        public void setInitialBid(float initialBid){
            TextView bid=(TextView)mView.findViewById(R.id.t6);
            bid.setText("Initial bid is:"+initialBid);
        }
        public void setFinalBid(float finalBid){
            TextView finbid=(TextView)mView.findViewById(R.id.t7);
            finbid.setText("Final bid is "+finalBid);
        }
        public void setPosition()
        {
            TextView c=(TextView)mView.findViewById(R.id.t8);
            c.setText(""+auctionSel);
            // c.setVisibility(mView.GONE);
        }
        public void setImage(Context ctx,String img) {
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
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),ConsumerHomeActivity.class);
        startActivity(newIntent);
    }
}


package com.example.shravanram.greenauction;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.DateFormat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Boolean.FALSE;

public class MarketAnalysis extends AppCompatActivity {

    private DatabaseReference tRef;
    private DatabaseReference mRef;

    String produce, chosen, deadline;
    int finalbid;
    int max = -1;
    int min,avg,sum;
    String str1, str2, str3;
    int flag = 0;

    private TextView t1,t2,t3,t4,t5,t6;

    private ArrayList<Integer> marketPrices = new ArrayList<Integer>();
    private ArrayList<String> auctionsSelect = new ArrayList<String>();

    //Calendar c = Calendar.getInstance();
    Date d1, d2;
    String str;
    ProgressDialog progressDialog;

    Dialog myDialog;
    TextView txtclose;
    Button showAnalysis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pop_up_market_analysis);
        showAnalysis = (Button) findViewById(R.id.showAnalysis);
        showAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        myDialog = new Dialog(this);
        //  myDialog.setContentView(R.layout.custom_popup_market);

}

    public void showPopup(View v) {
        myDialog.setContentView(R.layout.custom_popup_market);
        txtclose = (TextView) myDialog.findViewById(R.id.textClose);

        t1 = (TextView) myDialog.findViewById(R.id.minprice);
        t2 = (TextView) myDialog.findViewById(R.id.avgprice);
        t3 = (TextView) myDialog.findViewById(R.id.maxprice);
        t4 = (TextView) myDialog.findViewById(R.id.minpricetxt);
        t5 = (TextView) myDialog.findViewById(R.id.avgpricetxt);
        t6 = (TextView) myDialog.findViewById(R.id.maxpricetxt);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading the results");
        progressDialog.show();


        mRef =FirebaseDatabase.getInstance().getReference().child("auction");
        mRef.keepSynced(true);
        tRef =FirebaseDatabase.getInstance().getReference();
        tRef.addValueEventListener(new ValueEventListener() {
            @Override public void onDataChange (DataSnapshot dataSnapshot) {
                Bundle obj = getIntent().getExtras();
                str = obj.getString("produceSelected");

                DataSnapshot AuctionID = dataSnapshot.child("auction");
                Iterable<DataSnapshot> AllAuctions = AuctionID.getChildren();
                for (DataSnapshot var1 : AllAuctions) {
                    String id = var1.getKey().toString();
                    Calendar c = Calendar.getInstance();

                    // Log.d("heyyy my id",""+id);
                    //gives produce
                    produce = dataSnapshot.child("auction").child(id).child("prod").getValue().toString();
                    chosen = dataSnapshot.child("auction").child(id).child("chosen").getValue().toString();
                    if (produce.contains(str) && chosen.contains("yes")) {
                        flag = 1;
                        finalbid = Integer.parseInt(dataSnapshot.child("auction").
                                child(id).child("finalbid").getValue().toString());
                        deadline = dataSnapshot.child("auction").
                                child(id).child("deadline").getValue().toString();
                        Log.d("deadline",""+deadline);
                        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
                        String getCurrentDateTime = sdf.format(c.getTime());
                        try {
                            d1 = sdf.parse(getCurrentDateTime);
                            d2 = sdf.parse(deadline);
                            DateFormat.format("MM/dd/yy", d1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                        int myDays = 7;
                        c.add(Calendar.DATE, (myDays * -1));  // number of days to subtract
                        int newDate = (c.get(Calendar.YEAR) * 10000) +
                                ((c.get(Calendar.MONTH) + 1) * 100) +
                                (c.get(Calendar.DAY_OF_MONTH)
                                );
                        //newDate gets answer in :yyyymmdd
                        String inputdate = "" + newDate;
                        Log.d("newdate",""+inputdate);
                        //toconvert to MM/DD/YY:
                        Date date = null;

                        try {
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
                            //date is the today-seven days
                            date = df.parse(inputdate);
                            DateFormat.format("MM/dd/yy", date);
                            Log.d("date",""+date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

//deadline should be after today's date-7 days and before today's date d1
                        if (d2.after(date)) {
                            if (d1.after(d2)) {
                                //basically add that week's  auctions

                                marketPrices.add(finalbid);
                            }

                        }

                    }
                }
                if (flag == 1) {
                    int i;
                    max = -1;
                    for (i = 0; i < marketPrices.size(); i++) {

                        Log.d("tag",""+marketPrices.get(i));
                        if (marketPrices.get(i) > max) {
                            max = marketPrices.get(i);
                        }
                    }
                    min = 99999999;
                    for (i = 0; i < marketPrices.size(); i++) {
                        if (marketPrices.get(i) < min) {
                            min = marketPrices.get(i);
                        }
                    }
                    avg = 0;
                    sum = 0;
                    for (i = 0; i < marketPrices.size(); i++) {
                        sum = sum + marketPrices.get(i);
                    }
                    if (marketPrices.size() != 0)
                        avg = Math.round(sum / marketPrices.size());

                    if (min == 99999999 && max == -1) {
                        progressDialog.dismiss();

                        t1.setText("---");
                        // t4.setVisibility(View.GONE);
                        t2.setText("---");
                        //t5.setVisibility(View.GONE);
                        t3.setText("---");
                        //t6.setVisibility(View.GONE);
                    } else {
                        progressDialog.dismiss();

                        t1.setText("" + min);
                        t2.setText("" + avg);
                        t3.setText("" + max);
                    }
    }else {
        progressDialog.dismiss();

        t1.setText("---");

        t2.setText("---");

        t3.setText("---");


    }

            }
            @Override
            public void onCancelled (DatabaseError databaseError){
                                               }
        });



        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();
    }
}
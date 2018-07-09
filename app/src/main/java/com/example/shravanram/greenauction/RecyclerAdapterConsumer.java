package com.example.shravanram.greenauction;

import android.*;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shravanram.greenauction.CompletedConsumerSide;
import com.example.shravanram.greenauction.FarmerSide.RecyclerAdapter;
import com.example.shravanram.greenauction.R;
import com.example.shravanram.greenauction.firebase_models.FarmerInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by Shravan ram on 3/27/2018.
 */

public class RecyclerAdapterConsumer extends RecyclerView.Adapter<RecyclerAdapterConsumer.MyHoder>{
    List<FarmerInfo>  list;
    Context context;
    String auctionID="";
    FirebaseDatabase database;
    DatabaseReference myRef;
    Intent intent;
    String contact;
    public RecyclerAdapterConsumer(){

    }
    public RecyclerAdapterConsumer(List<FarmerInfo> list,Context context,String s)
    {
        this.list=list;
        this.context=context;
        this.auctionID=s;
        myRef = FirebaseDatabase.getInstance().getReference().child("auction").child(this.auctionID);
    }
    @Override
    public MyHoder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.blog_row_bid_completed,parent,false);
        MyHoder myHoder=new MyHoder(view);
        return myHoder;
    }
    @Override
    public int getItemCount(){
        int arr=0;
        try{
            if(list.size()==0)
            {
                arr=0;
            }
            else
            {
                arr=list.size();
            }
        }
        catch (Exception e )
        {

        }
        Log.d("count",""+arr);
        return arr;
    }

    @Override
    public void onBindViewHolder(MyHoder holder,int position)
    {
        FarmerInfo mylist=list.get(position);
        holder.name.setText(mylist.getFname());
        float rating_given=Float.parseFloat(mylist.getRating());
        holder.rating.setRating(rating_given);
        holder.price.setText(mylist.getPrice());
        holder.phoneNumber.setText(mylist.getPhone());

    }
    class MyHoder extends RecyclerView.ViewHolder{
        TextView name,price,phoneNumber;
        RatingBar rating;
        ImageView imgCall;
        public View view;
        public ClipData.Item currentItem;
        public MyHoder(final View itemView){
            super(itemView);
            view = itemView;
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    Log.d("inside","clicked");
                    //...
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure you want to select farmer "+name.getText()+"?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                   String h=String.valueOf(price.getText());
                                    myRef.child("chosen").setValue("yes");
                                    myRef.child("finalbid").setValue(Integer.parseInt(h));
                                    myRef.child("winner").setValue(name.getText());
                                    myRef.child("rated").setValue("no");

                                Intent i=new Intent(context,CallFarmerSelected.class);
                                    i.putExtra("winnerFarmer",name.getText());
                                    i.putExtra("winnerNumber",phoneNumber.getText());
                                    context.startActivity(i);

                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });


            name=(TextView)itemView.findViewById(R.id.fname);
            price=(TextView)itemView.findViewById(R.id.price);
            rating=(RatingBar) itemView.findViewById(R.id.rating);
            phoneNumber=(TextView)itemView.findViewById(R.id.phone);
            phoneNumber.setVisibility(View.GONE);
            imgCall=(ImageView)itemView.findViewById(R.id.call);
            imgCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Are you sure you want to call farmer "+name.getText()+"?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                    contact=phoneNumber.getText().toString();
                              //     String contact="9980565102";
                                   // myRef.child("chosen").setValue("yes");
                                    Intent i= new Intent(context,PhoneActivity.class);
                                   // intent.setData(Uri.parse("tel:" + contact));
                                    i.putExtra("phone",contact);

                                    //Intent i=new Intent(context,CompletedConsumerSide.class);
                                    context.startActivity(i);
                                }
                            });

                    builder1.setNegativeButton(
                            "No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });
        }
    }
}
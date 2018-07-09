package com.example.shravanram.greenauction;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;

public class RetrieveCameraActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    String imageUrl = "";
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_camera);
        img = (ImageView) findViewById(R.id.img);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is to give auction id
                imageUrl = dataSnapshot.child("photos").child("imageUrl").getValue().toString();
                Log.d("image",imageUrl);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Bitmap imageBitmap = decodeFromFirebaseBase64(imageUrl);
        img.setImageBitmap(imageBitmap);
    }


    public static Bitmap decodeFromFirebaseBase64(String imageUrl) {
        byte[] decodedByteArray = android.util.Base64.decode(imageUrl, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

}

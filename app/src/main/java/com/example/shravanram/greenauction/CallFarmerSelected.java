package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class CallFarmerSelected extends AppCompatActivity {
    String winner;
    private DatabaseReference mDatabase;
    String contact,city;
    private Button callFarmer;
    TextView name,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_farmer_selected);
        Bundle obj=getIntent().getExtras();
        winner=obj.getString("winnerFarmer");
        contact=obj.getString("winnerNumber");

        name=(TextView)findViewById(R.id.nameWinner);
        phone=(TextView)findViewById(R.id.phoneWinner);

        name.setText(winner);
        phone.setText(contact);


        callFarmer=(Button)findViewById(R.id.callFarmer);
        callFarmer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),PhoneActivity.class);
                i.putExtra("phone",contact);
                startActivity(i);

            }
        });


    }
}

package com.example.shravanram.greenauction;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class portalView extends AppCompatActivity implements View.OnClickListener {
    private Button ongoing;
    private Button completed;
    private Button buttonLogout;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_view);
        firebaseAuth=FirebaseAuth.getInstance();
        ongoing = (Button) findViewById(R.id.ongoing);
        completed = (Button) findViewById(R.id.completed);
        buttonLogout=(Button)findViewById(R.id.ButtonLogout);
        ongoing.setOnClickListener(this);
        completed.setOnClickListener(this);
    }
    public void onClick(View view) {
        if(view==ongoing)
        {
            //  finish();
            startActivity(new Intent(this,OngoingConsumerSide.class));

        }
        if(view==completed)
        {
            //  finish();
            startActivity(new Intent(this,CompletedConsumerSide.class));
        }
        if(view==buttonLogout) {
            //  finish();
            firebaseAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}

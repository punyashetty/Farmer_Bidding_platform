package com.example.shravanram.greenauction;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PhoneActivity extends AppCompatActivity {
    Intent intent;
    public String contact;
   // private Button callButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        Bundle obj=getIntent().getExtras();
        contact=obj.getString("phone");
       // Log.d("phonenumber",contact);

        call();
        /*callButton=(Button)findViewById(R.id.call);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Con.class);
                startActivity(i);
            }
        });*/
    }
        void call() {

            intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + contact));

            if (!checkPermission(android.Manifest.permission.CALL_PHONE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.CALL_PHONE}, 0);
            }


            if (checkPermission(android.Manifest.permission.CALL_PHONE)) {
                startActivity(intent);
            }


        }

        private boolean checkPermission(String permission) {
            return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            switch(requestCode) {
                case 0:
                    if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        startActivity(intent);
                    }
                    return;
            }
        }
  /*  @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),ConsumerHomeActivity.class);
        startActivity(newIntent);
    }*/

    }



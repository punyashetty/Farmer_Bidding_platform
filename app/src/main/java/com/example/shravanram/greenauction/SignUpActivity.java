package com.example.shravanram.greenauction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextRePassword;
    private TextView textViewSignin;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private Switch s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.editText);
        editTextPassword = (EditText) findViewById(R.id.editText2);
        editTextRePassword = (EditText) findViewById(R.id.editText3);
        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        s = (Switch) findViewById(R.id.farmerConsumer);
        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), ConsumerHomeActivity.class));
        }

    }
    private void registerUser()
    {   int flag=1;
        String email=editTextEmail.getText().toString().trim();
        final String password=editTextPassword.getText().toString().trim();
        final String repassword=editTextRePassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            flag=0;
            Toast.makeText(this,"Please enter email",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            flag=0;
            Toast.makeText(this,"Please enter password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(repassword))
        {
            flag=0;
            Toast.makeText(this,"Please retype password",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!password.contains(repassword)){
            flag=0;
            Toast.makeText(this,"Passwords dont match",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!TextUtils.isEmpty(password)){
            if(password.length()<6){
                flag=0;
                Toast.makeText(this,"Password length must be atleast six",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(flag==1) {
            progressDialog.setMessage("Registering user");
            progressDialog.show();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(SignUpActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                Log.v("hello", password.toString());
                                Intent intent = new Intent(getApplicationContext(), SignUpActivityStep2.class);
                                if (s.isChecked()) {
                                    intent.putExtra("farmerOrConsumer", "Consumer");

                                } else {
                                    intent.putExtra("farmerOrConsumer", "Farmer");
                                }

                                startActivity(intent);

                            } else {
                                Toast.makeText(SignUpActivity.this, "Registered unsuccessfully ", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });
        }
    }


    @Override

    public void onClick(View view) {
        if (view== buttonRegister)
        {
            registerUser();
        }
        if(view== textViewSignin)
        {
            progressDialog.dismiss();
            //finish();
            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
            //finished reg?no;
            //i.putExtra("finished","no");
            startActivity(i);
        }

    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(newIntent);
    }
}

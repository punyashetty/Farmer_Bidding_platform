package com.example.shravanram.greenauction;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.shravanram.greenauction.firebase_models.PersonInfo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

public class PortalCreation extends AppCompatActivity {
    private FirebaseAuth fire;
    private Spinner dropDown,dropDownCategory;
    private DatabaseReference mDatabase;
    private EditText produce;
    private EditText location;
    private EditText qty;
    private TextView time;
    private EditText iniprice;
    private Button createbut;
    Calendar dateTime=Calendar.getInstance();
    //private DatabaseReference mImg;
    FirebaseUser current;
    String e1[];


    String c="";
    int count;

    ArrayList<String> unitsMeasurement=new ArrayList<>();
    ArrayList<String> prodCategory=new ArrayList<>();

    Map<String, String> imgmap = new HashMap<String, String>();
    public  PortalCreation() {
//copy image address as the value for this
        imgmap.put("potato","https://tse1.mm.bing.net/th?id=OIP.oDSW7MrHpJpKcgIt4W3N4QHaE8&pid=15.1&P=0&w=261&h=175");
        imgmap.put("tomato","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTmXQk9i0iTphknHQySqdvcXmxOz5K1kFn2fGVeRNX_gJItQ7lY");
        imgmap.put("beans","https://images.scrippsnetworks.com/up/tp/Scripps_-_Food_Category_Prod/559/415/0203557_630x355.jpg");
        imgmap.put("orange","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSkid2LgT8k6OKuTPz4kQwX2Dnp3YK3Pq0mayTCFLfZbTmFZhgX-A");
        imgmap.put("coriander","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSsYIo1odcKOOu2Ba5KEitqS_ap2phJ1KTCAvHCjAUJu9HaQF17");
        imgmap.put("rice","https://cdn1.medicalnewstoday.com/content/images/articles/318/318699/white-rice-on-a-table.jpg");
        imgmap.put("onion","https://greenmylife-wpengine.netdna-ssl.com/wp-content/uploads/2017/02/Untitled-30.jpg");
        imgmap.put("cabbage","https://www.foodsforbetterhealth.com/wp-content/uploads/2017/02/cabbage.jpg");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portal_creation);
        fire=FirebaseAuth.getInstance();

        dropDown = (Spinner) findViewById(R.id.quantity1);

        //String[] unitsMeasurement = new String[]{"kg", "quintal", "litre"};
        unitsMeasurement.addAll(Arrays.asList(getResources().getStringArray(R.array.units)));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                unitsMeasurement);
        dropDown.setAdapter(adapter);

        dropDownCategory = (Spinner) findViewById(R.id.category);
        //String[] prodCategory = new String[]{"fruit", "vegetables", "pulses"};
        prodCategory.addAll(Arrays.asList(getResources().getStringArray(R.array.prodCategory)));
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,
                prodCategory);
        dropDownCategory.setAdapter(adapter1);
        produce = (EditText) findViewById(R.id.produce);
        location = (EditText) findViewById(R.id.location);
        //  count1=(TextView) findViewById(R.id.count);
        qty = (EditText) findViewById(R.id.qty);
        time = (TextView) findViewById(R.id.timeSlot);
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateTime();
                updateDate();

            }
        });
        time.setText("Click to select deadline>>");



        iniprice = (EditText) findViewById(R.id.iniPrice);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        final ArrayList<PersonInfo> person=new ArrayList<>();
        createbut = (Button) findViewById(R.id.create);
        createbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String prod = produce.getText().toString().trim();
                String loc = location.getText().toString().trim();
                String qq = qty.getText().toString().trim();
                int q = Integer.parseInt(qq);
                String units = dropDown.getSelectedItem().toString();
                String ts = time.getText().toString().trim();
                String init_pri = iniprice.getText().toString().trim();

                if (TextUtils.isEmpty(prod)) {

                    Toast.makeText(getApplicationContext(), "Please enter prod name", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(loc)) {

                    Toast.makeText(getApplicationContext(), "Please enter location", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(qq)) {

                    Toast.makeText(getApplicationContext(), "Please enter quantity", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(ts)) {

                    Toast.makeText(getApplicationContext(), "Please enter deadline", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(init_pri)) {

                    Toast.makeText(getApplicationContext(), "Please enter initial price", Toast.LENGTH_LONG).show();
                    return;
                }
                c = "" + count;
                //setting count of auctions
               mDatabase.child("count").setValue(c);
               Log.d("count",c);
                PersonInfo info=new PersonInfo();
                info.qty=q;
                info.deadline=ts;
                info.initialbid=Float.parseFloat(init_pri);
                info.loc=loc;
                info.prod=prod;
                info.weight=units;
                info.email=fire.getCurrentUser().getEmail().toString();
                info.chosen="no";
                info.rated="no";
                info.winner="none";
                info.category=dropDownCategory.getSelectedItem().toString();
                //get the produce name's value which is the image's address.
                /*when we write "imgaddr" it gets stored as "\"imgaddr\"" in firebase
                hence we replace " in link with blank space because firebase puts the link automatically
                in quotes.*/
                info.image=imgmap.get(prod.toLowerCase().replace("\"",""));
                e1=fire.getCurrentUser().getEmail().toString().split("\\.");
                String e2=e1[0];
                mDatabase.child("auction").child(c).setValue(info);

               mDatabase.child("Consumer").child(e2).child("AuctionID").push().setValue(c);

                startActivity(new Intent(getApplicationContext(),ConsumerHomeActivity.class));
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //this is to give auction id
                    c= dataSnapshot.child("count").getValue().toString();
                    count=Integer.parseInt(c);
                    count++;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    private void updateDate(){
        new DatePickerDialog(this,d,
                dateTime.get(Calendar.YEAR),dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateTime(){
        new TimePickerDialog(this,t,dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE),true).show();
    }
    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            dateTime.set(Calendar.YEAR,year);
            dateTime.set(Calendar.MONTH,month);
            dateTime.set(Calendar.DAY_OF_MONTH,day);
            updateTextLabel();
        }
    };
    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int min) {
            dateTime.set(Calendar.HOUR_OF_DAY,hour);
            dateTime.set(Calendar.MINUTE,min);

            updateTextLabel();
        }
    };
    private void updateTextLabel(){
        //displayText.setText(formatDateTime.format(dateTime.getTime()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm aa");
        String getCurrentDateTime = simpleDateFormat.format(dateTime.getTime());
        time.setText(getCurrentDateTime);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent newIntent=new Intent(getApplicationContext(),ConsumerHomeActivity.class);
        startActivity(newIntent);
    }

}




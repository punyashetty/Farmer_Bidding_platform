package com.example.shravanram.greenauction;

        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
        import android.widget.Button;
        import android.widget.RatingBar;

        import android.widget.Toast;

        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

public class RatingFarmer extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth fire=FirebaseAuth.getInstance();
    String emailno[];
    String e2="none";
    String each_ratings="none";
    int sum_of_ratings=0;
    int countOfRatingsTillNow;
    float rate ;
    String auctionselected="none";
    String farmerToBeRatedEmail="none";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_farmer);

        Bundle obj=getIntent().getExtras();
        auctionselected=obj.getString("auctionClicked");

        final RatingBar ratingRatingBar = (RatingBar) findViewById(R.id.rating_bar);
        Button submitButton = (Button) findViewById(R.id.submit_button);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rate=ratingRatingBar.getRating();
                Toast.makeText(getApplicationContext(), "You have successfully rated farmer "+rate, Toast.LENGTH_SHORT).show();

                Log.d("ratings",""+(countOfRatingsTillNow));
                float avg_rating=(sum_of_ratings+rate)/(countOfRatingsTillNow);
                Log.d("summ",""+sum_of_ratings);
                Log.d("countt",""+countOfRatingsTillNow);
                Log.d("avg",""+avg_rating);
              mDatabase.child("Farmer").child(farmerToBeRatedEmail).child("rating").setValue(""+Math.round(avg_rating));
                mDatabase.child("Farmer").child(farmerToBeRatedEmail).child("allRating").push().setValue(""+Math.round(rate));
                mDatabase.child("auction").child(auctionselected).child("rated").setValue("yes");
                Intent intent=new Intent(getApplicationContext(),ConsumerHomeActivity.class);
                startActivity(intent);
            }
        });

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String winnerFarmer=dataSnapshot.child("auction").child(auctionselected).child("winner")
                        .getValue().toString();

                //gets the bid clicked
                DataSnapshot allBids=dataSnapshot.child("Bids").child(auctionselected);
                //gets all the farmer names in the bid
                Iterable<DataSnapshot> farmers=allBids.getChildren();
                for(DataSnapshot var2:farmers) {
                   String winnerInDatabase= var2.child("fname").getValue().toString();
                   if(winnerFarmer.contains(winnerInDatabase)){
                       farmerToBeRatedEmail=var2.getKey();
                   }
                }


                DataSnapshot allRatings=dataSnapshot.child("Farmer").child(farmerToBeRatedEmail).child("allRating");
                Iterable<DataSnapshot> Ratings=allRatings.getChildren();
                sum_of_ratings=0;
                for(DataSnapshot var1:Ratings){

                    each_ratings=var1.getValue().toString();
                    Log.d("ratingg",each_ratings);
                    sum_of_ratings+=Integer.parseInt(each_ratings);
                    countOfRatingsTillNow++;

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}

package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import br.com.livroandroid.trainingmockup.Entities.Market;
import br.com.livroandroid.trainingmockup.R;

public class MarketActivity extends AppCompatActivity {

    private String title;
    private DatabaseReference mDatabase;
    private TextView tvTitle, tvLocation;
    private ImageView firstImage, secondImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        Bundle b = new Bundle();
        b = getIntent().getExtras();
        title = b.getString("Title");

        tvTitle = findViewById(R.id.tvTitleMarket);
        tvLocation = findViewById(R.id.tvLocationMarket);
        firstImage = findViewById(R.id.imageViewFisrt);
        secondImage = findViewById(R.id.imageViewSecond);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("markets");

        Query q = mDatabase.orderByChild("title").equalTo(title);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Market mkt = snapshot.getValue(Market.class);
                    tvTitle.setText(mkt.getTitle());
                    tvLocation.setText(mkt.getAdress());

                    Picasso.with(getApplicationContext())
                            .load(mkt.getUrlFirstImage())
                            .fit()
                            .centerCrop()
                            .into(firstImage);

                    Picasso.with(getApplicationContext())
                            .load(mkt.getUrlSecondImage())
                            .fit()
                            .centerCrop()
                            .into(secondImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),R.string.not_found,Toast.LENGTH_SHORT).show();
            }
        });
    }
}

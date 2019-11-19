package br.com.livroandroid.trainingmockup.Fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MarketFragment extends Fragment {

    private String title;
    private DatabaseReference mDatabase;
    private TextView tvTitle, tvLocation;
    private ImageView firstImage, secondImage;
    public MarketFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            title = getArguments().getString("Title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_market, container, false);

        tvTitle = view.findViewById(R.id.tvTitleMarket);
        tvLocation = view.findViewById(R.id.tvLocationMarket);
        firstImage = view.findViewById(R.id.imageViewFisrt);
        secondImage = view.findViewById(R.id.imageViewSecond);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("markets");

        Query q = mDatabase.orderByChild("title").equalTo(title);
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){

                    Market mkt = snapshot.getValue(Market.class);
                    tvTitle.setText(mkt.getTitle());
                    tvLocation.setText(mkt.getAdress());

                    Picasso.with(getContext())
                            .load(mkt.getUrlFirstImage())
                            .fit()
                            .centerCrop()
                            .into(firstImage);

                    Picasso.with(getContext())
                            .load(mkt.getUrlSecondImage())
                            .fit()
                            .centerCrop()
                            .into(secondImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(),"not found !",Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

}

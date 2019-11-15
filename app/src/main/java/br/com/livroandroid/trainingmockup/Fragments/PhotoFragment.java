package br.com.livroandroid.trainingmockup.Fragments;

import android.animation.ArgbEvaluator;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.livroandroid.trainingmockup.Activities.HomeActivity;
import br.com.livroandroid.trainingmockup.Activities.TakePhotoActivity;
import br.com.livroandroid.trainingmockup.Adapters.Adapter;

import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import androidx.viewpager.widget.ViewPager;
import br.com.livroandroid.trainingmockup.Entities.Card;
import br.com.livroandroid.trainingmockup.Entities.Model;
import br.com.livroandroid.trainingmockup.R;

import static android.app.Activity.RESULT_OK;


public class PhotoFragment extends Fragment {


    private FloatingActionButton floatingActionButton;
    private ViewPager viewPager;
    private Adapter adapter;
    private List<Card> cards;
    private ProgressBar mProgresBar;
    private DatabaseReference mDatabaseRef;
    private TextView tvNoImages;


    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_photo, container, false);

        floatingActionButton = view.findViewById(R.id.floatingActionButton);
        mProgresBar = view.findViewById(R.id.progressBar) ;
        tvNoImages = view.findViewById(R.id.tvNoImages);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        cards = new ArrayList<>();

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cards = new ArrayList<>();

                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    Card card = postSnapshot.getValue(Card.class);
                    cards.add(card);
                    tvNoImages.setVisibility(View.INVISIBLE);

                    Log.i("INFO",card.getImageUrl() + card.getTemp());

                }

                adapter = new Adapter(cards,getActivity());

                viewPager = view.findViewById(R.id.viewPager);
                viewPager.setAdapter(adapter);
                viewPager.setPadding(130,0,130,0);

                viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TakePhotoActivity.class);
                startActivity(intent);
            }

        });

        return view;
    }

}

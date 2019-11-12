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
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.livroandroid.trainingmockup.Activities.HomeActivity;
import br.com.livroandroid.trainingmockup.Adapters.Adapter;

import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;

    FloatingActionButton floatingActionButton;
    ViewPager viewPager;
    Adapter adapter;
    List<Model> models;
    ProgressBar mProgresBar;

    private static final int CAMERA_REQUEST_CODE = 1;
    private final int PICK_IMAGE_REQUEST = 1;


    public PhotoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=  inflater.inflate(R.layout.fragment_photo, container, false);
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingActionButton);
        mProgresBar = (ProgressBar)view.findViewById(R.id.progressBar) ;

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        models = new ArrayList<>();
        models.add(new Model(R.drawable.ic_google_logo,"MAP","This is a image MAP"));
        models.add(new Model(R.drawable.ic_photo,"PHOTO","This is a image PHOTO"));
        models.add(new Model(R.drawable.ic_calendar,"CALENDAR","This is a image CALENDAR"));
        models.add(new Model(R.drawable.ic_cloud,"CLOUD","This is a image CLOUD"));

        adapter = new Adapter(models,getActivity());

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




        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               chooseImage();
            }

        });

        return view;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

            mImageUri = data.getData();

            if(mUploadTask != null && mUploadTask.isInProgress()){

                Toast.makeText(getActivity(),"Upload in Progress",Toast.LENGTH_SHORT).show();

            }else {
                uploadImage();
            }

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(){
        if(mImageUri != null) {

            StorageReference fileReference = mStorageRef.child( System.currentTimeMillis()
            +"." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgresBar.setProgress(0);
                                }
                            },500);

                            Toast.makeText(getActivity(),"Upload Successful",Toast.LENGTH_SHORT).show();

                            String temp = "23";

                            Card card = new Card(temp.trim(),taskSnapshot.getStorage().getDownloadUrl().toString());
                            String cardId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(cardId).setValue(card);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mProgresBar.setProgress((int)progress);

                        }
                    });
        }else {
            Toast.makeText(getActivity(),"No File Selected",Toast.LENGTH_SHORT).show();
        }

    }

}

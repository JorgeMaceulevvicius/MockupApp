package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import br.com.livroandroid.trainingmockup.Connection.Weather;
import br.com.livroandroid.trainingmockup.Entities.Card;
import br.com.livroandroid.trainingmockup.Fragments.SigleChoiceDialog;
import br.com.livroandroid.trainingmockup.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

public class TakePhotoActivity extends AppCompatActivity implements SigleChoiceDialog.SingleChoiceListener {

    private static final int CAMERA_REQUEST_CODE = 1;
    private ImageView imageView;
    private Button btn_add_temp;
    private ImageButton imageButtonClose;
    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    //private ProgressBar progresBar_add;
    private Bitmap image;
    DialogFragment singgleChoiceDialog= new SigleChoiceDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);

        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        imageButtonClose = findViewById(R.id.imageButtonClose);
        imageView = findViewById(R.id.imageView_add_temperature);
        btn_add_temp = findViewById(R.id.btn_add_temperature);
       // progresBar_add = findViewById(R.id.progressBar_add);

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent,CAMERA_REQUEST_CODE);

        btn_add_temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // uploadImage();
              // finish();

                singgleChoiceDialog.setCancelable(false);
                singgleChoiceDialog.show(getSupportFragmentManager(),"Dialog");
            }
        });

        imageButtonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

           Bundle extras = data.getExtras();
           image = (Bitmap)extras.get("data");
           imageView.setImageBitmap(image);

        }

    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = TakePhotoActivity.this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImage(String temp){
        if(image != null) {

        //    progresBar_add.setVisibility(View.INVISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference fileReference = mStorageRef.child( System.currentTimeMillis()
                    +"."+data);

            mUploadTask = fileReference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                  //  progresBar_add.setProgress(0);
                                }
                            },500);

                            Toast.makeText(TakePhotoActivity.this,"Upload Successful",Toast.LENGTH_SHORT).show();

                           // String temp = "23";

                            Card card = new Card(temp.trim(),taskSnapshot.getStorage().getName());
                            String cardId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(cardId).setValue(card);

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TakePhotoActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                           // progresBar_add.setProgress((int)progress);

                        }
                    });
        }else {
            Toast.makeText(TakePhotoActivity.this,"No File Selected",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPositiveButtonClicked(String temp, int position) {
        if(temp != null){

            uploadImage(temp);
            finish();

        }else{
            String city = "Curitiba";
            uploadImage(getTemperature(city)); ///get Current Temp
            finish();
        }

    }

    @Override
    public void onNegativeBuuttonClicked() {
        singgleChoiceDialog.setCancelable(true);
    }
    private String getTemperature(String city){

        String temperature = "--";

        Weather weather = new Weather();

        try {
            String content = weather
                    .execute("https://openweathermap.org/data/2.5/weather?q="+ city +"&appid=b6907d289e10d714a6e88b30761fae22")
                    .get();

            JSONObject jsonObject = new JSONObject(content);
            String weatherData = jsonObject.getString("main");

            JSONObject jsonObjectmain = new JSONObject(weatherData);
            temperature = jsonObjectmain.getString("temp");


        } catch (Exception e) {
            e.printStackTrace();
        }
        return temperature;
    }
}

package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import br.com.livroandroid.trainingmockup.Connection.FirebaseConnection;
import br.com.livroandroid.trainingmockup.R;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    static final int GOOGLE_SING = 123;
    private FirebaseAuth mAuth;
    private Button btn_register,btn_singUp, btn_login_google , btn_logout_google, btn_goHome;
    private EditText edtEmail, edtPass;
    private ProgressBar progressBar;
    private GoogleSignInClient mGoogleSingInClient;

    private final int LOCATION_REQUEST = 0;
    private static final String[] LOCATION_PERMS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA

    };
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case LOCATION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED ){

                } else {
                    Toast.makeText(this,R.string.app_needs_pemisson,Toast.LENGTH_SHORT).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
                    }
                }
                return;
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(LOCATION_PERMS, LOCATION_REQUEST);
        }

        btn_logout_google = findViewById(R.id.logOutGoogle);
        btn_login_google = findViewById(R.id.logInGoogle);
        btn_singUp = findViewById(R.id.btn_singUp);
        btn_register = findViewById(R.id.btn_register);
        btn_goHome = findViewById(R.id.GoToHome);
        edtEmail = findViewById(R.id.editTextEmail);
        edtPass = findViewById(R.id.editTextPass);
        progressBar = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseConnection.getFirebaseAuth();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSingInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        btn_login_google.setOnClickListener(v -> SingInGoogle());
        btn_logout_google.setOnClickListener(v -> Logout());

        if(mAuth.getCurrentUser() != null){

            goToHome();

        }

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),NewUserActivity.class);
                startActivity(intent);

            }
        });
        btn_singUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = edtEmail.getText().toString().trim();
                String password = edtPass.getText().toString().trim();

                if(email.equals("") || password.equals("")){
                    Toast.makeText(getApplicationContext(),R.string.enter_correct_data,Toast.LENGTH_SHORT).show();
                }else {

                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(getApplicationContext(),R.string.welcome,Toast.LENGTH_SHORT).show();

                                        goToHome();

                                    }else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(getApplicationContext(),R.string.wrong_email_password,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });
        btn_goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToHome();
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    void SingInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SING);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GOOGLE_SING) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);
            try{

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if(account!= null) firebaseAuthWithGoogle(account);


            }catch (ApiException e){
                e.printStackTrace();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: "+ account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(),null);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task ->{
                    if(task.isSuccessful()){

                        Toast.makeText(getApplicationContext(),R.string.welcome,Toast.LENGTH_SHORT).show();

                        Log.d("TAG","singin sucess");

                        FirebaseUser user = mAuth.getCurrentUser();

                        goToHome();

                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG","singin failure",task.getException());

                        Toast.makeText(this,R.string.signin_failed,Toast.LENGTH_SHORT);
                        updateUI(null);

                    }

                });
    }
    private void goToHome(){

        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
        startActivity(intent);

    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {

            btn_login_google.setVisibility(View.INVISIBLE);
            btn_logout_google.setVisibility(View.VISIBLE);
            btn_singUp.setVisibility(View.INVISIBLE);
            btn_register.setVisibility(View.INVISIBLE);
            btn_goHome.setVisibility(View.VISIBLE);
            edtEmail.setEnabled(false);
            edtPass.setEnabled(false);

        }else {
            btn_logout_google.setVisibility(View.INVISIBLE);
            btn_login_google.setVisibility(View.VISIBLE);
            btn_singUp.setVisibility(View.VISIBLE);
            btn_register.setVisibility(View.VISIBLE);
            btn_goHome.setVisibility(View.INVISIBLE);
            edtEmail.setEnabled(true);
            edtPass.setEnabled(true);

        }
    }
    private void Logout() {
        edtPass.setText("");
        FirebaseAuth.getInstance().signOut();
        mGoogleSingInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mAuth.getCurrentUser() != null) {
            FirebaseUser user = mAuth.getCurrentUser();
            progressBar.setVisibility(View.INVISIBLE);
            updateUI(user);
        }

    }



}

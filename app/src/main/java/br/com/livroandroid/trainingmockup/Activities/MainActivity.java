package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import br.com.livroandroid.trainingmockup.Entities.User;
import br.com.livroandroid.trainingmockup.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    static final int GOOGLE_SING = 123;
    FirebaseAuth mAuth;

    Button btn_entrar, btn_login_google , btn_logout_google;

    EditText edtLogin, edtSenha;

    ProgressBar progressBar;

    GoogleSignInClient mGoogleSingInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout_google = findViewById(R.id.logOutGoogle);
        btn_login_google = findViewById(R.id.logInGoogle);
        btn_entrar = findViewById(R.id.buttonEntrar);
        progressBar = findViewById(R.id.progressBarLogin);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSingInClient = GoogleSignIn.getClient(this,googleSignInOptions);

        btn_login_google.setOnClickListener(v -> SingInGoogle());
        btn_logout_google.setOnClickListener(v -> Logout());

        if(mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);

        }

    }

    void SingInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSingInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SING);
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
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG","signin sucess");

                        FirebaseUser user = mAuth.getCurrentUser();

                        Intent intent = new Intent(MainActivity.this,HomeActivity.class);
                        startActivity(intent);

                        finish();

                    }else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.d("TAG","signin failure",task.getException());

                        Toast.makeText(this,"SignIn Failed !",Toast.LENGTH_SHORT);
                        updateUI(null);

                    }

                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            btn_login_google.setVisibility(View.INVISIBLE);
            btn_logout_google.setVisibility(View.VISIBLE);
            btn_entrar.setVisibility(View.INVISIBLE);
        }else {
            btn_logout_google.setVisibility(View.INVISIBLE);
            btn_login_google.setVisibility(View.VISIBLE);
            btn_entrar.setVisibility(View.VISIBLE);

        }
    }
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSingInClient.signOut().addOnCompleteListener(this,
                task -> updateUI(null));
    }

}

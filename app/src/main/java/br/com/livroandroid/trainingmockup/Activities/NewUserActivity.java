package br.com.livroandroid.trainingmockup.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import br.com.livroandroid.trainingmockup.Connection.FirebaseConnection;
import br.com.livroandroid.trainingmockup.R;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class NewUserActivity extends AppCompatActivity {

    private EditText edt_email, edt_pass;
    private Button btn_back, btn_regNew;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        btn_back = findViewById(R.id.btn_back);
        btn_regNew = findViewById(R.id.buttonRegisterFirebase);
        edt_email = findViewById(R.id.editTextEmailNewUser);
        edt_pass = findViewById(R.id.editTextPassNewUser);
        progressBar = findViewById(R.id.progressBarNewUser);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        btn_regNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edt_email.getText().toString().trim();
                String password = edt_pass.getText().toString().trim();

                if(email.equals("") || password.equals("")){

                    Toast.makeText(getApplicationContext(),R.string.enter_correct_data,Toast.LENGTH_SHORT).show();

                }else {

                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(NewUserActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(NewUserActivity.this,R.string.user_registred,Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(NewUserActivity.this,R.string.error,Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = FirebaseConnection.getFirebaseAuth();
    }
}

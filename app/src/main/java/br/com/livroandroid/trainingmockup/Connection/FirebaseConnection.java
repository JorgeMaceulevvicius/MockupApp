package br.com.livroandroid.trainingmockup.Connection;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;

public class FirebaseConnection {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    private FirebaseConnection(){

    }

    public static FirebaseAuth getFirebaseAuth(){
        if(firebaseAuth == null){
            startFirebaseAuth();
        }
        return firebaseAuth;
    }

    private static void startFirebaseAuth() {
        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    firebaseUser = user;
                }
            }
        };

    }
    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }
    public static void logOut(){
        firebaseAuth.signOut();
    }
}

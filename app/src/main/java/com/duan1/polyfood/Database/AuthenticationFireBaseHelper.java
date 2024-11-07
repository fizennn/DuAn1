package com.duan1.polyfood.Database;

import android.net.Uri;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationFireBaseHelper {
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    public AuthenticationFireBaseHelper() {
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
    }

    public String getUID(){
        if (currentUser != null){
            return currentUser.getUid();
        } else {
            return null;
        }

    }

    public String getEmail(){
        if (currentUser != null){
            return currentUser.getEmail();
        } else {
            return null;
        }
    }
    public String getName(){
        if (currentUser != null){
            return currentUser.getDisplayName();
        } else {
            return null;
        }
    }

    public Uri getPhotoURL(){
        if (currentUser != null){
            return currentUser.getPhotoUrl();
        } else {
            return null;
        }
    }

    public void signOut(){
        mAuth.signOut();
    }
}

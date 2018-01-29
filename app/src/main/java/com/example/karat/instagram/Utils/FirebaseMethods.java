package com.example.karat.instagram.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karat.instagram.Home.HomeActivity;
import com.example.karat.instagram.Login.LoginActivity;
import com.example.karat.instagram.Login.RegisterActivity;
import com.example.karat.instagram.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

import static android.content.ContentValues.TAG;

/**
 * Created by karat on 29/01/2018.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String userID;

    // Getting the variables values.
    public FirebaseMethods(Context context) {
        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    public boolean checkIfUsernameExists(String username, DataSnapshot dataSnapshot){
        Log.i(TAG, "checkIfUsernameExists: Checking if the " + username + " already exists.");

        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()){
            Log.i(TAG, "checkIfUsernameExists: Passing througt the dataShot " + ds);

            user.setUsername(ds.getValue(User.class).getUsername());

            if (StringManipulation.expandUsername(user.getUsername()).equals(username)){
                Log.i(TAG, "checkIfUsernameExists: FOUND A MATCH " + user.getUsername());
                return true;
            }

        }

        return false;

    }


    /**
     * Method to register a new Email for the user in the Firebase server.
     * @param email
     * @param username
     * @param password
     * @param progressBar
     * @param intent
     */
    public void RegisterNewEmail(String email, String username, String password, final ProgressBar progressBar, final Intent intent){

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            mContext.startActivity(intent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                        progressBar.setVisibility(View.GONE);
                        // ...
                    }
                });
    }


    /**
     * Method to signin the user in the Firebase server.
     * @param email
     * @param password
     * @param progressBar
     * @param intent
     */
    public void SignInWithEmail(String email, String password, final ProgressBar progressBar, final Intent intent){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            Log.i(TAG, "onComplete: User logged in" + user.getUid() + " " + user.getEmail());

                            Toast.makeText(mContext, "Authentication Successful", Toast.LENGTH_SHORT).show();
                            
                            mContext.startActivity(intent);
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            // updateUI(null);
                        }

                        progressBar.setVisibility(View.GONE);

                        // ...
                    }
                });


    }
}

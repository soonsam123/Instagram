package com.example.karat.instagram.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.instagram.Home.HomeActivity;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.FirebaseMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by karat on 26/01/2018.
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = AppCompatActivity.class.getName();

    private ImageView instagramLogo;
    private TextView registerNewAccount;
    private AppCompatEditText mEmail, mUsername, mPassword;
    private AppCompatButton registerButton;
    private ProgressBar mProgressBar;
    private RelativeLayout relativeLayout;
    private Context mContext = RegisterActivity.this;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: Initializing LoginActivity");

        setUpFirebaseAuth();
        setUpWidgets();
        setClickKeys();

        init();


    }

    // Set the onClickListener and onKeyListener.
    private void setClickKeys(){

        mProgressBar.setVisibility(View.GONE);
        instagramLogo.setOnClickListener(this);
        registerNewAccount.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
        mPassword.setOnKeyListener(this);

    }

    // Assigning all the values to the variables.
    private void setUpWidgets(){

        Log.i(TAG, "setUpWidgets: Setting up the widgets");

        instagramLogo = findViewById(R.id.instagramLogo_register);
        registerNewAccount = findViewById(R.id.registerNewAccount_register);
        mEmail = findViewById(R.id.emailEditText_register);
        mUsername = findViewById(R.id.fullNameEditText_register);
        mPassword = findViewById(R.id.passwordEditText_register);
        registerButton = findViewById(R.id.registerButton_register);
        mProgressBar = findViewById(R.id.progressBar_register);
        relativeLayout = findViewById(R.id.relLayout_container_register);

    }

    // Hide the keyBoard when clicking elsewhere in the screen.
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.relLayout_container_register || view.getId() == R.id.instagramLogo_register || view.getId() == R.id.registerNewAccount_register){
            hideKeyBoard();
        }
    }

    // Hide the KeyBoard from the screen.
    private void hideKeyBoard(){

        Log.i(TAG, "hideKeyBoard: Hiding the keyboard");

        if (this.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    // Register when pressind the enter key in the last field.
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        Log.i(TAG, "onKey: Pressing the enterKey when it is in the password field");

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            registerButton.performClick();
        }

        return false;
    }

    // Checks if a string is null.
    private boolean stringIsNull(String string){
        if (string.equals("")){return true;} else {return false;}
    }

    /*===================================== Firebase =====================================*/
    private void init(){

        Log.i(TAG, "init: Initializing the registraition");

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyBoard();

                String email = mEmail.getText().toString();
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                if (stringIsNull(email) || stringIsNull(username) || stringIsNull(password)){
                    Toast.makeText(RegisterActivity.this, "You must fill in all the fields", Toast.LENGTH_LONG).show();
                } else {

                    Intent intentHome = new Intent(mContext, HomeActivity.class);

                    firebaseMethods = new FirebaseMethods(mContext);
                    firebaseMethods.RegisterNewEmail(email, username, password, mProgressBar, intentHome);

                    // If registerNewEmail complete was successful.
                    // finish();

                }

            }
        });

    }

    private void setUpFirebaseAuth(){
        Log.i(TAG, "setUpFirebaseAuth: Setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // User is logged in
                    Log.i(TAG, "onAuthStateChanged: User logged in: " + user.getUid());
                } else {
                    // User is logged out
                    Log.i(TAG, "onAuthStateChanged: User logged out");
                }
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}

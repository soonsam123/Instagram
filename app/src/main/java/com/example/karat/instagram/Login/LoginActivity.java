package com.example.karat.instagram.Login;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by karat on 26/01/2018.
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    private static final String TAG = AppCompatActivity.class.getName();

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseMethods firebaseMethods;

    private Context mContext;
    private ProgressBar mProgressBar;
    private AppCompatEditText mEmail, mPassword;
    private AppCompatButton loginButton;
    private ImageView instagramLogo;
    private RelativeLayout relativeLayout;
    private TextView noAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Initializing LoginActivity");

        setUpWidgets();

        mProgressBar.setVisibility(View.GONE);
        relativeLayout.setOnClickListener(this);
        instagramLogo.setOnClickListener(this);
        mPassword.setOnKeyListener(this);
        noAccount.setOnClickListener(this);

        setUpFirebaseAuth();
        init();

    }

    private void setUpWidgets(){

        Log.i(TAG, "setUpWidgets: Setting up the widgets");
        
        mContext = LoginActivity.this;
        mProgressBar = findViewById(R.id.progressBar_login);
        mEmail = findViewById(R.id.emailEditText_login);
        mPassword = findViewById(R.id.passwordEditText_login);
        loginButton = findViewById(R.id.loginButton_login);
        instagramLogo = findViewById(R.id.instagramLogo_login);
        relativeLayout = findViewById(R.id.relLayout_container_login);
        noAccount = findViewById(R.id.noAccount_login);

    }

    public boolean stringIsNull(String string){
        if (string.equals("")){return true;} else {return false;}
    }


    @Override
    public void onClick(View view) {

        // Hide the keyboard when the user click on the screen.
        if (view.getId() == R.id.instagramLogo_login || view.getId() == R.id.relLayout_container_login){
            hideKeyBoard();
            } else if (view.getId() == R.id.noAccount_login){
            Intent intentRegister = new Intent(mContext, RegisterActivity.class);
            startActivity(intentRegister);
        }
        }

    private void hideKeyBoard() {
        if (this.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN){
            loginButton.performClick();
        }

        return false;
    }

    /*===================================== Firebase =====================================*/


    private void init() {

        Log.i(TAG, "init: Initializing the user authenticaton");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                hideKeyBoard();

                if (stringIsNull(email) || stringIsNull(password)) {
                    Toast.makeText(mContext, R.string.fillin_all_fields, Toast.LENGTH_SHORT).show();
                } else {


                    Intent intentHome = new Intent(mContext, HomeActivity.class);

                    firebaseMethods = new FirebaseMethods(mContext);
                    firebaseMethods.SignInWithEmail(email, password, mProgressBar, intentHome);



                }
            }
        });
    }


    /**
     * 1) Initialize mAuth;
     * 2) Log the user id if logged in, or logged out if logged out.
     */
    private void setUpFirebaseAuth(){
        Log.i(TAG, "setUpFirebaseAuth: Setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // User is logged in
                    Log.i(TAG, "onAuthStateChanged: User logged in" + user.getUid());
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

package com.example.karat.instagram.Profile;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.karat.instagram.Login.LoginActivity;
import com.example.karat.instagram.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class SignOutFragment extends Fragment {

    private static final String TAG = "SignOutFragment";

    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;

    private Context mContext;

    private ProgressBar mProgressBar;
    private AppCompatButton signOut_button, cancel_button;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        setUpFirebaseAuth();

        mProgressBar = view.findViewById(R.id.progressBar_signOut);
        signOut_button = view.findViewById(R.id.signOut_button_signout);
        cancel_button = view.findViewById(R.id.cancel_button_signout);

        mProgressBar.setVisibility(View.GONE);

        signOut_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mProgressBar.setVisibility(View.VISIBLE);

                mAuth.signOut();
                getActivity().finish();

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;

    }



    /*===================================== Firebase =====================================*/

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

                    // Clear the cache so the user can not come back to the last Activity by pressing the onBackButton.
                    Intent intentLogin = new Intent(getActivity(), LoginActivity.class);
                    intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentLogin);
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

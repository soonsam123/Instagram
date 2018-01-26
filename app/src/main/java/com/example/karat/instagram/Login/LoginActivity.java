package com.example.karat.instagram.Login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.karat.instagram.R;

/**
 * Created by karat on 26/01/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: Initializing LoginActivity");
    }
}

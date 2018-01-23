package com.example.karat.instagram;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by karat on 23/01/2018.
 */

public class LikesActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    private Context mContext = LikesActivity.this;

    int ACTIVITY_NUM = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting Likes Activity");

        setUpBottomNavViewEx();

    }

    private void setUpBottomNavViewEx(){

        Log.d(TAG, "setUpBottomNavViewEx: Setting up the BottomNavViewEx for LikesActivity");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavView);
        BottomNavigationHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enablePagination(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }
}

package com.example.karat.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by karat on 23/01/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    private Context mContext = ProfileActivity.this;

    int ACTIVITY_NUM = 4;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: Starting Likes Activity");

        setUpBottomNavViewEx();
        setUpToolBar();

    }

    private void setUpToolBar(){

        Log.d(TAG, "setUpToolBar: Setting up the toolbar");

        Toolbar profileToolBar = findViewById(R.id.profileToolBar);
        setSupportActionBar(profileToolBar);

        ImageView accSettingsMenu = findViewById(R.id.imgView_profile_accSettingsMenu);
        accSettingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAccountSetting = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intentAccountSetting);

            }
        });

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

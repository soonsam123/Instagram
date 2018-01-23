package com.example.karat.instagram.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.karat.instagram.BottomNavigationHelper;
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

        profileToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.editProfile_id:
                        Toast.makeText(mContext, "Edit Profile", Toast.LENGTH_SHORT).show();
                        break;

                }

                return false;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_top_toolbar_profile, menu);

        return true;

    }
}

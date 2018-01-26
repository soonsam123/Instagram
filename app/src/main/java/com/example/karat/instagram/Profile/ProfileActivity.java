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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.GridImageAdapter;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

/**
 * Created by karat on 23/01/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    private Context mContext = ProfileActivity.this;

    private static final int ACTIVITY_NUM = 4;
    private static final int NUMBER_GRID_COLUMNS = 3;

    private ImageView profilePicture;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: Starting Likes Activity");

        setUpWidgets();
        setUpBottomNavViewEx();
        setUpToolBar();
        setProfilePicture();

        tempGridSetup();

    }

    private void tempGridSetup(){

        ArrayList<String> imgURLs = new ArrayList<>();

        imgURLs.add("http://www.anapolis.go.gov.br/portal/arquivos/noticias/karate%20(4)-20171030-145011.JPG");
        imgURLs.add("https://pbs.twimg.com/profile_images/943509882808012800/9vmFcmIu_400x400.jpg");
        imgURLs.add("http://www.unievangelica.edu.br/files/noticias/5507/2690_18.47.2016_109988_ef_20160504.jpg");
        imgURLs.add("http://www.unievangelica.edu.br/files/noticias/5507/2690_18.46.2016_109993_ef_20160504.jpg");
        imgURLs.add("http://www.institutogalileu.com.br/admin/downloads/imagens/1406120443.jpg");
        imgURLs.add("https://pbs.twimg.com/profile_images/847255203174686720/O2eFYaoq_400x400.jpg");
        imgURLs.add("http://www.leafsoon.com/img/CONSTRUCAO.png");

        setUpImageGrid(imgURLs);

    }

    private void setUpImageGrid(ArrayList<String> imgURLs){
        GridView gridView = findViewById(R.id.gridView_profilePictures);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imgWidth = gridWidth/NUMBER_GRID_COLUMNS;

        gridView.setColumnWidth(imgWidth);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(mContext, R.layout.layout_grid_image, "", imgURLs);
        gridView.setAdapter(gridImageAdapter);
    }

    private void setUpWidgets(){

        profilePicture = findViewById(R.id.imgView_profilePicture);
        mProgressBar = findViewById(R.id.progressBar_profile);
        mProgressBar.setVisibility(View.GONE);

    }

    private void setProfilePicture(){

        String imgURL = "pbs.twimg.com/profile_images/943509882808012800/9vmFcmIu_400x400.jpg";
        UniversalImageLoader.setImage(imgURL, profilePicture, mProgressBar, "https://");

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

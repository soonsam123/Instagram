package com.example.karat.instagram.Share;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.Permissions;
import com.example.karat.instagram.Utils.SectionsPageAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by karat on 23/01/2018.
 */

public class ShareActivity extends AppCompatActivity {

    private static final String TAG = "ShareActivity";

    private Context mContext = ShareActivity.this;

    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.d(TAG, "onCreate: Starting Share Activity");

        if (checkPermissionsArray(Permissions.PERMISSIONS)){
            // Do our stuff because permission is already granted
        } else {
            verifyPermissions(Permissions.PERMISSIONS);
        }


    }


    private void setUpViewPager(){

        mViewPager = findViewById(R.id.viewPagerContainer);
        TabLayout bottomTabLayout = findViewById(R.id.bottomTabLayout_activityShare);

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());

        /*adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager.setAdapter(adapter);
        bottomTabLayout.setupWithViewPager(mViewPager);

        bottomTabLayout.getTabAt(0).setText(R.string.gallery);
        bottomTabLayout.getTabAt(1).setText(R.string.photo);*/

    }

    public void verifyPermissions(String[] permissions){
        Log.i(TAG, "verifyPermissions: Verifying the permissions");

        // Request the user's permission.
        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );

    }


    public boolean checkPermissionsArray(String[] permissions){
        Log.i(TAG, "checkPermissionsArray: Checking an array of permissions");

        for (int i=0; i < permissions.length; i++){
            if (!checkPermissions(permissions[i])){
                return false;
            }
        }

        return true;

    }

    public boolean checkPermissions(String permission){
        Log.i(TAG, "checkPermissions: Checking for a single permission: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(mContext, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.i(TAG, "checkPermissions: Permission was not granted for: " + permission);
            return false;
        } else {
            Log.i(TAG, "checkPermissions: Permission was granted for: " + permission);
            return true;
        }

    }
}

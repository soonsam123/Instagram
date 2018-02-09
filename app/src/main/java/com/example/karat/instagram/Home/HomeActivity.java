package com.example.karat.instagram.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.karat.instagram.Login.LoginActivity;
import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.SectionsPageAdapter;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private Context mContext = HomeActivity.this;
    int ACTIVITY_NUM = 0;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Starting onCreate");

        setUpFirebaseAuth();

        initImageLoader();
        setUpBottomNavViewEx();
        setUpViewPager();




    }

    private void initImageLoader(){
        Log.d(TAG, "initImageLoader: Initializing the ImageLoader");

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    private void setUpViewPager(){

        Log.d(TAG, "setUpViewPager: Setting up the View Pager for Home Activity");

        ViewPager viewPagerContainer = findViewById(R.id.viewPagerContainer);

        SectionsPageAdapter sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        TabLayout topTabLayout = findViewById(R.id.topTabLayout);

        sectionsPageAdapter.addFragment(new CameraFragment());
        sectionsPageAdapter.addFragment(new HomeFragment());
        sectionsPageAdapter.addFragment(new MessageFragment());

        viewPagerContainer.setAdapter(sectionsPageAdapter);
        topTabLayout.setupWithViewPager(viewPagerContainer);


        if (topTabLayout.getTabCount() > 0 && topTabLayout.getTabAt(0) != null) {
            topTabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        }
        if (topTabLayout.getTabCount() > 1 && topTabLayout.getTabAt(1) != null) {
            topTabLayout.getTabAt(1).setIcon(R.drawable.ic_instagram_logo);
        }
        if (topTabLayout.getTabCount() > 2 && topTabLayout.getTabAt(2) != null) {
            topTabLayout.getTabAt(2).setIcon(R.drawable.ic_message);
        }
    }

    /*==============Bottom Navigation Properties==============*/
    private void setUpBottomNavViewEx(){

        Log.d(TAG, "setUpBottomNavViewEx: Setting up the BottomNavViewEx");

        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavView);
        BottomNavigationHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enablePagination(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }


    /*===================================== Firebase =====================================*/

    private void checkCurrentUser(FirebaseUser user){

        // Go to the login screen if there is no user logged in.
        if (user == null){
            Intent intentLogin = new Intent(mContext, LoginActivity.class);
            startActivity(intentLogin);
        }

    }

    private void setUpFirebaseAuth(){
        Log.i(TAG, "setUpFirebaseAuth: Setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

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

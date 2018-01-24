package com.example.karat.instagram.Home;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.karat.instagram.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    private Context mContext = HomeActivity.this;
    int ACTIVITY_NUM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: Starting onCreate");
        setUpBottomNavViewEx();
        setUpViewPager();

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

}

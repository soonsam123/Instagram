package com.example.karat.instagram.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.SectionsStatePagerAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

/**
 * Created by karat on 24/01/2018.
 */

public class AccountSettings extends AppCompatActivity {

    private static final String TAG = AppCompatActivity.class.getName();

    int ACTIVITY_NUM = 4;

    private Context mContext = AccountSettings.this;

    private SectionsStatePagerAdapter sectionsStatePagerAdapter;
    private RelativeLayout relLayout_container;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);

        Log.d(TAG, "onCreate: Starting Account Settings");

        // Assigning values to the variables.
        viewPager = findViewById(R.id.viewPagerContainer);
        relLayout_container = findViewById(R.id.relLayout_container_accSetings);

        setUpBottomNavViewEx();
        setUpToolBar();
        setUpFragments();
        setUpListView();

    }

    private void setUpFragments(){

        Log.d(TAG, "setUpFragments: Setting up the Fragments");

        sectionsStatePagerAdapter = new SectionsStatePagerAdapter(getSupportFragmentManager());

        sectionsStatePagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile));
        sectionsStatePagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out));

    }

    private void setViewPager(int fragmentNumber){

        Log.i(TAG, "setViewPager: Setting up the fragmentNumber#: " + fragmentNumber);
        relLayout_container.setVisibility(View.GONE);
        viewPager.setAdapter(sectionsStatePagerAdapter);
        viewPager.setCurrentItem(fragmentNumber);

    }

    private void setUpListView(){

        Log.d(TAG, "setUpListView: Setting up the listView");
        ListView listView = findViewById(R.id.listView_accountSettings);

        ArrayList<String> optionsAccountSettings = new ArrayList<>();
        optionsAccountSettings.add(getString(R.string.edit_profile));
        optionsAccountSettings.add(getString(R.string.sign_out));

        ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, optionsAccountSettings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                setViewPager(i);

            }
        });

    }

    private void setUpToolBar(){

        Toolbar accountSettingsToolBar = findViewById(R.id.toolBar_accountSettings);
        setSupportActionBar(accountSettingsToolBar);
        // Back Left Arrow appears.
        if (getSupportActionBar() != null) {getSupportActionBar().setDisplayHomeAsUpEnabled(true);}

    }

    // Back Left Arrow works.
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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

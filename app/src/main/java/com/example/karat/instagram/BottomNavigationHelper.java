package com.example.karat.instagram;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by karat on 23/01/2018.
 */

public class BottomNavigationHelper {

    // public void can not be accesed in the other activities. public static can
    public static void setUpBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){

        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.enableItemShiftingMode(false);

    }

    public static void enablePagination(final Context context, BottomNavigationViewEx bottomNavigationViewEx){

        bottomNavigationViewEx.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.ic_home_id: // Menu item 0
                        Intent intentHome = new Intent(context, HomeActivity.class);
                        context.startActivity(intentHome);
                        break;

                    case R.id.ic_search_id: // Menu item 1
                        Intent intentSearch = new Intent(context, SearchActivity.class);
                        context.startActivity(intentSearch);
                        break;

                    case R.id.ic_plus_id: // Menu item 2
                        Intent intentShare = new Intent(context, ShareActivity.class);
                        context.startActivity(intentShare);
                        break;

                    case R.id.ic_heart_id: // Menu item 3
                        Intent intentLikes = new Intent(context, LikesActivity.class);
                        context.startActivity(intentLikes);
                        break;

                    case R.id.ic_avatar_id: // Menu item 4
                        Intent intentProfile = new Intent(context, ProfileActivity.class);
                        context.startActivity(intentProfile);
                        break;

                }

                return false;
            }
        });

    }

}

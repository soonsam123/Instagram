package com.example.karat.instagram.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/*=============This adapter put the fragments into a list and the display in the viewPager=============*/
public class SectionsPageAdapter extends FragmentPagerAdapter {

    private static final String TAG = FragmentPagerAdapter.class.getName();

    // declaring fragmentListHome as static mess up the things.
    // Static variable can be acessed from others activity without instantiating.
    // SectionsPageAdapter.staticVariable = ...
    private final List<Fragment> fragmentListHome = new ArrayList<>();

    public SectionsPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentListHome.get(position);
    }

    @Override
    public int getCount() {
        return fragmentListHome.size();
    }

    public void addFragment(Fragment fragment){
        Log.d(TAG, "addFragment: Adding the Fragments to the PagerAdapter");
        fragmentListHome.add(fragment);

    }


    public void deleteFragment(int position){

        fragmentListHome.remove(position);

    }

    public void deleteAllFragments(){

        if (fragmentListHome.size() != 0) {
            for (int i = 0; i < fragmentListHome.size(); i++) {

                fragmentListHome.remove(i);

            }
        }

    }

}

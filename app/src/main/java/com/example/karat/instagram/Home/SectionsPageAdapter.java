package com.example.karat.instagram.Home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by karat on 23/01/2018.
 */

public class SectionsPageAdapter extends FragmentPagerAdapter {


    private static List<Fragment> fragmentListHome = new ArrayList<>();

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

        fragmentListHome.add(fragment);

    }

}

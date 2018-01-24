package com.example.karat.instagram.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by karat on 24/01/2018.
 */

public class SectionsStatePagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentsList = new ArrayList<>();
    private final HashMap<Fragment, Integer> fragmentIntegerHashMap = new HashMap<>();
    private final HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
    private final HashMap<Integer, String> integerStringHashMap = new HashMap<>();

    public SectionsStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentsList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentsList.size();
    }

    /**
     * Add the fragment, string and integers to the List and HashMaps.
     * @param fragment
     * @param fragmentName
     */

    public void addFragment(Fragment fragment, String fragmentName){

        fragmentsList.add(fragment);
        fragmentIntegerHashMap.put(fragment, fragmentsList.size() - 1);
        stringIntegerHashMap.put(fragmentName, fragmentsList.size() - 1);
        integerStringHashMap.put(fragmentsList.size() - 1, fragmentName);

    }

    /**
     * Get the number by passing the fragment.
     * @param fragment
     * @return
     */
    public Integer getFragmentNumber(Fragment fragment){

        if (fragmentIntegerHashMap.containsKey(fragment)){
            return fragmentIntegerHashMap.get(fragment);
        }else {
            return null;
        }

    }

    /**
     * Get the number by passing the fragmentName
     * @param fragmentName
     * @return
     */

    public Integer getFragmentNumber(String fragmentName){

        if (stringIntegerHashMap.containsKey(fragmentName)){
            return stringIntegerHashMap.get(fragmentName);
        } else {
            return null;
        }

    }


    /**
     * Get the name by passing the position.
     * @param position
     * @return
     */
    public String getFragmentName(int fragmentNumber){

        if (integerStringHashMap.containsKey(fragmentNumber)){
            return integerStringHashMap.get(fragmentNumber);
        }else {
            return null;
        }

    }

}

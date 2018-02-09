package com.example.karat.instagram.Home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.instagram.R;

/*===============Fragment for Home when you type Instagram Logo===============*/
public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";


    // Inflates the layout for Home.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);

    }
}

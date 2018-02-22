package com.example.karat.instagram.Share;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.instagram.R;

/**
 * Created by karat on 21/02/2018.
 */

public class GalleryFragment extends Fragment {

    private static final String TAG = "GalleryFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting this fragment");

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        return view;

    }


}

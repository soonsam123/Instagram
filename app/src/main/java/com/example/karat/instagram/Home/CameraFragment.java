package com.example.karat.instagram.Home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.instagram.R;

/*===============Fragment for Camera when you type Camera Icon===============*/
public class CameraFragment extends Fragment {

    private static final String TAG = "CameraFragment";

    // Inflates the layout for Camera.
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_camera, container, false);

    }
}

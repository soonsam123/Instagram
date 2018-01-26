package com.example.karat.instagram.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.UniversalImageLoader;


public class EditProfileFragment extends Fragment {


    private static final String TAG = Fragment.class.getName();

    private ImageView mProfilePhoto;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Initializing the fragment");
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        mProfilePhoto = view.findViewById(R.id.imgView_editProfile_profilePicture);
        toolbar = view.findViewById(R.id.toolBar_editProfile);

        setUpToolbar();
        setProfileImage();

        return view;
    }


    private void setProfileImage(){
        Log.d(TAG, "setProfileImage: Setting up the profile image");

        String imgURL = "pbs.twimg.com/profile_images/943509882808012800/9vmFcmIu_400x400.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");

    }

    private void setUpToolbar(){
        Log.d(TAG, "setUpToolbar: Setting up the toolbar");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
            ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

}

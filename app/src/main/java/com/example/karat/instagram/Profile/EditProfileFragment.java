package com.example.karat.instagram.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;


public class EditProfileFragment extends Fragment {


    private static final String TAG = Fragment.class.getName();

    private ImageView mProfilePhoto;
    private Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        mProfilePhoto = view.findViewById(R.id.imgView_editProfile_profilePicture);
        toolbar = view.findViewById(R.id.toolBar_editProfile);

        initImageLoader();
        setProfileImage();

        return view;
    }


    private void initImageLoader(){

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(getActivity());
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    private void setProfileImage(){

        String imgURL = "i.pinimg.com/736x/c6/a4/64/c6a4645d9f9af45a9c9d7b094c18a47a--portrait-ideas-girl-photos.jpg";
        UniversalImageLoader.setImage(imgURL, mProfilePhoto, null, "https://");

    }



}

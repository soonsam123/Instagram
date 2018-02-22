package com.example.karat.instagram.Share;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.Permissions;

/**
 * Created by karat on 21/02/2018.
 */

public class PhotoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "PhotoFragment";

    private AppCompatButton btnOpenCamera;

    private Context mContext;

    private static final int CAMERA_REQUEST_CODE = 5;
    private static final int GALLERY_FRAGMENT_NUM = 0;
    private static final int PHOTO_FRAGMENT_NUM = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting this fragment");

        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        setUpWidgets(view);

        btnOpenCamera.setOnClickListener(this);

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser){
            if (((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                if (((ShareActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                    Log.i(TAG, "onCreateView: FRAGMENT NUMBER: " + ((ShareActivity) getActivity()).getCurrentTabNumber());

                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
                }else {
                    Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
                    intentShareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentShareActivity);
                }
            }
        }
    }

    /**
     * Setting up the widgets to the layout values.
     * @param view
     */
    private void setUpWidgets(View view){

        btnOpenCamera = view.findViewById(R.id.btnOpenCamera_fragmentPhoto);

        mContext = getActivity();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5){
            Log.i(TAG, "onActivityResult: Done taking the picture");
            Log.i(TAG, "onActivityResult: Attenpting to navigate tho share screen");
        }

    }





    /**
     * Open the camera when the user press the button.
     * In case it did not started the camera when the fragment started.
     * @param view
     */
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btnOpenCamera_fragmentPhoto){

            if (((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM) {
                if (((ShareActivity)getActivity()).checkPermissions(Permissions.CAMERA_PERMISSION[0])) {
                    Log.i(TAG, "onCreateView: FRAGMENT NUMBER: " + ((ShareActivity) getActivity()).getCurrentTabNumber());

                    Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
                }else {
                    Intent intentShareActivity = new Intent(getActivity(), ShareActivity.class);
                    intentShareActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intentShareActivity);
                }
            }

        }

    }
}

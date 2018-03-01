package com.example.karat.instagram.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.FilePaths;
import com.example.karat.instagram.Utils.FileSearch;
import com.example.karat.instagram.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by karat on 21/02/2018.
 */

public class GalleryFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "GalleryFragment";
    private static final int NUM_GRID_COLUMNS = 3;


    private GridView gridView;
    private ImageView previewImageView;
    private ProgressBar mProgressBar;
    private ImageView closeImageView;
    private Spinner mSpinner;
    private TextView nextTextView;

    private ArrayList<String> directories;
    private ArrayList<String> directoriesNames;

    private String mAppend = "file:/";
    private String mSelectedImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: Starting this fragment");

        View view = inflater.inflate(R.layout.fragment_gallery, container, false);

        setUpWidgets(view);
        setClickListeners();
        init();

        return view;

    }


    /**
     * Set up the spinner to the path values.
     */
    private void init(){

        FilePaths filePaths = new FilePaths();

        if (FileSearch.getDirectoryPaths(filePaths.PICTURES) != null){
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }


        // There is only files in /camera.
        directories.add(filePaths.CAMERA);


        ArrayList<String> directoriesNames = new ArrayList<String>();

        for (int i = 0; i < directories.size(); i++){

            int index = directories.get(i).lastIndexOf("/") + 1;
            String name = directories.get(i).substring(index);
            directoriesNames.add(name);

        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, directoriesNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemSelected: selected: " + directories.get(i));

                // setup our image grid for the directory chosen.
                setUpGridView(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    /**
     * Setting up the Grid View to the directory chosen.
     * @param selectedDirectory
     */
    private void setUpGridView(String selectedDirectory){
        Log.i(TAG, "setUpGridView: Setting up the gridView for: " + selectedDirectory);

        final ArrayList<String> imgUrls = FileSearch.getFilePaths(selectedDirectory);

        // set the grid column width.
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        // set up the gridView.
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_image, mAppend, imgUrls);
        gridView.setAdapter(adapter);

        // set the ImageBOX to be the first image.
        setImage(imgUrls.get(0), previewImageView, mAppend);
        mSelectedImage = imgUrls.get(0);

        // set the ImageBOX to be the image you clicked.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                setImage(imgUrls.get(i), previewImageView, mAppend);
                mSelectedImage = imgUrls.get(i);
            }
        });

    }

    /**
     * Setting an image to some ImageView widget.
     * @param imgURL
     * @param image
     * @param append
     */
    private void setImage(String imgURL, ImageView image, String append){
        Log.i(TAG, "setImage: Setting the image to the previewBOX: " + imgURL);

        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });

    }


    /**
     * Settting up the widgets to the layout values.
     * @param view
     */
    private void setUpWidgets(View view){
        Log.i(TAG, "setUpWidgets: Setting up the widgets to the layout values");

        gridView = view.findViewById(R.id.gridView_galleryFragment);
        previewImageView = view.findViewById(R.id.imgView_preview_galleryFragment);
        closeImageView = view.findViewById(R.id.imgView_close_galleryFragment);
        mProgressBar = view.findViewById(R.id.progressBar_galleryFragment);
        mSpinner = view.findViewById(R.id.spinner_galleryFragment);
        nextTextView = view.findViewById(R.id.textViewNext_galleryFragment);

        directories = new ArrayList<String>();
        directoriesNames = new ArrayList<String>();

        mProgressBar.setVisibility(View.GONE);

    }


    /**
     * Set all the click listeners.
     */
    private void setClickListeners(){
        Log.i(TAG, "setClickListeners: Setting up the click listeners");
        
        closeImageView.setOnClickListener(this);
        nextTextView.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: Something was clicked");
        
        if (view.getId() == R.id.imgView_close_galleryFragment){

            // Close the activity.
            getActivity().finish();

        } else if (view.getId() == R.id.textViewNext_galleryFragment){

            // Go to the add description screen.
            Intent intentNextActivity = new Intent(getActivity(), NextActivity.class);
            intentNextActivity.putExtra(getString(R.string.selected_image), mSelectedImage);
            startActivity(intentNextActivity);

        }

    }
}

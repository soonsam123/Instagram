package com.example.karat.instagram.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class NextActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "NextActivity";

    private ImageView imgViewPreview;
    private ImageView imgViewBackArrow;
    private EditText mEditText;
    private TextView mShare;
    private ProgressBar mProgressBar;

    private Intent intent;

    private String mAppend = "file:/";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next);
        Log.i(TAG, "onCreate: Starting the activity");

        setUpWidgets();
        setClickListeners();
        setImage();

    }

    /**
     * Setting the image that was selected to be displayed in the imgViewPreview.
     */
    private void setImage(){
        Log.i(TAG, "setImage: Setting the image");

        UniversalImageLoader.setImage(intent.getStringExtra(getString(R.string.selected_image)), imgViewPreview, mProgressBar, mAppend);

    }

    /**
     * Setting up the widgets to the layout values.
     */
    private void setUpWidgets(){
        Log.i(TAG, "setUpWidgets: Setting up the widgets");
        
        imgViewPreview = findViewById(R.id.imgView_preview_activityNext);
        imgViewBackArrow = findViewById(R.id.imgView_backArrow_activityNext);
        mEditText = findViewById(R.id.editText_description_activityNext);
        mShare = findViewById(R.id.textViewShare_activityNext);
        mProgressBar = findViewById(R.id.progressBar_activityNext);

        intent = getIntent();

    }


    /**
     * Setting the click listeners.
     */
    private void setClickListeners(){
        Log.i(TAG, "setClickListeners: Setting the click listeners");
        
        imgViewBackArrow.setOnClickListener(this);
        mShare.setOnClickListener(this);

    }

    /**
     * Clicked somewhere.
     * @param view
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: The user clicked somewhere in the screen");
        
        switch (view.getId()){
            case R.id.imgView_backArrow_activityNext:
                finish();
                break;
            case R.id.textViewShare_activityNext:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                // Upload the image to the Firebase Database and show in the profile page.
        }

    }
}

package com.example.karat.instagram.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.karat.instagram.Login.LoginActivity;
import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.BottomNavigationHelper;
import com.example.karat.instagram.Utils.FirebaseMethods;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.example.karat.instagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by karat on 07/02/2018.
 */

public class ProfileFragment extends Fragment{


    private static final String TAG = "ProfileFragment";
    private TextView mPosts, mFollowers, mFollowing, mEditProfile, mName, mDescription, mWebsite, mUsername;
    private CircleImageView mProfilePhoto;
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private Context mContext;
    private Toolbar profileToolBar;
    private ImageView accSettingsMenu;

    private static final int ACTIVITY_NUM = 4;


    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        setUpFirebaseAuth();

        setUpWidgets(view);
        setUpBottomNavViewEx();
        setUpToolBar();
        setUpClickListeners();

        return view;
    }


    /**
     * Setting up the Widgets Values for the variables.
     * @param view
     */
    private void setUpWidgets(View view){

        mPosts = view.findViewById(R.id.textView_postsNumber);
        mFollowers = view.findViewById(R.id.textView_followersNumbers);
        mFollowing = view.findViewById(R.id.textView_followingNumber);
        mEditProfile = view.findViewById(R.id.textView_editProfile);
        mName = view.findViewById(R.id.textView_name);
        mDescription = view.findViewById(R.id.textView_description);
        mWebsite = view.findViewById(R.id.textView_website);
        mUsername = view.findViewById(R.id.textView_username_toolbar);

        mProfilePhoto = view.findViewById(R.id.imgView_profilePicture);
        accSettingsMenu = view.findViewById(R.id.imgView_profile_accSettingsMenu);

        mGridView = view.findViewById(R.id.gridView_profilePictures);

        mProgressBar = view.findViewById(R.id.progressBar_profile);

        bottomNavigationViewEx = view.findViewById(R.id.bottomNavView);

        profileToolBar = view.findViewById(R.id.profileToolBar);

        mContext = getActivity();
        firebaseMethods = new FirebaseMethods(mContext);

    }

    /**
     * Setting up the types of Click Listeners.
     */
    private void setUpClickListeners(){
        Log.i(TAG, "setUpClickListeners: Setting up the Click Listeners");

        mEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAccountSettings = new Intent(mContext, AccountSettingsActivity.class);
                intentAccountSettings.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intentAccountSettings);

            }
        });

    }

    /**
     * Assigning values to the widgets with the real database values.
     * @param userSettings
     */
    private void setUpProfileWidgetsWithDBValues(UserSettings userSettings){

        Log.i(TAG, "setUpProfileWidgetsWithDBValues: Setting Up the firebase Database Values: " + userSettings.toString());

        mPosts.setText(String.valueOf(userSettings.getSettings().getPosts()));
        mFollowers.setText(String.valueOf(userSettings.getSettings().getFollowers()));
        mFollowing.setText(String.valueOf(userSettings.getSettings().getFollowing()));

        mName.setText(userSettings.getSettings().getDisplay_name());
        mDescription.setText(userSettings.getSettings().getDescription());
        mWebsite.setText(userSettings.getSettings().getWebsite());
        mUsername.setText(userSettings.getSettings().getUsername());

        UniversalImageLoader.setImage(userSettings.getSettings().getProfile_photo(), mProfilePhoto, mProgressBar, "");

        mProgressBar.setVisibility(View.GONE);

    }


    private void setUpToolBar(){

        Log.d(TAG, "setUpToolBar: Setting up the toolbar");

        ((ProfileActivity)getActivity()).setSupportActionBar(profileToolBar);

        accSettingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentAccountSetting = new Intent(mContext, AccountSettingsActivity.class);
                startActivity(intentAccountSetting);

            }
        });

    }


    private void setUpBottomNavViewEx(){

        Log.d(TAG, "setUpBottomNavViewEx: Setting up the BottomNavViewEx for LikesActivity");
        BottomNavigationHelper.setUpBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enablePagination(mContext, bottomNavigationViewEx);

        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }


    /*===================================== Firebase =====================================*/

     private void setUpFirebaseAuth(){
        Log.i(TAG, "setUpFirebaseAuth: Setting up firebase auth");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null){
                    // User is logged in
                    Log.i(TAG, "onAuthStateChanged: User logged in" + user.getUid());

                } else {
                    // User is logged out
                    Log.i(TAG, "onAuthStateChanged: User logged out");
                }
            }
        };

         myRef.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 // Retrieve the user information as a UserSettings Object.
                 Log.i(TAG, "onDataChange: Retrieving information from Database");
                 setUpProfileWidgetsWithDBValues(firebaseMethods.getUserSettings(dataSnapshot));


             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
    }


    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


}



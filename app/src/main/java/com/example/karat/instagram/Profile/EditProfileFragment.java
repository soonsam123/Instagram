package com.example.karat.instagram.Profile;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.instagram.R;
import com.example.karat.instagram.Utils.FirebaseMethods;
import com.example.karat.instagram.Utils.StringManipulation;
import com.example.karat.instagram.Utils.UniversalImageLoader;
import com.example.karat.instagram.models.User;
import com.example.karat.instagram.models.UserSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment implements View.OnClickListener {


    private static final String TAG = "EditProfileFragment";

    private TextView mChangePhoto;
    private EditText mUsername, mName, mWebsite, mDescription, mEmail, mPhoneNumber;
    private CircleImageView mProfilePhoto;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private Context mContext;
    private RelativeLayout relLayout;
    private ImageView mBackArrow, mCheck;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods firebaseMethods;

    // User
    private UserSettings current_user_settings;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Initializing the fragment");
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        setUpFirebaseAuth();
        setUpToolbar();
        setUpWidgets(view);
        setUpClickListeners();

        return view;
    }


    /**
     * Assigning the database real time values to the variables.
     * @param settings
     */
    private void setUpEditProfileWidgetsWithDBValue(UserSettings settings){
        Log.i(TAG, "setUpEditProfileWidgetsWithDBValue: Asssigning the real time database values to the variables: " + settings.toString());

        current_user_settings = settings;

        UniversalImageLoader.setImage(settings.getSettings().getProfile_photo(), mProfilePhoto, mProgressBar, "");

        mUsername.setText(settings.getSettings().getUsername());
        mName.setText(settings.getSettings().getDisplay_name());
        mWebsite.setText(settings.getSettings().getWebsite());
        mDescription.setText(settings.getSettings().getDescription());
        mEmail.setText(settings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(settings.getUser().getPhone_number()));

        mProgressBar.setVisibility(View.GONE);

    }

    /**
     * Setting up the widgets reference from the layout editProfile Fragment.
     * @param view
     */
    private void setUpWidgets(View view){
        Log.i(TAG, "setUpWidgets: Setting up the widgets references from the layout");

        toolbar = view.findViewById(R.id.toolBar_editProfile);
        mProfilePhoto = view.findViewById(R.id.imgView_editProfile_profilePicture);
        mUsername = view.findViewById(R.id.editText_username_editProfile);
        mName = view.findViewById(R.id.editText_name_editProfile);
        mWebsite = view.findViewById(R.id.editText_website_editProfile);
        mDescription = view.findViewById(R.id.editText_description_editProfile);
        mEmail = view.findViewById(R.id.editText_email_editProfile);
        mPhoneNumber = view.findViewById(R.id.editText_phoneNumber_editProfile);

        mBackArrow = view.findViewById(R.id.imgView_backArrow_editProfile);
        mCheck = view.findViewById(R.id.imgView_check_editProfile);

        mProgressBar = view.findViewById(R.id.progressBar_editProfile);

        relLayout = view.findViewById(R.id.relLayout_container_centerEditProfile);

        mContext = getActivity();

        firebaseMethods = new FirebaseMethods(mContext);

    }

    private void saveProfileChanges(){
        Log.i(TAG, "saveProfileChanges: Saving the profile changes");

        // Getting the texts from the fields.
        final String username = StringManipulation.condenseUsername(mUsername.getText().toString());
        final String display_name = mName.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phone_number = Long.parseLong(mPhoneNumber.getText().toString());

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // The user changed the username for something. Check if it is available.
                if (!current_user_settings.getSettings().getUsername().equals(username)){
                    Log.i(TAG, "onDataChange: Checking if this username already exists " + username);

                    checkIfUsernameExists(username);

                } else {
                    // The user did not change the username
                    Log.i(TAG, "onDataChange: The user did not try to change the username");
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void checkIfUsernameExists(final String username){
        Log.i(TAG, "checkIfUsernameExists: Checking if the " + username +" already exists.");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        // This query will receive the username the user is trying to change.
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // There is no such username like this one in the database.
                if (!dataSnapshot.exists()){
                    // add the username

                    firebaseMethods.updateUsername(username);

                    Toast.makeText(mContext, "saved username", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot singleSnapshot: dataSnapshot.getChildren()){

                    // There is a username like this one in the database. D
                    if (singleSnapshot.exists()){
                        // do not change the username.
                        Log.i(TAG, "onDataChange: FOUND A MATCH: " + singleSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(mContext, "That username already exists", Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    /**
     * Setting up the Toolbar as the oficial toolbar for this part of the app and putting the back button at the top left.
     */
    private void setUpToolbar(){
        Log.i(TAG, "setUpToolbar: Setting up the toolbar");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

    }

    /**
     * Setting up the types of Click Listeners in the fragment.
     */
    private void setUpClickListeners(){
        Log.i(TAG, "setUpClickListeners: Setting up the click listeners");
        
        relLayout.setOnClickListener(this);
        mProfilePhoto.setOnClickListener(this);
        mBackArrow.setOnClickListener(this);
        mCheck.setOnClickListener(this);

    }

    /**
     * When you click somewhere.
     * @param view
     */
    @Override
    public void onClick(View view) {
        Log.i(TAG, "onClick: The user is clicking somewhere");

        // Hide the keyboard when the user click in the Layout or in the Profile Picture.
        if (view.getId() == R.id.relLayout_container_centerEditProfile || view.getId() == R.id.imgView_editProfile_profilePicture) {
            Log.i(TAG, "onClick: Clicking on the layout to hide the keyboard");
            hideKeyBoard();
        } else if (view.getId() == R.id.imgView_backArrow_editProfile){
            Log.i(TAG, "onClick: Clicking on the BackArrow edit_profile Button");
            getActivity().finish();
        } else if (view.getId() == R.id.imgView_check_editProfile){
            Log.i(TAG, "onClick: Clicking on the Check edit_profile Button");
            saveProfileChanges();
        }
    }

    /**
     * Method to hide the Keyboard from the window. This is how you hide the keyboard from inside a Fragment.
     */

    private void hideKeyBoard(){

        if (this.getView() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputMethodManager != null) {
                inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }

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

                //Retrieving the data from the database
                setUpEditProfileWidgetsWithDBValue(firebaseMethods.getUserSettings(dataSnapshot));

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

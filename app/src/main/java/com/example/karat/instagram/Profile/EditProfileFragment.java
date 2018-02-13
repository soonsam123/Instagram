package com.example.karat.instagram.Profile;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
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
import com.example.karat.instagram.dialogs.ConfirmPasswordDialog;
import com.example.karat.instagram.models.User;
import com.example.karat.instagram.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfileFragment extends Fragment implements View.OnClickListener, ConfirmPasswordDialog.OnConfirmPasswordListener {

    private static final String TAG = "EditProfileFragment";

    private TextView mChangePhoto;
    private EditText mUsername, mName, mWebsite, mDescription, mEmail, mPhoneNumber;
    private CircleImageView mProfilePhoto;
    private Toolbar toolbar;
    private ProgressBar mProgressBar;
    private Context mContext;
    private ImageView mBackArrow, mCheck;

    private RelativeLayout relLayout;
    private CoordinatorLayout coordinatorLayout;

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
     *
     * @param settings
     */
    private void setUpEditProfileWidgetsWithDBValue(UserSettings settings) {
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
     *
     * @param view
     */
    private void setUpWidgets(View view) {
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
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout_editProfile);

        mContext = getActivity();

        firebaseMethods = new FirebaseMethods(mContext);

    }

    private void saveProfileChanges() {
        Log.i(TAG, "saveProfileChanges: Saving the profile changes");

        // Getting the texts from the fields.
        final String username = StringManipulation.condenseUsername(mUsername.getText().toString());
        final String display_name = mName.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phone_number = Long.parseLong(mPhoneNumber.getText().toString());


        // The user changed the USERNAME. Check if it is available.
        if (!current_user_settings.getSettings().getUsername().equals(username)) {
            Log.i(TAG, "onDataChange: Checking if this username already exists " + username);

            checkIfUsernameExists(username);

        } else {
            // The user DID NOT change the USERNAME
            Log.i(TAG, "onDataChange: The user did not try to change the username");
        }


        // The user changed the DISPLAY NAME.
        if (!current_user_settings.getSettings().getDisplay_name().equals(display_name)) {
            firebaseMethods.updateDisplayName(display_name);
        }

        // The user changed the WEBSITE.
        if (!current_user_settings.getSettings().getWebsite().equals(website)) {
            firebaseMethods.updateWebsite(website);
        }

        // The user changed the DESCRIPTION.
        if (!current_user_settings.getSettings().getDescription().equals(description)) {
            firebaseMethods.updateDescription(description);
        }

        // The user change the EMAIL.
        if (!current_user_settings.getUser().getEmail().equals(email)) {
            // Change the email.

            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
            // When the dialog closes it takes back to EditProfileFragment again, otherwise it would go to MainActivity.
            dialog.setTargetFragment(EditProfileFragment.this, 1);

        }

        // The user changed the PHONE NUMBER.
        if (!String.valueOf(current_user_settings.getUser().getPhone_number()).equals(String.valueOf(phone_number))) {
            firebaseMethods.updatePhoneNumber(phone_number);
        }


    }


    /*.**************************************** CONFIRM METHODS ****************************************/

    @Override
    public void onConfirmPassword(String password) {
        //============WARNING: Never prints the password in the logs in a release app.============//
        // I am doing this only with the purpose of debugging.
        Log.i(TAG, "onConfirmPassword: Password: " + password);

        /*.********************** 1º STEP - Re-authenticate the email **********************/

        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);

        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG, "onComplete: Reauthenticated");

                            /*.********************** 2º STEP - Check if there is another equal email **********************/
                            mAuth.fetchProvidersForEmail(mEmail.getText().toString())
                                    .addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {

                                            if (task.isSuccessful()){
                                                try {
                                                    if (task.getResult().getProviders().size() == 1) {
                                                        Log.i(TAG, "onComplete: There is another email equal this one");
                                                        Toast.makeText(mContext, "This email is already in use.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Log.i(TAG, "onComplete: No email in use as this one. ");


                                                        /*.********************** 3º STEP - Finally, updates the email **********************/
                                                        mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Log.i(TAG, "onComplete: User email address updated");
                                                                            // Updates the email in the firebase database also.
                                                                            firebaseMethods.updateEmail(mEmail.getText().toString());
                                                                            Toast.makeText(mContext, "Your email was updated. "
                                                                                    , Toast.LENGTH_SHORT).show();
                                                                            getActivity().finish();
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                } catch (NullPointerException e){
                                                    Log.i(TAG, "onComplete: NullPointerException: " + e.getMessage());
                                                }

                                            }

                                        }
                                    });

                        } else {
                            Log.i(TAG, "onComplete: Failed to reauthenticate email");
                            // Wrong password or the user does not have a password.
                            Toast.makeText(mContext, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    /*.**************************************** CHECK METHODS ****************************************/

    /**
     * Check if the username
     * @param username already exists. If not, updated it.
     */
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
                    Log.i(TAG, "onDataChange: Saving new username");
                    firebaseMethods.updateUsername(username);

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

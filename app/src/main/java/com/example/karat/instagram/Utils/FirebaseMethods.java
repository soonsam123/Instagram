package com.example.karat.instagram.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.karat.instagram.Home.HomeActivity;
import com.example.karat.instagram.Login.LoginActivity;
import com.example.karat.instagram.Login.RegisterActivity;
import com.example.karat.instagram.R;
import com.example.karat.instagram.models.User;
import com.example.karat.instagram.models.UserAccountSettings;
import com.example.karat.instagram.models.UserSettings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.ContentValues.TAG;

/**
 * Created by karat on 29/01/2018.
 */

public class FirebaseMethods {

    private static final String TAG = "FirebaseMethods";
    
    private Context mContext;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String userID;

    // Getting the variables values.
    public FirebaseMethods(Context context) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mContext = context;
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }


    /**
     * Method to register a new Email for the user in the Firebase server.
     * @param email
     * @param username
     * @param password
     * @param progressBar
     * @param intent
     */
    public void RegisterNewEmail(String email, String username, String password, final ProgressBar progressBar){

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            // Creates the user and get the ID.
                            FirebaseUser user = mAuth.getCurrentUser();
                            userID = mAuth.getCurrentUser().getUid();

                            // Send the verification Email and logout.
                            sendVerificationEmail();

                            Intent intentLogin = new Intent(mContext, LoginActivity.class);
                            mContext.startActivity(intentLogin);
                            // mAuth.signOut();

                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(mContext, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                        progressBar.setVisibility(View.GONE);
                        // ...
                    }
                });
    }


    public void sendVerificationEmail(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null){

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){

                                // This one is working.
                                Toast.makeText(mContext, "Please verify your account in your email inbox", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(mContext, "Couldn't send email verification", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        } else {

        }

    }


    /**
     * Method to signin the user in the Firebase server.
     * @param email
     * @param password
     * @param progressBar
     * @param intent
     */
    public void SignInWithEmail(String email, String password, final ProgressBar progressBar, final Intent intent){

        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                            // Making sure the user is logged in.
                            if (user != null) {
                                try {

                                    // SignIn only if the email is verified. Otherwise signOut.
                                    if (user.isEmailVerified()) {

                                        Log.i(TAG, "onComplete: User logged in" + user.getUid() + " " + user.getEmail());

                                        Toast.makeText(mContext, "Authentication Successful", Toast.LENGTH_SHORT).show();

                                        mContext.startActivity(intent);

                                    } else {

                                        mAuth.signOut();

                                        /* Showing the warning to check the email in a SnackBar.
                                        final Snackbar snackbar = Snackbar.make(view, "Please go to your inbox email " +
                                                "\n and verify your account", Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("OK", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                snackbar.dismiss();
                                            }
                                        });*/


                                        Toast.makeText(mContext, "Please verify your email inbox", Toast.LENGTH_LONG).show();

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }


                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(mContext, "Authentication failed.", Toast.LENGTH_SHORT).show();

                            // updateUI(null);
                        }

                        progressBar.setVisibility(View.GONE);

                        // ...
                    }
                });


    }



    /*************************************** ADD *************************************/

    /**
     * Add the new User's information to the database, for the child user_account_settings
     * and for the child users.
     * @param email
     * @param username
     * @param description
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String description, String website, String profile_photo){

        Log.i(TAG, "addNewUser: Adding database infor for userID: " + userID);
        User user = new User(
                email,
                1,
                userID,
                StringManipulation.condenseUsername(username));

        // Database: instagram-d0a2d --> users --> cgOhxf95dbZiXR8xOIPxdRFJC7G2 --> settingValues.
        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                description,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website);

        // Database: instagram-d0a2d --> user_account_settings --> cgOhxf95dbZiXR8xOIPxdRFJC7G2 --> settingValues.
        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }


    /*************************************** UPDATES *************************************/

    /**
     * This method updates the current description to
     * @param description
     */
    public void updateDescription(String description){
        Log.i(TAG, "updateDescription: Updating description to: " + description);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_description))
                .setValue(description);

    }


    /**
     * This method updates the current Display Name to
     * @param display_name
     */
    public void updateDisplayName(String display_name){
        Log.i(TAG, "updateDisplayName: Updating Display Name to: " + display_name);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_display_name))
                .setValue(display_name);

    }

    /**
     * This method updates the followers long number to
     * @param followers
     */
    public void updateFollowers(long followers){
        Log.i(TAG, "updateFollowers: Updating followers to: " + String.valueOf(followers));

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_followers))
                .setValue(followers);

    }

    /**
     * This method updates the following long number to
     * @param following
     */
    public void updateFollowing(long following){
        Log.i(TAG, "updateFollowing: Updating following to: " + String.valueOf(following));

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_following))
                .setValue(following);

    }


    /**
     * This method updates the posts long number to
     * @param posts
     */
    public void updatePosts(long posts){
        Log.i(TAG, "updatePosts: Updating the posts to: " + String.valueOf(posts));

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_posts))
                .setValue(posts);

    }


    /**
     * This method updates the profile_photo to
     * @param profile_photo
     */
    public void updateProfilePhoto(String profile_photo){
        Log.i(TAG, "updateProfilePhoto: Updating the profile photo to: " + profile_photo);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_profile_photo))
                .setValue(profile_photo);

    }


    /**
     * This method update the current username to
     * @param username
     */
    public void updateUsername(String username) {
        Log.i(TAG, "updateUsername: Updating the username to: " + username);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_username))
                .setValue(username);

    }

    /**
     * This method updates the website to
     * @param website
     */
    public void updateWebsite(String website){
        Log.i(TAG, "updateWebsite: Updating the website to: " + website);

        myRef.child(mContext.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContext.getString(R.string.field_website))
                .setValue(website);

    }

    /**
     * This method updates the email to
     * @param email
     */
    public void updateEmail(String email){
        Log.i(TAG, "updateEmail: Updating the email to: " + email);

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_email))
                .setValue(email);

    }

    /**
     * This method updates the phone number to
     * @param phone_number
     */
    public void updatePhoneNumber(long phone_number){
        Log.i(TAG, "updatePhoneNumber: Updating the phone number to: " + String.valueOf(phone_number));

        myRef.child(mContext.getString(R.string.dbname_users))
                .child(userID)
                .child(mContext.getString(R.string.field_phone_number))
                .setValue(phone_number);

    }


    /*************************************** GETTERS AND SETTERS *************************************/
    /**
     * Get all the user database information.
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot){
        Log.i(TAG, "getUserSettings: Getting all the user settings db information");

        User user = new User();
        UserAccountSettings settings = new UserAccountSettings();

        for (DataSnapshot ds: dataSnapshot.getChildren()){


            if (ds.getKey().equals(mContext.getString(R.string.dbname_user_account_settings))){
                Log.i(TAG, "getUserSettings: getting User_Settings information" + ds);

                try {

                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setDisplay_name(
                            StringManipulation.expandUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name())
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );

                } catch (NullPointerException e){e.printStackTrace();}


            }

            if (ds.getKey().equals(mContext.getString(R.string.dbname_users))){
                Log.i(TAG, "getUserSettings: Getting User information" + ds);

                try {

                    user.setEmail(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getEmail()
                    );
                    user.setPhone_number(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getPhone_number()
                    );
                    user.setUser_id(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUser_id()
                    );
                    user.setUsername(
                            ds.child(userID)
                                    .getValue(User.class)
                                    .getUsername()
                    );

                } catch (NullPointerException e){e.printStackTrace();}

            }

        }


        return new UserSettings(user, settings);

    }

}

package com.example.karat.instagram.Utils;


import android.Manifest;

/**
 * Created by karat on 13/02/2018.
 */

// Permissions.PERMISSIONS and you can access the array of permissions available.

public class Permissions {

    public static final String[] PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.CAMERA
    };


    public static final String[] WRITE_EXTERNAL_STORAGE_PERMISSIONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };


    public static final String[] READ_EXTERNAL_STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

}

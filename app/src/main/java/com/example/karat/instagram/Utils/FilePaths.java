package com.example.karat.instagram.Utils;

import android.os.Environment;

/**
 * Created by karat on 28/02/2018.
 */

public class FilePaths {

    // "storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    // "storage/emulated/0/Pictures"
    public String PICTURES = ROOT_DIR + "/Pictures";
    // "storage/emulated/0/DCIM/Camera"
    public String CAMERA = ROOT_DIR + "/DCIM/camera";

}

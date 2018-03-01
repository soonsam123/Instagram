package com.example.karat.instagram.Utils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by karat on 28/02/2018.
 */

public class FileSearch {

    /**
     * Returns a list of the paths of the directories.
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){

        ArrayList<String> pathArray = new ArrayList<String>();

        // E.G: directory = "storage/emulate/0/Pictures"
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Returns a list of the paths of the files.
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){

        ArrayList<String> pathArray = new ArrayList<String>();

        File file = new File(directory);
        File[] listfiles = file.listFiles();

        for (int i = 0; i < listfiles.length; i++){
            if (listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}

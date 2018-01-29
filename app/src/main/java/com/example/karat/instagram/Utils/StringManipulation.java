package com.example.karat.instagram.Utils;

/**
 * Created by karat on 29/01/2018.
 */

public class StringManipulation {


    // soonsam.ribeiro --> soonsam ribeiro
    public static String expandUsername(String username){
        return username.replace(".", " ");
    }


    // soonsam ribeiro --> soonsam.ribeiro
    public static String condenseUsername(String username){
        return username.replace(" ", ".");
    }

}

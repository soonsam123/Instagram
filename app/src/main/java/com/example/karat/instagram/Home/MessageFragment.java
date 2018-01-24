package com.example.karat.instagram.Home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.karat.instagram.R;

/*===============Fragment for Messages when you type Message Icon===============*/
public class MessageFragment extends Fragment{

    private static final String TAG = Fragment.class.getName();


    // Inflates the layout for Message.
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_message, container, false);

    }
}

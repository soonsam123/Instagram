package com.example.karat.instagram.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.karat.instagram.R;

/**
 * Created by karat on 10/02/2018.
 */

public class ConfirmPasswordDialog extends DialogFragment {
    private static final String TAG = "ConfirmPasswordDialog";

    private AppCompatButton mCancel, mConfirm;
    private EditText mPassword;

    private Context mContext;

    OnConfirmPasswordListener mOnConfirmPasswordListener;

    // This interface has the purpose to pass the password to EditProfileFragment.
    public interface OnConfirmPasswordListener{
        public void onConfirmPassword(String password);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);

        setUpWidgets(view);
        
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Clicking in the confirm button");
                
                String password = mPassword.getText().toString();
                if (!password.equals("")){
                    mOnConfirmPasswordListener.onConfirmPassword(password);
                } else {
                    Toast.makeText(mContext, "You must confirm your password.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        return view;
    }

    /**
     * Setting up the widgets with the layout references.
     * @param view
     */
    private void setUpWidgets(View view){
        Log.i(TAG, "setUpWidgets: Setting up the widgets");
        mCancel = view.findViewById(R.id.btnCancel_confirmPassword);
        mConfirm = view.findViewById(R.id.btnConfirm_confirmPassword);
        mPassword = view.findViewById(R.id.editText_password_confirmPasswordDialog);
        mContext = getActivity();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            mOnConfirmPasswordListener = (OnConfirmPasswordListener) getTargetFragment();
        }catch (ClassCastException e){Log.i(TAG, "onAttach: ClassCastException: " + e.getMessage());}

    }
}

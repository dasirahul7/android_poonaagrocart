package com.poona.agrocart.ui.verify_otp;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.poona.agrocart.R;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.custom_otp_edit_text.CustomOtpEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VerifyOtpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VerifyOtpFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //layout components
    private CustomButton btnVerifyOtp;
    private CustomOtpEditText cetOtp;



    public VerifyOtpFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static VerifyOtpFragment newInstance(String param1, String param2) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void findLayoutComponents(View view)
    {
        btnVerifyOtp=view.findViewById(R.id.cbtn_verify_otp);
        cetOtp=view.findViewById(R.id.cet_otp);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_verify_otp, container, false);

        findLayoutComponents(view);
        initializeVars();

        //showing action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        btnVerifyOtp.setOnClickListener(v -> {

        });






        return view;
    }

    private void initializeVars()
    {

        //to show otp keyboard
        InputMethodManager imgr = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imgr.showSoftInput(cetOtp, 0);
        imgr.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        cetOtp.requestFocus();


    }
}
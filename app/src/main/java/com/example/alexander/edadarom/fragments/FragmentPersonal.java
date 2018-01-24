package com.example.alexander.edadarom.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.Authorization.LoginActivity;
import com.example.alexander.edadarom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentPersonal extends Fragment {

    private static final int STATE_AUTH = 1;
    private static final int STATE_NOT_AUTH = 2;

    public static final int REQUEST_CODE = 20;

    private View view;
    private TextView tvToolbarTitle;
    private TextView tvSignInOut;
    private CircleImageView ivToolbarProfile;

    private ConstraintLayout clSignInOut;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        mAuth = FirebaseAuth.getInstance();
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        ivToolbarProfile = view.findViewById(R.id.profile_image);
        setStatusBarTranslucent(true);
        tvSignInOut = view.findViewById(R.id.tvSignInOut);
        clSignInOut = view.findViewById(R.id.clSignInOut);

        checkAuth();
        btnClickListeners();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void checkAuth() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            updateUI(STATE_AUTH);
        } else {
            updateUI(STATE_NOT_AUTH);
        }
    }

    private void updateUI () {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            updateUI(STATE_AUTH);
        } else updateUI(STATE_NOT_AUTH);
    }

    private void updateUI(int state) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        switch (state) {
            case STATE_AUTH:
                tvSignInOut.setText(R.string.title_personal_sign_out);
                tvToolbarTitle.setText(firebaseUser.getDisplayName());
                if (firebaseUser.getPhotoUrl() != null)
                    Picasso.with(getContext()).load(firebaseUser.getPhotoUrl().toString()).placeholder(R.mipmap.ic_launcher).into(ivToolbarProfile);
                break;
            case STATE_NOT_AUTH:
                tvSignInOut.setText(R.string.title_personal_sign_in);
                tvToolbarTitle.setText("Вы не авторизованы");
                Picasso.with(getContext()).load(R.mipmap.ic_launcher).into(ivToolbarProfile);
                break;
        }

    }

    private void btnClickListeners() {
        clSignInOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (firebaseUser == null) {
                    startActivityForResult(new Intent(getActivity(), LoginActivity.class), REQUEST_CODE);
                } else {
                    mAuth.signOut();
                    updateUI(STATE_NOT_AUTH);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK) {
            updateUI(STATE_AUTH);
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}

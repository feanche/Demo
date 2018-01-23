package com.example.alexander.edadarom.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentPersonal extends Fragment {

    private View view;
    private TextView tvToolbarTitle;
    private ImageView ivToolbarProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);

        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        ivToolbarProfile = view.findViewById(R.id.ivToolbar);
        setStatusBarTranslucent(true);

        checkAuth();
        return view;
    }

    private void checkAuth() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            tvToolbarTitle.setText(firebaseUser.getDisplayName());
            if(firebaseUser.getPhotoUrl()!=null) Picasso.with(getContext()).load(firebaseUser.getPhotoUrl().toString()).into(ivToolbarProfile);
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

package com.nuttertools.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nuttertools.Authorization.ProfileCreateActivity;
import com.nuttertools.MyAds.MyAdsActivity;
import com.nuttertools.Notifications.NotificationsActivity;
import com.nuttertools.R;
import com.nuttertools.UserAddressesActivity.AddressesActivity;
import com.nuttertools.models.Users;
import com.nuttertools.utils.GlideApp;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Alexander on 10.01.2018.
 */

public class FragmentPersonal extends Fragment {

    private static final int STATE_AUTH = 1;
    private static final int STATE_NOT_AUTH = 2;

    public static final int REQUEST_CODE = 20;
    private static final int RC_SIGN_IN = 123;

    private View view;
    private TextView tvToolbarTitle, tvToolbarSubtitle;
    private TextView tvSignInOut, tv3;
    private CircleImageView ivToolbarProfile;
    ImageView iv7;

    private ConstraintLayout clSignInOut, clEditProfile, clNotifications, clAddresses, clMyAds;
    private FirebaseAuth mAuth;
    private GoogleApiClient googleApiClient;

    CheckUserCallback checkUserCallback;

    interface CheckUserCallback {
        void onSuccess(Users user);
        void onUserDoNotCreate();
    }

    Users user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        mAuth = FirebaseAuth.getInstance();
        tvToolbarTitle = view.findViewById(R.id.tvToolbarTitle);
        tvToolbarSubtitle = view.findViewById(R.id.tvToolbarSubtitle);
        ivToolbarProfile = view.findViewById(R.id.profile_image);
        setStatusBarTranslucent(true);
        tvSignInOut = view.findViewById(R.id.tvSignInOut);
        clSignInOut = view.findViewById(R.id.clSignInOut);
        clEditProfile = view.findViewById(R.id.clProfile);
        clNotifications = view.findViewById(R.id.clNotifications);
        clAddresses = view.findViewById(R.id.clAddresses);
        clMyAds = view.findViewById(R.id.clMyAds);
        iv7 = view.findViewById(R.id.iv7);

        tv3 = view.findViewById(R.id.tv3);

        updateUI();
        btnClickListeners();
        getUserNotifications();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateUI();
    }

    private void updateUI () {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            updateUI(STATE_AUTH);
        } else {
            updateUI(STATE_NOT_AUTH);
        }
    }

    private void addressButtonActive() {
        clAddresses.setClickable(true);
        iv7.setColorFilter(getResources().getColor(R.color.black));
    }

    private void addressButtonNonActive() {
        clAddresses.setClickable(false);
        iv7.setColorFilter(getResources().getColor(R.color.secondaryText));
    }

    private void updateUI(int state) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        switch (state) {
            case STATE_AUTH:
                checkUserInFirestore(new CheckUserCallback() {
                    @Override
                    public void onSuccess(Users _user) {
                        user = _user;
                    }

                    @Override
                    public void onUserDoNotCreate() {
                    }
                });

                tvSignInOut.setText(R.string.title_personal_sign_out);
                tvToolbarTitle.setText(firebaseUser.getDisplayName());
                if (firebaseUser.getPhotoUrl() != null)
                    GlideApp.with(getContext())
                            .load(firebaseUser.getPhotoUrl().toString())
                            .placeholder(R.mipmap.ic_launcher)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .into(ivToolbarProfile);

                addressButtonActive();
                break;
            case STATE_NOT_AUTH:
                tvSignInOut.setText(R.string.title_personal_sign_in);
                tvToolbarTitle.setText(R.string.tv_not_authorization);
                GlideApp.with(getContext())
                        .load(R.mipmap.ic_launcher)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(ivToolbarProfile);

                addressButtonNonActive();
                break;
        }
    }

    private void btnClickListeners() {
        clSignInOut.setOnClickListener(v -> {
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser == null) {

                List<AuthUI.IdpConfig> providers = Arrays.asList(
                        new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setTheme(R.style.AppTheme)
                                .build(),
                        RC_SIGN_IN);

            } else {
                AuthUI.getInstance()
                        .signOut(getActivity())
                        .addOnCompleteListener(task -> updateUI(STATE_NOT_AUTH));
            }
        });

        clEditProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), ProfileCreateActivity.class)));

        clNotifications.setOnClickListener(v -> startActivity(new Intent(getContext(), NotificationsActivity.class)));

        clAddresses.setOnClickListener(v -> startActivity(new Intent(getContext(), AddressesActivity.class)));

        clMyAds.setOnClickListener(v -> startActivity(new Intent(getContext(), MyAdsActivity.class)));
    }

    private void checkUserInFirestore(CheckUserCallback checkUserCallback) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(firebaseUser.getUid())
                    .get()
                    .addOnCompleteListener(snapshotTask -> {
                        if (snapshotTask.isSuccessful()) {
                            if (!snapshotTask.getResult().exists()) {
                                checkUserCallback.onUserDoNotCreate();
                            } else {
                                user = snapshotTask.getResult().toObject(Users.class);
                                checkUserCallback.onSuccess(user);
                            }

                        }
                    });
        } else checkUserCallback.onUserDoNotCreate();
    }

    private void getUserNotifications() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(firebaseUser.getUid()).collection("notifications")
                    .addSnapshotListener((documentSnapshots, e) -> tv3.setText("Уведомления " + documentSnapshots.getDocuments().size()));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_OK) {
            updateUI(STATE_AUTH);
        }
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                updateUI();
                checkUserInFirestore(new CheckUserCallback() {
                    @Override
                    public void onSuccess(Users user) {

                    }

                    @Override
                    public void onUserDoNotCreate() {
                        startActivity(new Intent(getContext(), ProfileCreateActivity.class));
                    }
                });
            } else {
            }
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
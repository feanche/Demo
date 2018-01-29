package com.example.alexander.edadarom.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alexander.edadarom.Authorization.LoginActivity;
import com.example.alexander.edadarom.Authorization.PhoneAuthActivity;
import com.example.alexander.edadarom.Authorization.ProfileCreateActivity;
import com.example.alexander.edadarom.Notifications.NotificationsActivity;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.Users;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

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

    private ConstraintLayout clSignInOut, clEditProfile, clNotifications;
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
        } else updateUI(STATE_NOT_AUTH);
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
                    Picasso.with(getContext()).load(firebaseUser.getPhotoUrl().toString()).placeholder(R.mipmap.ic_launcher).into(ivToolbarProfile);
                break;
            case STATE_NOT_AUTH:
                tvSignInOut.setText(R.string.title_personal_sign_in);
                tvToolbarTitle.setText("Вы не авторизованы");
                //tvToolbarSubtitle.setText("");
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

                    // Choose authentication providers
                    List<AuthUI.IdpConfig> providers = Arrays.asList(
                            new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                            new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

// Create and launch sign-in intent
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
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    updateUI(STATE_NOT_AUTH);
                                }
                            });
                }
            }
        });

        clEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), ProfileCreateActivity.class));
            }
        });

        clNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), NotificationsActivity.class));
            }
        });
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
                                //Создание  пользователя после аутентификации
                                //startActivity(new Intent(getContext(), ProfileCreateActivity.class));
                                checkUserCallback.onUserDoNotCreate();
                            } else {
                                user = snapshotTask.getResult().toObject(Users.class);
                                checkUserCallback.onSuccess(user);
                                //tvToolbarSubtitle.setText(user.getPhoneNumber());
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
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                            tv3.setText("Уведомления " + documentSnapshots.getDocuments().size());
                        }
                    });
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
                // Successfully signed in
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
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
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

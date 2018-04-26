package com.nuttertools.Authorization;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.nuttertools.R;
import com.nuttertools.fragments.FragmentPersonal;
import com.nuttertools.models.Users;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.GlideApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class ProfileCreateActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextInputEditText edName, edSubName, edPhone, edEmail;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ConstraintLayout cl;
    private ProgressBar progressBar;

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.title_profile_edit));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        cl = findViewById(R.id.cl);
        cl.setVisibility(View.INVISIBLE);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        ivProfile = findViewById(R.id.ivProfile);
        edName = findViewById(R.id.edName);
        edSubName = findViewById(R.id.edSubName);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);

        db = FirebaseFirestore.getInstance();

        updateUI();
    }

    private void updateUI() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            checkUserInFirestore(firebaseUser);
        }
    }

    private void checkUserInFirestore (FirebaseUser firebaseUser) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.USERS).document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        if (snapshotTask.getResult().exists()) {
                            Users user = snapshotTask.getResult().toObject(Users.class);
                            edName.setText(user.getFirstName() == null ? (firebaseUser.getDisplayName()) : user.getFirstName());
                            edPhone.setText(user.getPhoneNumber() == null ? firebaseUser.getPhoneNumber() : user.getPhoneNumber());
                            edEmail.setText(user.getEmail() == null ? firebaseUser.getEmail() : user.getEmail());
                            if(user.getPhoto()!=null)
                                GlideApp.with(getApplicationContext())
                                        .load(user.getPhoto())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(ivProfile);
                            else if(firebaseUser.getPhotoUrl()!=null)
                                GlideApp.with(getApplicationContext())
                                        .load(firebaseUser.getPhotoUrl().toString())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(ivProfile);
                        } else {
                            edName.setText(firebaseUser.getDisplayName());
                            edPhone.setText(firebaseUser.getPhoneNumber());
                            edEmail.setText(firebaseUser.getEmail());
                            if(firebaseUser.getPhotoUrl()!=null)
                                GlideApp.with(getApplicationContext())
                                        .load(firebaseUser.getPhotoUrl().toString())
                                        .transition(DrawableTransitionOptions.withCrossFade())
                                        .into(ivProfile);
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        cl.setVisibility(View.VISIBLE);
                    }
                });
    }

    public void updateUserInfo (View view) {
        progressBar.setVisibility(View.VISIBLE);

        if(edName.getText().toString().isEmpty()) {
            edName.setError(getString(R.string.et_should_enter_name));
            return;
        }

        if(edPhone.getText().toString().isEmpty()) {
            edPhone.setError(getString(R.string.et_should_enter_phone));
            return;
        }

        FirebaseMessaging.getInstance().subscribeToTopic("notification_" + firebaseUser.getUid());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edName.getText() + " " + edSubName.getText())
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Users user = new Users(firebaseUser.getUid(), edName.getText().toString(), edEmail.getText().toString(), edPhone.getText().toString());
                        if(firebaseUser.getPhotoUrl()!=null) user.setPhoto(firebaseUser.getPhotoUrl().toString());
                        db.collection(FirebaseConst.USERS).document(firebaseUser.getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(getApplicationContext(), getString(R.string.toast_success_profile_edit), Toast.LENGTH_LONG).show();
                                    setResult(FragmentPersonal.REQUEST_CODE);
                                    progressBar.setVisibility(View.INVISIBLE);
                                    finish();
                                });
                    }
                });
    }
}
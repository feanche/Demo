package com.example.alexander.edadarom.Authorization;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.alexander.edadarom.MyFirebaseInstanceIDService;
import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.FragmentPersonal;
import com.example.alexander.edadarom.models.Users;
import com.example.alexander.edadarom.utils.FirebaseMethods;
import com.example.alexander.edadarom.utils.GlideApp;
import com.firebase.ui.auth.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;

public class ProfileCreateActivity extends AppCompatActivity {

    private static final String TAG = "ProfileCreateActivity";
    private ImageView ivProfile;
    private TextInputEditText edName, edSubName, edPhone, edEmail;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore db;
    private ConstraintLayout cl;
    private ProgressBar progressBar;


    //Toolbar back button click
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
        getSupportActionBar().setTitle("Редактирование");
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
        db.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        //Если пользователь есть в бд
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
            edName.setError("Вы должны ввести имя");
            return;
        }

        if(edPhone.getText().toString().isEmpty()) {
            edPhone.setError("Вы должны ввести номер");
            return;
        }

        FirebaseMessaging.getInstance().subscribeToTopic("notification_" + firebaseUser.getUid());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edName.getText() + " " + edSubName.getText())
                .build();

        //Обновляем профиль FireBase
        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ProfileCreateActivity", "User profile updated.");

                            //создаем/обновляем профиль в Firestore
                            Users user = new Users(firebaseUser.getUid(), edName.getText().toString(), edEmail.getText().toString(), edPhone.getText().toString());
                            if(firebaseUser.getPhotoUrl()!=null) user.setPhoto(firebaseUser.getPhotoUrl().toString());
                            //user.setPustNotificationToken(FirebaseInstanceId.getInstance().getToken());
                            //FirebaseMethods.updateToken();
                            db.collection("users").document(firebaseUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.d(TAG, "Document snapshot setted");
                                            Toast.makeText(getApplicationContext(), "Профиль успешно изменен!", Toast.LENGTH_LONG).show();
                                            setResult(FragmentPersonal.REQUEST_CODE);
                                            progressBar.setVisibility(View.INVISIBLE);
                                            finish();
                                        }
                                    });
                        }
                    }
                });
    }
}

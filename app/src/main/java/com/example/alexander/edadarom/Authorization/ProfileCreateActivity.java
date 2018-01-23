package com.example.alexander.edadarom.Authorization;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alexander.edadarom.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.Picasso;

public class ProfileCreateActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private EditText edName, edSubName, edPhone, edEmail;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);

        edName = findViewById(R.id.edName);
        edSubName = findViewById(R.id.edSubName);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);

        checkAuth();
    }

    private void checkAuth() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            edName.setText(firebaseUser.getDisplayName());
            edSubName.setText(firebaseUser.getDisplayName());
            edPhone.setText(firebaseUser.getPhoneNumber());
            edEmail.setText(firebaseUser.getEmail());
            if(firebaseUser.getPhotoUrl()!=null) Picasso.with(getApplicationContext()).load(firebaseUser.getPhotoUrl().toString()).into(ivProfile);
        }
    }

    public void updateUserInfo (View view) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(edName.getText() + " " + edSubName.getText())
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ProfileCreateActivity", "User profile updated.");
                        }
                    }
                });
    }
}

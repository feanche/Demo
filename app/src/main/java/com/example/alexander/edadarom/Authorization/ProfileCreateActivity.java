package com.example.alexander.edadarom.Authorization;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.fragments.FragmentPersonal;
import com.example.alexander.edadarom.models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ProfileCreateActivity extends AppCompatActivity {

    private static final String TAG = "ProfileCreateActivity";
    private ImageView ivProfile;
    private TextInputEditText edName, edSubName, edPhone, edEmail;
    FirebaseUser firebaseUser;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_create);

        ivProfile = findViewById(R.id.ivProfile);

        edName = findViewById(R.id.edName);
        edSubName = findViewById(R.id.edSubName);
        edPhone = findViewById(R.id.edPhone);
        edEmail = findViewById(R.id.edEmail);

        db = FirebaseFirestore.getInstance();

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

        if(edName.getText().toString().isEmpty()) {
            edName.setError("Вы должны ввести имя");
            return;
        }

        if(edSubName.getText().toString().isEmpty()) {
            edSubName.setError("Вы должны ввести фамилию");
            return;
        }

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

        Users user = new Users(firebaseUser.getUid(),"", edName.getText().toString(), edSubName.getText().toString(), edEmail.getText().toString(), "", 0);

       db.collection("users").document(firebaseUser.getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Document snapshot setted");
                        Toast.makeText(getApplicationContext(), "Профиль успешно создан!", Toast.LENGTH_LONG).show();
                        setResult(FragmentPersonal.REQUEST_CODE);
                        finish();
                    }
                });





    }
}

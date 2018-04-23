package com.nuttertools.Authorization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nuttertools.R;
import com.nuttertools.fragments.FragmentPersonal;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static final int RequestSignInCode = 7;
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    Button SignOutButton;

    com.google.android.gms.common.SignInButton signInButton;

    TextView LoginUserName, LoginUserEmail;

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInButton = findViewById(R.id.sign_in_button);
        SignOutButton = findViewById(R.id.sign_out);
        btn = findViewById(R.id.btnPhoneAuth);
        LoginUserName = findViewById(R.id.textViewName);
        LoginUserEmail = findViewById(R.id.textViewEmail);
        signInButton = findViewById(R.id.sign_in_button);
        firebaseAuth = FirebaseAuth.getInstance();
        LoginUserEmail.setVisibility(View.GONE);
        LoginUserName.setVisibility(View.GONE);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestProfile()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, connectionResult -> {

                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton.setOnClickListener(view -> UserSignInMethod());

        SignOutButton.setOnClickListener(view -> UserSignOutFunction());

        btn.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, PhoneAuthActivity.class));
            finish();
        });
    }

    public void UserSignInMethod() {
        Intent authIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(authIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode) {
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()) {
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        Toast.makeText(getApplicationContext(), "" + authCredential.getProvider(), Toast.LENGTH_LONG).show();
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        checkUserInFirestore(firebaseUser);
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.toast_something_wrong), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void checkUserInFirestore(FirebaseUser firebaseUser) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(snapshotTask -> {
                    if (snapshotTask.isSuccessful()) {
                        if (!snapshotTask.getResult().exists()) {
                            startActivity(new Intent(LoginActivity.this, ProfileCreateActivity.class));
                            finish();
                        } else {
                            setResult(FragmentPersonal.REQUEST_CODE);
                            finish();
                        }

                    }
                });
    }

    public void UserSignOutFunction() {
        firebaseAuth.signOut();
        Auth.GoogleSignInApi.signOut(googleApiClient
        ).setResultCallback(status -> {
            Toast.makeText(getApplicationContext(), getString(R.string.toast_successfull_logout), Toast.LENGTH_LONG).show();
        });
        SignOutButton.setVisibility(View.GONE);
        LoginUserName.setText(null);
        LoginUserEmail.setText(null);
        signInButton.setVisibility(View.VISIBLE);
    }

}
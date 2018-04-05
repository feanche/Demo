package com.nuttertools;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by GabdrakhmanovII on 29.01.2018.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.setFirestoreSettings(settings);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) FirebaseMessaging.getInstance().subscribeToTopic("notification_" + firebaseUser.getUid());
    }
}

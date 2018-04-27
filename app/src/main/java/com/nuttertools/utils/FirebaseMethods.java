package com.nuttertools.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lAntimat on 28.01.2018.
 */

public class FirebaseMethods {

    public static void updateToken() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String token = FirebaseInstanceId.getInstance().getToken();
        Map<String, Object> pushTokens = new HashMap<>();
        pushTokens.put("token", token);
        if(FirebaseAuth.getInstance().getUid()!=null) {
            db.collection("pushTokens").document(FirebaseAuth.getInstance().getUid())
                    .set(pushTokens)
                    .addOnCompleteListener(task -> {

                    });
        }
    }

}

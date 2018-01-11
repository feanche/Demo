package com.example.alexander.edadarom.NewItem;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.alexander.edadarom.R;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Alexander on 10.01.2018.
 */

public class AddNewItemActivity extends AppCompatActivity {

    DatabaseReference mRootRef= FirebaseDatabase.getInstance().getReference();
    private EditText editText;
    private Button button;
    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_add);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button2);
        backButton = (ImageView) findViewById(R.id.iv_close);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRootRef.child("new").setValue(editText.getText().toString());
            }
        };

        button.setOnClickListener(onClickListener);

        View.OnClickListener backToMainActivity = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };

        backButton.setOnClickListener(backToMainActivity);


    }
}

package com.example.alexander.edadarom.NewItem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alexander.edadarom.R;

import com.example.alexander.edadarom.models.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Alexander on 10.01.2018.
 */

public class AddNewItemActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private EditText editText;
    private Button button;
    private ImageView backButton;
    Uri file;
    private ImageButton photoButton;
    Target target;

    final static String TAG = "myLogs_AddNewItem";
    private ImageView photoImage;
    private TextView photoTextHide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_add);
        Log.d(TAG,"onCreate");

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button2);
        backButton = (ImageView) findViewById(R.id.iv_close);
        photoButton = (ImageButton) findViewById(R.id.imageButton);
        photoImage = (ImageView) findViewById(R.id.imageView);
        photoTextHide = (TextView) findViewById(R.id.textView);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mRootRef.child("new").setValue(editText.getText().toString());

                String id = mRootRef.child("new").push().getKey();
                UserModel userModel = new UserModel(editText.getText().toString(), id);
                Map<String, Object> userValues = userModel.toMap();
                Map<String, Object> user = new HashMap<>();
                user.put(id, userValues);
                mRootRef.child("new").updateChildren(user);
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

        View.OnClickListener photoButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        };

        photoButton.setOnClickListener(photoButtonClickListener);

        View.OnClickListener photoImageClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        };

        photoImage.setOnClickListener(photoImageClickListener);
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 100);
    }


    private Uri getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if(!mediaStorageDir.exists()){
            if(!mediaStorageDir.mkdirs()){
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File path = new File(mediaStorageDir.getPath()+File.separator+"IMG_"+timeStamp+".jpg");
        Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".com.example.alexander.edadarom.provider",path);
        return photoUri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode ==100) {
            if (resultCode == RESULT_OK) {
                upload(file);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(Uri file) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                Log.d(TAG, "onBitmapLoaded");
                uploadImage(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d(TAG,"onBitmapFailed");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                Log.d(TAG,"onPrepareLoad");

            }
        };
        Picasso.with(getApplicationContext()).load(file).resize(1000,1000).into(target);
    }

    public void uploadImage(Bitmap bitmap) {

        StorageReference mountainsRef = mStorageRef.child("mountains.jpg");

        photoImage.setImageBitmap(bitmap);
        photoImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        photoImage.setVisibility(View.VISIBLE);
        photoButton.setVisibility(View.INVISIBLE);


        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();


        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
    }
}

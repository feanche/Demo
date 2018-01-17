package com.example.alexander.edadarom.NewItem;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.example.alexander.edadarom.R;
import com.example.alexander.edadarom.models.UserAdsModel;

/**
 * Created by Alexander on 10.01.2018.
 */

public class AddNewItemActivity extends AppCompatActivity {

    private EditText title, description, price;
    private Button button;
    private ImageView backButton;
    Uri file;
    private ImageButton photoButton1, photoButton2, photoButton3;
    Target target;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    Long currentTime;
    Long timeOfAction = 2592000000L; //millis, 30 дней - время до окончания действия объявления
    Long adEndTime;

    final static String TAG = "myLogs_AddNewItem";
    private TextView photoTextHide;
    private Uri photoUri, uploadPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_add);
        Log.d(TAG,"onCreate");

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        title = (EditText)findViewById(R.id.editText);
        description = (EditText)findViewById(R.id.editText2);
        price = (EditText)findViewById(R.id.editText3);

        button = (Button)findViewById(R.id.button2);
        backButton = (ImageView)findViewById(R.id.iv_close);

        photoButton1 = (ImageButton)findViewById(R.id.imageButton2);
        photoButton2 = (ImageButton)findViewById(R.id.imageButton1);
        photoButton3 = (ImageButton)findViewById(R.id.imageButton);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoToFirestore();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View.OnClickListener photoButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        };

        photoButton1.setOnClickListener(photoButtonClickListener);
        photoButton2.setOnClickListener(photoButtonClickListener);
        photoButton3.setOnClickListener(photoButtonClickListener);
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 100);
    }

    public void uploadPhotoToFirestore(){
        StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
        ref.putFile(photoUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Uploaded",Toast.LENGTH_SHORT).show();
                        uploadPhotoUrl = taskSnapshot.getDownloadUrl();
                        sendDataToFirestore();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });

    }

    public void getTimes(){
        currentTime = System.currentTimeMillis();
        adEndTime = currentTime + timeOfAction;
    }

    public void sendDataToFirestore(){
        getTimes();
        UserAdsModel userAdsModel = new UserAdsModel(
                description.getText().toString(),
                adEndTime,
                2342,
                Integer.parseInt(price.getText().toString()),
                currentTime,
                title.getText().toString(),
                "Столярный инструмент",
                uploadPhotoUrl.toString()
        );

        Map<String, Object> userValues = userAdsModel.toMap();

        db.collection("ads")
                .add(userValues)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "Document snapshot written by ID: "+documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Error adding document ", e);
                    }
                });
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
        photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".com.example.alexander.edadarom.provider",path);
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
            photoButton1.setImageBitmap(bitmap);
            photoButton1.setScaleType(ImageView.ScaleType.CENTER_CROP);
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

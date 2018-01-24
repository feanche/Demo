package com.example.alexander.edadarom.NewItem;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.example.alexander.edadarom.MapsActivity;
import com.example.alexander.edadarom.utils.ItemClickSupport;
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

public class AddNewItemActivity extends AppCompatActivity implements ImagesRecyclerAdapter.BtnClickListener{

    private EditText title, description, price;
    private Button button;
    private ImageView backButton, ivPhoto;
    Uri fileUri;
    private ImageButton photoButton1, photoButton2, photoButton3;
    Target target;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    Long currentTime;
    Long timeOfAction = 2592000000L; //millis, 30 дней - время до окончания действия объявления
    Long adEndTime;
    String locality;
    float locationLat, locationLon;
    boolean complete = true;
    public static final int GET_LOCATION = 1;
    public static final int GALLERY = 2;

    final static String TAG = "myLogs_AddNewItem";
    private TextView localityText;
    private Uri photoUri, uploadPhotoUrl;
    private byte[] photo;
    private ConstraintLayout locationButton;

    RecyclerView recyclerView;
    ArrayList<UploadImage> arUploadImages = new ArrayList<>();
    ImagesRecyclerAdapter imagesRecyclerAdapter;

    ArrayList<String> arReportUrl = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item_add);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        localityText = (TextView)findViewById(R.id.textView6);

        title = (EditText)findViewById(R.id.editText);
        description = (EditText)findViewById(R.id.editText2);
        price = (EditText)findViewById(R.id.editText3);

        button = (Button)findViewById(R.id.button2);
        backButton = (ImageView)findViewById(R.id.iv_close);

        locationButton = (ConstraintLayout)findViewById(R.id.constraintLayout1);

        ivPhoto = (ImageView)findViewById(R.id.ivPhoto);


        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivityForResult(intent, GET_LOCATION);
            }
        });

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

        initRecyclerView();
    }

    public void completenessCheck() {
        String mTitle = title.getText().toString();
        String mDescription = description.getText().toString();
        String mPrice = price.getText().toString();
        if(mTitle.matches("")) {
            title.setHintTextColor(getResources().getColor(R.color.red));
        }
        if(mDescription.matches("")) {
            description.setHintTextColor(getResources().getColor(R.color.red));
        }
        if(mPrice.matches("")) {
            price.setHintTextColor(getResources().getColor(R.color.red));
        }
        if(locationLat==0 && locationLon==0){
            localityText.setTextColor(getResources().getColor(R.color.red));
        }
        if(mTitle.matches("")|mDescription.matches("")|mPrice.matches("")|locationLat==0|locationLon==0){
            complete = false;
        } else {
            complete = true;
        }
    }

    public void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, 100);
    }

    public void uploadPhotoToFirestore() {
        completenessCheck();
        if(complete) {
            StorageReference ref = storageReference.child("images/"+UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putBytes(photo);

            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                            uploadPhotoUrl = taskSnapshot.getDownloadUrl();
                            arReportUrl.add(uploadPhotoUrl.toString());
                            sendDataToFirestore();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Ошибка загрузки изображения",Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
                        }
                    });
        }
        else {
            Toast.makeText(getApplicationContext(), "Заполните все поля помеченные красным!", Toast.LENGTH_SHORT).show();
        }
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
                locationLat,
                locationLon,
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
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    //String path = saveImage(bitmap);
                    //photoButton1.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG,"requestCode = 100");
                imagesRecyclerAdapter.add(fileUri);
                upload(fileUri);
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                locality = data.getExtras().getString(MapsActivity.LOCALITY);
                locationLat = data.getExtras().getFloat(MapsActivity.LOCATION_LAT);
                locationLon = data.getExtras().getFloat(MapsActivity.LOCATION_LON);
                localityText.setText(locality);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(Uri file) {
        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                uploadImage(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load(file).resize(1000,1000).into(target);
    }

    public void uploadImage(Bitmap bitmap) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
            final byte[] data = byteArrayOutputStream.toByteArray();
            if (data.length > 0) {
                photo = data;
            }
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
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        //pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {"Выбрать из галереи","Сделать фото на камеру"};
        pictureDialog.setItems(pictureDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallery();
                        break;
                    case 1:
                        takePicture();
                        break;
                }
            }
        });
        pictureDialog.show();
    }

    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        arUploadImages.add(new UploadImage(Uri.parse("uri"), false));
        imagesRecyclerAdapter = new ImagesRecyclerAdapter(getApplicationContext(), arUploadImages, this);
        recyclerView.setAdapter(imagesRecyclerAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {

            }
        });
    }

    private void btnListeners() {

    }

    @Override
    public void ivAddClick() {
        showPictureDialog();
    }

    @Override
    public void ivDelClick() {

    }


}

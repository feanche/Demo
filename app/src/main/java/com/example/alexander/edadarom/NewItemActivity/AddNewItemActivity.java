package com.example.alexander.edadarom.NewItemActivity;

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
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.alexander.edadarom.UserAddressesActivity.AddressesActivity;
import com.example.alexander.edadarom.fragments.Category.Category;
import com.example.alexander.edadarom.models.UserAdsModel;
import com.example.alexander.edadarom.utils.CreateDialog;
import com.example.alexander.edadarom.utils.FirebaseConst;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import com.example.alexander.edadarom.R;

/**
 * Created by Alexander on 10.01.2018.
 */

public class AddNewItemActivity extends AppCompatActivity implements ImagesRecyclerAdapter.BtnClickListener {

    UploadTask uploadTask;
    private TextInputLayout description, title, price;
    private CardView publishButton;
    private ImageView backButton, ivPhoto;
    Target target;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    String locality;
    float locationLat, locationLon;
    String commentToAddress;
    String priceType;
    boolean complete = true;
    public static final int GALLERY = 200;
    public static final int CAMERA = 300;
    final static String TAG = "myLogs_AddNewItem";
    private TextView localityText;
    private Uri localPhotoUri;
    private ConstraintLayout locationButton;
    Spinner spinner, priceTypeSpinner;
    ArrayList<Category> arCategories;
    RecyclerView recyclerView;
    ArrayList<UploadImage> arUploadImages = new ArrayList<>();
    private int categoryId;
    Uri fileUri;
    Uri downloadUrl;
    ArrayList<String> arReportUrl = new ArrayList<>();
    ImagesRecyclerAdapter imagesRecyclerAdapter;
    CategoriesAdapter categoriesAdapter;
    private MaterialDialog dialog;

    public static final int TEXT_REQUEST = 400;
    public static final String EXTRA_MESSAGE = "com.example.alexander.edadarom.EXTRA_MESSAGE";

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arCategories = new ArrayList<>();
        setContentView(R.layout.activity_new_item_add);

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingToolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.activity_my_ad_name));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        localityText = findViewById(R.id.textView6);
        title = findViewById(R.id.textInputLayout6);
        description = findViewById(R.id.textInputLayout5);
        price = findViewById(R.id.textInputLayout7);
        publishButton = findViewById(R.id.button2);
        locationButton = findViewById(R.id.constraintLayout1);
        ivPhoto = findViewById(R.id.ivPhoto);
        spinner = findViewById(R.id.SpinnerCustom);
        priceTypeSpinner = findViewById(R.id.spinnerCustomPrice);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        categoriesAdapter = new CategoriesAdapter(this,android.R.layout.simple_spinner_item, arCategories);
        spinner.setAdapter(categoriesAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spinner.getSelectedItem()!=null){
                    categoryId = arCategories.get(position).getCategoryId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        priceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                priceType = priceTypeSpinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localityText.setTextColor(getResources().getColor(R.color.secondaryText));
                Intent intent = new Intent(getApplicationContext(), AddressesActivity.class);
                intent.putExtra(EXTRA_MESSAGE, "shit");
                startActivityForResult(intent, TEXT_REQUEST);
            }
        });

        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder yesOrNoDialog = new AlertDialog.Builder(AddNewItemActivity.this);
                yesOrNoDialog.setTitle("Подтверждаете размещение объявления?");
                yesOrNoDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        completenessCheck();
                        if (!complete) {
                            Toast.makeText(getApplicationContext(), "Заполните все поля", Toast.LENGTH_SHORT).show();
                        } else if (complete && arReportUrl.isEmpty()) {
                            Toast.makeText(getApplicationContext(), "Добавьте хотя бы одно изображение", Toast.LENGTH_SHORT).show();
                        } else if (complete && !arReportUrl.isEmpty()){
                            sendDataToFirestore();
                            finish();
                        }
                    }
                });

                yesOrNoDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Тебе что-то не понравилось", Toast.LENGTH_SHORT).show();
                    }
                });
                yesOrNoDialog.show();
            }
        });
        initRecyclerView();
        getData();
    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.CATEGORIES)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() == 0) {
                                return;
                            }
                            for (DocumentSnapshot document : task.getResult()) {
                                Category category = document.toObject(Category.class);
                                arCategories.add(category);
                                categoriesAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    public void completenessCheck() {
        String mTitle = title.getEditText().toString();
        String mDescription = description.getEditText().toString();
        String mPrice = price.getEditText().toString();
        if (mTitle.matches("") |
                mDescription.matches("") |
                mPrice.matches("") |
                locationLat == 0 |
                locationLon == 0) {
            complete = false;
        } else {
            complete = true;
        }
    }

    public void takePicture() {
        dialog = CreateDialog.createPleaseWaitDialog(AddNewItemActivity.this);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                fileUri = contentURI;
                imagesRecyclerAdapter.add(fileUri);
                upload(fileUri);
            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                imagesRecyclerAdapter.add(fileUri);
                upload(fileUri);
            } else {
            }
            dialog.dismiss();
        } else if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                locationLat = data.getExtras().getFloat(AddressesActivity.EXTRA_LAT);
                locationLon = data.getExtras().getFloat(AddressesActivity.EXTRA_LON);
                commentToAddress = data.getExtras().getString(AddressesActivity.EXTRA_COMMENT);
                locality = data.getExtras().getString(AddressesActivity.EXTRA_LOCALITY);
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
        Picasso.with(getApplicationContext()).load(file).resize(1000, 1000).centerInside().into(target);
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        final byte[] data = byteArrayOutputStream.toByteArray();
        if (data.length > 0) {
            uploadImageToStorage(data);
        }
    }

    public void uploadImageToStorage(byte[] data) {
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Ошибка загрузки изображения",Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                imagesRecyclerAdapter.setProgress(arReportUrl.size(), (int) progress);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRecyclerAdapter.setIsLoaded(arReportUrl.size(), true);
                downloadUrl = taskSnapshot.getDownloadUrl();
                arReportUrl.add(downloadUrl.toString());
                Toast.makeText(getApplicationContext(),"Изображение добавлено",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Uri getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File path = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        localPhotoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".com.example.alexander.edadarom.provider", path);
        return localPhotoUri;
    }

    public void sendDataToFirestore() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        UserAdsModel userAdsModel = new UserAdsModel(
                locationLat,
                locationLon,
                Integer.parseInt(price.getEditText().getText().toString()),
                categoryId,
                title.getEditText().getText().toString(),
                arReportUrl,
                description.getEditText().getText().toString(),
                new Date(),
                commentToAddress,
                priceType);
        if (firebaseUser != null) userAdsModel.setUserId(firebaseUser.getUid());
        db.collection(FirebaseConst.ADS)
                .add(userAdsModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getApplicationContext(),"Объявление успешно добавлено",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Ошибка загрузки данных",Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        String[] pictureDialogItems = {"Выбрать из галереи", "Сделать фото на камеру"};
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        arUploadImages.add(new UploadImage(Uri.parse("uri"), false));
        imagesRecyclerAdapter = new ImagesRecyclerAdapter(getApplicationContext(), arUploadImages, this);
        recyclerView.setAdapter(imagesRecyclerAdapter);
    }

    @Override
    public void ivAddClick() {
        showPictureDialog();
    }

    @Override
    public void ivDelClick(int position) {
        createListDialog(position);
    }

    public void deleteCurrentPhoto(int position) {
        StorageReference photoRef = storage.getReferenceFromUrl(arReportUrl.get(position).toString());
        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                arUploadImages.remove(position);
                arReportUrl.remove(position);
                imagesRecyclerAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Ошибка удаления изображения",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteAllPhoto() {
        for(int i=0; i<arReportUrl.size(); i++) {
            StorageReference photoRef = storage.getReferenceFromUrl(arReportUrl.get(i).toString());
            photoRef.delete();
        }
        finish();
    }

    public MaterialDialog createListDialog(int position) {
        return new MaterialDialog.Builder(this)
                .items(R.array.dialog_image_activity)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                deleteCurrentPhoto(position);
                                break;
                        }
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {
        exitAddActivityConfirmation();
    }

    private void exitAddActivityConfirmation() {
        AlertDialog.Builder yesOrNoDialog = new AlertDialog.Builder(AddNewItemActivity.this);
        yesOrNoDialog.setTitle("Действительно хотите выйти?");
        yesOrNoDialog.setMessage("После выхода вся информация будет утеряна");
        yesOrNoDialog
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAllPhoto();
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        yesOrNoDialog.show();
    }
}

package com.nuttertools.NewItemActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TextInputEditText;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nuttertools.R;
import com.nuttertools.UserAddressesActivity.AddressesActivity;
import com.nuttertools.fragments.Category.Category;
import com.nuttertools.models.UserAdsModel;
import com.nuttertools.utils.CreateDialog;
import com.nuttertools.utils.FirebaseConst;
import com.nuttertools.utils.GlideApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Alexander on 10.01.2018.
 */

public class AddNewItemActivity extends AppCompatActivity implements ImagesRecyclerAdapter.BtnClickListener {

    UploadTask uploadTask;
    private TextInputLayout description, title, price;
    private CardView publishButton;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageReference;
    String locality;
    double locationLat, locationLon;
    String commentToAddress;
    String priceType;
    boolean complete = true;
    public static final int GALLERY = 200;
    public static final int CAMERA = 300;
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
    private String adId;
    byte[] photo;
    ArrayList<String> arReportUrl = new ArrayList<>();
    ImagesRecyclerAdapter imagesRecyclerAdapter;
    CategoriesAdapter categoriesAdapter;
    private MaterialDialog dialog;

    public static final int TEXT_REQUEST = 400;
    public static final String EXTRA_MESSAGE = "com.nuttertools.EXTRA_MESSAGE";
    private TextInputEditText priceField;

    UserAdsModel userAdsModel;

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
        getSupportActionBar().setTitle(R.string.activity_my_ad_name);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        localityText = findViewById(R.id.textView6);
        title = findViewById(R.id.textInputLayout6);
        description = findViewById(R.id.textInputLayout5);
        price = findViewById(R.id.textInputLayout7);
        priceField = findViewById(R.id.textInputEditText7);
        publishButton = findViewById(R.id.button2);
        locationButton = findViewById(R.id.constraintLayout1);
        spinner = findViewById(R.id.SpinnerCustom);
        priceTypeSpinner = findViewById(R.id.spinnerCustomPrice);
        recyclerView = findViewById(R.id.recyclerView);
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

        locationButton.setOnClickListener(v -> {
            localityText.setTextColor(getResources().getColor(R.color.secondaryText));
            Intent intent = new Intent(getApplicationContext(), AddressesActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "shit");
            startActivityForResult(intent, TEXT_REQUEST);
        });

        publishButton.setOnClickListener(v -> {
            AlertDialog.Builder yesOrNoDialog = new AlertDialog.Builder(AddNewItemActivity.this);
            yesOrNoDialog.setTitle(R.string.dialog_approve_ad_placement);
            yesOrNoDialog.setPositiveButton(R.string.dialog_ok, (dialog, which) -> {
                completenessCheck();
                if (!complete) {
                    Toast.makeText(getApplicationContext(), R.string.toast_fill_all_fields, Toast.LENGTH_SHORT).show();
                } else if (complete && arReportUrl.isEmpty()) {
                    Toast.makeText(getApplicationContext(), R.string.toast_add_one_image, Toast.LENGTH_SHORT).show();
                } else if (complete && !arReportUrl.isEmpty()){
                    sendDataToFirestore();
                    finish();
                }
            });

            yesOrNoDialog.setNegativeButton(R.string.dialog_cancel, (dialog, which) ->
                    Toast.makeText(getApplicationContext(), R.string.toast_did_not_like_something, Toast.LENGTH_SHORT).show());
            yesOrNoDialog.show();
        });
        initRecyclerView();
        getData();
    }

    private void getData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(FirebaseConst.CATEGORIES)
                .get()
                .addOnCompleteListener(task -> {
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
                });
    }

    public void completenessCheck() {
        String mTitle = title.getEditText().getText().toString();
        String mDescription = description.getEditText().getText().toString();
        String mPrice = price.getEditText().getText().toString();
        if (mTitle.isEmpty() |
                mDescription.isEmpty() |
                mPrice.isEmpty() |
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
                locationLat = data.getExtras().getDouble(AddressesActivity.EXTRA_LAT);
                locationLon = data.getExtras().getDouble(AddressesActivity.EXTRA_LON);
                commentToAddress = data.getExtras().getString(AddressesActivity.EXTRA_COMMENT);
                locality = data.getExtras().getString(AddressesActivity.EXTRA_LOCALITY);
                localityText.setText(locality);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upload(Uri file) {
        GlideApp.with(getApplicationContext())
                .asBitmap()
                .load(file)
                .override(1000,1000)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        uploadImage(resource);
                    }
                });
    }

    public void uploadImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream);
        final byte[] data = byteArrayOutputStream.toByteArray();
        if (data.length > 0) {
            photo = data;
        }
    }

    public void uploadImageToStorage(byte[] data, String adId) {
        StorageReference ref = storageReference.child("images/" + adId + "/"+ UUID.randomUUID().toString());
        uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(),R.string.toast_error_data_send,Toast.LENGTH_SHORT)
                        .show())
                .addOnProgressListener(taskSnapshot -> {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    imagesRecyclerAdapter.setProgress(arReportUrl.size(), (int) progress);
                })
                .addOnSuccessListener(taskSnapshot -> {
                    imagesRecyclerAdapter.setIsLoaded(arReportUrl.size(), true);
                    downloadUrl = taskSnapshot.getDownloadUrl();
                    arReportUrl.add(downloadUrl.toString());
                    db.collection(FirebaseConst.ADS).document(adId).update("photoUrl",arReportUrl);
                    Toast.makeText(getApplicationContext(),R.string.toast_ad_placed,Toast.LENGTH_SHORT).show();
                    finish();
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
        localPhotoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".com.nuttertools.provider", path);
        return localPhotoUri;
    }

    public void sendDataToFirestore() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userAdsModel = new UserAdsModel();
        userAdsModel.setLocationLat(locationLat);
        userAdsModel.setLocationLon(locationLon);
        userAdsModel.setPrice(Integer.parseInt(price.getEditText().getText().toString()));
        userAdsModel.setCategoryId(categoryId);
        userAdsModel.setTitle(title.getEditText().getText().toString());
        userAdsModel.setDescription(description.getEditText().getText().toString());
        userAdsModel.setTimestamp(new Date());
        userAdsModel.setCommentToAddress(commentToAddress);
        userAdsModel.setPriceType(priceType);
        if (firebaseUser != null) userAdsModel.setUserId(firebaseUser.getUid());

        db.collection(FirebaseConst.ADS)
                .add(userAdsModel)
                .addOnSuccessListener(documentReference -> {
                    adId = documentReference.getId();
                    uploadImageToStorage(photo, adId);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getApplicationContext(),R.string.toast_error_data_send,Toast.LENGTH_SHORT)
                                .show());
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
        String[] pictureDialogItems = {getString(R.string.dialog_choose_from_galery), getString(R.string.dialog_take_photo)};
        pictureDialog.setItems(pictureDialogItems, (dialog, which) -> {
            switch (which) {
                case 0:
                    choosePhotoFromGallery();
                    break;
                case 1:
                    takePicture();
                    break;
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
        photoRef
                .delete()
                .addOnSuccessListener(aVoid -> {
                    arUploadImages.remove(position);
                    arReportUrl.remove(position);
                    imagesRecyclerAdapter.notifyDataSetChanged();
        })
                .addOnFailureListener(e ->
                Toast.makeText(getApplicationContext(),R.string.toast_error_image_removing,Toast.LENGTH_SHORT)
                        .show());
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
                .itemsCallback((dialog, view, which, text) -> {
                    switch (which) {
                        case 0:
                            deleteCurrentPhoto(position);
                            break;
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
        yesOrNoDialog.setTitle(R.string.dialog_get_out);
        yesOrNoDialog.setMessage(R.string.dialog_info_will_lost);
        yesOrNoDialog
                .setPositiveButton(R.string.dialog_yes, (dialog, which) ->
                        deleteAllPhoto())
                .setNegativeButton(R.string.dialog_no, (dialog, which) -> {

                });
        yesOrNoDialog.show();
    }
}
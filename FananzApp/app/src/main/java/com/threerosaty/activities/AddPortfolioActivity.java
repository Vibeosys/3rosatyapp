package com.threerosaty.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.threerosaty.R;
import com.threerosaty.adapters.CategorySpinnerAdapter;
import com.threerosaty.adapters.SubcategorySpinnerAdapter;
import com.threerosaty.data.responsedata.CategoryResponseDTO;
import com.threerosaty.utils.ServerRequestToken;
import com.threerosaty.utils.ServerSyncManager;
import com.threerosaty.views.CroppedImageView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;

public class AddPortfolioActivity extends BaseActivity implements ServerSyncManager.OnErrorResultReceived,
        ServerSyncManager.OnSuccessResultReceived {

    private static final int REQUEST_SELECT_PICTURE = 0x01;
    private static final String SAMPLE_CROPPED_IMAGE_NAME = "SampleCropImage";
    private static final String TAG = AddPortfolioActivity.class.getSimpleName();
    private CroppedImageView imgView;
    private ArrayList<CategoryResponseDTO> categoryResponseDTOs = new ArrayList<>();
    private Spinner spnCategory, spnSubCategory;
    private CategorySpinnerAdapter categorySpinnerAdapter;
    private SubcategorySpinnerAdapter subcategorySpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_portfolio);
        setTitle(getString(R.string.str_add_portfolio_title));
        imgView = (CroppedImageView) findViewById(R.id.img1);
        spnCategory = (Spinner) findViewById(R.id.spnCategory);
        spnSubCategory = (Spinner) findViewById(R.id.spnSubCategory);
        imgView.setBaseActivity(this);
        progressDialog.show();
        mServerSyncManager.uploadGetDataToServer(ServerRequestToken.REQUEST_CATEGORY_TOKEN, mSessionManager.getCategoryListUrl());
        mServerSyncManager.setOnStringErrorReceived(this);
        mServerSyncManager.setOnStringResultReceived(this);

        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                CategoryResponseDTO categoryResponseDTO = (CategoryResponseDTO) categorySpinnerAdapter.getItem(position);
                if (categoryResponseDTO.getSubCategories() != null) {
                    subcategorySpinnerAdapter = new SubcategorySpinnerAdapter(getApplicationContext(),
                            categoryResponseDTO.getSubCategories());
                    spnSubCategory.setAdapter(subcategorySpinnerAdapter);
                    spnSubCategory.setVisibility(View.VISIBLE);
                } else {
                    spnSubCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnSubCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void openGallery(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE,
                    getString(R.string.permission_read_storage_rationale),
                    REQUEST_STORAGE_READ_ACCESS_PERMISSION);
        } else {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(Intent.createChooser(intent, getString(R.string.str_select_picture)), REQUEST_SELECT_PICTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_SELECT_PICTURE) {
                final Uri selectedUri = data.getData();
                if (selectedUri != null) {
                    startCropActivity(data.getData());
                } else {
                    Toast.makeText(AddPortfolioActivity.this, R.string.str_cannot_retrieve_selected_image, Toast.LENGTH_SHORT).show();
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                handleCropResult(data);
            }
        }
        if (resultCode == UCrop.RESULT_ERROR) {
            handleCropError(data);
        }
    }

    private void startCropActivity(@NonNull Uri uri) {
        String destinationFileName = SAMPLE_CROPPED_IMAGE_NAME;

        destinationFileName += ".png";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), destinationFileName)));

       /* uCrop = basisConfig(uCrop);
        uCrop = advancedConfig(uCrop);*/

        uCrop.start(AddPortfolioActivity.this);
    }


    @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
    private void handleCropError(@NonNull Intent result) {
        final Throwable cropError = UCrop.getError(result);
        if (cropError != null) {
            Log.e(TAG, "handleCropError: ", cropError);
            Toast.makeText(AddPortfolioActivity.this, cropError.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(AddPortfolioActivity.this, R.string.str_unexpected_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCropResult(@NonNull Intent result) {
        final Uri resultUri = UCrop.getOutput(result);
        if (resultUri != null) {
            // ResultActivity.startWithUri(SampleActivity.this, resultUri);
            saveCropImage(resultUri);
        } else {
            Toast.makeText(AddPortfolioActivity.this, R.string.str_cannot_retrieve_cropped_image, Toast.LENGTH_SHORT).show();
        }
    }

    private void saveCropImage(Uri resultUri) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(resultUri.getPath()).getAbsolutePath(), options);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    getString(R.string.permission_write_storage_rationale),
                    REQUEST_STORAGE_WRITE_ACCESS_PERMISSION);
        } else {
            Uri imageUri = resultUri;
            if (imageUri != null && imageUri.getScheme().equals("file")) {
                try {
                    copyFileToDownloads(resultUri);
                } catch (Exception e) {
                    Toast.makeText(AddPortfolioActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, imageUri.toString(), e);
                }
            } else {
                Toast.makeText(AddPortfolioActivity.this, getString(R.string.str_unexpected_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void copyFileToDownloads(Uri croppedFileUri) throws Exception {
        String downloadsDirectoryPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = String.format("%d_%s", Calendar.getInstance().getTimeInMillis(), croppedFileUri.getLastPathSegment());

        File saveFile = new File(downloadsDirectoryPath, filename);

        FileInputStream inStream = new FileInputStream(new File(croppedFileUri.getPath()));
        FileOutputStream outStream = new FileOutputStream(saveFile);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
        imgView.setImageURI(Uri.fromFile(saveFile));
        // showNotification(saveFile);
    }

    @Override
    public void onVolleyErrorReceived(@NonNull VolleyError error, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onDataErrorReceived(int errorCode, String errorMessage, int requestToken) {
        progressDialog.dismiss();
    }

    @Override
    public void onResultReceived(@NonNull String data, int requestToken) {
        progressDialog.dismiss();
        switch (requestToken) {
            case ServerRequestToken.REQUEST_CATEGORY_TOKEN:
                categoryResponseDTOs = CategoryResponseDTO.deserializeToArray(data);
                categorySpinnerAdapter = new CategorySpinnerAdapter(getApplicationContext(), categoryResponseDTOs);
                spnCategory.setAdapter(categorySpinnerAdapter);
                Log.d(TAG, "##" + categoryResponseDTOs);
                break;
        }
    }
}

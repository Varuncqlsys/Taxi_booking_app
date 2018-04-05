package com.taxilive_driver.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxilive_driver.R;
import com.taxilive_driver.util.Funtions;
import com.taxilive_driver.util.SavePref;
import java.io.File;
import java.io.IOException;


public class Documents_photo extends AppCompatActivity implements View.OnClickListener {
    private static final int PERMISSION_CAMERA = 101;
    private static final int PERMISSION_STORAGE = 100;
    private static final int SELECT_FILE = 200;
    private static final int REQUEST_CAMERA = 102;
    Button take_photo, save;
    ImageView upload_pic, back;
    String image_path;
    SavePref savePref;
    TextView driver_license_text,toolbar_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_photo);
        savePref = new SavePref(Documents_photo.this);
        take_photo = (Button) findViewById(R.id.take_photo);
        save = (Button) findViewById(R.id.save);
        upload_pic = (ImageView) findViewById(R.id.upload_pic);
        back = (ImageView) findViewById(R.id.back);
        driver_license_text = (TextView) findViewById(R.id.driver_license_text);
        toolbar_text = (TextView) findViewById(R.id.toolbar_text);
        save.setOnClickListener(this);
        take_photo.setOnClickListener(this);
        back.setOnClickListener(this);

        if (getIntent().getStringExtra("image").equalsIgnoreCase("licence_front")) {
            driver_license_text.setText("Driver Licence Front Photo");
            toolbar_text.setText("Driver Licence Front Photo");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("licence_back")) {
            driver_license_text.setText("Driver Licence Back Photo");
            toolbar_text.setText("Driver Licence Back Photo");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("police_verification")) {
            driver_license_text.setText("Police Verification");
            toolbar_text.setText("Police Verification");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_permit")) {
            driver_license_text.setText("Vehicle Permit");
            toolbar_text.setText("Vehicle Permit");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_insurance")) {
            driver_license_text.setText("Vehicle Insurance");
            toolbar_text.setText("Vehicle Insurance");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("driver_photo")) {
            driver_license_text.setText("Driver Photo");
            toolbar_text.setText("Driver Photo");
        } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_registration")) {
            driver_license_text.setText("Registration Certificate");
            toolbar_text.setText("Registration Certificate");
        }else if (getIntent().getStringExtra("image").equalsIgnoreCase("identity")) {
            driver_license_text.setText("Identity Proof");
            toolbar_text.setText("Identity Proof");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                if (getIntent().getStringExtra("image").equalsIgnoreCase("licence_front")) {
                    Log.e("licence_front", ""+image_path);
                    savePref.set_emp_license_front_img(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("licence_back")) {
                    savePref.set_emp_license_back_img(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("police_verification")) {
                    savePref.set_police_verification(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_permit")) {
                    savePref.set_vehicle_permit(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_insurance")) {
                    savePref.set_vehicle_insurance(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("driver_photo")) {
                    savePref.setemp_image(image_path);
                } else if (getIntent().getStringExtra("image").equalsIgnoreCase("vehicle_registration")) {
                    savePref.set_vehicle_registration(image_path);
                }else if (getIntent().getStringExtra("image").equalsIgnoreCase("identity")) {
                    savePref.set_identity(image_path);
                }

                finish();
                break;

            case R.id.take_photo:
                selectImage();
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Documents_photo.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    if (Funtions.hasPermissions(Documents_photo.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}))
                        cameraIntent();
                    else
                        Funtions.requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CAMERA, Documents_photo.this, Documents_photo.this);
                } else if (item == 1) {
                    if (Funtions.hasPermissions(Documents_photo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}))
                        galleryIntent();
                    else
                        Funtions.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CAMERA, Documents_photo.this, Documents_photo.this);
                } else if (item == 2) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryIntent();
                }
                break;
            case PERMISSION_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    cameraIntent();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                setProfileImage(selectedImageUri);
//                savePref.setUserImage(selectedImageUri.toString());
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }
        }
    }

    private void onCaptureImageResult(Intent data) {
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
        Uri uri = Uri.fromFile(file);
//        savePref.setUserImage(uri.toString());
        setProfileImage(uri);
    }

    private void setProfileImage(Uri resultUri) {
        image_path = Funtions.getPath(this, resultUri);
        Bitmap bitmap = null;
        try {
            bitmap = Funtions.getCircularBitmap(MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), resultUri));
        } catch (IOException e) {
            e.printStackTrace();
        }
        upload_pic.setImageBitmap(bitmap);
    }


}

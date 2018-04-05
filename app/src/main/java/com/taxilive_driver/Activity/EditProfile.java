package com.taxilive_driver.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.Funtions;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {
    ImageView profile_pic_cam;
    EditText edt_name, edt_email, edt_phn, edt_city;
    Button save_btn;
    SavePref pref;
    ProgressDialog progressDialog;
    private static final int PERMISSION_CAMERA = 101;
    private static final int PERMISSION_STORAGE = 100;
    private static final int SELECT_FILE = 200;
    private static final int REQUEST_CAMERA = 102;
    String image_path;
    CircleImageView profile_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        pref = new SavePref(EditProfile.this);
        progressDialog = new ProgressDialog(EditProfile.this);
        initialize();
    }

    public void initialize() {
        profile_pic = (CircleImageView) findViewById(R.id.profile_pic);
        profile_pic_cam = (ImageView) findViewById(R.id.profile_pic_cam);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_phn = (EditText) findViewById(R.id.edt_phn);
        edt_city = (EditText) findViewById(R.id.edt_city);
        save_btn = (Button) findViewById(R.id.save_btn);
        save_btn.setOnClickListener(this);
        profile_pic_cam.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        edt_name.setText(pref.getusername());
        edt_email.setText(pref.getemp_email());
        edt_phn.setText(pref.getemp_phn());
        Glide.with(this).load(pref.getprofile_pic()).into(profile_pic);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                update_profile();
                break;
            case R.id.profile_pic_cam:
                selectImage();
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    if (Funtions.hasPermissions(EditProfile.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}))
                        cameraIntent();
                    else
                        Funtions.requestPermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CAMERA, EditProfile.this, EditProfile.this);
                } else if (item == 1) {
                    if (Funtions.hasPermissions(EditProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}))
                        galleryIntent();
                    else
                        Funtions.requestPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CAMERA, EditProfile.this, EditProfile.this);
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
        profile_pic.setImageBitmap(bitmap);
    }


    private void update_profile() {

        progressDialog.show();
        MultipartBody.Builder formBuilder = new MultipartBody.Builder();
        formBuilder.setType(MultipartBody.FORM);
        if (!pref.getemp_image().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getemp_image().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getemp_image());
            formBuilder.addFormDataPart(Parameters.Emp_image, "emp_image.png", RequestBody.create(MEDIA_TYPE, file));
        }


        formBuilder.addFormDataPart(Parameters.EMP_ID, pref.getid());
        formBuilder.addFormDataPart(Parameters.AUTH_TOKEN, pref.getauth_token());
        formBuilder.addFormDataPart(Parameters.USER_NAME, edt_name.getText().toString());
        formBuilder.addFormDataPart(Parameters.EMP_EMAIL, edt_email.getText().toString());
        formBuilder.addFormDataPart(Parameters.EMP_PHONE, edt_phn.getText().toString());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(EditProfile.this, AllDriveAppConstants.EditProfile, formBody) {
            @Override
            public void getValueParse(String result) {
                progressDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    Log.e("resultttt", result.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            finish();
                        }
                        Toast.makeText(EditProfile.this, status.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void retry() {

            }


        };
        mAsync.execute();
    }


}

package com.taxilive_driver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;

import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class Documents extends AppCompatActivity implements View.OnClickListener {

    Button continue_btn;
    RelativeLayout driver_license_front, driver_license_back, police_verification_certificate,
            vehicle_permit, vehicle_insurance, vehicle_registration, driver_photo, identity_lay;
    SavePref pref;
    ProgressDialog progressDialog;
    ImageView identity_image, driver_photo_image, driver_licensefront_image,
            driver_licenseback_image,
            police_verification_certificate_image, vehicle_permit_image, vehicle_insurance_image, vehicle_registration_image;
    TextView identity, driver_photo_text, driver_license_text, driver_licenseback_text, police_verification_certificate_text,
            vehicle_permit_text, vehicle_insurance_text, vehicle_registration_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);
        pref = new SavePref(Documents.this);
        progressDialog = new ProgressDialog(Documents.this);
        initialize();

    }

    public void initialize() {
        identity = (TextView) findViewById(R.id.identity);
        driver_photo_text = (TextView) findViewById(R.id.driver_photo_text);
        driver_license_text = (TextView) findViewById(R.id.driver_license_text);
        driver_licenseback_text = (TextView) findViewById(R.id.driver_licenseback_text);
        police_verification_certificate_text = (TextView) findViewById(R.id.police_verification_certificate_text);
        vehicle_permit_text = (TextView) findViewById(R.id.vehicle_permit_text);
        vehicle_insurance_text = (TextView) findViewById(R.id.vehicle_insurance_text);
        vehicle_registration_text = (TextView) findViewById(R.id.vehicle_registration_text);

        driver_license_front = (RelativeLayout) findViewById(R.id.driver_license_front);
        identity_lay = (RelativeLayout) findViewById(R.id.identity_lay);
        driver_license_back = (RelativeLayout) findViewById(R.id.driver_license_back);
        police_verification_certificate = (RelativeLayout) findViewById(R.id.police_verification_certificate);
        vehicle_permit = (RelativeLayout) findViewById(R.id.vehicle_permit);
        vehicle_insurance = (RelativeLayout) findViewById(R.id.vehicle_insurance);
        driver_photo = (RelativeLayout) findViewById(R.id.driver_photo);
        vehicle_registration = (RelativeLayout) findViewById(R.id.vehicle_registration);
        continue_btn = (Button) findViewById(R.id.continue_btn);
        vehicle_registration_image = (ImageView) findViewById(R.id.vehicle_registration_image);
        vehicle_insurance_image = (ImageView) findViewById(R.id.vehicle_insurance_image);
        vehicle_permit_image = (ImageView) findViewById(R.id.vehicle_permit_image);
        police_verification_certificate_image = (ImageView) findViewById(R.id.police_verification_certificate_image);
        driver_licenseback_image = (ImageView) findViewById(R.id.driver_licenseback_image);
        driver_licensefront_image = (ImageView) findViewById(R.id.driver_licensefront_image);
        driver_photo_image = (ImageView) findViewById(R.id.driver_photo_image);
        identity_image = (ImageView) findViewById(R.id.identity_image);

        continue_btn.setOnClickListener(this);
        driver_license_front.setOnClickListener(this);
        driver_license_back.setOnClickListener(this);
        police_verification_certificate.setOnClickListener(this);
        vehicle_permit.setOnClickListener(this);
        vehicle_insurance.setOnClickListener(this);
        driver_photo.setOnClickListener(this);
        vehicle_registration.setOnClickListener(this);
        identity_lay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                if (pref.getemp_image().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Driver image");
                } else if (pref.getemp_license_front_img().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Driver Licence Front Photo");
                } else if (pref.getemp_license_back_img().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Driver Licence Back Photo");
                } else if (pref.getvehicle_registration().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Vehicle Registration");
                }
//                else if (pref.getvehicle_permit().equalsIgnoreCase("")) {
//                    Util.showToast(Documents.this, "Please Upload Vehicle Permit");
//                }
                else if (pref.getpolice_verification().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Police Verification");
                } else if (pref.getvehicle_insurance().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Vehicle Insurance");
                } else if (pref.getidentity().equalsIgnoreCase("")) {
                    Util.showToast(Documents.this, "Please Upload Any Identity Proof");
                } else {
                    update_document();
                }

                break;
            case R.id.driver_license_front:
                Intent intent1 = new Intent(Documents.this, Documents_photo.class);
                intent1.putExtra("image", "licence_front");
                startActivity(intent1);
                break;
            case R.id.driver_license_back:
                Intent intent2 = new Intent(Documents.this, Documents_photo.class);
                intent2.putExtra("image", "licence_back");
                startActivity(intent2);
                break;
            case R.id.police_verification_certificate:
                Intent intent3 = new Intent(Documents.this, Documents_photo.class);
                intent3.putExtra("image", "police_verification");
                startActivity(intent3);
                break;
            case R.id.vehicle_permit:
                Intent intent4 = new Intent(Documents.this, Documents_photo.class);
                intent4.putExtra("image", "vehicle_permit");
                startActivity(intent4);
                break;
            case R.id.vehicle_insurance:
                Intent intent5 = new Intent(Documents.this, Documents_photo.class);
                intent5.putExtra("image", "vehicle_insurance");
                startActivity(intent5);
                break;
            case R.id.driver_photo:
                Intent intent6 = new Intent(Documents.this, Documents_photo.class);
                intent6.putExtra("image", "driver_photo");
                startActivity(intent6);
                break;
            case R.id.vehicle_registration:
                Intent intent7 = new Intent(Documents.this, Documents_photo.class);
                intent7.putExtra("image", "vehicle_registration");
                startActivity(intent7);
                break;

            case R.id.identity_lay:
                Intent intent8 = new Intent(Documents.this, Documents_photo.class);
                intent8.putExtra("image", "identity");
                startActivity(intent8);
                break;
        }
    }

    private void update_document() {

        progressDialog.show();
        MultipartBody.Builder formBuilder = new MultipartBody.Builder();
        formBuilder.setType(MultipartBody.FORM);
        if (!pref.getemp_image().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getemp_image().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getemp_image());
            formBuilder.addFormDataPart("emp_image", "emp_image.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getemp_license_front_img().equalsIgnoreCase("")) {
            Log.e("licence_front", ""+pref.getemp_license_front_img());
            final MediaType MEDIA_TYPE = pref.getemp_license_front_img().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getemp_license_front_img());
            formBuilder.addFormDataPart("emp_license_front_img", "emp_license_front_img.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getemp_license_back_img().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getemp_license_back_img().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getemp_license_back_img());
            formBuilder.addFormDataPart("emp_license_back_img", "emp_license_back_img.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getvehicle_insurance().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getvehicle_insurance().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getvehicle_insurance());
            formBuilder.addFormDataPart("vehicle_insurance", "vehicle_insurance.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getpolice_verification().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getpolice_verification().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getpolice_verification());
            formBuilder.addFormDataPart("police_varification", "police_varification.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getvehicle_permit().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getvehicle_permit().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getvehicle_permit());
            formBuilder.addFormDataPart("vehicle_permit", "vehicle_permit.png", RequestBody.create(MEDIA_TYPE, file));
        }
        if (!pref.getidentity().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getidentity().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getidentity());
            formBuilder.addFormDataPart("driver_identity_image", "driver_identity_image.png", RequestBody.create(MEDIA_TYPE, file));
        }

        if (!pref.getvehicle_registration().equalsIgnoreCase("")) {
            final MediaType MEDIA_TYPE = pref.getvehicle_registration().endsWith("png") ?
                    MediaType.parse("image/png") : MediaType.parse("image/jpeg");

            File file = new File(pref.getvehicle_registration());
            formBuilder.addFormDataPart("vehicle_registration", "vehicle_registration.png", RequestBody.create(MEDIA_TYPE, file));
        }

        formBuilder.addFormDataPart(Parameters.EMP_ID, pref.getemp_id());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(Documents.this, AllDriveAppConstants.Upload_Document, formBody) {
            @Override
            public void getValueParse(String result) {
                progressDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    Log.e("resultttt", result.toString());
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            pref.set_emp_license_front_img("");
                            pref.set_emp_license_back_img("");
                            pref.set_police_verification("");
                            pref.set_vehicle_permit("");
                            pref.set_vehicle_insurance("");
                            pref.setemp_image("");
                            pref.set_vehicle_registration("");
                            pref.set_identity("");
                            Intent in = new Intent(Documents.this, AddBankAccount.class);
                            in.putExtra("from","");
                            in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(in);

                        }
                        Toast.makeText(Documents.this, status.getString("message"), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onResume() {
        super.onResume();

        if (!pref.getemp_image().equalsIgnoreCase("")) {
            driver_photo_image.setImageResource(R.drawable.right_mark);
            driver_photo_text.setTextColor(Color.parseColor("#c0c0c0"));
        }
         if (!pref.getemp_license_front_img().equalsIgnoreCase("")) {
            driver_licensefront_image.setImageResource(R.drawable.right_mark);
            driver_license_text.setTextColor(Color.parseColor("#c0c0c0"));
        }  if (!pref.getemp_license_back_img().equalsIgnoreCase("")) {
            driver_licenseback_image.setImageResource(R.drawable.right_mark);
            driver_licenseback_text.setTextColor(Color.parseColor("#c0c0c0"));
        }  if (!pref.getvehicle_registration().equalsIgnoreCase("")) {
            vehicle_registration_image.setImageResource(R.drawable.right_mark);
            vehicle_registration_text.setTextColor(Color.parseColor("#c0c0c0"));
        }
        if (!pref.getvehicle_permit().equalsIgnoreCase("")) {
            vehicle_permit_image.setImageResource(R.drawable.right_mark);
            vehicle_permit_text.setTextColor(Color.parseColor("#c0c0c0"));
        }
        if (!pref.getpolice_verification().equalsIgnoreCase("")) {
            police_verification_certificate_image.setImageResource(R.drawable.right_mark);
            police_verification_certificate_text.setTextColor(Color.parseColor("#c0c0c0"));
        }  if (!pref.getvehicle_insurance().equalsIgnoreCase("")) {
            vehicle_insurance_image.setImageResource(R.drawable.right_mark);
            vehicle_insurance_text.setTextColor(Color.parseColor("#c0c0c0"));
        }  if (!pref.getidentity().equalsIgnoreCase("")) {
            identity_image.setImageResource(R.drawable.right_mark);
            identity.setTextColor(Color.parseColor("#c0c0c0"));
        }


    }
}

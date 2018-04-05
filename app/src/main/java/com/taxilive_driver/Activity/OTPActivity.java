package com.taxilive_driver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class OTPActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog mDialog;
    EditText edt_otp;
    TextView resend_otp,continue_btn;
    SavePref savePref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        mDialog = new ProgressDialog(OTPActivity.this);
        savePref = new SavePref(OTPActivity.this);
        initialize();

    }

    public void initialize()
    {
        continue_btn = (TextView) findViewById(R.id.continue_btn);
        resend_otp = (TextView) findViewById(R.id.resend_otp);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        continue_btn.setOnClickListener(this);
        resend_otp.setOnClickListener(this);
    }

    private void VerifyOtp() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID,getIntent().getStringExtra("emp_id"));
        formBuilder.add(Parameters.OTP, edt_otp.getText().toString());


        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.Verify_Otp, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            savePref.set_empid(body.getString("emp_id"));
                            Intent in = new Intent(OTPActivity.this, Documents.class);
                            startActivity(in);

                        } else {
                            Util.showToast(OTPActivity.this, status.getString("message"));
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace(

                        );
                    }
                }
            }


            @Override
            public void retry() {

            }
        };
        mAsync.execute();
    }



    private void ResendOtp() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID,getIntent().getStringExtra("emp_id"));

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.Resend_Otp, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");

                        } else {
                            Util.showToast(OTPActivity.this, status.getString("message"));
                        }
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    } catch (Exception ex) {
                        ex.printStackTrace(

                        );
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.continue_btn:
                VerifyOtp();
                break;
            case R.id.resend_otp:
                ResendOtp();
                break;
        }
    }
}

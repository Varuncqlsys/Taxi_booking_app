package com.taxilive_driver.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    EditText old_password,new_password,confirm_password;
    TextView send;
    LinearLayout back_lay;
    SavePref pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        pref = new SavePref(ChangePassword.this);
        initialize();

    }
    public void initialize()
    {
        old_password = (EditText) findViewById(R.id.old_password);
        new_password = (EditText) findViewById(R.id.new_password);
        back_lay = (LinearLayout) findViewById(R.id.back_lay);
        confirm_password = (EditText) findViewById(R.id.confirm_password);
        send = (TextView) findViewById(R.id.send);
        send.setOnClickListener(this);
        back_lay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch(v.getId())
        {
            case R.id.back_lay:
                finish();

                break;
            case R.id.send:
              if(new_password.getText().toString().equals(confirm_password.getText().toString()))
              {
                  Util.hideKeyboard(ChangePassword.this);
                  changepassword();

              }
              else {
                  AlertDialog alertDialog = new AlertDialog.Builder(ChangePassword.this).create();
                                alertDialog.setTitle("Sorry,");
                                alertDialog.setMessage("Please check password");
                                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                                alertDialog.show();
              }
                break;
        }
    }

    private void changepassword() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.EMP_ID, pref.getid());
        formBuilder.add(Parameters.AUTH_TOKEN, pref.getauth_token());
        formBuilder.add(Parameters.EMP_PASSWORD, old_password.getText().toString());
        formBuilder.add(Parameters.New_password, new_password.getText().toString());
        formBuilder.add(Parameters.Confirm_password, confirm_password.getText().toString());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(ChangePassword.this, AllDriveAppConstants.ChangePassword, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {

                            finish();
                        }
                        Toast.makeText(ChangePassword.this, status.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException ex) {
                        ex.printStackTrace();
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

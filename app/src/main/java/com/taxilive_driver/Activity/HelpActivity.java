package com.taxilive_driver.Activity;

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
import com.taxilive_driver.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class HelpActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout back_lay;
    EditText edt_name,edt_email,edt_subject,edt_message;
    TextView send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        back_lay = (LinearLayout) findViewById(R.id.back_lay);
        send = (TextView) findViewById(R.id.send);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_email = (EditText) findViewById(R.id.edt_email);
        edt_subject = (EditText) findViewById(R.id.edt_subject);
        edt_message = (EditText) findViewById(R.id.edt_message);
        back_lay.setOnClickListener(this);
        send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send:
                if(edt_email.getText().toString().equals(""))
                {
                    Util.AlertDialog(HelpActivity.this,"Please enter emailid");
                }
                else if(!Util.isValidEmail(edt_email.getText().toString()))
                {
                    Util.AlertDialog(HelpActivity.this,"Please enter valid emailid");
                }

                else  if(edt_message.getText().toString().equals(""))
                {
                    Util.AlertDialog(HelpActivity.this,"Please enter message");
                }
                else  if(edt_name.getText().toString().equals(""))
                {
                    Util.AlertDialog(HelpActivity.this,"Please enter Name");
                }
                else  if(edt_subject.getText().toString().equals(""))
                {
                    Util.AlertDialog(HelpActivity.this,"Please enter Subject");
                }
                else
                {
                    help();
                }

                break;
            case R.id.back_lay:
                finish();
                break;
        }


    }
    private void help() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.NAME, edt_name.getText().toString());
        formBuilder.add(Parameters.Email,edt_email.getText().toString());
        formBuilder.add(Parameters.Subject,edt_subject.getText().toString());
        formBuilder.add(Parameters.Message,edt_message.getText().toString() );
        formBuilder.add(Parameters.Created,String.valueOf(System.currentTimeMillis()) );

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(HelpActivity.this, AllDriveAppConstants.Help, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {

                            finish();
                        }
                        Toast.makeText(HelpActivity.this, status.getString("message"), Toast.LENGTH_SHORT).show();
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

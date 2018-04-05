package com.taxilive_driver.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AddBankAccount extends AppCompatActivity implements View.OnClickListener {
    LinearLayout back_lay;
    String bankName = "", holderName = "", accountNumber = "", confirmaccountNumber = "", ifscCode = "", mobileNumber = "";
    SavePref savePref;
    AddBankAccount context;
    EditText holder_name, acct_no, conf_acc_no, mobile, ifsc_code;
    TextView send;
    Spinner bank_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_account);
        context = this;
        savePref = new SavePref(context);
        setUpUI();
        initialize();
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.adapter_spinner, getResources().getStringArray(R.array.bank_name));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bank_name.setAdapter(aa);

        bank_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bankName = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpUI() {
        ifsc_code = (EditText) findViewById(R.id.ifsc_code);
        holder_name = (EditText) findViewById(R.id.holder_name);
        acct_no = (EditText) findViewById(R.id.acct_no);
        conf_acc_no = (EditText) findViewById(R.id.conf_acc_no);
        bank_name = (Spinner) findViewById(R.id.bank_name);
        mobile = (EditText) findViewById(R.id.mobile);
        send = (TextView) findViewById(R.id.send);

        holder_name.setText(savePref.getholder_name());
        acct_no.setText(savePref.getaccount_no());
        mobile.setText(savePref.getmobile_no());
        ifsc_code.setText(savePref.getifsccode());


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (!accountNumber.equals(confirmaccountNumber)) {
                    Toast.makeText(AddBankAccount.this, "Please Check your account details", Toast.LENGTH_SHORT).show();
                } else {
                    addBankDetail();
                }

            }
        });
    }

    private void getData() {
        holderName = holder_name.getText().toString();
        accountNumber = acct_no.getText().toString();
        confirmaccountNumber = conf_acc_no.getText().toString();
//        bankName = bamk_name.getText().toString();
        mobileNumber = mobile.getText().toString();
        ifscCode = ifsc_code.getText().toString();
    }

    public void initialize() {
        back_lay = (LinearLayout) findViewById(R.id.back_lay);
        back_lay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_lay:

                finish();
                break;
        }
    }

    private void addBankDetail() {

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, savePref.getemp_id());
        formBuilder.add(Parameters.Holder_Name, holderName);
        formBuilder.add(Parameters.Account_Number, accountNumber);
        formBuilder.add(Parameters.IFSC_Code, ifscCode);
        formBuilder.add(Parameters.Mobile, mobileNumber);
        formBuilder.add(Parameters.Bank_Name, bankName);

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(context, AllDriveAppConstants.BankDetail, formBody) {
            @Override
            public void getValueParse(String result) {


                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {

                            JSONObject body = jsonObject.getJSONObject("body");
                            savePref.setholder_name(body.getString("holder_name"));
                            savePref.setaccount_no(body.getString("account_no"));
                            savePref.setifsccode(body.getString("ifsc_code"));
                            savePref.setmobile_no(body.getString("mobile"));

                            Util.showToast(context, "Bank Details Add Successfully");
                            if (getIntent().getStringExtra("from").equals("Setting")) {
                                finish();
                            } else {
                                Intent in = new Intent(AddBankAccount.this, ThanksActivity.class);
                                startActivity(in);
                                finish();
                            }

                        } else {
                            Util.showToast(context, status.getString("message"));
                        }
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

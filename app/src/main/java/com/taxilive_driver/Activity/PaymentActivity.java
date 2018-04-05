package com.taxilive_driver.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.taxilive_driver.Adapter.TransactionAdapter;
import com.taxilive_driver.Model.Transaction_data;
import com.taxilive_driver.Model.WithdrawHistory;
import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    TextView transaction_history, withdraw_history, withdraw;
    ListView transaction_list, withdraw_list;
    String transaction_show = "hide", withdraw_show = "hide";
    ProgressDialog mDialog;
    SavePref pref;
    ArrayList<Transaction_data> transactionDataArrayList;
    ArrayList<WithdrawHistory> withdrawHistoryArrayList;
    TransactionAdapter transactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        mDialog = new ProgressDialog(PaymentActivity.this);
        pref = new SavePref(PaymentActivity.this);
        transactionDataArrayList = new ArrayList<>();
        withdrawHistoryArrayList = new ArrayList<>();
        initialize();
        payment_history();
    }

    public void initialize() {
        transaction_history = (TextView) findViewById(R.id.transaction_history);
        withdraw_history = (TextView) findViewById(R.id.withdraw_history);
        withdraw = (TextView) findViewById(R.id.withdraw);
        withdraw_list = (ListView) findViewById(R.id.withdraw_list);
        LinearLayout back_lay = (LinearLayout) findViewById(R.id.back_lay);
        transaction_list = (ListView) findViewById(R.id.transaction_list);
        withdraw.setOnClickListener(this);
        withdraw_history.setOnClickListener(this);
        transaction_history.setOnClickListener(this);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        transaction_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(PaymentActivity.this, WeekDetailactivity.class);
                in.putExtra("month", transactionDataArrayList.get(position).getMonth_name());
                in.putExtra("year", transactionDataArrayList.get(position).getYear());
                startActivity(in);
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw:
                Intent withdraw = new Intent(PaymentActivity.this, WithdrawActivity.class);
                startActivity(withdraw);

                break;

            case R.id.transaction_history:
                if (transaction_show.equalsIgnoreCase("hide")) {
                    transaction_list.setVisibility(View.VISIBLE);
                    transaction_show = "show";
                } else {
                    transaction_list.setVisibility(View.GONE);
                    transaction_show = "hide";
                }
                break;

            case R.id.withdraw_history:
                if (withdraw_show.equalsIgnoreCase("hide")) {
                    withdraw_list.setVisibility(View.VISIBLE);
                    withdraw_show = "show";
                } else {
                    withdraw_list.setVisibility(View.GONE);
                    withdraw_show = "hide";
                }
                break;
        }
    }


    private void payment_history() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, pref.getid());


        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.Withdraw_history, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            JSONArray month = body.getJSONArray("Month");
                            JSONArray withdraw = body.getJSONArray("withdral_History");
                            for (int i = 0; i < month.length(); i++) {
                                JSONObject month_obj = month.getJSONObject(i);
                                Transaction_data transactionData = new Transaction_data();
                                transactionData.setMonth_name(month_obj.getString("month_name"));
                                transactionData.setTotal_amt(month_obj.getString("total_amt"));
                                transactionData.setYear(month_obj.getString("year"));
                                transactionDataArrayList.add(transactionData);
                            }
                            for (int i = 0; i < withdraw.length(); i++) {
                                JSONObject withdraw_historyobj = month.getJSONObject(i);
                                WithdrawHistory withdrawHistory = new WithdrawHistory();
                                withdrawHistoryArrayList.add(withdrawHistory);
                            }

                            transactionAdapter = new TransactionAdapter(PaymentActivity.this, transactionDataArrayList, "payment");
                            transaction_list.setAdapter(transactionAdapter);


                        } else {
                            Util.showToast(PaymentActivity.this, status.getString("message"));
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

}

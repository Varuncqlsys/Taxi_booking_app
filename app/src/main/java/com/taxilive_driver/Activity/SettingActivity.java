package com.taxilive_driver.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taxilive_driver.R;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout back_lay;
    TextView add_paypal, add_bankaccount, change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();

    }

    public void initialize() {
        back_lay = (LinearLayout) findViewById(R.id.back_lay);
        add_paypal = (TextView) findViewById(R.id.add_paypal);
        add_bankaccount = (TextView) findViewById(R.id.add_bankaccount);
        change_password = (TextView) findViewById(R.id.change_password);
        back_lay.setOnClickListener(this);
        change_password.setOnClickListener(this);
        add_bankaccount.setOnClickListener(this);
        add_paypal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_paypal:

                break;
            case R.id.change_password:
                Intent cp = new Intent(SettingActivity.this, ChangePassword.class);
                startActivity(cp);


                break;
            case R.id.add_bankaccount:
                Intent in = new Intent(SettingActivity.this, AddBankAccount.class);
                in.putExtra("from","Setting");
                startActivity(in);


                break;
            case R.id.back_lay:
              finish();

                break;
        }

    }
}

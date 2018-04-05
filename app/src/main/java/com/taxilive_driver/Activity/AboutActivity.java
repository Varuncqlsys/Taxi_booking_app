package com.taxilive_driver.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.taxilive_driver.R;

public class AboutActivity extends AppCompatActivity {

    LinearLayout back_lay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        back_lay = (LinearLayout) findViewById(R.id.back_lay);
        back_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

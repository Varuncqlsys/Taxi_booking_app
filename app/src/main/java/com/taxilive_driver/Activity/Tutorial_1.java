package com.taxilive_driver.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.taxilive_driver.R;


public class Tutorial_1 extends AppCompatActivity {

    LinearLayout next_lay, skip_lay, pre_lay;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vertification_firststep);
        next_lay = (LinearLayout) findViewById(R.id.next_lay);
        skip_lay = (LinearLayout) findViewById(R.id.skip_lay);
        pre_lay = (LinearLayout) findViewById(R.id.pre_lay);
        next_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Tutorial_1.this, Tutorial_2.class);
                startActivity(in);
                finish();
            }
        });

        skip_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in = new Intent(Tutorial_1.this, Login_Activity.class);
                startActivity(in);
                finish();
            }
        });

        pre_lay.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in = new Intent(Tutorial_1.this, Verification_Activity.class);
                startActivity(in);
                finish();
            }
        });
    }
}
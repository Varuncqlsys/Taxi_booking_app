package com.taxilive_driver.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxilive_driver.R;

public class Verification_Activity extends AppCompatActivity {
    ImageView learn_more;
    TextView verify_identity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_);
        initialize();
    }

    public void initialize()
    {
        learn_more = (ImageView) findViewById(R.id.learn_more);
        verify_identity = (TextView) findViewById(R.id.verify_identity);
        learn_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Verification_Activity.this,Tutorial_1.class);
                startActivity(in);
                finish();
            }
        });
        verify_identity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Verification_Activity.this,Login_Activity.class);
                startActivity(in);
                finish();

            }
        });
    }
}

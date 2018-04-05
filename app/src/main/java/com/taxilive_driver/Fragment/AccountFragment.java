package com.taxilive_driver.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.taxilive_driver.Activity.AboutActivity;
import com.taxilive_driver.Activity.EditProfile;
import com.taxilive_driver.Activity.HelpActivity;
import com.taxilive_driver.Activity.PaymentActivity;
import com.taxilive_driver.Activity.SettingActivity;
import com.taxilive_driver.R;
import com.taxilive_driver.util.SavePref;


public class AccountFragment extends Fragment implements View.OnClickListener {
    View view;
    RelativeLayout help, payment, setting, about;
    TextView username;
    SavePref pref;
    LinearLayout edit_profile;
    ImageView profile_pic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        pref = new SavePref(getActivity());
        initialize();

        return view;
    }

    private void initialize() {
        help = (RelativeLayout) view.findViewById(R.id.help);
        profile_pic = (ImageView) view.findViewById(R.id.profile_pic);
        payment = (RelativeLayout) view.findViewById(R.id.payment);
        edit_profile = (LinearLayout) view.findViewById(R.id.edit_profile);
        setting = (RelativeLayout) view.findViewById(R.id.setting);
        about = (RelativeLayout) view.findViewById(R.id.about);
        username = (TextView) view.findViewById(R.id.username);
        help.setOnClickListener(this);
        about.setOnClickListener(this);
        setting.setOnClickListener(this);
        payment.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        Glide.with(this).load(pref.getprofile_pic()).into(profile_pic);
    }

    @Override
    public void onResume() {
        super.onResume();
        username.setText(pref.getusername());
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.help:
                Intent in = new Intent(getActivity(), HelpActivity.class);
                startActivity(in);
                break;

            case R.id.edit_profile:
                Intent edit_profile = new Intent(getActivity(), EditProfile.class);
                startActivity(edit_profile);
                break;

            case R.id.payment:
                Intent in1 = new Intent(getActivity(), PaymentActivity.class);
                startActivity(in1);
                break;

            case R.id.about:
                Intent about = new Intent(getActivity(), AboutActivity.class);
                startActivity(about);
                break;

            case R.id.setting:
                Intent setting = new Intent(getActivity(), SettingActivity.class);
                startActivity(setting);
                break;
        }
    }
}

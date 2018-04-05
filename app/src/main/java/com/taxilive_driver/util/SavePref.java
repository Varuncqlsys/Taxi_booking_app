package com.taxilive_driver.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cqlsys on 1/15/2016.
 */
public class SavePref {
    public static final String TAG = "SavePref";
    Context context;
    public static final String PREF_TOKEN = "Droppin_app";
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    public SavePref() {

    }

    public SavePref(Context c) {
        context = c;
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = preferences.edit();

    }

    public void set_id(String id) {
        editor.putString("id", id);
        editor.commit();
    }

    public String getid() {
        String id = preferences.getString("id", "");
        return id;
    }

    public void set_empid(String emp_id) {
        editor.putString("emp_id", emp_id);
        editor.commit();
    }

    public String getemp_id() {
        String emp_id = preferences.getString("emp_id", "");
        return emp_id;
    }

    public void setusername(String username) {
        editor.putString("username", username);
        editor.commit();
    }

    public String getusername() {
        String username = preferences.getString("username", "");
        return username;
    }

    public void setestimated_cost(String estimated_cost) {
        editor.putString("estimated_cost", estimated_cost);
        editor.commit();
    }

    public String getestimated_cost() {
        String estimated_cost = preferences.getString("estimated_cost", "");
        return estimated_cost;
    }

    public void setemp_email(String emp_email) {
        editor.putString("emp_email", emp_email);
        editor.commit();
    }

    public String getemp_email() {
        String emp_email = preferences.getString("emp_email", "");
        return emp_email;
    }

    public void setemp_phn(String emp_phn) {
        editor.putString("emp_phn", emp_phn);
        editor.commit();
    }

    public String getemp_phn() {
        String emp_phn = preferences.getString("emp_phn", "");
        return emp_phn;
    }

    public void setaccount_no(String account_no) {
        editor.putString("account_no", account_no);
        editor.commit();
    }

    public String getaccount_no() {
        String account_no = preferences.getString("account_no", "");
        return account_no;
    }

    public void setholder_name(String holder_name) {
        editor.putString("holder_name", holder_name);
        editor.commit();
    }

    public String getholder_name() {
        String holder_name = preferences.getString("holder_name", "");
        return holder_name;
    }

    public void setifsccode(String ifsccode) {
        editor.putString("ifsccode", ifsccode);
        editor.commit();
    }

    public String getifsccode() {
        String ifsccode = preferences.getString("ifsccode", "");
        return ifsccode;
    }

    public void setmobile_no(String mobile_no) {
        editor.putString("mobile_no", mobile_no);
        editor.commit();
    }

    public String getmobile_no() {
        String mobile_no = preferences.getString("mobile_no", "");
        return mobile_no;
    }


    public void setauth_token(String auth_token) {
        editor.putString("auth_token", auth_token);
        editor.commit();
    }

    public String getauth_token() {
        String auth_token = preferences.getString("auth_token", "");
        return auth_token;
    }

    public void setrating(String rating) {
        editor.putString("rating", rating);
        editor.commit();
    }

    public String getrating() {
        String rating = preferences.getString("rating", "");
        return rating;
    }


    public void setemp_image(String emp_image
    ) {
        editor.putString("emp_image", emp_image);
        editor.commit();
    }

    public String getemp_image() {
        String emp_image = preferences.getString("emp_image", "");
        return emp_image;
    }

    public void setprofile_pic(String profile_pic) {
        editor.putString("profile_pic", profile_pic);
        editor.commit();
    }

    public String getprofile_pic() {
        String profile_pic = preferences.getString("profile_pic", "");
        return profile_pic;
    }

    public void setonline_status(String online_status) {
        editor.putString("online_status", online_status);
        editor.commit();
    }

    public String getonline_status() {
        String online_status = preferences.getString("online_status", "");
        return online_status;
    }

    public void setavg_rating(String avg_ratting) {
        editor.putString("avg_ratting", avg_ratting);
        editor.commit();
    }

    public String getavg_ratting() {
        String avg_ratting = preferences.getString("avg_ratting", "");
        return avg_ratting;
    }

    public void setuser_id(String user_id) {
        editor.putString("user_id", user_id);
        editor.commit();
    }

    public String getuser_id() {
        String user_id = preferences.getString("user_id", "");
        return user_id;
    }


    public void set_emp_license_front_img(String emp_license_front_img) {
        editor.putString("emp_license_front_img", emp_license_front_img);
        editor.commit();
    }

    public String getemp_license_front_img() {
        String emp_license_front_img = preferences.getString("emp_license_front_img", "");
        return emp_license_front_img;
    }


    public void set_emp_license_back_img(String emp_license_back_img) {
        editor.putString("emp_license_back_img", emp_license_back_img);
        editor.commit();
    }

    public String getemp_license_back_img() {
        String emp_license_back_img = preferences.getString("emp_license_back_img", "");
        return emp_license_back_img;
    }

    public void set_vehicle_permit(String vehicle_permit) {
        editor.putString("vehicle_permit", vehicle_permit);
        editor.commit();
    }

    public String getvehicle_permit() {
        String vehicle_permit = preferences.getString("vehicle_permit", "");
        return vehicle_permit;
    }

    public void set_identity(String identity) {
        editor.putString("identity", identity);
        editor.commit();
    }

    public String getidentity() {
        String identity = preferences.getString("identity", "");
        return identity;
    }

    public void set_police_verification(String police_verification) {
        editor.putString("police_verification", police_verification);
        editor.commit();
    }

    public String getpolice_verification() {
        String police_verification = preferences.getString("police_verification", "");
        return police_verification;
    }

    public void set_vehicle_insurance(String vehicle_insurance) {
        editor.putString("vehicle_insurance", vehicle_insurance);
        editor.commit();
    }

    public String getvehicle_insurance() {
        String vehicle_insurance = preferences.getString("vehicle_insurance", "");
        return vehicle_insurance;
    }

    public void set_vehicle_registration(String vehicle_registration) {
        editor.putString("vehicle_registration", vehicle_registration);
        editor.commit();
    }

    public String getvehicle_registration() {
        String vehicle_registration = preferences.getString("vehicle_registration", "");
        return vehicle_registration;
    }

    public void set_presentactivity(String activity) {
        editor.putString("activity", activity);
        editor.commit();
    }

    public String get_presentactivity() {
        String activity = preferences.getString("activity", "");
        return activity;
    }

    public void setCurrentLat(String current_lat) {
        editor.putString("current_lat", current_lat);
        editor.commit();
    }

    public String getCurrentLat() {
        String current_lat = preferences.getString("current_lat", "");
        return current_lat;
    }

    public void setCurrentLong(String current_long) {
        editor.putString("current_long", current_long);
        editor.commit();
    }

    public String getCurrentLong() {
        String current_long = preferences.getString("current_long", "");
        return current_long;
    }

    public static void setDeviceToken(Context mContext, String key, String value) {
        SharedPreferences sharedpreferences = mContext.getSharedPreferences(PREF_TOKEN, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getDeviceToken(Context mContext, String key) {
        SharedPreferences preferences = mContext.getSharedPreferences(PREF_TOKEN, Context.MODE_PRIVATE);
        String stringvalue = preferences.getString(key, null);
        return stringvalue;
    }


    public void clearPreferences() {
        preferences.edit().clear().commit();
    }
}

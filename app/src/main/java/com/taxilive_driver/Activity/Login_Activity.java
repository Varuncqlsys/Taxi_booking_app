package com.taxilive_driver.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.taxilive_driver.R;
import com.taxilive_driver.Services.LocationService;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class Login_Activity extends AppCompatActivity implements View.OnClickListener ,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    EditText email, password, dialog_email;
    TextView forgot_password;
    ProgressDialog mDialog;
    SavePref pref;
    TextView sign_in;
    RelativeLayout sign_up;
    Dialog forgotpassword_dialog;
    Button send;
    ImageButton cross;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logindetail);
        mDialog = new ProgressDialog(Login_Activity.this);
        pref = new SavePref(Login_Activity.this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        initialize();
    }

    public void initialize() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        sign_up = (RelativeLayout) findViewById(R.id.sign_up);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        sign_in = (TextView) findViewById(R.id.sign_in);
        forgot_password.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                Util.hideKeyboard(Login_Activity.this);
                Intent in = new Intent(Login_Activity.this, SignupActivity.class);
                startActivity(in);
                finish();
                break;
            case R.id.sign_in:
                Util.hideKeyboard(Login_Activity.this);
                if (email.getText().toString().equals("")) {
                    Util.showToast(Login_Activity.this, "Please fill email address");
                } else if (password.getText().toString().equals("")) {
                    Util.showToast(Login_Activity.this, "Please fill password");
                } else {
                    Login();
                }
                break;

            case R.id.forgot_password:

                forgotpassword_dialog = new Dialog(Login_Activity.this, R.style.Theme_Dialog);
                forgotpassword_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                forgotpassword_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                forgotpassword_dialog.setContentView(R.layout.forgot_dialog_layout);
                forgotpassword_dialog.show();
                send = (Button) forgotpassword_dialog.findViewById(R.id.send);
                cross = (ImageButton) forgotpassword_dialog.findViewById(R.id.cross);
                dialog_email = (EditText) forgotpassword_dialog.findViewById(R.id.dialog_email);
                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Forgot_password(dialog_email.getText().toString());
                        Util.hideKeyboard(Login_Activity.this);

                    }
                });
                cross.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Util.hideKeyboard(Login_Activity.this);
                        forgotpassword_dialog.dismiss();
                    }
                });
                break;


        }
    }

    private void Login() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();

        formBuilder.add(Parameters.EMP_EMAIL, email.getText().toString());
        formBuilder.add(Parameters.EMP_PASSWORD, password.getText().toString());
        formBuilder.add(Parameters.DEVICE_TOKEN, pref.getDeviceToken(Login_Activity.this, "token"));
        formBuilder.add(Parameters.DEVICE_TYPE, "1");
        formBuilder.add(Parameters.LAT, String.valueOf(currentLatitude));
        formBuilder.add(Parameters.LONG, String.valueOf(currentLongitude));

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.LOGIN, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            JSONObject bank = body.getJSONObject("bank");
                            String is_verify = body.getString("is_verify");
//                            pref.set_id(body.getString("emp_id"));
//                                    pref.setusername(body.getString("username"));
//                                    pref.setauth_token(body.getString("auth_token"));
//                                    pref.setprofile_pic(body.getString("emp_image"));
//                                    pref.setonline_status(body.getString("on_duty"));
//                                    pref.setavg_rating(body.getString("avg_ratting"));
//                                    pref.setemp_email(body.getString("emp_email"));
//                                    pref.setemp_phn(body.getString("emp_mobile"));
//                            Intent in = new Intent(Login_Activity.this, MainActivity.class);
//                            in.putExtra("request_id", "");
//                            in.putExtra("estimation_cost", "");
//                            in.putExtra("start_time_from", "");
//                            startActivity(in);
//                            startService(new Intent(Login_Activity.this, LocationService.class));
//                            finish();

                            pref.set_empid(body.getString("emp_id"));
                            if (is_verify.equalsIgnoreCase("1")) {
                                if (!body.getString("emp_image").equalsIgnoreCase("")) {
                                if (body.getString("is_approved").equalsIgnoreCase("1")) {
                                    pref.set_id(body.getString("emp_id"));
                                    pref.setusername(body.getString("username"));
                                    pref.setauth_token(body.getString("auth_token"));
                                    pref.setprofile_pic(body.getString("emp_image"));
                                    pref.setonline_status(body.getString("on_duty"));
                                    pref.setavg_rating(body.getString("avg_ratting"));
                                    pref.setemp_email(body.getString("emp_email"));
                                    pref.setemp_phn(body.getString("emp_mobile"));
                                    pref.setholder_name(bank.getString("holder_name"));
                                    pref.setifsccode(bank.getString("ifsc_code"));
                                    pref.setmobile_no(bank.getString("mobile"));
                                    pref.setaccount_no(bank.getString("account_no"));

                                    Intent in = new Intent(Login_Activity.this, MainActivity.class);
                                    in.putExtra("request_id", "");
                                    in.putExtra("estimation_cost", "");
                                    in.putExtra("start_time_from", "");
                                    startActivity(in);
                                    startService(new Intent(Login_Activity.this, LocationService.class));
                                    finish();
                                    }
                                    else {
                                        AlertDialog alertDialog = new AlertDialog.Builder(Login_Activity.this).create();
                                        alertDialog.setTitle("Wait");
                                        alertDialog.setMessage("Please wait for Admin approval");
                                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                });

                                        alertDialog.show();
                                    }

                                } else {

                                    Intent in = new Intent(Login_Activity.this, Documents.class);
                                    in.putExtra("emp_id", body.getString("emp_id"));
                                    pref.set_empid(body.getString("emp_id"));
                                    startActivity(in);
                                }
                            } else {
                                Intent in = new Intent(Login_Activity.this, OTPActivity.class);
                                in.putExtra("emp_id", body.getString("emp_id"));
                                pref.set_empid(body.getString("emp_id"));
                                startActivity(in);
                            }


                        } else {
                            Util.showToast(Login_Activity.this, status.getString("message"));
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

    private void Forgot_password(String email) {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_EMAIL, email);

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.Forgot_Password, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            forgotpassword_dialog.dismiss();
                            Util.showToast(Login_Activity.this, status.getString("message"));
                        } else {
                            Util.showToast(Login_Activity.this, status.getString("message"));
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

    @Override
    protected void onResume() {
        super.onResume();
        //Now lets connect to the API
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();

//            Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    public void onLocationChanged(Location location) {

        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        pref.setCurrentLat(String.valueOf(currentLatitude));
        pref.setCurrentLong(String.valueOf(currentLongitude));

//        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }



}

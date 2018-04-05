package com.taxilive_driver.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    TextView continue_btn, sign_in,term_condition;
    ProgressDialog mDialog;
    EditText full_name, email, contact_no, password, invite_code, mobile_code,vehicle_no,year,model,make,address,surname;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    SavePref pref;
    CheckBox checkbox;
    Spinner city;
    String city_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mDialog = new ProgressDialog(SignupActivity.this);
        pref = new SavePref(SignupActivity.this);

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

        ArrayAdapter aa = new ArrayAdapter(this,R.layout.adapter_spinner,getResources().getStringArray(R.array.city_name));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        city.setAdapter(aa);
        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                city_name = parent.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initialize() {
        continue_btn = (TextView) findViewById(R.id.continue_btn);

        sign_in = (TextView) findViewById(R.id.sign_in);
        full_name = (EditText) findViewById(R.id.full_name);
        term_condition = (TextView) findViewById(R.id.term_condition);
        email = (EditText) findViewById(R.id.email);
        vehicle_no = (EditText) findViewById(R.id.vehicle_no);
        make = (EditText) findViewById(R.id.make);
        model = (EditText) findViewById(R.id.model);
        year = (EditText) findViewById(R.id.year);
        contact_no = (EditText) findViewById(R.id.contact_no);
        mobile_code = (EditText) findViewById(R.id.mobile_code);
        password = (EditText) findViewById(R.id.password);
        address = (EditText) findViewById(R.id.address);
        surname = (EditText) findViewById(R.id.surname);
        city = (Spinner) findViewById(R.id.city);
        invite_code = (EditText) findViewById(R.id.invite_code);
        continue_btn.setOnClickListener(this);
        sign_in.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.continue_btn:
                if (full_name.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter FullName", SignupActivity.this);
                }
                else if (surname.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Last Name", SignupActivity.this);
                }
                else if (email.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Email", SignupActivity.this);
                }
                else if (!Util.isValidEmail(email.getText().toString())) {
                    Util.showSnackBar(v, "Please Enter Valid Email Address", SignupActivity.this);
                }

                else if (contact_no.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Contact Number", SignupActivity.this);
                }
                else if (contact_no.getText().toString().startsWith("0")) {
                    Util.showSnackBar(v, "Contact Number should not be start with 0", SignupActivity.this);
                }
                else if (password.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Password", SignupActivity.this);
                }
//                else if (city.getText().toString().equalsIgnoreCase("")) {
//                    Util.showSnackBar(v, "Please Enter City", SignupActivity.this);
//                }
                else if (vehicle_no.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Vehicle Number", SignupActivity.this);
                }
                else if (make.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Make", SignupActivity.this);
                }
                else if (model.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Model", SignupActivity.this);
                }
                else if (year.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Year", SignupActivity.this);
                }

                else if (address.getText().toString().equalsIgnoreCase("")) {
                    Util.showSnackBar(v, "Please Enter Address", SignupActivity.this);
                }

                else {
                    Signup();

                }

                break;
            case R.id.sign_in:
                Intent in = new Intent(SignupActivity.this, Login_Activity.class);
                startActivity(in);
                finish();
                break;

        }
    }

    private void Signup() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_PHONE, contact_no.getText().toString());
        formBuilder.add(Parameters.MOBILE_CODE, "27");
        formBuilder.add(Parameters.EMP_EMAIL, email.getText().toString());
        formBuilder.add(Parameters.EMP_PASSWORD, password.getText().toString());
        formBuilder.add(Parameters.NAME, full_name.getText().toString());
        formBuilder.add(Parameters.SURNAME, surname.getText().toString());
        formBuilder.add(Parameters.LAT, String.valueOf(currentLatitude));
        formBuilder.add(Parameters.LONG, String.valueOf(currentLongitude));
        formBuilder.add(Parameters.EMP_LOCATION, city_name);
        formBuilder.add(Parameters.DEVICE_TOKEN, pref.getDeviceToken(SignupActivity.this,"token"));
        formBuilder.add(Parameters.DEVICE_TYPE, "1");
        formBuilder.add(Parameters.INVITE_CODE, invite_code.getText().toString());
        formBuilder.add(Parameters.USER_NAME, full_name.getText().toString());
        formBuilder.add(Parameters.Vehicle_Number, vehicle_no.getText().toString());
        formBuilder.add(Parameters.ModelName, model.getText().toString());
        formBuilder.add(Parameters.Model_No, make.getText().toString());
        formBuilder.add(Parameters.ModelYear, year.getText().toString());
        formBuilder.add(Parameters.Physical_Address, address.getText().toString());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(this, AllDriveAppConstants.Driver_Signup, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
//                            pref.set_empid(body.getString("emp_id"));


                            Intent in = new Intent(SignupActivity.this, OTPActivity.class);
                            in.putExtra("emp_id",body.getString("emp_id"));
                            startActivity(in);

                        } else {
                            Util.showToast(SignupActivity.this, status.getString("message"));
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

//        Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();
    }
}

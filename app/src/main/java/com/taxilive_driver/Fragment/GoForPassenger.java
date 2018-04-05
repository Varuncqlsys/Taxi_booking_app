package com.taxilive_driver.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.model.CameraPosition;
import com.taxilive_driver.Activity.MainActivity;
import com.taxilive_driver.Activity.Stop_TripActivity;
import com.taxilive_driver.R;
import com.taxilive_driver.Services.LatLngInterpolator;
import com.taxilive_driver.Services.MarkerAnimation;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.parser.DataParser;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.taxilive_driver.util.Util;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.RequestBody;

public class GoForPassenger extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private GoogleMap mMap;
    View view;

    private float currentZoom = 13;
    ArrayList<LatLng> MarkerPoints;
    String presentlat = "", presentlong = "";

    private SavePref savePref;
    private Context context;
    String request_id, user_id, lat_from, lng_from, is_scheduled, lat_to, lng_to, strstart_trip = "",
            username = "", req_location_from = "", estimation_cost = "", contact = "", image = "";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    Location mLastLocation;
    Marker mCurrLocationMarker;

    TextView start_trip_btn, name, location, estimated_cost, txtcontact, stop_trip_btn;
    ProgressDialog mDialog;
    RelativeLayout user_detail;
    ImageView user_image;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_go_for_passenger, container, false);
        }

        context = getActivity();
        savePref = new SavePref(getActivity());
        mDialog = new ProgressDialog(getActivity());


        MarkerPoints = new ArrayList<>();
        user_image = (ImageView) view.findViewById(R.id.user_image);
        name = (TextView) view.findViewById(R.id.name);
        location = (TextView) view.findViewById(R.id.location);
        estimated_cost = (TextView) view.findViewById(R.id.estimated_cost);
        stop_trip_btn = (TextView) view.findViewById(R.id.stop_trip_btn);
        txtcontact = (TextView) view.findViewById(R.id.contact);

        final Bundle bundle = this.getArguments();
        request_id = bundle.getString("request_id");
        user_id = bundle.getString("user_id");
        lat_from = bundle.getString("req_lat_from");
        lng_from = bundle.getString("req_lng_from");
        is_scheduled = bundle.getString("is_scheduled");
        lat_to = bundle.getString("req_lat_to");
        lng_to = bundle.getString("req_lng_to");
        username = bundle.getString("username");
        image = bundle.getString("user_image");
        req_location_from = bundle.getString("req_location_from");
        estimation_cost = "ZAR (R)" + bundle.getString("estimation_cost");
        contact = bundle.getString("contact");
        estimated_cost.setText(estimation_cost);
        name.setText(username);
        location.setText(req_location_from);

        Glide.with(getActivity()).load(image)
                .placeholder(R.drawable.user)
                .override(150, 150).centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.RESULT).into(user_image);

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(2 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000);


        txtcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + contact));
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(callIntent);


//                TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);  //gets the current TelephonyManager
//                if (tm.getSimState() != TelephonyManager.SIM_STATE_ABSENT){
//
//                }
//                else {
//                    Toast.makeText(getActivity(), "Please insert SIM card", Toast.LENGTH_SHORT).show();
//                }


            }
        });


        start_trip_btn = (TextView) view.findViewById(R.id.start_trip_btn);
        user_detail = (RelativeLayout) view.findViewById(R.id.user_detail);


//        go_for_passenger_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Util.showToast(context, "Navigation Started");
//
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +
//                        "&daddr=" + String.valueOf(lat_from) + "," + String.valueOf(lng_from)));
//                context.startActivity(intent);
//                go_for_passenger_btn.setVisibility(View.GONE);
//                start_trip_btn.setVisibility(View.VISIBLE);
//                strstart_trip = "start";
//            }
//        });

        start_trip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_trip();
            }
        });
        stop_trip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop_trip();
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else {
//            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


        LatLng from = new LatLng(Double.valueOf(lat_from), Double.valueOf(lng_from));
        mMap.addMarker(new MarkerOptions().position(from).title("from location"));

        LatLng to = new LatLng(Double.valueOf(lat_to), Double.valueOf(lng_to));
        mMap.addMarker(new MarkerOptions().position(to).title("to location"));

//        LatLng from = new LatLng(Double.valueOf(presentlat), Double.valueOf(presentlong));
//        mMap.addMarker(new MarkerOptions().position(from).title("from location"));
//
//        LatLng to = new LatLng(Double.valueOf(lat_from), Double.valueOf(lng_from));
//        mMap.addMarker(new MarkerOptions().position(to).title("to location"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(7));

        MarkerPoints.add(from);
        MarkerPoints.add(to);

        if (MarkerPoints.size() >= 2) {
            LatLng origin = MarkerPoints.get(0);
            LatLng dest = MarkerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());

            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
            //move map camera

        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {

            @Override
            public void onCameraChange(CameraPosition pos) {
                currentZoom = pos.zoom;
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
//        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mLastLocation != null) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//            presentlat = String.valueOf(mLastLocation.getLatitude());
//            presentlong = String.valueOf(mLastLocation.getLongitude());
//
//
//        }
        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }


//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
//        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(getActivity(), CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("change", "called");
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        presentlat = String.valueOf(location.getLatitude());
        presentlong = String.valueOf(location.getLongitude());


        if (mCurrLocationMarker != null) {
//            Toast.makeText(context, ""+ presentlat + presentlong, Toast.LENGTH_SHORT).show();
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, currentZoom));
            MarkerAnimation.animateMarkerToICS(mCurrLocationMarker, latLng, new LatLngInterpolator.Spherical());
            mCurrLocationMarker.setPosition(latLng);

//            mCurrLocationMarker.remove();
        } else {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
//            Toast.makeText(context, ""+ presentlat + presentlong, Toast.LENGTH_SHORT).show();
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        //Place current location marker


        //move map camera


        //stop location updates
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
//        }
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }


    private void stop_trip() {

        if (presentlat.equals("")) {
            presentlat = savePref.getCurrentLat();
            presentlong = savePref.getCurrentLong();
        }

        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, savePref.getid());
        formBuilder.add(Parameters.User_id, user_id);
        formBuilder.add(Parameters.Request_id, request_id);
        formBuilder.add(Parameters.Stop_TripLat, presentlat);
        formBuilder.add(Parameters.Stop_TripLong, presentlong);
        formBuilder.add(Parameters.CheckOut, String.valueOf(System.currentTimeMillis() / 1000));
        formBuilder.add(Parameters.Status, "4");

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.Stop_Trip, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {

                            final Dialog finish_ride = new Dialog(getActivity(), R.style.Theme_Dialog);
                            finish_ride.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            finish_ride.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                            finish_ride.setContentView(R.layout.popup_trip_finish);
                            finish_ride.setCancelable(false);
//                            Window window = finish_ride.getWindow();
//                            WindowManager.LayoutParams wlp = window.getAttributes();
//                            wlp.gravity = Gravity.CENTER;
//                            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
//                            window.setAttributes(wlp);
                            finish_ride.show();
                            TextView ok = (TextView) finish_ride.findViewById(R.id.ok);
                            ok.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent in = new Intent(getActivity(), MainActivity.class);
                                    savePref.setuser_id("");
                                    in.putExtra("request_id", "");
                                    startActivity(in);

                                }
                            });


                        } else {
                            Util.showToast(getActivity(), status.getString("message"));
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

    private void start_trip() {

        if (presentlat.equals("")) {
            presentlat = savePref.getCurrentLat();
            presentlong = savePref.getCurrentLong();
        }

        mDialog.show();

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, savePref.getid());
        formBuilder.add(Parameters.AUTH_TOKEN, savePref.getauth_token());
        formBuilder.add(Parameters.Request_id, request_id);
        formBuilder.add(Parameters.LAT, presentlat);
        formBuilder.add(Parameters.LONG, presentlong);
        formBuilder.add(Parameters.Check_in, String.valueOf(System.currentTimeMillis() / 1000));

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.Start_Trip, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            savePref.setuser_id(body.getString("user_id"));

//                            Intent in = new Intent(getActivity(), Stop_TripActivity.class);
//                            in.putExtra("req_lat_from", body.getString("req_lat_from"));
//                            in.putExtra("req_lng_from", body.getString("req_lng_from"));
//                            in.putExtra("req_lat_to", body.getString("req_lat_to"));
//                            in.putExtra("req_lng_to", body.getString("req_lng_to"));
//                            in.putExtra("user_id", body.getString("user_id"));
//                            in.putExtra("emp_id", body.getString("emp_id"));
//                            in.putExtra("request_id", body.getString("request_id"));
//                            startActivity(in);
                            user_detail.setVisibility(View.VISIBLE);
                            stop_trip_btn.setVisibility(View.VISIBLE);
                            start_trip_btn.setVisibility(View.GONE);

//                            start_trip_btn.setBackgroundResource(R.drawable.stop_trip);
//                            strstart_trip = "stop";

                        } else {
                            Util.showToast(getActivity(), status.getString("message"));
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
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

}
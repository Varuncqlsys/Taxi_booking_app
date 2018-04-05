package com.taxilive_driver.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.taxilive_driver.Adapter.PreviousTripAdapter;
import com.taxilive_driver.Adapter.Upcoming_adapter;
import com.taxilive_driver.Model.PreviousTripRowItems;
import com.taxilive_driver.Model.Upcoming_data;
import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class PreviousTrip extends Fragment {
    ListView listView, upcoming_list;
    ArrayList<PreviousTripRowItems> rowItems;
    ArrayList<Upcoming_data> upcoming_dataArrayList;
    View view;
    SavePref pref;
    PreviousTripAdapter adapter;
    RadioButton upcoming, trip_history;
    RadioGroup radio_grp;
    Upcoming_adapter upcoming_adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_previous_trip, container, false);
        upcoming = (RadioButton) view.findViewById(R.id.upcoming);
        trip_history = (RadioButton) view.findViewById(R.id.trip_history);
        listView = (ListView) view.findViewById(R.id.trip_historylist);
        upcoming_list = (ListView) view.findViewById(R.id.upcoming_list);
        radio_grp = (RadioGroup) view.findViewById(R.id.radio_grp);
        pref = new SavePref(getActivity());
        rowItems = new ArrayList<PreviousTripRowItems>();
        upcoming_dataArrayList = new ArrayList<Upcoming_data>();
        trip_history.setChecked(true);


        /*PreviousTripRowItems item = new PreviousTripRowItems(titles);
        rowItems.add(item);
*/

        adapter = new PreviousTripAdapter(getActivity(), rowItems);
        listView.setAdapter(adapter);

        upcoming_adapter = new Upcoming_adapter(getActivity(), upcoming_dataArrayList,PreviousTrip.this );
        upcoming_list.setAdapter(upcoming_adapter);

        previous_trip();
        upcoming_trip();
        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                // find which radio button is selected

                if (checkedId == R.id.upcoming) {
                    upcoming_list.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);

                } else if (checkedId == R.id.trip_history) {
                    upcoming_list.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                }
            }


        });

        return view;
    }

    private void previous_trip() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.EMP_ID, pref.getid());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.TripHistory, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONArray body = jsonObject.getJSONArray("body");
                            for (int i = 0; i < body.length(); i++) {
                                JSONObject body_obj = body.getJSONObject(i);
                                PreviousTripRowItems previousTripRowItems = new PreviousTripRowItems();
                                previousTripRowItems.setRequest_id(body_obj.getString("request_id"));
                                previousTripRowItems.setEmp_id(body_obj.getString("emp_id"));
                                previousTripRowItems.setReq_lat_from(body_obj.getString("req_lat_from"));
                                previousTripRowItems.setReq_lng_from(body_obj.getString("req_lng_from"));
                                previousTripRowItems.setReq_lng_to(body_obj.getString("req_lng_to"));
                                previousTripRowItems.setReq_lat_to(body_obj.getString("req_lat_to"));
//                                previousTripRowItems.setEmp_image(body_obj.getString("emp_image"));
                                previousTripRowItems.setUsername(body_obj.getString("username"));
                                previousTripRowItems.setUser_image(body_obj.getString("user_image"));
                                previousTripRowItems.setTotal_amt(body_obj.getString("total_amt"));
                                previousTripRowItems.setStart_date(body_obj.getString("start_date"));
                                previousTripRowItems.setStart_time_from(body_obj.getString("start_time_from"));
                                rowItems.add(previousTripRowItems);

                            }
                            adapter.notifyDataSetChanged();


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

    public void apicall()
    {
        upcoming_dataArrayList = new ArrayList<>();
        upcoming_trip();
    }
    private void upcoming_trip() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.User_id, pref.getid());
        formBuilder.add(Parameters.Type, "2");

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.Upcoming_trip, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONArray body = jsonObject.getJSONArray("body");
                            for (int i = 0; i < body.length(); i++) {
                                JSONObject body_obj = body.getJSONObject(i);
                                Upcoming_data upcoming_data = new Upcoming_data();
                                upcoming_data.setRequest_id(body_obj.getString("request_id"));
                                upcoming_data.setUser_id(body_obj.getString("user_id"));
                                upcoming_data.setEmp_id(body_obj.getString("emp_id"));
                                upcoming_data.setReq_lat_from(body_obj.getString("req_lat_from"));
                                upcoming_data.setReq_lng_from(body_obj.getString("req_lng_from"));
                                upcoming_data.setReq_lng_to(body_obj.getString("req_lng_to"));
                                upcoming_data.setReq_lat_to(body_obj.getString("req_lat_to"));
                                upcoming_data.setReq_location_from(body_obj.getString("req_location_from"));
                                upcoming_data.setReq_location_to(body_obj.getString("req_location_to"));
                                upcoming_data.setEstimation_cost(body_obj.getString("estimation_cost"));
                                upcoming_data.setCreated_date(body_obj.getString("created_date"));

                                upcoming_dataArrayList.add(upcoming_data);

                            }
                            upcoming_adapter = new Upcoming_adapter(getActivity(), upcoming_dataArrayList,PreviousTrip.this );
                            upcoming_list.setAdapter(upcoming_adapter);

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


    private void cancel() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.User_id, pref.getid());
        formBuilder.add(Parameters.Type, "2");

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.Upcoming_trip, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONArray body = jsonObject.getJSONArray("body");
                            for (int i = 0; i < body.length(); i++) {
                                JSONObject body_obj = body.getJSONObject(i);
                                Upcoming_data upcoming_data = new Upcoming_data();
                                upcoming_data.setRequest_id(body_obj.getString("request_id"));
                                upcoming_data.setEmp_id(body_obj.getString("emp_id"));
                                upcoming_data.setReq_lat_from(body_obj.getString("req_lat_from"));
                                upcoming_data.setReq_lng_from(body_obj.getString("req_lng_from"));
                                upcoming_data.setReq_lng_to(body_obj.getString("req_lng_to"));
                                upcoming_data.setReq_lat_to(body_obj.getString("req_lat_to"));
                                upcoming_data.setReq_location_from(body_obj.getString("req_location_from"));
                                upcoming_data.setReq_location_to(body_obj.getString("req_location_to"));
                                upcoming_data.setEstimation_cost(body_obj.getString("estimation_cost"));
                                upcoming_data.setCreated_date(body_obj.getString("created_date"));

                                upcoming_dataArrayList.add(upcoming_data);

                            }
                            upcoming_adapter.notifyDataSetChanged();


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

}

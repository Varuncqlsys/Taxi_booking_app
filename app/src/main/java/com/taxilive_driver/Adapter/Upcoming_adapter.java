package com.taxilive_driver.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.taxilive_driver.Fragment.PreviousTrip;
import com.taxilive_driver.Model.Upcoming_data;
import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * Created by ADT on 6/30/2017.
 */

public class Upcoming_adapter extends BaseAdapter {

    Context context;
    ArrayList<Upcoming_data> rowItems;
    PreviousTrip previousTrip;

    public Upcoming_adapter(Context context, ArrayList<Upcoming_data> items, PreviousTrip previousTrip) {
        this.context = context;
        this.rowItems = items;
        this.previousTrip = previousTrip;
    }

    /*private view holder class*/
    private class ViewHolder {

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        Upcoming_adapter.ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.adapter_upcoming, null);
            holder = new Upcoming_adapter.ViewHolder();
            TextView date_time = (TextView) convertView.findViewById(R.id.date_time);
            TextView pickup_from_text = (TextView) convertView.findViewById(R.id.pickup_from_text);
            TextView destination_text = (TextView) convertView.findViewById(R.id.destination_text);
            TextView confirm_ride = (TextView) convertView.findViewById(R.id.confirm_ride);
            TextView cancel_ride = (TextView) convertView.findViewById(R.id.cancel_ride);
            date_time.setText(getDateCurrentTimeZone(Long.parseLong(rowItems.get(position).getCreated_date())));
            pickup_from_text.setText(rowItems.get(position).getReq_location_from());
            destination_text.setText(rowItems.get(position).getReq_location_to());
            confirm_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm(rowItems.get(position).getUser_id(), rowItems.get(position).getEmp_id(), rowItems.get(position).getRequest_id(), "1", String.valueOf(position));

                }
            });
            cancel_ride.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirm(rowItems.get(position).getUser_id(), rowItems.get(position).getEmp_id(), rowItems.get(position).getRequest_id(), "2", String.valueOf(position));
                }
            });

            convertView.setTag(holder);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String getDateCurrentTimeZone(long timestamp) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);

            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
            String dayOfTheWeek = (String) android.text.format.DateFormat.format("EEEE", dayNum);

            TimeZone tz = TimeZone.getDefault();
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM");
            Date currenTimeZone = (Date) calendar.getTime();
            String date = dayOfTheWeek + "," + sdf.format(currenTimeZone);

            return date;
        } catch (Exception e) {

        }
        return "";
    }

    private void confirm(String user_id, String emp_id, String request_id, final String api_status, final String position) {

        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, emp_id);
        formBuilder.add(Parameters.User_id, user_id);
        formBuilder.add(Parameters.Request_id, request_id);
        formBuilder.add(Parameters.Status, api_status);


        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(context, AllDriveAppConstants.confirm_cancel_ride, formBody) {
            @Override
            public void getValueParse(String result) {

                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            if (api_status.equals("1")) {
                                Util.showToast(context, "Request Confirm");
                            } else {
                                Util.showToast(context, "Request Cancel");

                                previousTrip.apicall();
//                                rowItems.remove(position);
//                                notifyDataSetChanged();
                            }


                        } else {
                            Util.showToast(context, status.getString("message"));
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

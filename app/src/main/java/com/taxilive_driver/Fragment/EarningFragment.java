package com.taxilive_driver.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.taxilive_driver.R;
import com.taxilive_driver.parser.AllDriveAppConstants;
import com.taxilive_driver.util.GetAsync;
import com.taxilive_driver.util.Parameters;
import com.taxilive_driver.util.SavePref;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class EarningFragment extends Fragment {
    View view;
    float monday,tuesday,wednesday,thursday,friday,sat,sun;
    BarChart chart;
    SavePref pref;
    TextView week_earn, month_earn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_earning, container, false);
        pref = new SavePref(getActivity());
        initialize();
        getearing_data();

        return view;
    }

    private void initialize() {
        chart = (BarChart) view.findViewById(R.id.chart);
        week_earn = (TextView) view.findViewById(R.id.week_earn);
        month_earn = (TextView) view.findViewById(R.id.month_earn);
    }

    private ArrayList<BarDataSet> getDataSet() {
        ArrayList<BarDataSet> dataSets = null;

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        BarEntry v1e1 = new BarEntry(monday, 0);
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(tuesday, 1);
        valueSet1.add(v1e2);
        BarEntry v1e3 = new BarEntry(wednesday, 2);
        valueSet1.add(v1e3);
        BarEntry v1e4 = new BarEntry(thursday, 3);
        valueSet1.add(v1e4);
        BarEntry v1e5 = new BarEntry(friday, 4);
        valueSet1.add(v1e5);
        BarEntry v1e6 = new BarEntry(sat, 5);
        valueSet1.add(v1e6);
        BarEntry v1e7 = new BarEntry(sun, 6);
        valueSet1.add(v1e7);

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Weekly Earn");
//        barDataSet1.setColor(R.color.green);
        barDataSet1.setColor(Color.parseColor("#32CD32"));

        dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);

        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("M");
        xAxis.add("TU");
        xAxis.add("W");
        xAxis.add("TH");
        xAxis.add("F");
        xAxis.add("SA");
        xAxis.add("SU");
        return xAxis;
    }


    private void getearing_data() {

        FormBody.Builder formBuilder = new FormBody.Builder();


        formBuilder.add(Parameters.EMP_ID, pref.getid());

        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.GetEarningData, formBody) {
            @Override
            public void getValueParse(String result) {
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            week_earn.setText("ZAR (R)" + body.getString("monthly_amount"));
                            month_earn.setText("ZAR (R)" + body.getString("weekly_amount"));
                            monday = Float.parseFloat(body.getString("Monday"));
                            tuesday = Float.parseFloat(body.getString("Tuesday"));
                            wednesday = Float.parseFloat(body.getString("Wednesday"));
                            thursday = Float.parseFloat(body.getString("Thrusday"));
                            friday = Float.parseFloat(body.getString("Friday"));
                            sat = Float.parseFloat(body.getString("Saturday"));
                            sun = Float.parseFloat(body.getString("Sunday"));


                            BarData data = new BarData(getXAxisValues(), getDataSet());
                            chart.setData(data);
                            chart.animateXY(2000, 2000);
                            chart.invalidate();
                            //chart.setBackgroundColor(Color.TRANSPARENT);
                            chart.setDrawGridBackground(false);
                            chart.getAxisLeft().setTextColor(Color.rgb(255, 255, 255)); // left y-axis
                            chart.getXAxis().setTextColor(Color.rgb(255, 255, 255));
                            chart.getLegend().setTextColor(Color.rgb(255, 255, 255));

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
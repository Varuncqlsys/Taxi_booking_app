package com.taxilive_driver.Fragment;

import android.app.ProgressDialog;
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
import com.taxilive_driver.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;
import okhttp3.RequestBody;


public class RatingFragment extends Fragment {
    View view;
    TextView rating_text, current_rating_text, requests_accepted_text, trips_cancelled_text;
    ProgressDialog mDialog;
    SavePref pref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_rating, container, false);
        mDialog = new ProgressDialog(getActivity());
        pref = new SavePref(getActivity());

        initialize();
        getrating();

        return view;
    }

    private void initialize() {
        rating_text = (TextView) view.findViewById(R.id.rating_text);
        current_rating_text = (TextView) view.findViewById(R.id.current_rating_text);
        requests_accepted_text = (TextView) view.findViewById(R.id.requests_accepted_text);
        trips_cancelled_text = (TextView) view.findViewById(R.id.trips_cancelled_text);

    }

    private void getrating() {
        mDialog.show();
        FormBody.Builder formBuilder = new FormBody.Builder();
        formBuilder.add(Parameters.EMP_ID, pref.getid());


        RequestBody formBody = formBuilder.build();
        GetAsync mAsync = new GetAsync(getActivity(), AllDriveAppConstants.GetRating, formBody) {
            @Override
            public void getValueParse(String result) {
                mDialog.dismiss();
                if (result != null && !result.equalsIgnoreCase("")) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject status = jsonObject.getJSONObject("status");
                        if (status.getString("code").equalsIgnoreCase("1")) {
                            JSONObject body = jsonObject.getJSONObject("body");
                            rating_text.setText(body.getString("avgrating"));
                            current_rating_text.setText(body.getString("avgrating"));
                            requests_accepted_text.setText(body.getString("request_accepted"));
                            trips_cancelled_text.setText(body.getString("trip_canceled"));


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

}

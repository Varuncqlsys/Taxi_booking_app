package com.taxilive_driver.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.taxilive_driver.Model.PreviousTripRowItems;
import com.taxilive_driver.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PreviousTripAdapter extends BaseAdapter
{
    Context context;
    List<PreviousTripRowItems> rowItems;

    public PreviousTripAdapter(Context context, List<PreviousTripRowItems> items)
    {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder
    {

    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.previous_trip_row, null);
            holder = new ViewHolder();
            CircleImageView profile_pic = (CircleImageView) convertView.findViewById(R.id.profile_pic);
            TextView username = (TextView) convertView.findViewById(R.id.username);
            TextView amount = (TextView) convertView.findViewById(R.id.amount);
            TextView start_date = (TextView) convertView.findViewById(R.id.start_date);
            TextView time_text = (TextView) convertView.findViewById(R.id.time_text);
            amount.setText("ZAR (R)"+rowItems.get(position).getTotal_amt());
            username.setText(rowItems.get(position).getUsername());
            start_date.setText(rowItems.get(position).getStart_date());
            time_text.setText(rowItems.get(position).getStart_time_from());
            Glide.with(context).load(rowItems.get(position).getEmp_image()).into(profile_pic);

            convertView.setTag(holder);
        }


        return convertView;
    }

    @Override
    public int getCount()
    {
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
}
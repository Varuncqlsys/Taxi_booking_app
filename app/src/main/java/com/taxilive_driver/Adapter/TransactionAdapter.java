package com.taxilive_driver.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.taxilive_driver.Model.Transaction_data;
import com.taxilive_driver.R;

import java.util.ArrayList;

/**
 * Created by ADT on 6/27/2017.
 */

public class TransactionAdapter extends BaseAdapter {

    Context context;
    ArrayList<Transaction_data> rowItems;
    String data;

    public TransactionAdapter(Context context, ArrayList<Transaction_data> items, String data) {
        this.context = context;
        this.rowItems = items;
        this.data = data;
    }

    /*private view holder class*/
    private class ViewHolder {

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.adapter_payment, null);
        TextView month_name = (TextView) convertView.findViewById(R.id.month_name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);
        month_name.setText(rowItems.get(position).getMonth_name());
        amount.setText(rowItems.get(position).getTotal_amt());
        if (data.equals("week")) {
            image.setVisibility(View.GONE);
        }


        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}

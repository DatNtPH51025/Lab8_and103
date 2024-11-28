package com.example.lab8_ph51025_and103.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lab8_ph51025_and103.R;
import com.example.lab8_ph51025_and103.model.District;

import java.util.ArrayList;

public class Adapter_Item_District_Select_GHN extends BaseAdapter {
    Context context;
    ArrayList<District> list;

    public Adapter_Item_District_Select_GHN(Context context, ArrayList<District> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_district_spinner,parent,false);
        TextView tv_dictrict = convertView.findViewById(R.id.textViewDistrictName);
        District district = list.get(position);
        tv_dictrict.setText(district.getDistrictName());
        return convertView;
    }
}

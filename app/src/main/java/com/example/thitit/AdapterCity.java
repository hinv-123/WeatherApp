package com.example.thitit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterCity extends BaseAdapter {
    Context context;
    ArrayList<Thanhpho> arrayList;
    public AdapterCity(Context context, ArrayList<Thanhpho> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.dsthanhpho,null);

        Thanhpho tp = arrayList.get(position);

        TextView currentcity = view.findViewById(R.id.currentcity);
        TextView currentcountry = view.findViewById(R.id.currentcountry);
        TextView currenttemp = view.findViewById(R.id.currenttemp);

        currentcity.setText(tp.city);
        currentcountry.setText(tp.country);
        currenttemp.setText(tp.temp + "  ̊C");

        return view;
    }
}

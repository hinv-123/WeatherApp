package com.example.thitit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterHour extends RecyclerView.Adapter<AdapterHour.HourViewHolder>{
    Context context;
    ArrayList<Dubaotheogio> list;
    public AdapterHour(Context context, ArrayList<Dubaotheogio> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dscacgio,parent,false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        Dubaotheogio dubao = list.get(position);
        holder.nexttemp.setText(dubao.nexttemp + " ̊C");
        holder.nextwind.setText(dubao.nextwind + " m/s");
        Picasso.get().load("https://www.weatherbit.io/static/img/icons/"+dubao.nextimg+".png").into(holder.nextimg);
        holder.hour.setText(dubao.hour);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class HourViewHolder extends RecyclerView.ViewHolder {
        TextView hour;
        TextView nexttemp;
        ImageView nextimg;
        TextView nextwind;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            hour = itemView.findViewById(R.id.hour);
            nexttemp = itemView.findViewById(R.id.nexttemp);
            nextimg = itemView.findViewById(R.id.nextimg);
            nextwind = itemView.findViewById(R.id.nextwind);
        }

    }
}

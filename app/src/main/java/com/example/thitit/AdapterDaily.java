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
import java.util.ArrayList;

public class AdapterDaily extends RecyclerView.Adapter<AdapterDaily.DailyViewHolder>{
        Context context;
        ArrayList<Dubaotheongay> list;
public AdapterDaily(Context context, ArrayList<Dubaotheongay> list) {
        this.context = context;
        this.list = list;
        }
@NonNull
@Override
public DailyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dscacngay,parent,false);
        return new DailyViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull DailyViewHolder holder, int position) {
        Dubaotheongay dubao = list.get(position);
        holder.daily.setText(dubao.daily);
        holder.dailymax.setText(dubao.dailymax + "  ̊C");
        holder.dailymin.setText(dubao.dailymin + "  ̊C");
        holder.dailywind.setText(dubao.dailywind + " m/s");
        Picasso.get().load("https://www.weatherbit.io/static/img/icons/"+dubao.dailyimg+".png").into(holder.dailyimg);
        }

@Override
public int getItemCount() {
        return list.size();
        }

public class DailyViewHolder extends RecyclerView.ViewHolder {
    TextView daily,dailymax,dailymin,dailywind;
    ImageView dailyimg;

    public DailyViewHolder(@NonNull View itemView) {
        super(itemView);
        daily = itemView.findViewById(R.id.daily);
        dailymax = itemView.findViewById(R.id.dailymax);
        dailymin = itemView.findViewById(R.id.dailymin);
        dailywind = itemView.findViewById(R.id.dailywind);
        dailyimg = itemView.findViewById(R.id.dailyimg);
    }

}
}

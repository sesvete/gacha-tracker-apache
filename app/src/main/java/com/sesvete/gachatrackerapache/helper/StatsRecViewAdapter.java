package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.model.Statistic;

import java.util.ArrayList;

public class StatsRecViewAdapter extends RecyclerView.Adapter<StatsRecViewAdapter.ViewHolder>{

    private ArrayList<Statistic> statisticList = new ArrayList<>();
    private Context context;

    public StatsRecViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stats_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtStatsListDescription.setText(statisticList.get(position).getStatisticDescription());
        holder.txtStatsListNumber.setText(String.valueOf(statisticList.get(position).getStatisticNumber()));
    }

    @Override
    public int getItemCount() {
        return statisticList.size();
    }

    public void setStatisticList(ArrayList<Statistic> statisticList){
        this.statisticList = statisticList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtStatsListDescription;
        private TextView txtStatsListNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtStatsListDescription = itemView.findViewById(R.id.txt_stats_list_description);
            txtStatsListNumber = itemView.findViewById(R.id.txt_stats_list_number);
        }
    }

}

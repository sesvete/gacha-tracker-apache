package com.sesvete.gachatrackerapache.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.DatabaseHelperMariaDB;
import com.sesvete.gachatrackerapache.helper.StatsRecViewAdapter;
import com.sesvete.gachatrackerapache.model.Statistic;

import java.util.ArrayList;


public class StatsFragment extends Fragment {

    private MaterialButton btnStatsPersonal;
    private MaterialButton btnStatsGlobal;
    private TextView txtStatsTitle;
    private RecyclerView recyclerViewStats;
    private String userId;
    private int uid;

    private String game;
    private String bannerType;

    private ArrayList<Statistic> statisticList;
    private StatsRecViewAdapter adapter;



    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GachaTrackerPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", null);
        uid = Integer.parseInt(userId);

        game = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("game", "genshin_impact");
        bannerType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("banner", "limited");

        btnStatsGlobal = view.findViewById(R.id.btn_stats_global);
        btnStatsPersonal = view.findViewById(R.id.btn_stats_personal);
        txtStatsTitle = view.findViewById(R.id.txt_stats_title);
        recyclerViewStats = view.findViewById(R.id.recycler_view_stats);

        statisticList = new ArrayList<>();
        adapter = new StatsRecViewAdapter(getContext());
        recyclerViewStats.setAdapter(adapter);

        //initial Load Personal stats
        onPersonalPress(txtStatsTitle, btnStatsPersonal, btnStatsGlobal);
        DatabaseHelperMariaDB.getPersonalStats(getContext(), getResources(), statisticList, uid, game, bannerType, adapter);

        recyclerViewStats.setLayoutManager(new LinearLayoutManager(getContext()));
        btnStatsPersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPersonalPress(txtStatsTitle, btnStatsPersonal, btnStatsGlobal);
                DatabaseHelperMariaDB.getPersonalStats(getContext(), getResources(), statisticList, uid, game, bannerType, adapter);
            }
        });
        btnStatsGlobal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGlobalPress(txtStatsTitle, btnStatsPersonal, btnStatsGlobal);
                DatabaseHelperMariaDB.getGlobalStats(getContext(), getResources(), statisticList, game, bannerType, adapter);
            }
        });
        return view;
    }
    private void onPersonalPress(TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        txtStatsTitle.setText(R.string.personal_stats);
        btnStatsPersonal.setEnabled(false);
        btnStatsGlobal.setEnabled(true);
    }

    private void onGlobalPress(TextView txtStatsTitle, MaterialButton btnStatsPersonal, MaterialButton btnStatsGlobal){
        txtStatsTitle.setText(R.string.global_stats);
        btnStatsPersonal.setEnabled(true);
        btnStatsGlobal.setEnabled(false);
    }

}
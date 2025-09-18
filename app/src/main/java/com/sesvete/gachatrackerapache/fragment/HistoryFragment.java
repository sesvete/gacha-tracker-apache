package com.sesvete.gachatrackerapache.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.DatabaseHelperMariaDB;
import com.sesvete.gachatrackerapache.helper.HistoryRecViewAdapter;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private TextView txtHistoryBannerDescription;
    private String bannerType;
    private String userId;
    private int uid;
    private String game;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GachaTrackerPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", null);
        uid = Integer.parseInt(userId);

        game = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("game", "genshin_impact");
        bannerType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("banner", "limited");

        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
        txtHistoryBannerDescription = view.findViewById(R.id.txt_history_banner_description);

        if (bannerType.equals("standard") || bannerType.equals("bangboo")){
            txtHistoryBannerDescription.setVisibility(View.GONE);
        }

        HistoryRecViewAdapter adapter = new HistoryRecViewAdapter(getContext());

        DatabaseHelperMariaDB.retrievePullsHistory(getContext(), getResources(), uid, game, bannerType, adapter, recyclerViewHistory);

        return view;
    }
}
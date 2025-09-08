package com.sesvete.gachatrackerapache.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.DatabaseHelper;
import com.sesvete.gachatrackerapache.helper.HistoryRecViewAdapter;
import com.sesvete.gachatrackerapache.model.PulledUnit;

import java.util.ArrayList;


public class HistoryFragment extends Fragment {

    private RecyclerView recyclerViewHistory;
    private TextView txtHistoryBannerDescription;
    private String bannerType;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private String uid;
    private String game;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null){
            uid = currentUser.getUid();
        }
        game = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("game", "genshin_impact");
        bannerType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("banner", "limited");

        recyclerViewHistory = view.findViewById(R.id.recycler_view_history);
        txtHistoryBannerDescription = view.findViewById(R.id.txt_history_banner_description);

        if (bannerType.equals("standard") || bannerType.equals("bangboo")){
            txtHistoryBannerDescription.setVisibility(View.GONE);
        }

        HistoryRecViewAdapter adapter = new HistoryRecViewAdapter(getContext());

        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.retrievePullsHistory(uid, game, bannerType, new DatabaseHelper.OnRetrievePullsHistoryCallback() {
            @Override
            public void OnRetrievedPullsHistory(ArrayList<PulledUnit> pulledUnitList) {
                adapter.setPulledUnits(pulledUnitList);
            }
        });

        recyclerViewHistory.setAdapter(adapter);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
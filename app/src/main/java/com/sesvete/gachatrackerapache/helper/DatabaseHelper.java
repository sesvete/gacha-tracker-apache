/*

TODO: trenutno database helper deluje izklučno za Firebase podatkovno bazo
naredi tak, da bo delal za naše relcaijsko podatkovno bazo

package com.sesvete.gachatrackerapache.helper;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sesvete.gachatrackerapache.model.CounterProgress;
import com.sesvete.gachatrackerapache.model.PulledUnit;
import com.sesvete.gachatrackerapache.model.UserStats;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DatabaseHelper {

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final DatabaseReference rootReference = database.getReference();
    private final DatabaseReference usersReference = rootReference.child("users");

    public interface OnCheckExistingUser{
        void onCreateNewUser(String uid);
        void onRetrieveExistingData(String uid);
    }

    public interface OnCounterReceivedCallback {
        void onCounterReceived(CounterProgress counterProgress);
    }

    public interface OnCreateUserCallback{
        void onCreateUser(String uid);
    }

    public interface OnCounterUpdateCallback{
        void onCounterUpdated(boolean success);
    }

    public interface OnSavePulledUnitCallback{
        void onSavedPulledUnit(boolean success);
    }

    public interface OnRetrievePullsHistoryCallback{
        void OnRetrievedPullsHistory(ArrayList<PulledUnit> pulledUnitList);
    }

    public interface OnRetrieveNewestUnitCallback{
        void OnRetrievedNewestPulledUnit(PulledUnit newestPulledUnit);
    }

    public interface OnPathExistsCallback {
        void onPathExists(boolean exists);
    }

    public interface OnRetrievePersonalStatsCallback {
        void onPersonalStatsRetrieved(ArrayList<Integer> numOfPullsList, ArrayList<Boolean> fiftyFiftyOutcomes);
    }

    public interface OnRetrieveGlobalStatsCallback {
        void onGlobalStatsRetrieved(ArrayList<UserStats> allUserStats);
    }

    public void checkIfUserExists(String uid, OnCheckExistingUser check){
        DatabaseReference usernameReference = usersReference.child(uid);
        ValueEventListener checkForUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    writeNewUser(uid, new OnCreateUserCallback() {
                        @Override
                        public void onCreateUser(String uid) {
                            Log.d("UserSuccess", "Successfully created user");
                        }
                    });
                    check.onCreateNewUser(uid);
                } else {
                    check.onRetrieveExistingData(uid);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("checkUser", error.getMessage());
            }
        };
        usernameReference.addListenerForSingleValueEvent(checkForUserListener);
    }

    // nastavi za vse bannerje in igre strukturo podatkovne baze že ob ustvaritvi
    public void writeNewUser(String uid, OnCreateUserCallback callback){

        CounterProgress initialValues = new CounterProgress(0, false);

        String[] games = {"genshin_impact", "honkai_star_rail", "zenless_zone_zero", "tribe_nine"};
        String[][] bannerTypes = {
                {"limited", "weapon", "chronicled_wish", "standard"},
                {"limited", "light_cone", "collaboration_character", "collaboration_light_cone", "standard"},
                {"limited", "w_engine", "standard", "bangboo"},
                {"limited", "tension_card", "standard"}
        };
        for (int i = 0; i < games.length; i++) {
            String game = games[i];
            for (String banner : bannerTypes[i]) {
                usersReference.child(uid).child("games").child(game).child(banner).child("counter_progress").setValue(initialValues).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Database Creation", game + " " + banner + " banner created");
                        } else {
                            Log.d("Database Creation", "Error creating " + game + " " + banner + " banner: " + task.getException().getMessage());
                        }
                    }
                });
            }
        }
        callback.onCreateUser(uid);
    }

    public void getCounterStatus(String uid, String game, String banner, OnCounterReceivedCallback callback){
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference counterNumberReference = userNameReference.child("games").child(game).child(banner).child("counter_progress");
        counterNumberReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                CounterProgress counterProgress = dataSnapshot.getValue(CounterProgress.class);
                callback.onCounterReceived(counterProgress);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Retrieval", "Failed to retrieve counter progress: " + databaseError.getMessage());
                callback.onCounterReceived(null);
            }
        });
    }

    public void updateCounter(String uid, String game, String banner, int newCounter, boolean newGuaranteed, OnCounterUpdateCallback callback){
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference counterNumberReference = userNameReference.child("games").child(game).child(banner).child("counter_progress");

        Map<String, Object> updates = new HashMap<>();
        updates.put("number", newCounter);
        updates.put("guaranteed", newGuaranteed);

        counterNumberReference.updateChildren(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d("Database Update", game + " " + banner + " counter updated successfully.");
                    callback.onCounterUpdated(true); // Indicate success
                } else {
                    Log.e("Database Update", "Failed to update " + game + " " + banner + " counter: " + task.getException());
                    callback.onCounterUpdated(false); // Indicate failure
                }
            }
        });
    }

    public void savePulledUnit(String uid, String game, String banner, String unitName, int counterProgress, boolean statusFiftyFifty, String formattedDate, OnSavePulledUnitCallback callback){
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference pulledUnitsReference = userNameReference.child("games").child(game).child(banner).child("pulled_units");

        // needs to be the exact same name as the Pulled unit model
        Map<String, Object> unitData = new HashMap<>();
        unitData.put("unitName", unitName);
        unitData.put("numOfPulls", counterProgress);
        unitData.put("fromBanner", statusFiftyFifty);
        unitData.put("date", formattedDate);

        String uniqueKey = pulledUnitsReference.push().getKey();

        if (uniqueKey != null){
            pulledUnitsReference.child(uniqueKey).setValue(unitData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Database Save", "Pulled unit saved successfully: " + unitName);
                        callback.onSavedPulledUnit(true);
                    } else {
                        Log.e("Database Save", "Failed to save pulled unit: " + unitName + ", Error: " + task.getException());
                        callback.onSavedPulledUnit(false);
                    }
                }
            });
        } else {
            Log.e("Database Save", "Failed to generate unique key");
            callback.onSavedPulledUnit(false);
        }
    }

    public void retrievePullsHistory(String uid, String game, String banner, OnRetrievePullsHistoryCallback callback){
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference pulledUnitsReference = userNameReference.child("games").child(game).child(banner).child("pulled_units");

        Query query = pulledUnitsReference.orderByChild("date");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<PulledUnit> pulledUnitList = new ArrayList<>();
                ArrayList<DataSnapshot> reversedChildren = new ArrayList<>();
                for (DataSnapshot unitSnapshot : snapshot.getChildren()){
                    reversedChildren.add(unitSnapshot);
                }
                // we're reversing the order so the newest uni are displayed first
                for (int i = reversedChildren.size()-1; i>=0; i--) {
                    PulledUnit pulledUnit = reversedChildren.get(i).getValue(PulledUnit.class);
                    if (pulledUnit != null){
                        pulledUnitList.add(pulledUnit);
                    }
                }
                callback.OnRetrievedPullsHistory(pulledUnitList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Retrieval", "Failed to retrieve pulled unit values: " + error.getMessage());
                callback.OnRetrievedPullsHistory(null);
            }
        });
    }

    public void retrieveNewestPulledUnit(String uid, String game, String banner, OnRetrieveNewestUnitCallback callback){
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference pulledUnitsReference = userNameReference.child("games").child(game).child(banner).child("pulled_units");

        Query query = pulledUnitsReference.orderByChild("date").limitToLast(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // initialize the object
                PulledUnit newestUnit = null;
                for (DataSnapshot unitSnapshot : snapshot.getChildren()){
                    newestUnit = unitSnapshot.getValue(PulledUnit.class);
                }
                callback.OnRetrievedNewestPulledUnit(newestUnit);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Retrieval", "Failed to retrieve pulled units: " + error.getMessage());
                callback.OnRetrievedNewestPulledUnit(null);
            }
        });
    }

    // to check whether the pulled unit path already exists, so we can retrieve the latest unit from history

    public void checkPathExists(String uid, String game, String banner, OnPathExistsCallback callback) {

        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference pulledUnitsReference = userNameReference.child("games").child(game).child(banner).child("pulled_units");

        pulledUnitsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Path exists (regardless of data)
                    callback.onPathExists(true);
                } else {
                    // Path does not exist
                    callback.onPathExists(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Database Check", "Failed to check path existence: " + databaseError.getMessage());
                callback.onPathExists(false); // Or handle the error appropriately
            }
        });
    }

    public void retrievePersonalStats(String uid, String game, String banner, OnRetrievePersonalStatsCallback callback) {
        DatabaseReference userNameReference = usersReference.child(uid);
        DatabaseReference pulledUnitsReference = userNameReference.child("games").child(game).child(banner).child("pulled_units");

        Query query = pulledUnitsReference.orderByChild("date");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Integer> numOfPullsList = new ArrayList<>();
                ArrayList<Boolean> fiftyFiftyOutcomes = new ArrayList<>();
                ArrayList<Boolean> fromBannerList = new ArrayList<>();

                for (DataSnapshot unitSnapshot : snapshot.getChildren()){
                    Long numOfPullsLong = unitSnapshot.child("numOfPulls").getValue(Long.class);
                    if (numOfPullsLong != null){
                        numOfPullsList.add(numOfPullsLong.intValue());
                    }
                    Boolean fromBanner = unitSnapshot.child("fromBanner").getValue(Boolean.class);
                    if (fromBanner != null) {
                        fromBannerList.add(fromBanner);
                    }
                }
                boolean lost5050 = false; // Flag to track if the previous unit was a loss. We assume we won the one before starting (we don't have guarantee)
                for (Boolean isFromBanner : fromBannerList){
                    if (isFromBanner) {
                        if (!lost5050) {
                            fiftyFiftyOutcomes.add(true); // Won 50/50 (banner unit, not after a loss)
                        }
                        lost5050 = false; // Reset the loss flag
                    } else {
                        fiftyFiftyOutcomes.add(false); // Lost 50/50 (not from banner)
                        lost5050 = true; // Set the loss flag for the next unit
                    }
                }
                callback.onPersonalStatsRetrieved(numOfPullsList, fiftyFiftyOutcomes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Retrieval", "Failed to retrieve personal stats: " + error.getMessage());
                callback.onPersonalStatsRetrieved(null, null);
            }
        });
    }

    public void retrieveGlobalStats(String game, String banner, OnRetrieveGlobalStatsCallback callback){
        usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // List to store stats for each user
                ArrayList<UserStats> allUserStats = new ArrayList<>();

                // Iterate through all users in the database
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    DataSnapshot pulledUnitsSnapshot = userSnapshot.child("games").child(game).child(banner).child("pulled_units");

                    // Check if the pulled_units path exists for the user and banner
                    if (pulledUnitsSnapshot.exists()) {
                        ArrayList<Integer> numOfPullsList = new ArrayList<>();
                        ArrayList<Boolean> fiftyFiftyOutcomes = new ArrayList<>();
                        ArrayList<Boolean> fromBannerList = new ArrayList<>();

                        // Iterate through each pulled unit for the user
                        for (DataSnapshot unitSnapshot : pulledUnitsSnapshot.getChildren()) {
                            Long numOfPullsLong = unitSnapshot.child("numOfPulls").getValue(Long.class);
                            if (numOfPullsLong != null) {
                                numOfPullsList.add(numOfPullsLong.intValue());
                            }
                            Boolean fromBanner = unitSnapshot.child("fromBanner").getValue(Boolean.class);
                            if (fromBanner != null) {
                                fromBannerList.add(fromBanner);
                            }
                        }

                        // Apply the fifty-fifty logic for this user's data
                        boolean lost5050 = false;
                        for (Boolean isFromBanner : fromBannerList) {
                            if (isFromBanner) {
                                if (!lost5050) {
                                    fiftyFiftyOutcomes.add(true);
                                }
                                lost5050 = false;
                            } else {
                                fiftyFiftyOutcomes.add(false);
                                lost5050 = true;
                            }
                        }

                        UserStats userStats = new UserStats(numOfPullsList, fiftyFiftyOutcomes);
                        allUserStats.add(userStats);
                    }
                }
                callback.onGlobalStatsRetrieved(allUserStats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Database Retrieval", "Failed to retrieve global stats: " + error.getMessage());
                callback.onGlobalStatsRetrieved(null);
            }
        });
    }


}

 */

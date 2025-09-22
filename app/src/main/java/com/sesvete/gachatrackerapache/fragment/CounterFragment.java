package com.sesvete.gachatrackerapache.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.ApiClient;
import com.sesvete.gachatrackerapache.helper.ApiService;
import com.sesvete.gachatrackerapache.helper.CounterHelper;
import com.sesvete.gachatrackerapache.helper.DatabaseHelperMariaDB;
import com.sesvete.gachatrackerapache.helper.DialogHelper;
import com.sesvete.gachatrackerapache.model.CounterInitialization;
import com.sesvete.gachatrackerapache.model.PulledUnit;
import com.sesvete.gachatrackerapache.model.ResponseError;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CounterFragment extends Fragment {

    //od tle dalje so moje spremenljivke
    private TextView txtCounterProgressNumber;
    private TextView txtCounterSpentTillJackpot;
    private TextView txtCounterSpentTillJackpotCurrency;
    private TextView txtCounterSpentTillJackpotTotal;
    private TextView txtCounterSpentTillJackpotDescription;
    private TextView txtCounterSpentTillJackpotCurrencyDescription;
    private TextView txtCounterSpentTillJackpotTotalDescription;
    private TextView txtCounterHistoryNumber;
    private TextView txtCounterHistoryUnit;
    private TextView txtCounterHistoryFeaturedUnitDescription;
    private TextView txtCounterProgressGuaranteedDescription;
    private MaterialButton btnCounterPlusOne;
    private MaterialButton btnCounterPlusX;
    private MaterialButton btnCounterPlusTen;
    private MaterialButton btnCounterConfirm;
    private CardView cardCounterProgress;
    private ImageView imgCounterProgressGuaranteedDescription;
    private ShapeableImageView imgCounterHistoryFeaturedUnitStatus;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter;
    private String formatedDate;
    private String game;
    private String bannerType;
    private int softPity;
    private int wishValue;
    private String currencyType;
    private String userId;
    private int uid;

    private int counterNumber;

    // to se bo še pobral iz podatkovne baze
    private boolean guaranteed;
    private boolean radioButtonChoice;
    private boolean wonFiftyFifty;
    private int wonFiftyFiftyDatabase;
    private long timerStartInitialCounterLoad;

    public CounterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        timerStartInitialCounterLoad = System.nanoTime();

        txtCounterProgressNumber = view.findViewById(R.id.txt_counter_progress_number);
        txtCounterHistoryNumber = view.findViewById(R.id.txt_counter_history_number);
        txtCounterHistoryNumber = view.findViewById(R.id.txt_counter_history_number);
        txtCounterHistoryUnit = view.findViewById(R.id.txt_counter_history_unit);
        cardCounterProgress = view.findViewById(R.id.card_counter_progress);
        imgCounterProgressGuaranteedDescription = view.findViewById(R.id.img_counter_progress_guaranteed_description);
        imgCounterHistoryFeaturedUnitStatus = view.findViewById(R.id.img_counter_history_featured_unit_status);

        btnCounterPlusOne = view.findViewById(R.id.btn_counter_plus_one);
        btnCounterPlusTen = view.findViewById(R.id.btn_counter_plus_ten);
        btnCounterPlusX = view.findViewById(R.id.btn_counter_plus_x);
        btnCounterConfirm = view.findViewById(R.id.btn_counter_confirm);
        txtCounterSpentTillJackpot = view.findViewById((R.id.txt_counter_spent_till_jackpot));
        txtCounterSpentTillJackpotDescription = view.findViewById(R.id.txt_counter_spent_till_jackpot_description);
        txtCounterSpentTillJackpotCurrency = view.findViewById(R.id.txt_counter_spent_till_jackpot_currency);
        txtCounterSpentTillJackpotCurrencyDescription = view.findViewById(R.id.txt_counter_spent_till_jackpot_currency_description);
        txtCounterSpentTillJackpotTotal = view.findViewById(R.id.txt_counter_spent_till_jackpot_total);
        txtCounterSpentTillJackpotTotalDescription = view.findViewById(R.id.txt_counter_spent_till_jackpot_total_description);
        txtCounterHistoryFeaturedUnitDescription = view.findViewById(R.id.txt_counter_history_featured_unit_description);
        txtCounterProgressGuaranteedDescription = view.findViewById(R.id.txt_counter_progress_guaranteed_description);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("GachaTrackerPrefs", getContext().MODE_PRIVATE);
        userId = sharedPreferences.getString("uid", null);
        uid = Integer.parseInt(userId);

        //DatabaseHelper databaseHelper = new DatabaseHelper();

        //disable buttons
        disableButtons();

        // get game and banner from preferencess
        game = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("game", "genshin_impact");
        bannerType = PreferenceManager.getDefaultSharedPreferences(getContext()).getString("banner", "limited");

        // if it's a standard banner the guaranteed is hidden
        if (bannerType.equals("standard") || bannerType.equals("bangboo")){
            txtCounterHistoryFeaturedUnitDescription.setVisibility(View.GONE);
            txtCounterProgressGuaranteedDescription.setVisibility(View.GONE);
            imgCounterHistoryFeaturedUnitStatus.setVisibility(View.GONE);
            imgCounterProgressGuaranteedDescription.setVisibility(View.GONE);
        }

        // set soft pity wish value and currency type
        softPity = CounterHelper.adjustSoftPity(game, bannerType);
        wishValue = CounterHelper.adjustWishValue(game);
        currencyType = CounterHelper.adjustCurrencyString(getResources(), game);

        // this is the lunching fragment, so we check here whether the user and the database entry exist

        // retrieves counter data from database
        getDatabaseCounterData(getContext(), getResources(), game, bannerType, txtCounterProgressNumber, imgCounterProgressGuaranteedDescription, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotTotal, softPity, wishValue, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotalDescription);

        btnCounterPlusOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timerBtnPlusOneStart= System.nanoTime();
                disableButtons();
                CounterHelper.updateCounter(getResources(), txtCounterProgressNumber, 1, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
                counterNumber = counterNumber + 1;
                DatabaseHelperMariaDB.updateDatabaseCounter(getContext(), getResources(), game, bannerType, counterNumber, guaranteed, btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen, timerBtnPlusOneStart);
            }
        });
        btnCounterPlusTen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long timerBtnPlusTenStart= System.nanoTime();
                disableButtons();
                CounterHelper.updateCounter(getResources(), txtCounterProgressNumber, 10, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
                counterNumber = counterNumber + 10;
                DatabaseHelperMariaDB.updateDatabaseCounter(getContext(), getResources(), game, bannerType, counterNumber, guaranteed, btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen, timerBtnPlusTenStart);
            }
        });
        btnCounterPlusX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater dialogInflater = getLayoutInflater();
                View dialogView = dialogInflater.inflate(R.layout.plus_x_dialog, null);
                builder.setView(dialogView);

                EditText inputXCounter = dialogView.findViewById(R.id.input_x_counter);
                MaterialButton btnXConfirm = dialogView.findViewById(R.id.btn_x_confirm);
                MaterialButton btnXCancel = dialogView.findViewById(R.id.btn_x_cancel);

                AlertDialog dialog = builder.create();

                DialogHelper.buildAlertDialogWindowWithKeyboard(dialog, getContext(), inputXCounter, getActivity());

                btnXConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long timerBtnPlusXStart= System.nanoTime();
                        String inputString = inputXCounter.getText().toString();
                        if (inputString.isEmpty()){
                            Toast.makeText(getContext(), R.string.enter_a_number_error, Toast.LENGTH_SHORT).show();
                        } else {
                            try{
                                int numCustomWishes = Integer.parseInt(inputString);
                                if (numCustomWishes <= 0) {
                                    Toast.makeText(getContext(), R.string.num_wishes_grater_0_error, Toast.LENGTH_SHORT).show();
                                } else {
                                    disableButtons();
                                    CounterHelper.updateCounter(getResources(), txtCounterProgressNumber, numCustomWishes, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
                                    counterNumber = counterNumber + numCustomWishes;
                                    DatabaseHelperMariaDB.updateDatabaseCounter(getContext(), getResources(), game, bannerType, counterNumber, guaranteed, btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen, timerBtnPlusXStart);
                                    dialog.dismiss();
                                }
                            } catch (Exception e) {
                                Log.e("bntXConfirm", "An error occurred: " + e.getMessage(), e);
                            }
                        }
                    }
                });
                btnXCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });
        btnCounterConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringCounterNumber = String.valueOf(counterNumber);
                try {
                    if (counterNumber <= 0) {
                        Toast.makeText(getContext(), R.string.num_wishes_grater_0_error, Toast.LENGTH_SHORT).show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        LayoutInflater dialogInflater = getLayoutInflater();
                        View dialogView = dialogInflater.inflate(R.layout.confirm_unit_dialog, null);
                        builder.setView(dialogView);

                        EditText inputConfirmCounter = dialogView.findViewById(R.id.input_confirm_counter);
                        MaterialButton btnConfirmConfirm = dialogView.findViewById(R.id.btn_confirm_confirm);
                        MaterialButton btnConfirmCancel = dialogView.findViewById(R.id.btn_confirm_cancel);
                        RadioGroup rGrConfirm = dialogView.findViewById(R.id.r_gr_confirm);
                        if (guaranteed || bannerType.equals("standard") || bannerType.equals("bangboo")){
                            rGrConfirm.setVisibility(View.GONE);
                            radioButtonChoice = true;
                            rGrConfirm.check(R.id.won_radio_button);
                        }
                        AlertDialog dialog = builder.create();

                        DialogHelper.buildAlertDialogWindowWithKeyboard(dialog, getContext(), inputConfirmCounter, getActivity());

                        radioFunction(rGrConfirm);
                        btnConfirmConfirm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                long timerInputPullStart = System.nanoTime();
                                String inputString = inputConfirmCounter.getText().toString();
                                inputString = inputString.toUpperCase();
                                if (inputString.isEmpty()){
                                    Toast.makeText(getContext(), R.string.enter_received_unit_error, Toast.LENGTH_SHORT).show();
                                }
                                else if (rGrConfirm.getCheckedRadioButtonId() == -1) {
                                    Toast.makeText(getContext(), R.string.select_radio_choice_error, Toast.LENGTH_SHORT).show();
                                } else {
                                    disableButtons();
                                    // Datum se bo shranil v obliki "yyyy-MM-dd" v podatkovno bazo
                                    calendar = Calendar.getInstance();
                                    dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    formatedDate = dateFormatter.format(calendar.getTime());
                                    txtCounterHistoryNumber.setText(stringCounterNumber);
                                    txtCounterHistoryUnit.setText(inputString);
                                    txtCounterProgressNumber.setText(String.valueOf(0));
                                    int numOfPulls = counterNumber;
                                    CounterHelper.updateSoftPityTracker(getResources(), 0, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
                                    if (radioButtonChoice){
                                        guaranteed = false;
                                        wonFiftyFifty = true;
                                        wonFiftyFiftyDatabase = 1;
                                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_block_red);
                                        imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_checkmark_green);
                                    } else {
                                        guaranteed = true;
                                        wonFiftyFifty = false;
                                        wonFiftyFiftyDatabase = 0;
                                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_checkmark_green);
                                        imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_block_red);
                                    }
                                    PulledUnit pulledUnit = new PulledUnit(numOfPulls, inputString, wonFiftyFiftyDatabase, formatedDate);
                                    pulledUnit.writePulledUnitToDatabase(getContext(), getResources(), uid, game, bannerType, timerInputPullStart);
                                    counterNumber = 0;
                                    DatabaseHelperMariaDB.updateDatabaseCounter(getContext(), getResources(), game, bannerType, counterNumber, guaranteed, btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen, timerInputPullStart);
                                    dialog.dismiss();
                                }
                            }
                        });
                        btnConfirmCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("bntCounterConfirm", "An error occurred: " + e.getMessage(), e);
                }
            }
        });
        cardCounterProgress.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater dialogInflater = getLayoutInflater();
                View dialogView = dialogInflater.inflate(R.layout.confirm_unit_dialog, null);
                builder.setView(dialogView);

                RadioGroup rGrConfirm = dialogView.findViewById(R.id.r_gr_confirm);
                TextView txtConfirmTitle = dialogView.findViewById(R.id.txt_confirm_title);
                RadioButton wonRadioButton = dialogView.findViewById(R.id.won_radio_button);
                RadioButton lostRadioButton = dialogView.findViewById(R.id.lost_radio_button);

                txtConfirmTitle.setText(R.string.update_counter);
                wonRadioButton.setText("G");
                lostRadioButton.setText("N");

                EditText inputUpdateCounter = dialogView.findViewById(R.id.input_confirm_counter);
                MaterialButton btnConfirmConfirm = dialogView.findViewById(R.id.btn_confirm_confirm);
                MaterialButton btnConfirmCancel = dialogView.findViewById(R.id.btn_confirm_cancel);
                inputUpdateCounter.setHint(R.string.enter_counter_number_hint);
                inputUpdateCounter.setInputType(InputType.TYPE_CLASS_NUMBER);

                if (bannerType.equals("standard") || bannerType.equals("bangboo")){
                    rGrConfirm.setVisibility(View.GONE);
                    radioButtonChoice = true;
                    rGrConfirm.check(R.id.won_radio_button);
                }
                AlertDialog dialog = builder.create();

                DialogHelper.buildAlertDialogWindowWithKeyboard(dialog, getContext(), inputUpdateCounter, getActivity());

                radioFunction(rGrConfirm);

                btnConfirmConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        long timerCardStart = System.nanoTime();
                        String inputString = inputUpdateCounter.getText().toString();
                        if (inputString.isEmpty()){
                            Toast.makeText(getContext(), R.string.enter_a_number_error, Toast.LENGTH_SHORT).show();
                        } else if (rGrConfirm.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(getContext(), R.string.select_radio_choice_error, Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                int numCustomWishes = Integer.parseInt(inputString);
                                if (numCustomWishes < 0) {
                                    Toast.makeText(getContext(), R.string.num_wishes_at_least_0, Toast.LENGTH_SHORT).show();
                                } else {
                                    disableButtons();
                                    if (radioButtonChoice) {
                                        guaranteed = true;
                                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_checkmark_green);
                                    } else {
                                        guaranteed = false;
                                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_block_red);
                                    }
                                    txtCounterProgressNumber.setText(String.valueOf(numCustomWishes));
                                    CounterHelper.updateSoftPityTracker(getResources(), numCustomWishes, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
                                    counterNumber = numCustomWishes;
                                    DatabaseHelperMariaDB.updateDatabaseCounter(getContext(), getResources(), game, bannerType, counterNumber, guaranteed, btnCounterConfirm, btnCounterPlusOne, btnCounterPlusX, btnCounterPlusTen, timerCardStart);
                                    dialog.dismiss();

                                }
                            } catch (Exception e){
                                Log.e("bntCounterProgress", "An error occurred: " + e.getMessage(), e);
                            }
                        }
                    }
                });
                btnConfirmCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                return true;
            }
        });
        return view;
    }

    private void radioFunction(RadioGroup rGrConfirm){
        int checkedChoice = rGrConfirm.getCheckedRadioButtonId();
        if (checkedChoice == R.id.won_radio_button){
            radioButtonChoice = true;
        } else if (checkedChoice == R.id.lost_radio_button){
            radioButtonChoice = false;
        }
        rGrConfirm.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.won_radio_button){
                    radioButtonChoice = true;
                } else if (checkedId == R.id.lost_radio_button){
                    radioButtonChoice = false;
                }
            }
        });
    }

    // initial setup
    // mora se pobrati zadnji pull iz zgodovine
    // mora se pobrati stanje trenutenga counterja
    // morajo se posodobiti tista polja


    private void getDatabaseCounterData(Context context, Resources resources, String game, String banner, TextView txtCounterProgressNumber, ImageView imgCounterProgressGuaranteedDescription, TextView txtCounterSpentTillJackpot, TextView txtCounterSpentTillJackpotCurrency, TextView txtCounterSpentTillJackpotTotal, int softPity, int wishValue, TextView txtCounterSpentTillJackpotCurrencyDescription, TextView txtCounterSpentTillJackpotTotalDescription) {
        Retrofit retrofit = ApiClient.getClient(context, resources);

        ApiService apiService = retrofit.create(ApiService.class);
        Call<CounterInitialization> call = apiService.getDatabaseCounterData(game, banner);

        call.enqueue(new Callback<CounterInitialization>() {
            @Override
            public void onResponse(Call<CounterInitialization> call, Response<CounterInitialization> response) {
                if (response.isSuccessful()){
                    // update polja in enablaj buttone
                    CounterInitialization counterInitialization = response.body();
                    counterNumber = counterInitialization.getProgress();
                    int databaseGuaranteed = counterInitialization.getGuaranteed();
                    txtCounterProgressNumber.setText(String.valueOf(counterNumber));

                    if (databaseGuaranteed == 1) {
                        guaranteed = true;
                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_checkmark_green);
                    } else {
                        guaranteed = false;
                        imgCounterProgressGuaranteedDescription.setImageResource(R.drawable.ic_block_red);
                    }
                    CounterHelper.initialPityTrackerSetup(counterNumber, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotTotal, softPity, wishValue);
                    CounterHelper.initialTextviewAdjust(resources, game, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotalDescription);

                    if(!(counterInitialization.getUnitName() == null || counterInitialization.getNumOfPulls() == null || counterInitialization.getFromBanner() == null)){
                        txtCounterHistoryNumber.setText(String.valueOf(counterInitialization.getNumOfPulls()));
                        txtCounterHistoryUnit.setText(counterInitialization.getUnitName());
                        if (counterInitialization.getFromBanner().equals("1")){
                            imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_checkmark_green);
                        } else {
                            imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_block_red);
                        }
                    }
                    long timerEndInitialCounterLoad = System.nanoTime();
                    long timerInitialCounterResult = (timerEndInitialCounterLoad - timerStartInitialCounterLoad)/1000000;
                    Log.i("Timer counter initialization", Long.toString(timerInitialCounterResult) + " " + "ms");
                    enableButtons();

                } else {
                    // handle errors
                    try {
                        Gson gson = new GsonBuilder().create();
                        ResponseError error = gson.fromJson(response.errorBody().string(), ResponseError.class);
                        Toast.makeText(context, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                    } catch (Exception e) {
                        Toast.makeText(context, "An unexpected error occurred.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<CounterInitialization> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void disableButtons(){
        btnCounterConfirm.setEnabled(false);
        btnCounterPlusOne.setEnabled(false);
        btnCounterPlusX.setEnabled(false);
        btnCounterPlusTen.setEnabled(false);
    }

    private void enableButtons(){
        btnCounterConfirm.setEnabled(true);
        btnCounterPlusOne.setEnabled(true);
        btnCounterPlusX.setEnabled(true);
        btnCounterPlusTen.setEnabled(true);
    }
}
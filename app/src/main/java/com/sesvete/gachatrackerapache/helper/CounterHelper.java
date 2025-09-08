package com.sesvete.gachatrackerapache.helper;


import android.content.res.Resources;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.model.PulledUnit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class CounterHelper {

    public static void updateCounter(Resources resources, TextView txtCounterProgressNumber, int amountToAdd, int softPity, int wishValue, String currencyType, TextView txtCounterSpentTillJackpot, TextView txtCounterSpentTillJackpotDescription, TextView txtCounterSpentTillJackpotCurrency, TextView txtCounterSpentTillJackpotCurrencyDescription, TextView txtCounterSpentTillJackpotTotal, TextView txtCounterSpentTillJackpotTotalDescription){
        String stringCounterProgressNumber = txtCounterProgressNumber.getText().toString();
        try {
            int intCounterProgressNumber = Integer.parseInt(stringCounterProgressNumber);
            int counterProgressNumber = counterPlusX(intCounterProgressNumber, amountToAdd);
            txtCounterProgressNumber.setText(String.valueOf(counterProgressNumber));
            updateSoftPityTracker(resources, counterProgressNumber, softPity, wishValue, currencyType, txtCounterSpentTillJackpot, txtCounterSpentTillJackpotDescription, txtCounterSpentTillJackpotCurrency, txtCounterSpentTillJackpotCurrencyDescription, txtCounterSpentTillJackpotTotal, txtCounterSpentTillJackpotTotalDescription);
        }catch (Exception e){
            Log.e("updateCounter", "An error occurred: " + e.getMessage(), e);
        }
    }

    private static int counterPlusX(int currentCount, int amountToAdd){
        return currentCount + amountToAdd;
    }

    public static void updateSoftPityTracker(Resources resources, int counter, int softPity, int wishValue, String currencyType, TextView txtNumToSoftPity, TextView txtToSoftPity, TextView txtNumCurrencyToSoftPity, TextView txtCurrencyToSoftPity, TextView txtNumTotalSpent, TextView txtTotalSpent){
        String stringNumToSoftPity;
        String stringToSoftPity;
        String stringNumCurrencyToSoftPity;
        String stringCurrencyToSoftPity;
        String stringCombinedCurrencyToSoftPity;
        String stringTotalCurrencySpent = resources.getString(R.string.total_currency_spent);
        String stringCombinedTotalSpent = currencyType + " " + stringTotalCurrencySpent;
        int intTotalSpent = counter * wishValue;
        String stringTotalSpent = String.valueOf(intTotalSpent);
        if (counter < softPity){
            int intToSoftPity = softPity - counter;
            int intCurrencyToSoftPity = intToSoftPity * wishValue;

            stringNumToSoftPity = String.valueOf(intToSoftPity);

            stringToSoftPity = resources.getString(R.string.till_soft_pity);

            stringNumCurrencyToSoftPity = String.valueOf(intCurrencyToSoftPity);
            stringCurrencyToSoftPity = resources.getString(R.string.currency_till_soft_pity);
            stringCombinedCurrencyToSoftPity = currencyType + " " + stringCurrencyToSoftPity;

            updateTextFields(txtNumToSoftPity, txtToSoftPity, txtNumCurrencyToSoftPity,txtCurrencyToSoftPity, txtNumTotalSpent, txtTotalSpent, stringNumToSoftPity, stringToSoftPity, stringNumCurrencyToSoftPity, stringCombinedCurrencyToSoftPity, stringTotalSpent, stringCombinedTotalSpent);
        } else if (counter == softPity) {
            stringNumToSoftPity = String.valueOf(0);
            String stringReachedSoftPity = resources.getString(R.string.reached_soft_pity);
            updateTextFields(txtNumToSoftPity, txtToSoftPity, txtNumCurrencyToSoftPity,txtCurrencyToSoftPity, txtNumTotalSpent, txtTotalSpent, stringNumToSoftPity, stringReachedSoftPity, stringNumToSoftPity, stringReachedSoftPity, stringTotalSpent, stringCombinedTotalSpent);
        }
        else {
            int intPastSoftPity = counter - softPity;
            int intCurrencyToSoftPity = intPastSoftPity * wishValue;

            stringNumToSoftPity = String.valueOf(intPastSoftPity);

            stringToSoftPity = resources.getString(R.string.past_soft_pity);

            stringNumCurrencyToSoftPity = String.valueOf(intCurrencyToSoftPity);
            stringCurrencyToSoftPity = resources.getString(R.string.currency_past_soft_pity);
            stringCombinedCurrencyToSoftPity = currencyType + " " + stringCurrencyToSoftPity;

            updateTextFields(txtNumToSoftPity, txtToSoftPity, txtNumCurrencyToSoftPity,txtCurrencyToSoftPity, txtNumTotalSpent, txtTotalSpent, stringNumToSoftPity, stringToSoftPity, stringNumCurrencyToSoftPity, stringCombinedCurrencyToSoftPity, stringTotalSpent, stringCombinedTotalSpent);
        }
    }
    private static void updateTextFields(TextView txtNumToSoftPity, TextView txtToSoftPity, TextView txtNumCurrencyToSoftPity, TextView txtCurrencyToSoftPity, TextView txtNumTotalSpent, TextView txtTotalSpent, String stringNumToSoftPity, String stringToSoftPity, String stringNumCurrencyToSoftPity, String stringCombinedCurrencyToSoftPity, String stringTotalSpent, String stringCombinedTotalSpent){
        txtNumToSoftPity.setText(stringNumToSoftPity);
        txtToSoftPity.setText(stringToSoftPity);
        txtNumCurrencyToSoftPity.setText(stringNumCurrencyToSoftPity);
        txtCurrencyToSoftPity.setText(stringCombinedCurrencyToSoftPity);
        txtNumTotalSpent.setText(stringTotalSpent);
        txtTotalSpent.setText(stringCombinedTotalSpent);
    }

    public static int adjustSoftPity(String game, String bannerType){
        Set<String> weaponBanners = new HashSet<>();
        weaponBanners.add("weapon");
        weaponBanners.add("light_cone");
        weaponBanners.add("w_engine");
        weaponBanners.add("bangboo");

        int softPity;
        if (game.equals("tribe_nine")) {
            softPity = 80;
        } else if (weaponBanners.contains(bannerType)) {
            softPity = 65;
        } else {
            softPity = 75;
        }
        return softPity;
    }

    public static int adjustWishValue(String game){
        int wishValue;
        if (game.equals("tribe_nine")){
            wishValue = 120;
        } else {
            wishValue = 160;
        }
        return wishValue;
    }

    public static String adjustCurrencyString(Resources resources, String game){
        String currencyType;
        if (game.equals("genshin_impact")){
            currencyType = resources.getString(R.string.primogens);
        } else if (game.equals("honkai_star_rail")) {
            currencyType = resources.getString(R.string.stellar_jades);
        } else if (game.equals("zenless_zone_zero")) {
            currencyType = resources.getString(R.string.polychrome);
        } else {
            currencyType = resources.getString(R.string.enigma_entity);
        }
        return currencyType;
    }

    public static void initialTextviewAdjust(Resources resources, String game, TextView txtCounterSpentTillJackpotCurrencyDescription, TextView txtCounterSpentTillJackpotTotalDescription){
        switch (game){
            case "genshin_impact":
                txtCounterSpentTillJackpotCurrencyDescription.setText(resources.getString(R.string.primogens) + " " + resources.getString(R.string.currency_till_soft_pity));
                txtCounterSpentTillJackpotTotalDescription.setText(resources.getString(R.string.primogens) + " " + resources.getString(R.string.total_currency_spent));
                break;
            case "honkai_star_rail":
                txtCounterSpentTillJackpotCurrencyDescription.setText(resources.getString(R.string.stellar_jades) + " " + resources.getString(R.string.currency_till_soft_pity));
                txtCounterSpentTillJackpotTotalDescription.setText(resources.getString(R.string.stellar_jades) + " " + resources.getString(R.string.total_currency_spent));
                break;
            case "zenless_zone_zero":
                txtCounterSpentTillJackpotCurrencyDescription.setText(resources.getString(R.string.polychrome) + " " + resources.getString(R.string.currency_till_soft_pity));
                txtCounterSpentTillJackpotTotalDescription.setText(resources.getString(R.string.polychrome) + " " + resources.getString(R.string.total_currency_spent));
                break;
            case "tribe_nine":
                txtCounterSpentTillJackpotCurrencyDescription.setText(resources.getString(R.string.enigma_entity) + " " + resources.getString(R.string.currency_till_soft_pity));
                txtCounterSpentTillJackpotTotalDescription.setText(resources.getString(R.string.enigma_entity) + " " + resources.getString(R.string.total_currency_spent));
                break;
            default:
                txtCounterSpentTillJackpotCurrencyDescription.setText(resources.getString(R.string.primogens)+ " " + resources.getString(R.string.currency_till_soft_pity));
                txtCounterSpentTillJackpotTotalDescription.setText(resources.getString(R.string.primogens) + " " + resources.getString(R.string.total_currency_spent));
                break;
        }
    }

    public static void initialPityTrackerSetup(int counterNumber, TextView txtCounterSpentTillJackpot, TextView txtCounterSpentTillJackpotCurrency, TextView txtCounterSpentTillJackpotTotal, int softPity, int wishValue){
        txtCounterSpentTillJackpot.setText(String.valueOf(softPity - counterNumber));
        txtCounterSpentTillJackpotCurrency.setText(String.valueOf((softPity - counterNumber)*wishValue));
        txtCounterSpentTillJackpotTotal.setText(String.valueOf(counterNumber * wishValue));
    }

    public static void retrieveNewestUnit(String uid, String game, String banner, TextView txtCounterHistoryNumber, TextView txtCounterHistoryUnit, ShapeableImageView imgCounterHistoryFeaturedUnitStatus) {
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.retrieveNewestPulledUnit(uid, game, banner, new DatabaseHelper.OnRetrieveNewestUnitCallback() {
            @Override
            public void OnRetrievedNewestPulledUnit(PulledUnit newestPulledUnit) {
                txtCounterHistoryNumber.setText(String.valueOf(newestPulledUnit.getNumOfPulls()));
                txtCounterHistoryUnit.setText(newestPulledUnit.getUnitName());
                if (newestPulledUnit.isFromBanner()){
                    imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_checkmark_green);
                } else {
                    imgCounterHistoryFeaturedUnitStatus.setImageResource(R.drawable.ic_block_red);
                }
            }
        });
    }

    public static String dateFormatter(String inputDateString){
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());

            Date date = inputFormat.parse(inputDateString);
            return outputFormat.format(date);

        } catch (ParseException e) {
            Log.e("Date formatter", "An error occurred: " + e.getMessage(), e);
            return inputDateString;
        }
    }

    public static String truncateString(String inputString, int stringLengthLimit){
        if (inputString == null){
            return null;
        } else if (inputString.length() <= stringLengthLimit) {
            return inputString;
        } else {
            return inputString.substring(0, stringLengthLimit) + "...";
        }
    }
}

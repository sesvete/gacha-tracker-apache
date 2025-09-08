package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceManager;

import com.sesvete.gachatrackerapache.R;

public class SettingsHelper {

    public static String getEntryFromValue(Context context, String key, String defaultValue) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String value = prefs.getString(key, defaultValue);

        Resources res = context.getResources();
        String[] entryValues = res.getStringArray(getEntryValuesResourceId(context, key));
        String[] entries = res.getStringArray(getEntriesResourceId(context, key));

        if (entryValues != null && entries != null && entryValues.length == entries.length) {
            for (int i = 0; i < entryValues.length; i++) {
                if (entryValues[i].equals(value)) {
                    return entries[i];
                }
            }
        }
        return defaultValue; // Return default if not found
    }

    private static int getEntriesResourceId(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(key.equals("game")){
            return R.array.game_entries;
        } else if (key.equals("banner")){
            String game = prefs.getString("game", "genshin_impact");
            if (game.equals("genshin_impact")){
                return R.array.genshin_impact_banner_entries;
            } else if (game.equals("honkai_star_rail")) {
                return R.array.honkai_star_rail_banner_entries;
            } else if (game.equals("zenless_zone_zero")) {
                return R.array.zenless_zone_zero_banner_entries;
            } else if (game.equals("tribe_nine")) {
                return R.array.tribe_nine_banner_entries;
            } else {
                return R.array.banner_entries;
            }
        } else if (key.equals("language")){
            return R.array.language_entries;
        } else {
            return 0;
        }
    }

    private static int getEntryValuesResourceId(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if(key.equals("game")){
            return R.array.game_values;
        } else if (key.equals("banner")){
            String game = prefs.getString("game", "genshin_impact");
            if (game.equals("genshin_impact")){
                return R.array.genshin_impact_banner_values;
            } else if (game.equals("honkai_star_rail")) {
                return R.array.honkai_star_rail_banner_values;
            } else if (game.equals("zenless_zone_zero")) {
                return R.array.zenless_zone_zero_banner_values;
            } else if (game.equals("tribe_nine")) {
                return R.array.tribe_nine_banner_values;
            } else {
                return R.array.banner_values;
            }
        } else if (key.equals("language")){
            return R.array.language_values;
        } else {
            return 0;
        }
    }

    public static void updateBannerPreference(String gameValue, ListPreference bannerPreference) {
        int entriesResId;
        int valuesResId;

        switch (gameValue) {
            case "genshin_impact":
                entriesResId = R.array.genshin_impact_banner_entries;
                valuesResId = R.array.genshin_impact_banner_values;
                break;
            case "honkai_star_rail":
                entriesResId = R.array.honkai_star_rail_banner_entries;
                valuesResId = R.array.honkai_star_rail_banner_values;
                break;
            case "zenless_zone_zero":
                entriesResId = R.array.zenless_zone_zero_banner_entries;
                valuesResId = R.array.zenless_zone_zero_banner_values;
                break;
            case "tribe_nine":
                entriesResId = R.array.tribe_nine_banner_entries;
                valuesResId = R.array.tribe_nine_banner_values;
                break;
            default:
                entriesResId = R.array.banner_entries; // Default
                valuesResId = R.array.banner_values;
                break;
        }
        bannerPreference.setEntries(entriesResId);
        bannerPreference.setEntryValues(valuesResId);
    }

}

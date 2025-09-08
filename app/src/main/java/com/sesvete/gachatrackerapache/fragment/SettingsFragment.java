package com.sesvete.gachatrackerapache.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import com.sesvete.gachatrackerapache.MainActivity;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;
import com.sesvete.gachatrackerapache.helper.SettingsHelper;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ListPreference bannerPreference;
    private SwitchPreferenceCompat darkModeSwitch;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        bannerPreference = findPreference("banner");
        darkModeSwitch = findPreference("dark_mode");

        SettingsHelper.updateBannerPreference(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("game", "genshin_impact"), bannerPreference);
    }

    // register and unregister the settings fragment as a listener
    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
        updateDarkMode();
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (key.equals("game")) {
            SettingsHelper.updateBannerPreference(sharedPreferences.getString(key, "genshin_impact"), findPreference("banner"));
            bannerPreference.setValueIndex(0);
        }
        if (key.equals("dark_mode")){
            updateDarkMode();
        }
        if (key.equals("language")) {
            String languageCode = sharedPreferences.getString(key, "en");
            if (getContext() != null) {
                LocaleHelper.updateLocale(getContext(), languageCode);
                if (getActivity() != null) {
                    getActivity().recreate();
                }
            }
        }
        if (getActivity() instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.updateNavHeader(mainActivity.findViewById(R.id.nav_view));
        }
    }

    // custom alert dialog builder za settingse
    @Override
    public void onDisplayPreferenceDialog(@NonNull Preference preference) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext(), R.style.RoundedDialog); // Apply your theme

            // za sestavo radio buttona
            // list Preference.get entries je char sequence zato moramo njapre to pretvorit v String []

            CharSequence[] entriesCharSequence = listPreference.getEntries();
            CharSequence[] entryValuesCharSequence = listPreference.getEntryValues();

            String[] entries = new String[entriesCharSequence.length];
            String[] entryValues = new String[entryValuesCharSequence.length];

            for (int i = 0; i < entriesCharSequence.length; i++) {
                entries[i] = entriesCharSequence[i].toString();
                entryValues[i] = entryValuesCharSequence[i].toString();
            }

            String currentValue = listPreference.getValue();

            int checkedItem = -1; // default - ni izbrane možnosti
            for (int i = 0; i < entryValues.length; i++) {
                if (entryValues[i].equals(currentValue)) {
                    checkedItem = i;
                    break;
                }
            }
            // dejansko sestavi window
            builder.setTitle(listPreference.getTitle())
                    .setSingleChoiceItems(entries, checkedItem, (dialog, which) -> {
                        listPreference.setValue(entryValues[which]);
                        dialog.dismiss();
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            super.onDisplayPreferenceDialog(preference); // Use the default dialog for other preferences
        }
    }

    private void updateDarkMode(){
        if(darkModeSwitch != null && darkModeSwitch.isChecked()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
package com.sesvete.gachatrackerapache.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;

public class LanguageSettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
        setPreferencesFromResource(R.xml.language_preferences, rootKey);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
        if (key.equals("language")) {
            String languageCode = sharedPreferences.getString(key, "en");
            if (getContext() != null) {
                LocaleHelper.updateLocale(getContext(), languageCode);
                if (getActivity() != null) {
                    getActivity().recreate();
                }
            }
        }
    }

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

}

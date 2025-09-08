package com.sesvete.gachatrackerapache.helper;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocaleHelper {

    public static Context setLocaleFromPreferences(Context context) {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "en");
        return updateLocale(context, language);
    }

    public static Context updateLocale(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources resources = context.getResources();
        Configuration config = new Configuration(resources.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);

        return context;
    }

    public static Context onAttach(Context context) {
        return setLocaleFromPreferences(context);
    }

    public static Context onAttachBaseWithDefault(Context context, String defaultLanguage) {
        String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", defaultLanguage);
        return updateLocale(context, language);
    }
}


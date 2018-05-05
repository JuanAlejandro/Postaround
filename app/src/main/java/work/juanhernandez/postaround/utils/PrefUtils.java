package work.juanhernandez.postaround.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.securepreferences.SecurePreferences;

/**
 * Created by juan.hernandez on 5/4/18.
 * PrefUtils
 */

public class PrefUtils {
    public static String getStringPref(Context context, String key) {
        SharedPreferences preferences = new SecurePreferences(context);
        return preferences.getString(key, null);
    }

    public static void setStringPref(Context context, String key, String value) {
        SharedPreferences preferences = new SecurePreferences(context);
        preferences.edit().putString(key, value).apply();
    }
}

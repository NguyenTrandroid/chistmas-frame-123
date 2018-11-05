package pt.content.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Saver {


    public static void write(Context context, String key, boolean value) {
        getPreferencesEditor(context).putBoolean(key, value).apply();
    }

    public static void write(Context context, String key, int value) {
        getPreferencesEditor(context).putInt(key, value).apply();
    }

    public static void write(Context context, String key, String value) {
        getPreferencesEditor(context).putString(key, value).apply();
    }

    public static void write(Context context, String key, long value) {
        getPreferencesEditor(context).putLong(key, value).apply();
    }
    public static boolean readBoolean(Context context, String key, boolean def) {
        return getPreferences(context).getBoolean(key, def);
    }

    public static int readInt(Context context, String key, int def) {
        return getPreferences(context).getInt(key, def);
    }

    public static long readLong(Context context, String key, long def) {
        return getPreferences(context).getLong(key, def);
    }

    public static String readString(Context context, String key, String def) {
        return getPreferences(context).getString(key, def);
    }

    private static SharedPreferences getPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static SharedPreferences.Editor getPreferencesEditor(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).edit();
    }
//    public static String getStrResId(Context context, String name) {
//        d("AdsHelper", "getStrResId: " + name);
//        int resourceID = context.getResources().getIdentifier(name, "string", context.getPackageName());
//        if(resourceID==0) return null;
//        return context.getString(resourceID);
//    }
}

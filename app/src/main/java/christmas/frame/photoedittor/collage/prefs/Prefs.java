package christmas.frame.photoedittor.collage.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by thongph on 1/31/18
 * <p>
 * Last modified on 1/31/18
 */

public class Prefs {

    private static Prefs instance;

    public static Prefs getInstance() {
        if (instance == null) {
            instance = new Prefs();
        }
        return instance;
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    /**
     * Init SharedPreferences
     *
     * @param context
     */
    public void init(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = sharedPreferences.edit();
    }

    /**
     * Store value to SharedPreferences
     *
     * @param key
     * @param value
     */
    public void storeValue(String key, Object value) {
        if (value instanceof String) {
            editor.putString(key, (String) value);
        }
        if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }
        if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        }
        if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.apply();
    }

    /**
     * Get String value with default value = null
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return getStringValue(key, null);
    }

    /**
     * Get String value with default value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String getStringValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    /**
     * Get Integer value with default value = -1
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        return getIntValue(key, -1);
    }

    /**
     * Get Integer value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int getIntValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    /**
     * Get Long value with default value = -1
     *
     * @param key
     * @return
     */
    public long getLongValue(String key) {
        return getLongValue(key, -1);
    }

    /**
     * Get Long value with default value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long getLongValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    /**
     * Get Float value with default value = -1
     *
     * @param key
     * @return
     */
    public float getFloatValue(String key) {
        return getFloatValue(key, -1);
    }

    /**
     * Get Float value with default value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float getFloatValue(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    /**
     * Get Boolean value with default value = false
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        return getBooleanValue(key, false);
    }

    /**
     * Get Boolean value with default value
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean getBooleanValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    /**
     * Clear all values
     */
    public void clearAll() {
        editor.clear().apply();
    }
}

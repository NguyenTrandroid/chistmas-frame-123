package pt.content.library.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.util.Log.d;
import static pt.content.library.Constant.PACKAGE_NAME;
import static pt.content.library.Constant.SERVER_IP;
import static pt.content.library.Constant.TIME_ADS_REFRESH;
import static pt.content.library.Constant.TIME_RATE_REFRESH;


public class ApiHelper {

    public static String get(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .header("User-Agent", System.getProperty("http.agent"))
                .build();
        try {
            Response response = client.newCall(request).execute();

            return response.body().string();
        } catch (Exception e) {
            return "";
        }
    }


    public static void startAds(final Context context) {
//        if(true) return;
        if (Saver.readLong(context, "last_request_ads_time", -1) == -1) {
            save(context, readRawTextFile(context, "ads"), "fisrt_resquest_ads_time");
//            return;
        }
        if (Saver.readLong(context, "last_request_ads_time", 0) < System.currentTimeMillis() - TIME_ADS_REFRESH)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    int version = 0;
                    try {
                        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String response = get(SERVER_IP + PACKAGE_NAME + ".php?t=ads2&l=" + getCurrentLocate(context) + "&v=" + version + "&p=" + context.getPackageName());
                    d("ApiHelper", "run: " + response);

                    save(context, response, "last_request_ads_time");
                    d("ApiHelper", "startAds :load ok " + (System.currentTimeMillis() - time));
                }
            }).start();

    }

    public static void startRate(final Context context) {
        if (Saver.readLong(context, "last_request_rate_time", 0) < System.currentTimeMillis() - TIME_RATE_REFRESH)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    int version = 0;
                    try {
                        version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String response = get(SERVER_IP + PACKAGE_NAME + ".php?t=rate&l=" + getCurrentLocate(context) + "&v=" + version + "&p=" + context.getPackageName());
//                    d("ApiHelper", "run: " + response);

                    save(context, response, "last_request_rate_time");
                    d("ApiHelper", "startRate :load ok " + (System.currentTimeMillis() - time));
                }
            }).start();

    }

//    public static void clear(Context context) {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
//        if (!preferences.contains("api_list"))
//            return;
//        try {
//            JSONArray list = new JSONArray(preferences.getString("api_list", ""));
//            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
//            for (int i = 0; i < list.length(); i++) {
//                editor.remove(list.getString(i));
//            }
//            editor.remove("api_list");
//            editor.apply();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        d("ApiHelper", "clear :");
//
//
//    }

    private static void save(Context context, String response, String type) {
        SharedPreferences.Editor preferences = Saver.getPreferencesEditor(context);
        try {
            if (response == null || response.trim().isEmpty()) {
                preferences.putInt("level", 0);
                preferences.apply();
                return;
            }
            JSONObject arr = new JSONObject(response);
            for (Iterator<String> iter = arr.keys(); iter.hasNext(); ) {
                String key = iter.next();

                if (arr.get(key) instanceof Boolean)
                    preferences.putBoolean(key, arr.getBoolean(key));
                else if (arr.get(key) instanceof Integer)
                    preferences.putInt(key, arr.getInt(key));
                else if (arr.get(key) instanceof String)
                    preferences.putString(key, arr.getString(key));
                else if (arr.get(key) instanceof JSONObject)
                    preferences.putString(key, arr.getJSONObject(key).toString());
                else if (arr.get(key) instanceof JSONArray)
                    preferences.putString(key, arr.getJSONArray(key).toString());

            }
            preferences.putLong(type, System.currentTimeMillis());
            preferences.apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getCurrentLocate(Context context) {
        String countryCode = Saver.readString(context, "local", "");
        if (countryCode.isEmpty()) {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager != null) {
                countryCode = manager.getNetworkCountryIso().toUpperCase();
            }
            Saver.write(context, "local", countryCode);
        }
        d("LocateHelper", "getCurrentLocate:init " + countryCode);
        return countryCode;
    }
    public static String readRawTextFile(Context context, String  name)
    {
        int resourceID = context.getResources().getIdentifier(name, "raw", context.getPackageName());
        InputStream inputStream = context.getResources().openRawResource(resourceID);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while (( line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        Log.d("ApiHelper","readRawTextFile: "+text.toString());
        return text.toString();
    }
}

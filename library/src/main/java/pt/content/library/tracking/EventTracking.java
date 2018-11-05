package pt.content.library.tracking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EventTracking {
    @SuppressLint("MissingPermission")
    public static void send(Context context, String tag, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        FirebaseAnalytics.getInstance(context).logEvent(tag, bundle);
    }

    @SuppressLint("MissingPermission")
    public static void send(Context context, String tag, String... map) {
        Bundle bundle = new Bundle();
        for (int i = 0; i < map.length; i = i + 2) {
            bundle.putString(map[i], map[i + 1]);

        }

        FirebaseAnalytics.getInstance(context).logEvent(tag, bundle);
    }

    @SuppressLint("MissingPermission")
    public static void send(Context context, String tag, String key, int value) {
        Bundle bundle = new Bundle();
        bundle.putInt(key, value);
        FirebaseAnalytics.getInstance(context).logEvent(tag, bundle);
    }

    @SuppressLint("MissingPermission")
    public static void setUserProperty(Context context, String tag, String value) {
        FirebaseAnalytics.getInstance(context).setUserProperty(tag, value);
    }
}

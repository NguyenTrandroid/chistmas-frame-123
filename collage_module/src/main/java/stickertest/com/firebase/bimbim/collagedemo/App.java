package stickertest.com.firebase.bimbim.collagedemo;

import android.app.Application;
import android.content.Context;

public class App extends Application {
    private static Context context;
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        context = this;
    }

    public static Context getAppContext() {
        return context;
    }
}
package pt.content.library.ads;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pt.content.library.BuildConfig;
import pt.content.library.tracking.EventTracking;
import pt.content.library.util.ApiHelper;
import pt.content.library.util.RateHelper;
import pt.content.library.util.Saver;

import static android.util.Log.d;
import static pt.content.library.Constant.TIME_PREMIUM;


public class AdsHelper {
    private final String ADMOB_PREFIX = "admob_";
    private final String DFP_PREFIX = "dfp_";
    private final String FB_PREFIX = "facebook_";
    private final String AND_PREFIX = "_AND_";
    private final String SUFFIX_250 = "_250_";

    private static final String LAST_TIME_LOAD = "last_time_";


    private com.facebook.ads.AdView fbAdview = null;
    private AdView adViewNE;
    private PublisherAdView mPublisherAdView;
    private boolean animate = false;

    private boolean isShowing = false;
    private boolean hadDestroyCalled = false;
    private Map<String, AdsStatus> adsStatusList = new HashMap<>();
    private List<String> adsIndex = new ArrayList<>();
    private AdsCallback fullAdsCallback;

    public void setAnimate(boolean animate) {
        this.animate = animate;
    }

    public void onDestroy() {

        if (fbAdview != null) {
            fbAdview.destroy();
            fbAdview = null;
        }
        if (adViewNE != null)
            adViewNE.destroy();

        if (mPublisherAdView != null)
            mPublisherAdView.destroy();

        hadDestroyCalled = true;
        if (isShowing)
            return;
        for (String key : adsStatusList.keySet()) {
            AdsStatus ads = adsStatusList.get(key);
            ads.destroy();
        }
        adsStatusList.clear();
    }

    public boolean isFullAdLoaded() {
        for (String key : adsStatusList.keySet()) {
            AdsStatus ads = adsStatusList.get(key);
            if (ads.isLoaded())
                return true;
        }
        return false;

    }

    public boolean isBestFullAdLoaded() {
        if (adsIndex.size() == 0)
            return false;
        if (adsStatusList.containsKey(adsIndex.get(0)))
            return false;
        return adsStatusList.get(adsIndex.get(0)).isLoaded();

    }


    public void showFullAd() {

        for (String key : adsIndex) {
            AdsStatus ads = adsStatusList.get(key);
            d("ICT_AdsHelper", "showFullAd: " + key + " " + ads.isLoaded());
            if (ads.isLoaded()) {
                ads.show();
                return;
            }
        }
    }

    public void loadAds(final Context context, final LinearLayout container, final String position, AdsCallback _callback) {
        d("ICT_AdsHelper", "loadAds: " + position);
        ApiHelper.startAds(context);
        final AdsCallback callback = (_callback == null) ? new AdsCallback() {
        } : _callback;

        if (RateHelper.isPremium(context) | isNewBuild()) {
            callback.onError(context, position, "premium", "no_ads", -1, -1000);
            EventTracking.setUserProperty(context, "no_ads", "true");
            return;
        } else {
            EventTracking.setUserProperty(context, "no_ads", "false");
        }
        try {
            final JSONArray jsonArray = getListAds(context, position);
//            selectAds(context, container, 250, jsonArray, 0, callback);
            if (jsonArray != null && jsonArray.length() > 0) {

                container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
//                currentBannerSelected = getAdType(context, position);

                        float adHeight = (container.getHeight() + 1) / context.getResources().getDisplayMetrics().density;
                        d("ICT_AdsHelper", "onGlobalLayout: " + adHeight);
                        if (adHeight > 0)
                            container.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        else {
                            callback.onError(context, position, "ad height 0", "no_ads", -1, -1100);
                            return;
                        }
                        selectAds(context, container, adHeight, jsonArray, 0, callback);
                    }
                });
            }
        } catch (Exception e) {
            callback.onError(context, position, "try catch json", "no_ads", -1, -1200);
            e.printStackTrace();
        }
    }

    private void selectAds(final Context context, final LinearLayout container, float height, JSONArray jsonArray, int index, final AdsCallback callback) {
        if (jsonArray == null || jsonArray.length() == 0 || index > jsonArray.length() - 1) {
            callback.onError(context, "banner", "json null", "no_ads", -1, -1300);
            return;
        }
        String id;
        try {
            id = jsonArray.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError(context, "banner", "json null", "no_ads", -1, -1400);
            return;
        }
        if (id == null || id.trim().isEmpty()) {
            callback.onError(context, "banner", "id null", "no_ads", -1, -1500);
            return;
        }
        d("ICT_AdsHelper", "selectAds: " + id + " index:" + index);

        if (id.startsWith(ADMOB_PREFIX))
            loadAdmobAds(context, container, height, id.replace(ADMOB_PREFIX, ""), jsonArray, index, callback);
        else if (id.startsWith(DFP_PREFIX))
            loadDfpAds(context, container, height, id.replace(DFP_PREFIX, ""), jsonArray, index, callback);
        else if (id.startsWith(FB_PREFIX))
            loadFbAds(context, container, height, id.replace(FB_PREFIX, ""), jsonArray, index, callback);
        else
            callback.onError(context, "banner", "no prefix match", "no_ads", -1, -1600);


    }


    @SuppressLint("MissingPermission")
    private void loadAdmobAds(final Context context, final LinearLayout container, final float adsHeight, final String id, final JSONArray listAds, final int index, final AdsCallback callback) {
        d("ICT_AdsHelper", "loadAdmobAds: " + id);
        if (adViewNE != null)
            adViewNE.destroy();
        adViewNE = new AdView(context);
        if (adsHeight >= 250) {
            adViewNE.setAdSize(AdSize.MEDIUM_RECTANGLE);
            if (BuildConfig.DEBUG)
                adViewNE.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            else
                adViewNE.setAdUnitId(id.replace(ADMOB_PREFIX, ""));
        } else if (adsHeight >= 90) {
            d("ICT_AdsHelper", "loadAdmobAds: 90");
            adViewNE.setAdSize(new AdSize(AdSize.FULL_WIDTH, 90));
            if (BuildConfig.DEBUG)
                adViewNE.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            else
                adViewNE.setAdUnitId(id.replace(ADMOB_PREFIX, ""));
        } else if (adsHeight >= 50) {
            adViewNE.setAdSize(new AdSize(AdSize.FULL_WIDTH, 50));
            if (BuildConfig.DEBUG)
                adViewNE.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            else
                adViewNE.setAdUnitId(id.replace(ADMOB_PREFIX, ""));
        } else {
            adViewNE.setAdSize(AdSize.SMART_BANNER);
            if (BuildConfig.DEBUG)
                adViewNE.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
            else
                adViewNE.setAdUnitId(id.replace(ADMOB_PREFIX, ""));
        }


        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adRequestBuilder.addTestDevice(AdRequest.DEVICE_ID_EMULATOR);
        container.removeAllViews();
        container.addView(adViewNE);
        adViewNE.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                d("ICT_AdsHelper", "loadAdmobAds: success " + id + " index:" + index);
                callback.onLoaded(context, "", id, ADMOB_PREFIX, index);

            }

            @Override
            public void onAdFailedToLoad(int i) {
                d("ICT_AdsHelper", "loadAdmobAds: fail id:" + id + " index:" + index);
                adViewNE.destroy();
                selectAds(context, container, adsHeight, listAds, index + 1, callback);

            }

        });
        adViewNE.loadAd(adRequestBuilder.build());
    }

    @SuppressLint("MissingPermission")
    private void loadDfpAds(final Context context, final LinearLayout container, final float adsHeight, final String id, final JSONArray listAds, final int index, final AdsCallback callback) {
        d("ICT_AdsHelper", "loadDfpAds: " + id);
        if (mPublisherAdView != null)
            mPublisherAdView.destroy();
        mPublisherAdView = new PublisherAdView(context);
        container.removeAllViews();
        container.addView(mPublisherAdView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (adsHeight >= 250) {
            mPublisherAdView.setAdSizes(AdSize.MEDIUM_RECTANGLE, new AdSize(AdSize.FULL_WIDTH, 90), new AdSize(AdSize.FULL_WIDTH, 50), AdSize.SMART_BANNER);
        } else if (adsHeight >= 90) {
            mPublisherAdView.setAdSizes(new AdSize(AdSize.FULL_WIDTH, 90), new AdSize(AdSize.FULL_WIDTH, 50), AdSize.SMART_BANNER);
        } else if (adsHeight >= 50) {
            mPublisherAdView.setAdSizes(new AdSize(AdSize.FULL_WIDTH, 50), AdSize.SMART_BANNER);
        } else {
            mPublisherAdView.setAdSizes(AdSize.SMART_BANNER);
        }
        if (BuildConfig.DEBUG)
            mPublisherAdView.setAdUnitId("/6499/example/banner");
        else
            mPublisherAdView.setAdUnitId(id.replace(DFP_PREFIX, ""));
        mPublisherAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                d("ICT_AdsHelper", "loadDfpAds: success " + id + " index:" + index);
                callback.onLoaded(context, "aaa", id, DFP_PREFIX, index);

            }

            @Override
            public void onAdFailedToLoad(int i) {
                d("ICT_AdsHelper", "loadDfpAds: fail " + id + " index:" + index + " error:" + i);
                mPublisherAdView.destroy();
                selectAds(context, container, adsHeight, listAds, index + 1, callback);
            }

        });
        PublisherAdRequest adRequest = new PublisherAdRequest.Builder().build();
        mPublisherAdView.loadAd(adRequest);


    }

    private void loadFbAds(final Context context, final LinearLayout container, final float adsHeight, final String id, final JSONArray listAds, final int index, final AdsCallback callback) {
        d("ICT_AdsHelper", "loadFbAds: " + id);
        container.removeAllViews();
        String _id = id;
        String _id250 = id;
        if (id.contains(SUFFIX_250)) {
            String[] arr = id.split(SUFFIX_250);
            _id = arr[0];
            _id250 = arr[1];
        }
        if (fbAdview != null)
            fbAdview.destroy();
        if (adsHeight >= 250) {
            fbAdview = new com.facebook.ads.AdView(context, _id250, com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        } else if (adsHeight >= 90) {
            d("ICT_AdsHelper", "loadFbAds: 90 " + _id);
            fbAdview = new com.facebook.ads.AdView(context, _id, com.facebook.ads.AdSize.BANNER_HEIGHT_90);
        } else {
            fbAdview = new com.facebook.ads.AdView(context, _id, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
        }

        container.addView(fbAdview);
        fbAdview.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                d("ICT_AdsHelper", "loadFbAds:  fail  " + id + " index:" + index + " error " + adError.getErrorMessage());
                fbAdview.destroy();
                selectAds(context, container, adsHeight, listAds, index + 1, callback);

            }

            @Override
            public void onAdLoaded(Ad ad) {
                d("ICT_AdsHelper", "loadFbAds: success " + id + " index:" + index);
                callback.onLoaded(context, "aaa", id, FB_PREFIX, index);

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        fbAdview.loadAd();

    }


    @SuppressLint("MissingPermission")
    public void loadFullAds(final Context context, final String position, AdsCallback _callback) {
        d("ICT_AdsHelper", "loadFullAds: ");
        fullAdsCallback = _callback == null ? new AdsCallback() {
        } : _callback;
        if (RateHelper.isPremium(context) | isNewBuild()) {
            fullAdsCallback.onError(context, position, "", FB_PREFIX, -1, -100);
            EventTracking.setUserProperty(context, "no_ads", "true");
            return;
        } else {
            EventTracking.setUserProperty(context, "no_ads", "false");
        }

        EventTracking.send(context, position, "request", ApiHelper.getCurrentLocate(context));
        ApiHelper.startAds(context);
        try {

            final JSONArray jsonArray = getListAds(context, position);

            if (jsonArray != null && jsonArray.length() > 0) {
                selectFullAds(context, jsonArray, 0, position);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void selectFullAds(final Context context, JSONArray jsonArray, int index, String position) {
        if (jsonArray == null || jsonArray.length() == 0) {
            fullAdsCallback.onError(context, position, "", "", index, -200);
            return;
        }
        if (index > jsonArray.length() - 1) {
            fullAdsCallback.onError(context, position, "", "", index, -300);
            return;
        }
        String id;
        try {
            id = jsonArray.getString(index);
        } catch (JSONException e) {
            e.printStackTrace();
            fullAdsCallback.onError(context, position, "", "", index, -400);
            return;
        }
        if (id == null || id.trim().isEmpty()) {
            fullAdsCallback.onError(context, position, "", "", index, -500);
            return;
        }
        d("ICT_AdsHelper", "selectFullAds: " + id + " index:" + index);

        for (String key : adsStatusList.keySet()) {
            AdsStatus ads = adsStatusList.get(key);
            ads.destroy();
        }
        adsStatusList.clear();
        adsIndex.clear();
        String[] arr = id.split(AND_PREFIX);
        for (int i = 0; i < arr.length; i++) {
            String ads_id = arr[i];
            adsIndex.add(ads_id);
            adsStatusList.put(ads_id, new AdsStatus());
            if (ads_id.startsWith(ADMOB_PREFIX)) {
                loadAdmobFullAds(context, ads_id.replace(ADMOB_PREFIX, ""), jsonArray, index, position);
            } else if (ads_id.startsWith(DFP_PREFIX)) {
                loadDfpFullAds(context, ads_id.replace(DFP_PREFIX, ""), jsonArray, index, position);
            } else if (ads_id.startsWith(FB_PREFIX)) {
                loadFbFullAds(context, ads_id.replace(FB_PREFIX, ""), jsonArray, index, position);
            }
        }


    }


    @SuppressLint("MissingPermission")
    private void loadAdmobFullAds(final Context context, final String id, final JSONArray jsonArray, final int index, final String position) {
        d("ICT_AdsHelper", "loadAdmobFullAds: " + id);
//        if (!isAdmobInit) {
//            MobileAds.initialize(context, getID(context, "admob_app_id"));
//            isAdmobInit = true;
//        }
        if (adsStatusList.size() == 0 || !adsStatusList.containsKey(ADMOB_PREFIX + id)) {
            fullAdsCallback.onError(context, position, "", "", index, -600);
            return;
        }
        InterstitialAd InterstitialAd = adsStatusList.get(ADMOB_PREFIX + id).getAdmobFullAds(context);
        if (BuildConfig.DEBUG)
            InterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        else
            InterstitialAd.setAdUnitId(id);

        InterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (fullAdsCallback != null)
                    fullAdsCallback.onClose(context, ADMOB_PREFIX + "id");
                isShowing = false;
                if (hadDestroyCalled)
                    onDestroy();
            }

            @Override
            public void onAdLoaded() {
                d("ICT_AdsHelper", "loadAdmobFullAds: success " + id + " index:" + index);
//                if (callback != null)
//                    callback.onLoaded(context, ADMOB_PREFIX + "id", "", ADMOB_PREFIX, index);
                fullAdsLoaded(context, position, ADMOB_PREFIX + id, ADMOB_PREFIX, index);

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                isShowing = true;
                fullAdsCallback.onImpression(context, "", "", ADMOB_PREFIX, index);
            }

            @Override
            public void onAdFailedToLoad(int i) {
                d("ICT_AdsHelper", "loadAdmobFullAds: fail " + id + " index:" + index + " error:" + i);

                fullAdsFail(context, jsonArray, index, ADMOB_PREFIX + id, position);
            }

            @Override
            public void onAdImpression() {

            }

            @Override
            public void onAdClicked() {
                fullAdsCallback.onClick(context, "", "", ADMOB_PREFIX, index);
            }
        });
        InterstitialAd.loadAd(new AdRequest.Builder().build());

    }

    @SuppressLint("MissingPermission")
    private void loadDfpFullAds(final Context context, final String id, final JSONArray jsonArray, final int index, final String position) {
        d("ICT_AdsHelper", "loaDfpFullAds: " + id);

        if (adsStatusList.size() == 0 || !adsStatusList.containsKey(DFP_PREFIX + id)) {
            fullAdsCallback.onError(context, position, "", "", index, -400);
            return;
        }
        PublisherInterstitialAd dfpInterstitialAd = adsStatusList.get(DFP_PREFIX + id).getDfpFullAds(context);
        if (BuildConfig.DEBUG)
            dfpInterstitialAd.setAdUnitId("/6499/example/interstitial");
        else
            dfpInterstitialAd.setAdUnitId(id);
        dfpInterstitialAd.loadAd(new PublisherAdRequest.Builder().build());

        dfpInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                if (fullAdsCallback != null)
                    fullAdsCallback.onClose(context, DFP_PREFIX + id);
                isShowing = false;
                if (hadDestroyCalled)
                    onDestroy();
            }

            @Override
            public void onAdLoaded() {
                d("ICT_AdsHelper", "loadDfpFullAds: success " + id + " index:" + index);
                fullAdsLoaded(context, DFP_PREFIX + id, DFP_PREFIX + id, DFP_PREFIX, index);

            }

            @Override
            public void onAdFailedToLoad(int i) {
                d("ICT_AdsHelper", "loadDfpFullAds: fail " + id + " index:" + index + " error:" + i);
                fullAdsFail(context, jsonArray, index, DFP_PREFIX + id, position);
            }

            @Override
            public void onAdImpression() {

            }

            @Override
            public void onAdOpened() {
                isShowing = true;
                fullAdsCallback.onImpression(context, position, DFP_PREFIX + id, DFP_PREFIX, index);
            }

            @Override
            public void onAdClicked() {
                fullAdsCallback.onClick(context, DFP_PREFIX + id, DFP_PREFIX + id, DFP_PREFIX, index);
            }
        });
    }

    private void loadFbFullAds(final Context context, final String id, final JSONArray jsonArray, final int index, final String position) {
        AdSettings.addTestDevice("61c57eaf-8c0c-47cc-8680-138c6bff21c0");
        d("ICT_AdsHelper", "loadFbFullAds: " + id);
        if (adsStatusList.size() == 0 || !adsStatusList.containsKey(FB_PREFIX + id)) {
            fullAdsCallback.onError(context, position, "", "", index, -600);
            return;
        }
        com.facebook.ads.InterstitialAd fbInterstitialAd = adsStatusList.get(FB_PREFIX + id).getFbFullAds(context, id);
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                d("ICT_AdsHelper", "loadFbFullAds: fail " + id + " index:" + index + " onError:" + adError.getErrorMessage());
                fullAdsFail(context, jsonArray, index, FB_PREFIX + id, position);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                d("ICT_AdsHelper", "loadFbFullAds: success " + id + " index:" + index);
                fullAdsLoaded(context, FB_PREFIX + id, FB_PREFIX + id, FB_PREFIX, index);
            }

            @Override
            public void onInterstitialDisplayed(Ad ad) {
                isShowing = true;
                fullAdsCallback.onImpression(context, position, FB_PREFIX + id, FB_PREFIX, index);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                if (fullAdsCallback != null)
                    fullAdsCallback.onClose(context, FB_PREFIX + id);
                isShowing = false;
                if (hadDestroyCalled)
                    onDestroy();
            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {
                fullAdsCallback.onClick(context, FB_PREFIX + id, FB_PREFIX + id, FB_PREFIX, index);
            }
        });
        fbInterstitialAd.loadAd();
    }

    private synchronized void fullAdsLoaded(Context context, String position, String id, String type, int reload) {
        if (adsIndex.size() == 0)
            return;
        if (adsStatusList.size() == 0)
            return;
        if (!adsStatusList.containsKey(id))
            return;
        d("ICT_AdsHelper", "fullAdsLoaded: " + id);
        adsStatusList.get(id).setLoaded(true);
        adsStatusList.get(id).finishLoading();
        if (!adsIndex.get(0).equals(id))
            return;
        if (fullAdsCallback != null)
            fullAdsCallback.onLoaded(context, DFP_PREFIX + id, DFP_PREFIX + id, DFP_PREFIX, reload);
    }

    private synchronized void fullAdsFail(Context context, JSONArray jsonArray, int index, String id, String position) {
        if (adsIndex.size() == 0) {
            d("ICT_AdsHelper", "fullAdsFail: 100");
            return;
        }
        if (adsStatusList.size() == 0) {
            d("ICT_AdsHelper", "fullAdsFail: 200");
            return;
        }
        if (!adsStatusList.containsKey(id)) {
            d("ICT_AdsHelper", "fullAdsFail: 300");
            return;
        }
        adsStatusList.get(id).finishLoading();
        for (String adsId : adsIndex) {
            if (!adsStatusList.containsKey(adsId)) {
                d("ICT_AdsHelper", "fullAdsFail: 400");
                return;
            }
            if (adsStatusList.get(adsId).isLoading()) {
                d("ICT_AdsHelper", "fullAdsFail: 500");
                return;
            }
        }
        Log.d("ICT_AdsHelper", "fullAdsFail: next");
        selectFullAds(context, jsonArray, index + 1, position);
    }

    public String getAdmobAdID(Context context) {

        String result = Saver.readString(context, "admob_app_id", "");
        if (result.isEmpty()) {

            String jsonRespone = ApiHelper.readRawTextFile(context, "ads");
            try {
                JSONObject jsonObject = new JSONObject(jsonRespone);
                result = jsonObject.getString("admob_app_id");
            } catch (JSONException e) {
                e.printStackTrace();
                result = "Admob app id not found";
            }
        }
        d("ICT_AdsHelper", "getID: " + "admob_app_id" + " -- " + result);
        return result;
    }


    private JSONArray getListAds(Context context, String position) {
        int random = new Random().nextInt(100);
        d("ICT_AdsHelper", "getListAds:" + position + " random " + random);
        try {
            JSONArray jsonArray = getJsonAds(context, position);
            JSONArray listAds = null;
            int sumRate = 0;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getInt("rate") == 0)
                    continue;
                sumRate += jsonObject.getInt("rate");
                if (random < sumRate) {
                    listAds = jsonObject.getJSONArray("id");
                    if (jsonObject.has("skip_time")) {
                        long skipTime = jsonObject.getLong("skip_time");
                        long lastedLoad = Saver.readLong(context, LAST_TIME_LOAD + position, 0);
                        if (lastedLoad + skipTime > System.currentTimeMillis()) {
                            listAds = null;
                            d("ICT_AdsHelper", "getListAds: skip " + position);
                        }

                    }
                    d("ICT_AdsHelper", "getListAds: " + position + " index:" + i);
                    break;
                }
            }
            return listAds;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private JSONArray getJsonAds(Context context, String position) throws JSONException {
        String result = Saver.readString(context, position, "");
        if (!result.isEmpty())
            return new JSONArray(result);
        String jsonRespone = ApiHelper.readRawTextFile(context, "ads");
        JSONObject jsonObject = new JSONObject(jsonRespone);
        if (jsonObject.has(position))
            return jsonObject.getJSONArray(position);
        return null;
    }

    private boolean isNewBuild() {
        return (System.currentTimeMillis() < BuildConfig.TIMESTAMP + TIME_PREMIUM) && !BuildConfig.DEBUG;
    }

    public static abstract class AdsCallback {
        public void onLoaded(Context context, String position, String id, String type, int reload) {
            d("ICT_AdsCallback", "onLoaded: " + position + " id:" + id);
            EventTracking.send(context, type + id + "_" + reload + "_loaded");
        }

        public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
            d("ICT_AdsCallback", "onError: " + errorCode + " position: " + position);
            EventTracking.send(context, type + id + "_" + reload + "_error", "error_code", type + errorCode);
        }

        public void onClose(Context context, String position) {
            d("ICT_AdsCallback", "onClose:    position: " + position);
        }

        public void onImpression(Context context, String position, String id, String type, int reload) {
            EventTracking.send(context, type + id + "_" + reload + "_imp");
            Saver.write(context, LAST_TIME_LOAD + position, System.currentTimeMillis());

        }

        public void onClick(Context context, String position, String id, String type, int reload) {
            EventTracking.send(context, type + id + "_" + reload + "_click");
        }


    }

    private class AdsStatus {
        private String type;
        private Object ads;
        private boolean isLoaded = false;
        private boolean isLoading = true;


        public boolean isLoaded() {
            return isLoaded;
        }

        public void setLoaded(boolean loaded) {
            isLoaded = loaded;
        }

        public boolean isLoading() {
            return isLoading;
        }

        public void finishLoading() {
            isLoading = false;
        }

        public com.google.android.gms.ads.InterstitialAd getAdmobFullAds(Context context) {
            ads = new com.google.android.gms.ads.InterstitialAd(context);
            type = ADMOB_PREFIX;
            return (com.google.android.gms.ads.InterstitialAd) ads;
        }

        public PublisherInterstitialAd getDfpFullAds(Context context) {
            ads = new PublisherInterstitialAd(context);
            type = DFP_PREFIX;
            return (PublisherInterstitialAd) ads;
        }

        public com.facebook.ads.InterstitialAd getFbFullAds(Context context, String id) {
            ads = new com.facebook.ads.InterstitialAd(context, id);
            type = FB_PREFIX;
            return (com.facebook.ads.InterstitialAd) ads;
        }

        public void show() {
            if (!isLoaded || ads == null)
                return;
            switch (type) {
                case ADMOB_PREFIX:
                    ((com.google.android.gms.ads.InterstitialAd) ads).show();
                    break;
                case DFP_PREFIX:
                    ((PublisherInterstitialAd) ads).show();
                    break;
                case FB_PREFIX:
                    ((com.facebook.ads.InterstitialAd) ads).show();
                    break;
            }
        }

        public void destroy() {
            if (ads == null)
                return;
            switch (type) {
                case FB_PREFIX:
                    ((com.facebook.ads.InterstitialAd) ads).destroy();
                    break;
            }
        }
    }

}

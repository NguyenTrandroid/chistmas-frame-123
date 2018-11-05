package pt.content.library.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import pt.content.library.R;


public class RateHelper {

    public static void check(Context context) {
        ApiHelper.startRate(context);
    }

    public static void showOnAction(AppCompatActivity context) {
        Log.d("RateHelper", "showOnAction :");
        if (!isRated(context) && !hadShow(context) && checkShow(context) && enable(context)) {
            Log.d("RateHelper", "showOnAction : inside");
            show(context, false);
        }
    }

    public static boolean showOnBackpress(AppCompatActivity context) {

        if (count(context, "backpress") % 5 == 0 && !isRated(context) && enable(context)) {
            if (!show(context, true))
                return true;
            else return false;
        } else
            return true;
    }


    public static void actionStart(Context context) {
        Saver.write(context, "SHOW_REVIEW", true);
    }

    private static boolean checkShow(Context context) {
        return Saver.readBoolean(context, "SHOW_REVIEW", false);
    }

    private static boolean enable(Context context) {
        return Saver.readBoolean(context, "SHOW_ENABLE", true);
    }

    private static boolean isRated(Context context) {
        return Saver.readBoolean(context, "RATED", false);
    }

    public static boolean isPremium(Context context) {
        return Saver.readBoolean(context, "PREMIUM", false);
    }

    private static boolean hadShow(Context context) {
        return Saver.readBoolean(context, "SHOW_TIME", false);
    }


    public static boolean show(final AppCompatActivity context, final boolean isBackpress) {
        final int level = Saver.readInt(context, "level", 0);
        if (level == 0)
            return false;
        Saver.write(context, "SHOW_TIME", true);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        TextView subtitle = (TextView) view.findViewById(R.id.sub_title);
        TextView body = (TextView) view.findViewById(R.id.body);
        subtitle.setText(Saver.readString(context, "dialog_sub_title", ""));
        body.setText(Saver.readString(context, "dialog_body", ""));
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                .title(Saver.readString(context, "dialog_title", ""))
                .customView(view, false)
                .positiveText(Saver.readString(context, "dialog_pos", ""))
                .negativeText(Saver.readString(context, "dialog_nev", ""))
                .backgroundColor(Color.WHITE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Saver.write(context, "RATED", true);
                        if (level == 2) {
                            Saver.write(context, Saver.readString(context, "key", "RATE_ID"), true);

                        }
                        gotoPlay(context);
                    }
                })
                .positiveColor(Color.parseColor("#FF6D00"))
                .negativeColor(Color.GRAY)
                .titleColor(Color.parseColor("#FF6D00"))
                .autoDismiss(true)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (isBackpress)
                            context.finish();
                        if (count(context, "DISMISS_COUNT") > 5)
                            Saver.write(context, "SHOW_ENABLE", false);
                    }
                });


        dialog.show();

        return true;
    }

    public static boolean showNoTracking(final AppCompatActivity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
        TextView subtitle = view.findViewById(R.id.sub_title);
        TextView body = view.findViewById(R.id.body);
        subtitle.setText(Saver.readString(context, "dialog_sub_title_default", ""));
        body.setText(Saver.readString(context, "dialog_body_default", ""));
        MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                .title(Saver.readString(context, "dialog_title_default", ""))
                .customView(view, false)
                .positiveText(Saver.readString(context, "dialog_pos_default", ""))
                .negativeText(Saver.readString(context, "dialog_nev_default", ""))
                .backgroundColor(Color.WHITE)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Saver.write(context, "RATED", true);
                        gotoPlay(context);
                    }
                })
                .positiveColor(Color.parseColor("#FF6D00"))
                .negativeColor(Color.GRAY)
                .titleColor(Color.parseColor("#FF6D00"))
                .autoDismiss(true);


        dialog.show();

        return true;
    }

    public static void gotoPlay(Context context) {
        try {
            Intent rateIntent = rateIntentForUrl(context, "market://details");
            context.startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl(context, "https://play.google.com/store/apps/details");
            context.startActivity(rateIntent);
        }
    }

    private static Intent rateIntentForUrl(Context context, String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    private static int count(Context context, String key) {
        int result = Saver.readInt(context, key, 0);
        result++;
        Saver.write(context, key, result);
        return result;
    }

}

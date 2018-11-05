package pt.content.library.version;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import pt.content.library.tracking.EventTracking;
import pt.content.library.util.ApiHelper;
import pt.content.library.util.RateHelper;
import pt.content.library.util.Saver;

import static pt.content.library.Constant.TIME_VERSION_REFRESH;

public class VersionUpdater {
    public static void check(final Activity context) {
        if (Saver.readLong(context, "UPDATE_TIME", 0) > System.currentTimeMillis() - TIME_VERSION_REFRESH)
            return;
        EventTracking.setUserProperty(context.getApplicationContext(),"country_tag", ApiHelper.getCurrentLocate(context.getApplicationContext()));
        int updateVersion = Saver.readInt(context, "update_version", 0);
        int version = 0;
        Drawable icon = null;
        try {
            version = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
            icon = context.getPackageManager().getApplicationIcon(context.getPackageName());

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (version < updateVersion) {
            MaterialDialog.Builder dialog = new MaterialDialog.Builder(context)
                    .title("New Version Available")
                    .content("Please, update app to new version to continue using.")
                    .contentColor(Color.GRAY)
                    .positiveText("UPDATE")
                    .backgroundColor(Color.WHITE)
                    .negativeText("LATER")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            RateHelper.gotoPlay(context);
                            Saver.write(context, "UPDATE_TIME", System.currentTimeMillis());
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            Saver.write(context, "UPDATE_TIME", System.currentTimeMillis());
                        }
                    })
                    .positiveColor(Color.parseColor("#FF6D00"))
                    .negativeColor(Color.GRAY)
                    .titleColor(Color.parseColor("#FF6D00"))
                    .autoDismiss(true);
            if (icon != null)
                dialog.icon(icon).limitIconToDefaultSize();
            dialog.show();
        }
    }

}

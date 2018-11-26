package christmas.frame.photoedittor.collage.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import christmas.frame.photoedittor.collage.R;


/**
 * Created by admin on 9/3/18.
 */

public class SLoading {
    private Context context;
    private AlertDialog dialog;

    public SLoading(Context context) {
        this.context = context;
    }

    /**
     * Show loading dialog
     */
    public void show() {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_loading, null, true);
        dialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();

        if (((Activity) context).isFinishing()) {
            return;
        }
        if (!dialog.isShowing()) {
            dialog.show();
        }
        Window window = dialog.getWindow();
        if (window != null) {
            int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.30);
            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
            window.setLayout(width, height);
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setDimAmount(0.5f);
        }
    }

    /**
     * Dismiss loading dialog
     */
    public void dismiss() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }
}

package pt.content.library.dialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import pt.content.library.R;

import static android.util.Log.d;

public class RateView extends LinearLayout {
    Timer timer = new Timer();
    int position = 1;
    boolean fill = true;

    public RateView(Context context) {
        super(context);
        inflate(context, R.layout.rate_view, this);
    }

    public RateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.rate_view, this);
    }

    public RateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, R.layout.rate_view, this);
    }

    public void start() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("RateView","run: ");
                new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {


                        if (position == 6) {
                            if(fill)
                                empty(5);
                            else
                                fill(5);
                            fill=!fill;
                        } else {
                            if (position > 1)
                                findViewById(getStrResId(getContext(), "_" + (position - 1) + "hand")).setVisibility(INVISIBLE);
                            fill(position);
                            position++;
                        }
                    }
                });

            }
        }, 0, 300);
    }

    private int getStrResId(Context context, String name) {
        return context.getResources().getIdentifier(name, "id", context.getPackageName());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        timer.cancel();
    }

    private void fill(int position) {
        findViewById(getStrResId(getContext(), "_" + position + "hand")).setVisibility(VISIBLE);
        ((ImageView) findViewById(getStrResId(getContext(), "_" + position + "star"))).setImageResource(R.drawable.ic_green_star);
    }

    private void empty(int position) {
        findViewById(getStrResId(getContext(), "_" + position + "hand")).setVisibility(INVISIBLE);
        ((ImageView) findViewById(getStrResId(getContext(), "_" + position + "star"))).setImageResource(R.drawable.ic_emtry_star);
    }
}

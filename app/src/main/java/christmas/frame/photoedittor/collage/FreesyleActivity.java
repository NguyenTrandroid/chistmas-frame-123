package christmas.frame.photoedittor.collage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import bo.photo.module.sticker.StickerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.background.FragmentBackground;
import christmas.frame.photoedittor.collage.background.FragmentGalleryBackground;
import christmas.frame.photoedittor.collage.background.OnBackgroundSelect;
import christmas.frame.photoedittor.collage.filter.FragmentFilter;
import christmas.frame.photoedittor.collage.filter.OnFilterSelect;
import christmas.frame.photoedittor.collage.filter.OnValueAlphaFilter;
import christmas.frame.photoedittor.collage.tab.OnColorSelect;

public class FreesyleActivity extends AppCompatActivity implements OnBackgroundSelect, OnColorSelect, OnFilterSelect, OnValueAlphaFilter {

    @BindView(R.id.iv_framearea)
    ImageView ivFramearea;
    @BindView(R.id.photo_area)
    StickerView photoArea;
    @BindView(R.id.sticker_area)
    StickerView stickerArea;
    @BindView(R.id.iv_filterarea)
    ImageView ivFilterarea;
    @BindView(R.id.cv_freestyle)
    CardView cvFreestyle;
    @BindView(R.id.iv_save)
    ImageButton ivSave;
    @BindView(R.id.iv_bgr)
    ImageView ivBgr;
    @BindView(R.id.iv_addphoto)
    ImageView ivAddphoto;
    @BindView(R.id.iv_sticker)
    ImageView ivSticker;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.iv_txt)
    ImageView ivTxt;
    @BindView(R.id.f_addframe)
    FrameLayout fAddframe;
    @BindView(R.id.ln_ads)
    LinearLayout lnAds;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.ln_chucnag)
    LinearLayout lnChucnag;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freestyle);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


    }

    @OnClick({R.id.iv_framearea, R.id.photo_area, R.id.sticker_area, R.id.iv_filterarea, R.id.iv_save, R.id.iv_addphoto, R.id.iv_sticker, R.id.iv_filter, R.id.iv_bgr})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_framearea:
                break;
            case R.id.photo_area:
                break;
            case R.id.sticker_area:
                break;
            case R.id.iv_filterarea:
                break;
            case R.id.iv_save:
                break;
            case R.id.iv_addphoto:
                break;
            case R.id.iv_sticker:
                break;
            case R.id.iv_filter:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, new FragmentFilter()).addToBackStack("addFilter");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {


                }

                break;
            case R.id.iv_bgr:
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, new FragmentBackground()).addToBackStack("addphoto");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {


                }
                break;
        }
    }

    @Override
    public void sendBackground(String path, boolean closeFragment) {
        if (path.equals("none")) {
            ivFramearea.setBackground(null);
        } else if (path.equals("add")) {
            fragmentManager.popBackStack();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.f_addframe, new FragmentGalleryBackground()).addToBackStack("gallerybackground");
            try {
                fragmentTransaction.commit();
            } catch (IllegalStateException ignored) {

            }
        } else {
            ivFramearea.setBackground(Drawable.createFromPath(path));
        }
    }


    @Override
    public void sendColor(int color) {
        if (color == 0) {
            fragmentManager.popBackStack();
        } else {
            ivFramearea.setBackgroundColor(color);
            fragmentManager.popBackStack();
        }
    }

    @Override
    public void sendPathLib(String path) {
        fragmentManager.popBackStack();
        ivFramearea.setBackground(Drawable.createFromPath(path));

    }

    @Override
    public void sendFilter(String path) {
        ivFilterarea.setBackground(Drawable.createFromPath(path));

    }

    @Override
    public void sendValue(float value) {
        ivFilterarea.setAlpha(1 - value);

    }
}

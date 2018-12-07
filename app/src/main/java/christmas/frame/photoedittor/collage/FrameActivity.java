package christmas.frame.photoedittor.collage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import bo.photo.module.sticker.BitmapStickerIcon;
import bo.photo.module.sticker.DeleteIconEvent;
import bo.photo.module.sticker.DrawableSticker;
import bo.photo.module.sticker.FlipHorizontallyEvent;
import bo.photo.module.sticker.StickerView;
import bo.photo.module.sticker.TextSticker;
import bo.photo.module.sticker.ZoomIconEvent;
import bo.photo.module.util.ImageHelper;
import bo.photo.module.util.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.addphoto.FragmentAddPhoto;
import christmas.frame.photoedittor.collage.addphoto.FragmentAddPhotoList;
import christmas.frame.photoedittor.collage.addphoto.FragmentGalleryPhoto;
import christmas.frame.photoedittor.collage.addphoto.FragmentGalleryPhotoList;
import christmas.frame.photoedittor.collage.addphoto.OnImgCamera;
import christmas.frame.photoedittor.collage.addphoto.OnPhotoSelectt;
import christmas.frame.photoedittor.collage.addsticker.Activity_MenuSticker;
import christmas.frame.photoedittor.collage.addsticker.FragmentAddSticker;
import christmas.frame.photoedittor.collage.addsticker.OnStickerSelect;
import christmas.frame.photoedittor.collage.background.FragmentGalleryBackground;
import christmas.frame.photoedittor.collage.background.OnBackgroundSelect;
import christmas.frame.photoedittor.collage.filter.FragmentFilter;
import christmas.frame.photoedittor.collage.filter.OnFilterSelect;
import christmas.frame.photoedittor.collage.filter.OnValueAlphaFilter;
import christmas.frame.photoedittor.collage.frame.FragmentFrame;
import christmas.frame.photoedittor.collage.frame.FragmentGalleryFrame;
import christmas.frame.photoedittor.collage.frame.OnFrameSelect;
import christmas.frame.photoedittor.collage.frame.OnGalleryFrameSelect;
import christmas.frame.photoedittor.collage.model.TextPicker;
import christmas.frame.photoedittor.collage.prefs.Extras;
import christmas.frame.photoedittor.collage.saveScreen.SaveActivity;
import christmas.frame.photoedittor.collage.tab.OnColorSelect;
import christmas.frame.photoedittor.collage.text.FragmentText;
import christmas.frame.photoedittor.collage.text.OnTextSelete;
import christmas.frame.photoedittor.collage.utils.SaveImg;
import pt.content.library.ads.AdsHelper;
public class FrameActivity extends AppCompatActivity implements OnFrameSelect, OnGalleryFrameSelect, OnImgCamera, OnBackgroundSelect, OnColorSelect, OnValueAlphaFilter, OnTextSelete, OnFilterSelect,SaveImg.OnSave, OnStickerSelect, OnPhotoSelectt {

    @BindView(R.id.ln_ads)
    LinearLayout lnAds;
    @BindView(R.id.iv_framearea)
    ImageView ivFramearea;
    @BindView(R.id.photo_area)
    StickerView photoArea;
    @BindView(R.id.sticker_area)
    StickerView stickerArea;
    @BindView(R.id.iv_filterarea)
    ImageView ivFilterarea;
    @BindView(R.id.cv_frame)
    CardView cvFrame;
    @BindView(R.id.iv_save)
    ImageButton ivSave;
    @BindView(R.id.iv_bgr)
    ImageView ivBgr;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.iv_addphoto)
    ImageView ivAddphoto;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.iv_sticker)
    ImageView ivSticker;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.iv_txt)
    ImageView ivTxt;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.ln_chucnag)
    LinearLayout lnChucnag;
    @BindView(R.id.f_addframe)
    FrameLayout fAddframe;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    SaveImg saveImg;
    FragmentGalleryPhotoList fragmentGalleryPhotoList;
    FragmentAddSticker fragmentAddSticker;
    FragmentFrame fragmentFrame;
    FragmentAddPhotoList fragmentAddPhotoList;
    FragmentFilter fragmentFilter;
    FragmentText fragmentText;
    FragmentGalleryPhoto fragmentGalleryPhoto;
    FragmentAddPhoto fragmentAddPhoto;
    @BindView(R.id.iv_ads)
    ImageView ivAds;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    private ImageHelper imageHelper;
    private ArrayList<String> listphoto;
    AdsHelper adsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        adsHelper = new AdsHelper();
        fragmentManager = getSupportFragmentManager();
        fragmentAddPhoto = new FragmentAddPhoto();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentGalleryPhoto = new FragmentGalleryPhoto();
        fragmentFrame = new FragmentFrame();
        fragmentAddPhotoList = new FragmentAddPhotoList();
        fragmentAddSticker = new FragmentAddSticker();
        fragmentText = new FragmentText();
        fragmentFilter = new FragmentFilter();
        BitmapStickerIcon deleteIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_close_white_18dp),
                BitmapStickerIcon.LEFT_TOP);
        deleteIcon.setIconEvent(new DeleteIconEvent());
        BitmapStickerIcon zoomIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_scale_white_18dp),
                BitmapStickerIcon.RIGHT_BOTOM);
        zoomIcon.setIconEvent(new ZoomIconEvent());
        BitmapStickerIcon flipIcon = new BitmapStickerIcon(ContextCompat.getDrawable(this,
                R.drawable.sticker_ic_flip_white_18dp),
                BitmapStickerIcon.RIGHT_TOP);
        flipIcon.setIconEvent(new FlipHorizontallyEvent());
        stickerArea.setIcons(Arrays.asList(deleteIcon, zoomIcon, flipIcon));
        imageHelper = new ImageHelper(this);
        saveImg = new SaveImg(this);
        adsHelper.loadAds(this, lnAds, "banner_artwork", new AdsHelper.AdsCallback() {
            @Override
            public void onLoaded(Context context, String position, String id, String type, int reload) {
                super.onLoaded(context, position, id, type, reload);
            }

            @Override
            public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
                super.onError(context, position, id, type, reload, errorCode);
                lnAds.setVisibility(View.GONE);
                ivAds.setVisibility(View.VISIBLE);

            }
        });

    }

    @OnClick({R.id.iv_save, R.id.iv_bgr, R.id.iv_addphoto, R.id.iv_sticker, R.id.iv_filter, R.id.iv_txt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_save:
                stickerArea.clearIcon();
                photoArea.clearIcon();
                try {
                    String picName = TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss") + ".jpg";
                    Bitmap bitmap = applyChangesBitmap();
                    saveImg.saveImageToInternalStorage(bitmap, picName);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Save Fails", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_bgr:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, fragmentFrame).addToBackStack("addframe");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {
                }
                break;
            case R.id.iv_addphoto:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, fragmentAddPhoto).addToBackStack("addphoto");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }

                break;
            case R.id.iv_sticker:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, fragmentAddSticker).addToBackStack("addsticker");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
                break;
            case R.id.iv_filter:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, fragmentFilter).addToBackStack("addFilter");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {


                }
                break;
            case R.id.iv_txt:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, fragmentText).addToBackStack("text");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {


                }
                break;
        }
    }

    @Override
    public void sendFrame(String path) {
        if (path.equals("none")) {
            ivFilterarea.setBackground(null);
        } else if (path.equals("add")) {
            ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.f_addframe, new FragmentGalleryFrame()).addToBackStack("galleryframe");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {
                    Log.d("AAA", ignored + "");
                }
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } else {
            ivFilterarea.setBackground(Drawable.createFromPath(path));

        }
    }

    private Bitmap applyChangesBitmap() {
        final Bitmap newBitmap = Bitmap.createBitmap(cvFrame.getWidth(), cvFrame.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas cv = new Canvas(newBitmap);

        cvFrame.draw(cv);

        return newBitmap;
    }

    @Override
    public void sendGalleryFrame(String path) {
        ivFramearea.setBackground(Drawable.createFromPath(path));
        fragmentManager.popBackStack();

    }


    @Override
    public void sendImgCamera(BitmapDrawable bitmapDrawable) {
        photoArea.addSticker(new DrawableSticker(imageHelper.resize(bitmapDrawable)));

    }
    @Override
    public void sendSticker(String path) {
        if (path.equals("none")) {
            stickerArea.removeAllStickers();
        } else if (path.equals("add")) {
            fragmentManager.popBackStack();
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                Intent intent = new Intent(this, Activity_MenuSticker.class);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } else {
            Drawable drawable = Drawable.createFromPath(path);
            stickerArea.addSticker(new DrawableSticker(drawable));
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
            fragmentManager.popBackStack();
        }

    }

    @Override
    public void sendFilter(String path) {
        ivFilterarea.setBackground(Drawable.createFromPath(path));

    }

    @Override
    public void sendValue(float value) {
        ivFilterarea.setAlpha(1 - value);

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
        imageHelper.resize(Drawable.createFromPath(path));
        ivFramearea.setBackground(Drawable.createFromPath(path));


    }

    @Override
    public void sendText(TextPicker textPicker) {
        stickerArea.addSticker(
                new TextSticker(getApplicationContext())
                        .setText(textPicker.getText())
                        .setMaxTextSize(textPicker.getTextsize())
                        .setTypeface(textPicker.getTypeface(), textPicker.isBold(), textPicker.isItalic(), textPicker.isUnderline())
                        .setTextColor(textPicker.getTextcolor())
                        .resizeText());

    }

    @Override
    public void onSaveCompleted(String path, String share) {
        Intent intentSave = new Intent(this, SaveActivity.class);
        intentSave.putExtra(Extras.SAVE_PATH, path);
        intentSave.putExtra("share", share);
        finish();
        startActivity(intentSave);
        Toast.makeText(this, "Save complete", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("testcount", count + "");
        if (count == 0) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to save?");
            builder.setCancelable(false);
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ivSave.callOnClick();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.setNegativeButton("Leave", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            if (count == 1) {
                if (getCurrentFragment() != null) {
                    if (getCurrentFragment().equals(fragmentGalleryPhotoList) && listphoto.isEmpty()) {
                        finish();
                    }
                }
                getSupportFragmentManager().popBackStack();
                for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
            getSupportFragmentManager().popBackStack();
        }
    }

    Fragment getCurrentFragment() {
        Fragment currentFragment = getSupportFragmentManager()
                .findFragmentById(R.id.rl_main);
        return currentFragment;
    }


    @Override
    public void sendPhoto(String path, boolean closeFragment) {
        Drawable drawable = Drawable.createFromPath(path);
        photoArea.removeAllStickers();
        photoArea.addSticker(new DrawableSticker(imageHelper.resize(drawable)));
    }
}

package christmas.frame.photoedittor.collage;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;

import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.dialog.DialogPolicy;
import christmas.frame.photoedittor.collage.utils.FileUtils;
import christmas.frame.photoedittor.collage.utils.Permissionruntime;
import pt.content.library.ads.AdsHelper;

import static android.util.Log.d;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.m_policy)
    ImageView mPolicy;
    @BindView(R.id.iv_frame)
    ImageView ivFrame;
    @BindView(R.id.iv_freestyle)
    ImageView ivFreestyle;
    @BindView(R.id.iv_arwork)
    ImageView ivArwork;
    @BindView(R.id.iv_rate)
    ImageView ivRate;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.m_rlbt)
    ConstraintLayout mRlbt;
    Permissionruntime permissionruntime;
    AdsHelper adsHelper;
    @BindView(R.id.iv_ads1)
    ImageView ivAds1;
    @BindView(R.id.ln_ads1)
    LinearLayout lnAds1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionruntime = new Permissionruntime(this);
        permissionruntime.requestPermission();
        Log.d("CCC",permissionruntime.toString());
        adsHelper = new AdsHelper();
        ButterKnife.bind(this);
        adsHelper.loadAds(this, lnAds1, "banner_artwork", new AdsHelper.AdsCallback() {
            @Override
            public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
                super.onError(context, position, id, type, reload, errorCode);
                lnAds1.setVisibility(View.GONE);
                ivAds1.setVisibility(View.VISIBLE);
                d("ICT_FragmentMenuOption", "onError: main");
            }

            @Override
            public void onLoaded(Context context, String position, String id, String type, int reload) {
                super.onLoaded(context, position, id, type, reload);
                d("ICT_FragmentMenuOption", "onLoaded: main");
            }
        });

        File file = new
                File(SupportUtils.getRootDirPath(this) + "/sticker/");
        String[] list;
        list = file.list();
        if (list == null) {
            Log.d("testmain", "loading file");
            FileUtils.getInst().copyAssetDirToFiles(this, "frame");
            FileUtils.getInst().copyAssetDirToFiles(this, "background");
            FileUtils.getInst().copyAssetDirToFiles(this, "filter");
            FileUtils.getInst().copyAssetDirToFiles(this, "sticker");
        }
    }

    @OnClick({R.id.m_policy, R.id.iv_frame, R.id.iv_freestyle, R.id.iv_arwork, R.id.iv_rate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.m_policy:
                DialogPolicy dialogPolicy = new DialogPolicy();
                dialogPolicy.buildDialog(this).show();

                break;
            case R.id.iv_frame:
                if(checkCameraPermission() && checkReadExternalPermission() && checkWriteExternalPermission()){
                startActivity(new Intent(this, FrameActivity.class));
                }else {
                    permissionruntime.requestPermission();
                }
                break;
            case R.id.iv_freestyle:
                if(checkCameraPermission() && checkReadExternalPermission() && checkWriteExternalPermission()){
                    startActivity(new Intent(this, FreesyleActivity.class));
                }else {
                    permissionruntime.requestPermission();
                }
                break;
            case R.id.iv_arwork:
                if(checkWriteExternalPermission()){
                    startActivity(new Intent(this, ArtworkkActivity.class));
                }else {
                    permissionruntime.requestPermission();
                }
                break;
            case R.id.iv_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));
                break;
        }
    }
    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkReadExternalPermission() {
        String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }
    private boolean checkCameraPermission() {
        String permission = Manifest.permission.CAMERA;
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

}

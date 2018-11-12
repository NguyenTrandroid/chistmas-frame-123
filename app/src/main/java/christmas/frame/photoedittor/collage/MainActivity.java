package christmas.frame.photoedittor.collage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.utils.FileUtils;
import christmas.frame.photoedittor.collage.utils.Permissionruntime;

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
    Permissionruntime permissionruntime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permissionruntime = new Permissionruntime(this);
        permissionruntime.requestStoragePermission();

        ButterKnife.bind(this);
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
                break;
            case R.id.iv_frame:
                break;
            case R.id.iv_freestyle:
                startActivity(new Intent(this,FreesyleActivity.class));

                break;
            case R.id.iv_arwork:
                break;
            case R.id.iv_rate:
                break;
        }
    }
}

package christmas.frame.photoedittor.collage.saveScreen;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.App;
import christmas.frame.photoedittor.collage.ArtworkkActivity;
import christmas.frame.photoedittor.collage.MainActivity;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.prefs.Extras;

public class SaveActivity extends AppCompatActivity {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_home)
    TextView tvHome;
    @BindView(R.id.rl_rela_top)
    RelativeLayout rlRelaTop;
    @BindView(R.id.iv_review)
    ImageView ivReview;
    @BindView(R.id.cv_1)
    CardView cv1;
    @BindView(R.id.tv_center)
    TextView tvCenter;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R.id.tv_rate)
    TextView tvRate;
    @BindView(R.id.iv_rate)
    ImageView ivRate;
    @BindView(R.id.rl_rate)
    RelativeLayout rlRate;
    @BindView(R.id.rl_1)
    RelativeLayout rl1;
    @BindView(R.id.iv_ads)
    ImageView ivAds;
    private String path;
    ArrayList<String> a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            path = getIntent().getStringExtra(Extras.SAVE_PATH);
            Glide.with(SaveActivity.this)
                    .load(path)
                    .into(ivReview);
        }
        a = new ArrayList<>();
    }

    @OnClick({R.id.rl_back, R.id.tv_home, R.id.rl_share, R.id.rl_rate, R.id.iv_ads})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                startActivity(new Intent(this, ArtworkkActivity.class));
                break;
            case R.id.tv_home:
                finish();
                break;
            case R.id.rl_share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                try {
                    a = saveToInternalStorage(bitmap, "share.jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("testshare", a.get(1));
                Log.d("testshare", a.get(0));
                Uri screenshotUri = Uri.parse(a.get(1));
                share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivityForResult(Intent.createChooser(share, "Share image via..."), 1);
                break;
            case R.id.rl_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));
                break;
            case R.id.iv_ads:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(this, ArtworkkActivity.class));
        super.onBackPressed();
    }

    private ArrayList<String> saveToInternalStorage(Bitmap bitmapImage, String fileName) throws IOException {
        ArrayList<String> a = new ArrayList<>();
        ContextWrapper cw = new ContextWrapper(App.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        String share = MediaStore.Images.Media.insertImage(App.getAppContext().getContentResolver(), mypath.getAbsolutePath(), mypath.getName(), fileName);
        a.add(mypath.getAbsolutePath());
        a.add(share);
        return a;
    }
}

package christmas.frame.photoedittor.collage;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import christmas.frame.photoedittor.collage.adapter.ArtworkAdapter;
import christmas.frame.photoedittor.collage.showcastimage.FragmentShowCastImage;
import pt.content.library.ads.AdsHelper;

public class ArtworkkActivity extends AppCompatActivity implements ArtworkAdapter.OnPhotoSelect, FragmentShowCastImage.OnDeleteImage {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_artwork)
    TextView tvArtwork;
    @BindView(R.id.rl_rela_top)
    RelativeLayout rlRelaTop;
    @BindView(R.id.rv_galleryartwork)
    RecyclerView rvGalleryartwork;
    @BindView(R.id.ll_ads)
    LinearLayout llAds;
    @BindView(R.id.iv_ads)
    ImageView ivAds;
    @BindView(R.id.layoutAds)
    FrameLayout layoutAds;
    @BindView(R.id.f_addframe)
    FrameLayout fAddframe;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    AdsHelper adsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artworkk);
        ButterKnife.bind(this);
        init();


    }

    private void init() {
        adsHelper = new AdsHelper();
        ArrayList<String> test;
        test = loadFile(this, "app_imageDir");
        Collections.reverse(test);
        rvGalleryartwork.setLayoutManager(new GridLayoutManager(this, 2));
        rvGalleryartwork.setHasFixedSize(true);
        ArtworkAdapter adapter = new ArtworkAdapter(test, this);
        rvGalleryartwork.setAdapter(adapter);
        adsHelper.loadAds(this, llAds, "banner_artwork", new AdsHelper.AdsCallback() {
            @Override
            public void onLoaded(Context context, String position, String id, String type, int reload) {
                super.onLoaded(context, position, id, type, reload);
            }

            @Override
            public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
                super.onError(context, position, id, type, reload, errorCode);
                llAds.setVisibility(View.GONE);
                ivAds.setVisibility(View.VISIBLE);

            }
        });

    }

    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new File(Environment.getExternalStorageDirectory().toString().concat("/").concat(App.getAppContext().getResources().getString(R.string.app_name)));
        String[] list;
        list = file.list();
        if (list == null) {
            return arrayList;
        } else {
            for (String files : list) {
                Log.d("testfile2", files);
                arrayList.add(file + "/" + files);
            }
        }
        return arrayList;

    }

    @Override
    public void onPhotoPass(String path) {
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentShowCastImage fragmentShowCastImage = new FragmentShowCastImage();
        fragmentShowCastImage.setArguments(bundle);
        fragmentTransaction.replace(R.id.f_addframe, fragmentShowCastImage).addToBackStack("showcast");
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException ignored) {

        }
        Log.d("CCC", path);
    }

    @Override
    public void onDeleted() {
        ArrayList<String> test = loadFile(this, "app_imageDir");
        Collections.reverse(test);
        ArtworkAdapter adapter = new ArtworkAdapter(test, this);
        rvGalleryartwork.swapAdapter(adapter, true);

    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("testcount", count + "");
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}

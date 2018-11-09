package christmas.frame.photoedittor.collage;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

import bo.photo.module.util.SupportUtils;
import christmas.frame.photoedittor.collage.addbackground.FragmentAddBackground;
import christmas.frame.photoedittor.collage.addbackground.OnBackgroundSelect;
import christmas.frame.photoedittor.collage.utils.FileUtils;

public class MainActivity extends AppCompatActivity implements OnBackgroundSelect {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        File file = new
                File(SupportUtils.getRootDirPath(this) + "/sticker/");
        String[] list;
        list = file.list();
        if(list==null){
            Log.d("testmain", "loading file");
            FileUtils.getInst().copyAssetDirToFiles(this, "frame");
            FileUtils.getInst().copyAssetDirToFiles(this, "background");
            FileUtils.getInst().copyAssetDirToFiles(this, "filter");
            FileUtils.getInst().copyAssetDirToFiles(this, "sticker");
        }
        FragmentAddBackground fragmentAddBackground = new FragmentAddBackground();
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.m_rlbt, fragmentAddBackground).addToBackStack("addBackground");
        try {
            fragmentTransaction.commit();
        } catch (IllegalStateException ignored) {

        }
    }

    @Override
    public void sendBackground(String path, boolean closeFragment) {

    }
}

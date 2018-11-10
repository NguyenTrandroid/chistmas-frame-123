package christmas.frame.photoedittor.collage;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import christmas.frame.photoedittor.collage.tab.TabBackground;
import christmas.frame.photoedittor.collage.tab.TabColor;
import christmas.frame.photoedittor.collage.tab.TabLib;

public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                TabColor tabColor = new TabColor();
                Log.d("AAA","0");
                return tabColor;
            }
            case 1: {
                TabBackground tabBackground = new TabBackground();
                Log.d("AAA","1");

                return tabBackground;
            }
            case 2: {
                TabLib tabLib = new TabLib();
                Log.d("AAA","2");

                return tabLib;
            }

        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position){
            case 0:
                title="Color";
                break;
            case 1:
                title="Background";
                break;
            case 2:
                title="Library";
                break;
        }
        return title;
    }
}

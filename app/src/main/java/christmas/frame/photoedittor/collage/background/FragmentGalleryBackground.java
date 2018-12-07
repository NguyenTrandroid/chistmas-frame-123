package christmas.frame.photoedittor.collage.background;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import christmas.frame.photoedittor.collage.adapter.PagerAdapter;
import christmas.frame.photoedittor.collage.R;
public class FragmentGalleryBackground extends Fragment {
    private ViewPager pager;
    private TabLayout tabLayout;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addbackground, null);
        pager = view.findViewById(R.id.wp_addbgr);
        tabLayout = view.findViewById(R.id.tl_addbgr);
        PagerAdapter adapter = new PagerAdapter(getFragmentManager());
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);
        return view;
    }
}
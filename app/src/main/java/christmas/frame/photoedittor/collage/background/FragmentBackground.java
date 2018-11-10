package christmas.frame.photoedittor.collage.background;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.background.adapter.BackgroundAdapter;

public class FragmentBackground extends Fragment {


    Unbinder unbinder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentGalleryBackground fragmentGalleryBackground;
    String pathnone;
    String pathtow;
    @BindView(R.id.rv_background)
    RecyclerView rvBackground;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentGalleryBackground = new FragmentGalleryBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_background, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_background);
        ArrayList<String> test = loadFile(getContext(), "background");
        Collections.reverse(test);
        test.add(0,pathnone);
        test.add(0,pathtow);


        BackgroundAdapter adapter = new BackgroundAdapter(test, getContext());
        recyclerView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new
                File(SupportUtils.getRootDirPath(context) + "/" + folder + "/");
        String[] list;
        list = file.list();
        for (String files : list) {
            if (files.equals("addd.png")) {
                pathnone = file + "/" + files;
            }else if(files.equals("nonee.png")){
                pathtow = file + "/" + files;
            }else {
            arrayList.add(file + "/" + files);
        }}
        return arrayList;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.rv_background, R.id.rl_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rv_background:
                break;
            case R.id.rl_main:
                break;
        }
    }
}

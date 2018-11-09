package christmas.frame.photoedittor.collage.addbackground;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import christmas.frame.photoedittor.collage.addbackground.adapter.AddBackgroundAdapter;

public class FragmentAddBackground extends Fragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.rv_background)
    RecyclerView rvFilter;
    @BindView(R.id.iv_opengallerybackground)
    ImageView ivAddsticker;
    Unbinder unbinder;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentGalleryBackground fragmentGalleryBackground;
    String pathnone;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentGalleryBackground = new FragmentGalleryBackground();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addbackground2, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_background);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        ArrayList<String> test = loadFile(getContext(),"background");
        Collections.reverse(test);
        test.add(0,pathnone);
        AddBackgroundAdapter adapter = new AddBackgroundAdapter(test,getContext());
        recyclerView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }
    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new
                File(SupportUtils.getRootDirPath(context) + "/"+folder+"/");
        String[] list;
        list = file.list();
        for (String files : list) {
            if(files.equals("ic_none.png")){
                pathnone=file+"/"+files;
            }else arrayList.add(file+"/"+files);
        }
        return arrayList;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_close, R.id.rv_background, R.id.iv_opengallerybackground})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                fragmentManager.popBackStack();
                break;
            case R.id.rv_background:
                break;
            case R.id.iv_opengallerybackground:
                fragmentManager.popBackStack();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_main,fragmentGalleryBackground).addToBackStack("gallerybackground");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
                break;
        }
    }
}

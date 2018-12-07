package christmas.frame.photoedittor.collage.addsticker;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.AddStickerAdapter;

public class FragmentAddSticker extends Fragment {
    Unbinder unbinder;
    boolean isCollapseRV = true;
    OnStickerSelect onStickerSelect;

    FragmentManager fragmentManager;
    private String pathnone;
    private String pathadd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bgr_sticker, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fragmentManager.popBackStack();

            }
        });
        onStickerSelect = (OnStickerSelect) getActivity();
        RecyclerView recyclerView = view.findViewById(R.id.rv_background);
        ArrayList<String> test = loadFile(getContext(), "sticker");

        Collections.reverse(test);
        test.add(0, pathadd);
        test.add(0, pathnone);
        AddStickerAdapter adapter = new AddStickerAdapter(test, getContext());
        recyclerView.setAdapter(adapter);
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
                pathadd = file + "/" + files;
            } else if (files.equals("nonee.png")) {
                pathnone = file + "/" + files;
            } else if (files.contains(".")) {
                arrayList.add(file + "/" + files);
            } else arrayList.addAll(loadFile(context, folder + "/" + files));
        }
        return arrayList;

    }

    public ArrayList<String> getFile(File dir) {
        ArrayList<String> arrayList = new ArrayList<>();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (File file : listFile) {
                if (file.isDirectory()) {
                    getFile(file);
                } else {
                    if (file.getName().endsWith(".png")
                            || file.getName().endsWith(".jpg")
                            || file.getName().endsWith(".jpeg")
                            || file.getName().endsWith(".gif")
                            || file.getName().endsWith(".bmp")
                            || file.getName().endsWith(".webp")) {
                        String temp = file.getPath().substring(0, file.getPath().lastIndexOf('/'));
                        if (!arrayList.contains(temp))
                            arrayList.add(temp);
                    }
                }
            }
        }
        return arrayList;
    }
    public FragmentManager getSupportFragmentManager() {
        return getSupportFragmentManager();
    }
}
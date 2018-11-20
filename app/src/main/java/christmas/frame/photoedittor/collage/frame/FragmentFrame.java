package christmas.frame.photoedittor.collage.frame;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import bo.photo.module.util.SupportUtils;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.FrameAdapter;

public class FragmentFrame extends Fragment {
    private String pathnone;
    private String pathtow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bgr_sticker, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_background);
        ArrayList<String> test = loadFile(getContext(), "frame");
        Collections.reverse(test);
        test.add(0, pathnone);
        test.add(0, pathtow);
        FrameAdapter adapter = new FrameAdapter(test, getContext());
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
                pathnone = file + "/" + files;
            } else if (files.equals("nonee.png")) {
                pathtow = file + "/" + files;
            } else {
                arrayList.add(file + "/" + files);
            }
        }
        return arrayList;
    }
}

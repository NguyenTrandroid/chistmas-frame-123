package christmas.frame.photoedittor.collage.filter;

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
import android.widget.SeekBar;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.BackgroundAdapter;
import christmas.frame.photoedittor.collage.adapter.FilterAdapter;
import christmas.frame.photoedittor.collage.background.FragmentGalleryBackground;

public class FragmentFilter extends Fragment {
    SeekBar seekBar;
    OnValueAlphaFilter onValueAlphaFilter;
    FragmentManager fragmentManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filter, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        RecyclerView recyclerView = view.findViewById(R.id.rv_filter);
        seekBar = view.findViewById(R.id.sb_opacity);
        onValueAlphaFilter = (OnValueAlphaFilter) getActivity();
        ArrayList<String> test = loadFile(getContext(), "filter");
        Collections.reverse(test);
        FilterAdapter adapter = new FilterAdapter(test, getContext());
        recyclerView.setAdapter(adapter);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                onValueAlphaFilter.sendValue((float) progress / 10);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return view;
    }

    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new
                File(SupportUtils.getRootDirPath(context) + "/" + folder + "/");
        String[] list;
        list = file.list();
        for (String files : list) {
            arrayList.add(file + "/" + files);
        }
        return arrayList;
    }
}


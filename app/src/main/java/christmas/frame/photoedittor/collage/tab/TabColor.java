package christmas.frame.photoedittor.collage.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bo.photo.module.color_picker_module.ColorPicker;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;

public class TabColor extends Fragment {
    @BindView(R.id.colorPicker)
    ColorPicker colorPicker;
    @BindView(R.id.bt_ok)
    Button btOk;
    @BindView(R.id.bt_cc)
    Button btCc;
    Unbinder unbinder;
    OnColorSelect onColorSelect;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        onColorSelect = (OnColorSelect) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_addbgr_color, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.bt_ok, R.id.bt_cc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_ok:
                onColorSelect.sendColor(colorPicker.getColor());
                break;
            case R.id.bt_cc:
                onColorSelect.sendColor(0);
                break;
        }
    }
}

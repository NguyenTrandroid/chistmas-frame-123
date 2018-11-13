package christmas.frame.photoedittor.collage.text;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import bo.photo.module.asset_picker_module.AssetPicker;
import bo.photo.module.color_picker_module.ColorPickerDialog;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.FontAdapter;
import christmas.frame.photoedittor.collage.model.TextPicker;


public class FragmentText extends Fragment implements OnFontSelect, ColorPickerDialog.OnColorSelectedListener {
    int color = Color.BLACK;
    OnTextSelete onTextSelete;
    Paint paint;
    String path;
    int textsize = 25;
    boolean bold = false;
    boolean italic = false;
    boolean underline = false;
    Typeface typeface = Typeface.DEFAULT;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_input)
    EditText etInput;
    @BindView(R.id.tv_ok)
    TextView tvOk;
    @BindView(R.id.iv_bold)
    ImageView ivBold;
    @BindView(R.id.iv_italic)
    ImageView ivItalic;
    @BindView(R.id.iv_underline)
    ImageView ivUnderline;
    @BindView(R.id.iv_colorpicker)
    ImageView ivColorpicker;
    @BindView(R.id.ll_format)
    LinearLayout llFormat;
    @BindView(R.id.tv_fontsize)
    TextView tvFontsize;
    @BindView(R.id.sb_sizepicker)
    SeekBar sbSizepicker;
    @BindView(R.id.rl_fontsize)
    RelativeLayout rlFontsize;
    @BindView(R.id.tv_fontname)
    TextView tvFontname;
    @BindView(R.id.rv_font)
    RecyclerView rvFont;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, null);
        this.onTextSelete  = (OnTextSelete) getContext();
        AssetPicker assetPicker = new AssetPicker(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_font);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        FontAdapter fontAdapter = new FontAdapter(assetPicker.listAssetFiles("fonts"), getActivity(), this);
        recyclerView.setAdapter(fontAdapter);
        unbinder = ButterKnife.bind(this, view);
        sbSizepicker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress < 5) {
                    etInput.setTextSize(25 - (5 - progress));
                } else etInput.setTextSize(25 + (progress - 5));
                textsize = (int) etInput.getTextSize();
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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_bold, R.id.iv_italic, R.id.iv_underline, R.id.iv_colorpicker,R.id.tv_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_ok:
                if (etInput.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please input text", Toast.LENGTH_SHORT).show();
                } else {
                    getFragmentManager().popBackStack();

                    TextPicker textPicker = new TextPicker(textsize, typeface
                            , color, etInput.getText().toString(), bold, italic, underline);
                    onTextSelete.sendText(textPicker);
                }
                break;
            case R.id.iv_bold:
                if (bold == false) {
                    if (italic == true) {
                        etInput.setTypeface(typeface, Typeface.BOLD_ITALIC);
                        bold = true;
                    } else {
                        etInput.setTypeface(typeface, Typeface.BOLD);
                        bold = true;
                    }
                } else {
                    if (italic == true) {
                        etInput.setTypeface(typeface, Typeface.ITALIC);
                        bold = false;
                    } else {
                        etInput.setTypeface(typeface, Typeface.NORMAL);
                        bold = false;
                    }
                }
                break;
            case R.id.iv_italic:
                if (italic == false) {
                    if (bold == true) {
                        etInput.setTypeface(typeface, Typeface.BOLD_ITALIC);
                        italic = true;
                    } else {
                        etInput.setTypeface(typeface, Typeface.ITALIC);
                        italic = true;
                    }
                } else {
                    if (bold == true) {
                        etInput.setTypeface(typeface, Typeface.BOLD);
                        italic = false;
                    } else {
                        etInput.setTypeface(typeface, Typeface.NORMAL);
                        italic = false;
                    }
                }

                break;
            case R.id.iv_underline:
                if (underline == false) {
                    etInput.setPaintFlags(etInput.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    underline = true;
                } else {
                    etInput.setPaintFlags(0);
                    underline = false;
                }
                break;
            case R.id.iv_colorpicker:
                paint = new Paint();
                new ColorPickerDialog(getContext(), paint.getColor(), this).show();
                break;
        }
    }

    @Override
    public void sendFont(String path) {
        typeface = Typeface.createFromAsset(getContext().getAssets(), path);
        etInput.setTypeface(typeface);
    }

    @Override
    public void onColorSelected(int color) {
        this.color = color;
        etInput.setTextColor(color);


    }
}
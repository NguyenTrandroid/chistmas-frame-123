package christmas.frame.photoedittor.collage.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import bo.photo.module.image_picker_module.furntion.ImageFileLoader;
import bo.photo.module.image_picker_module.furntion.ImageLoaderListener;
import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.AddImgLibAdapter;
import pt.content.library.ads.AdsHelper;

public class TabLib extends Fragment {
    ArrayList<String> listResource;
    ImageFileLoader imageFileLoader;
    ArrayList<File> excludedImages;
    boolean isCompleted;
    @BindView(R.id.iv_ads)
    ImageView ivAds;
    @BindView(R.id.ll_ads)
    LinearLayout llAds;
    Unbinder unbinder;
    AdsHelper adsHelper = new AdsHelper();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_addbgr_bgr_lib, null);
        RecyclerView recyclerView = view.findViewById(R.id.rv_bgr_lib);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        isCompleted = false;
        listResource = new ArrayList<>();
        excludedImages = new ArrayList<>();
        imageFileLoader = new ImageFileLoader(getContext());
        AddImgLibAdapter addImgLibAdapter = new AddImgLibAdapter(listResource, getActivity());
        recyclerView.setAdapter(addImgLibAdapter);
        imageFileLoader.loadDeviceImages(true, false, excludedImages, new ImageLoaderListener() {
            @Override
            public void onImageLoaded(List<Image> images, List<Folder> folders) {
                for (int i = 0; i < images.size(); i++) {
                    listResource.add(images.get(i).getPath());
                }
                isCompleted = true;

            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        }, true, false, false, false);
        while (isCompleted == false) {
//            adapter.createHashMapCheckSelected(listResource);
            addImgLibAdapter.notifyDataSetChanged();
        }

        unbinder = ButterKnife.bind(this, view);
        adsHelper.loadAds(getContext(), llAds, "banner_artwork", new AdsHelper.AdsCallback() {
            @Override
            public void onLoaded(Context context, String position, String id, String type, int reload) {
                super.onLoaded(context, position, id, type, reload);
            }

            @Override
            public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
                super.onError(context, position, id, type, reload, errorCode);
                llAds.setVisibility(View.GONE);
                ivAds.setVisibility(View.VISIBLE);

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

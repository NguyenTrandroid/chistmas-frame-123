package christmas.frame.photoedittor.collage.tab;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import bo.photo.module.image_picker_module.furntion.ImageFileLoader;
import bo.photo.module.image_picker_module.furntion.ImageLoaderListener;
import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.AddImgLibAdapter;

public class TabLib extends Fragment   {
    ArrayList<String> listResource;
    ImageFileLoader imageFileLoader;
    ArrayList<File> excludedImages;
    boolean isCompleted;

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
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
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


        return view;
    }
}

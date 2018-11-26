package christmas.frame.photoedittor.collage.addphoto;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bo.photo.module.image_picker_module.furntion.ImageFileLoader;
import bo.photo.module.image_picker_module.furntion.ImageLoaderListener;
import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.addphoto.adapter.AddPhotoAdapter;


public class FragmentAddPhoto extends Fragment {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentGalleryPhoto fragmentGalleryPhoto;
    ArrayList<String> listResource;
    ArrayList<String> listPhotoSelected;
    ImageFileLoader imageFileLoader;
    ArrayList<File> excludedImages;
    Bundle bundle;
    boolean isCompleted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentGalleryPhoto = new FragmentGalleryPhoto();
        bundle = new Bundle();
        listPhotoSelected = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_addphoto, null);
        ImageView opengallery = view.findViewById(R.id.iv_opengallery);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        ImageView check = view.findViewById(R.id.iv_check);
        check.setVisibility(View.GONE);
        ImageView close = view.findViewById(R.id.iv_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });

        opengallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
                fragmentGalleryPhoto.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_main, fragmentGalleryPhoto).addToBackStack("galleryphoto");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
            }
        });
        /**
         *
         */
        isCompleted = false;
        listResource = new ArrayList<>();
        excludedImages = new ArrayList<>();
        imageFileLoader = new ImageFileLoader(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_photo);
        Log.d("test", listResource.size() + "size");
        AddPhotoAdapter adapter = new AddPhotoAdapter(listResource, getActivity());
        recyclerView.setAdapter(adapter);
        imageFileLoader.loadDeviceImages(true, false, excludedImages, new ImageLoaderListener() {
            @Override
            public void onImageLoaded(List<Image> images, List<Folder> folders) {
                bundle.putSerializable("listimage", (Serializable) images);
                bundle.putSerializable("listfolder", (Serializable) folders);

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
            adapter.notifyDataSetChanged();
        }
        return view;
    }

    public String getURLForResource(int resourceId) {
        return Uri.parse("android.resource://" + R.class.getPackage().getName() + "/" + resourceId).toString();
    }
}

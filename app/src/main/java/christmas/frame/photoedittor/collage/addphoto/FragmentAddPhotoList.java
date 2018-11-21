package christmas.frame.photoedittor.collage.addphoto;

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
import christmas.frame.photoedittor.collage.addphoto.adapter.GalleryPhotoListAdapter;


public class FragmentAddPhotoList extends Fragment implements OnPhotoSelect {
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FragmentGalleryPhotoList fragmentGalleryPhotoList;
    ArrayList<String> listResource;
    ArrayList<String> listPhotoSelected;
    ImageFileLoader imageFileLoader;
    ArrayList<File> excludedImages;
    OnPhotoListSelect onPhotoListSelect;
    Bundle bundle;
    boolean isCompleted;
    ImageView check;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager = getFragmentManager();
        fragmentGalleryPhotoList = new FragmentGalleryPhotoList();
        bundle = new Bundle();
        listPhotoSelected = new ArrayList<>();
        onPhotoListSelect = (OnPhotoListSelect) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_addphoto, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        ImageView opengallery = view.findViewById(R.id.iv_opengallery);
         check = view.findViewById(R.id.iv_check);
         check.setVisibility(View.GONE);
        ImageView close = view.findViewById(R.id.iv_close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
            }
        });
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPhotoListSelect.sendPhotolist(listPhotoSelected,false);
            }
        });
        opengallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager.popBackStack();
                fragmentGalleryPhotoList.setArguments(bundle);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_main, fragmentGalleryPhotoList).addToBackStack("galleryphoto");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
            }
        });
        /**
         *
         */
        isCompleted=false;
        listResource= new ArrayList<>();
        excludedImages = new ArrayList<>();
        imageFileLoader = new ImageFileLoader(getContext());
        RecyclerView recyclerView = view.findViewById(R.id.rv_photo);
        Log.d("test",listResource.size()+"size");
        GalleryPhotoListAdapter adapter = new GalleryPhotoListAdapter(listResource,getContext(),R.layout.item_photo,this,3);
        recyclerView.setAdapter(adapter);
        imageFileLoader.loadDeviceImages(true, false, excludedImages, new ImageLoaderListener() {
            @Override
            public void onImageLoaded(List<Image> images, List<Folder> folders) {
                bundle.putSerializable("listimage", (Serializable) images);
                bundle.putSerializable("listfolder", (Serializable) folders);
                for (int i=0;i<images.size();i++){
                    listResource.add(images.get(i).getPath());
                }
                isCompleted=true;

            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        },true,false,false,false);
        while (isCompleted==false){
            adapter.createHashMapCheckSelected(listResource);
            adapter.notifyDataSetChanged();
        }
        return view;
    }



    @Override
    public void sendPhoto(String path, boolean closeFragment) {
        check.setVisibility(View.VISIBLE);
        boolean isSelected = false;
        int position=-1;
        if (listPhotoSelected.isEmpty()) {
            listPhotoSelected.add(path);
        } else {
            for (int i = 0; i < listPhotoSelected.size(); i++) {
                if (listPhotoSelected.get(i).equals(path)) {
                    isSelected = true;
                    position=i;
                }
            }
            if(isSelected==true){
                listPhotoSelected.remove(position);
                if(listPhotoSelected.isEmpty()){
                    check.setVisibility(View.GONE);
                }
            }else listPhotoSelected.add(path);
        }

        Log.d("testAddPhoto",listPhotoSelected.size()+"");
        for (int i = 0; i <listPhotoSelected.size() ; i++) {
            Log.d("testAddPhoto",listPhotoSelected.get(i) );
        }
    }
}

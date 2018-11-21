package christmas.frame.photoedittor.collage.addphoto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.addphoto.adapter.GalleryPhotoListAdapter;
import pt.content.library.ads.AdsHelper;

import static android.util.Log.d;


public class FragmentGalleryPhotoList extends Fragment implements OnPhotoSelect {
    ArrayList<Image> images;
    ArrayList<Folder> folders;
    ArrayList<String> listResource;
    ArrayList<String> listPhotoSelected;
    FragmentGallerySelect fragmentGallerySelect;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    GalleryPhotoListAdapter adapter;
    RecyclerView recyclerView;
    Bundle bundle;
    OnPhotoListSelect onPhotoListSelect;
    @BindView(R.id.rl_gallery)
    RelativeLayout rlGallery;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.rl_back)
    RelativeLayout ivback;
    Unbinder unbinder;

    @BindView(R.id.iv_ads)
    ImageView ivAds;
    @BindView(R.id.ll_ads)
    LinearLayout ll_ads;
    public AdsHelper adsHelper = new AdsHelper();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listResource = new ArrayList<>();
        listPhotoSelected = new ArrayList<>();
        fragmentGallerySelect = new FragmentGallerySelect();
        fragmentManager = getFragmentManager();
        onPhotoListSelect = (OnPhotoListSelect) getActivity();
        bundle = getArguments();
        images = (ArrayList<Image>) bundle.getSerializable("listimage");
        folders = (ArrayList<Folder>) bundle.getSerializable("listfolder");
        for (int i = 0; i < images.size(); i++) {
            listResource.add(images.get(i).getPath());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galleryphoto, null);

        /////////////////////
        recyclerView = view.findViewById(R.id.rv_galleryphoto);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        adapter = new GalleryPhotoListAdapter(listResource, getContext(), R.layout.item_galleryphoto, this, 9);
        recyclerView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);

//        add ADS banner
        adsHelper.loadAds(getContext(), ll_ads, "banner_artwork", new AdsHelper.AdsCallback() {
            @Override
            public void onError(Context context, String position, String id, String type, int reload, int errorCode) {
                super.onError(context, position, id, type, reload, errorCode);
                ll_ads.setVisibility(View.GONE);
                ivAds.setVisibility(View.VISIBLE);
                d("ICT_FragmentMenuOption", "onError: FragmentGellaryPhoto");
            }

            @Override
            public void onLoaded(Context context, String position, String id, String type, int reload) {
                super.onLoaded(context, position, id, type, reload);
                d("ICT_FragmentMenuOption", "onLoaded: FragmentGellaryPhoto");
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (int i = 0; i < folders.get(data.getIntExtra("position", 0)).getImages().size(); i++) {
            if(listResource.contains(folders.get(data.getIntExtra("position",0)).getImages().get(i).getPath())){
                listResource.remove(folders.get(data.getIntExtra("position",0)).getImages().get(i).getPath());
                listResource.add(0,folders.get(data.getIntExtra("position",0)).getImages().get(i).getPath());
            }else {
                listResource.add(0,folders.get(data.getIntExtra("position",0)).getImages().get(i).getPath());
            }
        }
//

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_gallery, R.id.tv_done,R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gallery:
                listPhotoSelected.clear();
                fragmentGallerySelect.setArguments(bundle);
                fragmentGallerySelect.setTargetFragment(FragmentGalleryPhotoList.this, 1);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_main, fragmentGallerySelect, "galleryphoto").addToBackStack("galleryselect");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
                break;
            case R.id.tv_done:
                onPhotoListSelect.sendPhotolist(listPhotoSelected,false);
                break;
            case R.id.rl_back:
                getActivity().onBackPressed();
                break;
        }
    }


    @Override
    public void sendPhoto(String path, boolean closeFragment) {
        tvDone.setVisibility(View.VISIBLE);
        boolean isSelected = false;
        int position = -1;
        if (listPhotoSelected.isEmpty()) {
            listPhotoSelected.add(path);
        } else {
            for (int i = 0; i < listPhotoSelected.size(); i++) {
                if (listPhotoSelected.get(i).equals(path)) {
                    isSelected = true;
                    position = i;
                }
            }
            if (isSelected == true) {
                listPhotoSelected.remove(position);
                if(listPhotoSelected.isEmpty()){
                    tvDone.setVisibility(View.GONE);
                }
            } else listPhotoSelected.add(path);
        }
        Log.d("testGalleryPhoto", listPhotoSelected.size() + "");
    }


    //        @Override
//    public void onStickerSend(int data) {
//        fragmentManager.popBackStack();
//
//    }
}
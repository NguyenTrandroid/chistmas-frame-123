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
import christmas.frame.photoedittor.collage.addphoto.adapter.GalleryPhotoAdapter;
import pt.content.library.ads.AdsHelper;

import static android.util.Log.d;


public class FragmentGalleryPhoto extends Fragment {
    ArrayList<Image> images;
    ArrayList<Folder> folders;
    ArrayList<String> listResource;
    ArrayList<String> listPhotoSelected;
    FragmentGallerySelect fragmentGallerySelect;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    GalleryPhotoAdapter adapter;
    RecyclerView recyclerView;
    Bundle bundle;
    OnPhotoSelectt onPhotoSelect;
    @BindView(R.id.rl_gallery)
    RelativeLayout rlGallery;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.rl_back)
    RelativeLayout rlback;
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
        adapter = new GalleryPhotoAdapter(listResource, getActivity(), getContext());
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
            if (listResource.contains(folders.get(data.getIntExtra("position", 0)).getImages().get(i).getPath())) {
                listResource.remove(folders.get(data.getIntExtra("position", 0)).getImages().get(i).getPath());
                Log.d("testliss", "ele");

                listResource.add(0, folders.get(data.getIntExtra("position", 0)).getImages().get(i).getPath());
            } else {
                Log.d("testliss", "ele");
                listResource.add(0, folders.get(data.getIntExtra("position", 0)).getImages().get(i).getPath());
            }
        }

        Log.d("testliss", listResource.size() + "");


    }


    @Override
    public void onDestroyView() {
        adsHelper.onDestroy();
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_gallery, R.id.tv_done, R.id.rl_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_gallery:
                listPhotoSelected.clear();
                fragmentGallerySelect.setArguments(bundle);
                fragmentGallerySelect.setTargetFragment(FragmentGalleryPhoto.this, 1);
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.rl_main, fragmentGallerySelect, "galleryphoto").addToBackStack("galleryselect");
                try {
                    fragmentTransaction.commit();
                } catch (IllegalStateException ignored) {

                }
                break;
            case R.id.tv_done:
                break;

            case R.id.rl_back:
                getActivity().onBackPressed();
                break;
        }
    }

}
package christmas.frame.photoedittor.collage.addphoto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import christmas.frame.photoedittor.collage.adapter.AddPhotoAdapter;
import christmas.frame.photoedittor.collage.adapter.GalleryPhotoAdapter2;
import christmas.frame.photoedittor.collage.addphoto.adapter.GalleryPhotoAdapter;


public class FragmentGalleryPhoto2 extends Fragment {
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
    AddPhotoAdapter.OnPhotoSelecte photoSelect;
    @BindView(R.id.rl_gallery)
    RelativeLayout rlGallery;
    @BindView(R.id.tv_done)
    TextView tvDone;
    @BindView(R.id.rl_back)
    RelativeLayout rlback;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listResource = new ArrayList<>();
        listPhotoSelected = new ArrayList<>();
        fragmentGallerySelect = new FragmentGallerySelect();
        fragmentManager = getFragmentManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galleryphoto, null);
        bundle = getArguments();
        images = (ArrayList<Image>) bundle.getSerializable("listimage");
        folders = (ArrayList<Folder>) bundle.getSerializable("listfolder");
        for (int i = 0; i < images.size(); i++) {
            listResource.add(images.get(i).getPath());
        }
        /////////////////////
        recyclerView = view.findViewById(R.id.rv_galleryphoto);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setHasFixedSize(true);
        GalleryPhotoAdapter2 adapter2 = new GalleryPhotoAdapter2(listResource,getActivity(),getContext());
//        adapter = new GalleryPhotoAdapter(listResource, getContext(), R.layout.item_galleryphoto, this, 9);
        recyclerView.setAdapter(adapter2);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        listResource.clear();
        for (int i = 0; i < folders.get(data.getIntExtra("position", 0)).getImages().size(); i++) {
            listResource.add(folders.get(data.getIntExtra("position", 0)).getImages().get(i).getPath());
        }

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
                fragmentGallerySelect.setTargetFragment(FragmentGalleryPhoto2.this, 1);
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

//        @Override
//    public void onStickerSend(int data) {
//        fragmentManager.popBackStack();
//
//    }
}
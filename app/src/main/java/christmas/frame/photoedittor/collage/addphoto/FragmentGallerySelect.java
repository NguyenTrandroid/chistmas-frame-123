package christmas.frame.photoedittor.collage.addphoto;

import android.app.Activity;
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

import java.util.ArrayList;

import bo.photo.module.image_picker_module.model.Folder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.addphoto.adapter.GalleryFolderSelectAdapter;

public class FragmentGallerySelect extends Fragment implements GalleryFolderSelectAdapter.OnDataPass {
    ArrayList<Folder> folders;
    ArrayList<String> foldernames;
    ArrayList<String> firstimages;
    FragmentGalleryPhotoList fragmentGalleryPhotoList;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Bundle bundle;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentGalleryPhotoList = new FragmentGalleryPhotoList();
        fragmentManager = getFragmentManager();
        foldernames = new ArrayList<>();
        firstimages = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_galleryselect, null);
        bundle = getArguments();
        folders = (ArrayList<Folder>) bundle.getSerializable("listfolder");
        for (int i = 0; i < folders.size(); i++) {
            foldernames.add(folders.get(i).getFolderName());
            firstimages.add(folders.get(i).getImages().get(0).getPath());
        }
        /////////////////////
        RecyclerView recyclerView = view.findViewById(R.id.rv_galleryselect);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        recyclerView.setHasFixedSize(true);
        GalleryFolderSelectAdapter adapter = new GalleryFolderSelectAdapter(foldernames, getContext(), firstimages, this);
        recyclerView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDataPass(int data) {
        Log.d("teststring", data + "");
        getFragmentManager().popBackStack();
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                new Intent().putExtra("position", data)
        );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_close)
    public void onViewClicked() {
        Log.d("testclick","oke" );
        fragmentManager.popBackStack();
    }
}
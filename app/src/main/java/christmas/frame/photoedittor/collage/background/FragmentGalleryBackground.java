package christmas.frame.photoedittor.collage.background;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bo.photo.module.util.SupportUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.App;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.background.adapter.GalleryBackgroundAdapter;
import christmas.frame.photoedittor.collage.api.APIClient;
import christmas.frame.photoedittor.collage.api.APIService;
import christmas.frame.photoedittor.collage.prefs.Const;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentGalleryBackground extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> listPath;
    GalleryBackgroundAdapter.OnDownload onDownload;
    GalleryBackgroundAdapter adapter;
    String rootDirPath;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gallerybackground, null);
        rootDirPath = SupportUtils.getRootDirPath(getContext()) + "/background/";
        recyclerView = view.findViewById(R.id.rv_gallerybackground);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        onDownload = new GalleryBackgroundAdapter.OnDownload() {
            @Override
            public void onDownloadCompleted() {
                Toast.makeText(getContext(), "Download Completed", Toast.LENGTH_SHORT).show();
                listPath = loadFile(getContext(), "background");
                Collections.reverse(listPath);
                loadImageData(Const.RE_BACKGROUND, "background");
                adapter = new GalleryBackgroundAdapter(listPath, getContext(), onDownload, "background", "background", R.layout.item_galleryframe2);
                recyclerView.swapAdapter(adapter, true);
            }
        };
        listPath = new ArrayList<>();
        listPath = loadFile(getContext(), "background");
        Collections.reverse(listPath);
        loadImageData(Const.RE_BACKGROUND, "background");
        adapter = new GalleryBackgroundAdapter(listPath, getContext(), onDownload, "background", "background", R.layout.item_galleryframe2);
        recyclerView.setAdapter(adapter);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new
                File(SupportUtils.getRootDirPath(context) + "/" + folder + "/");
        String[] list;
        list = file.list();
        for (String files : list) {
            Log.d("testfile2", files);
            if (!files.equals("ic_none.png")) {
                arrayList.add(file + "/" + files);
            }
        }
        return arrayList;

    }

    public void loadImageData(final String type, final String dir) {
        final APIService service = APIClient.getClient();
        if (type.equals(Const.RE_STICKER)) {
            service.getImageData(App.getApp().getPackageName(), "sticker/" + dir)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                            Log.i("Api:", String.valueOf(d) + "subscribe2");
                        }

                        @Override
                        public void onNext(List<String> response) {
//                            urlFromApi.addAll(response);
//                             mToHostView.loadImageSuccess(response,dir,type);
/// url sticker String url = "http://45.32.99.2/image/sticker/" + dir + "/frame/" + imageName.get(i);
                            Log.i("Api:", String.valueOf(response) + "next2");

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.i("Api:", e.toString() + "eorro2");
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            service.getImageData(App.getApp().getPackageName(), dir)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                            Log.i("Api:", String.valueOf(d));
                        }

                        @Override
                        public void onNext(List<String> response) {
                            for (int i = 0; i < response.size(); i++) {
                                if (!listPath.contains(rootDirPath + response.get(i))) {
                                    Log.d("testre", response.get(i));
                                    Log.d("testre", rootDirPath + response.get(i));
                                    listPath.add(response.get(i));
                                }
                            }
                            // mToHostView.loadImageSuccess(response,dir,type);
//  url bt: String url = "http://45.32.99.2/image/" + dir + "/frame/" + imageName.get(i);
                            Log.i("Api:", String.valueOf(response));

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.i("Api:", e.toString());
                        }

                        @Override
                        public void onComplete() {
                            GalleryBackgroundAdapter adapter = new GalleryBackgroundAdapter(listPath, getContext(), onDownload, "background", "background", R.layout.item_galleryframe2);
                            recyclerView.swapAdapter(adapter, true);
                            // Updates UI with data
                            Log.i("Api:", "Thanh cong");

                        }
                    });
        }


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        getActivity().onBackPressed();
    }
}
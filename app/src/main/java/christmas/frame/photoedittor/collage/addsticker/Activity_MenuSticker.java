package christmas.frame.photoedittor.collage.addsticker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import bo.photo.module.util.SupportUtils;
import christmas.frame.photoedittor.collage.App;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.adapter.StickerDownloadAdapter;
import christmas.frame.photoedittor.collage.adapter.StickerMenuAdapter;
import christmas.frame.photoedittor.collage.api.APIClient;
import christmas.frame.photoedittor.collage.api.APIService;
import christmas.frame.photoedittor.collage.model.PickerImageMenu;
import christmas.frame.photoedittor.collage.prefs.Const;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class Activity_MenuSticker extends AppCompatActivity implements StickerMenuAdapter.OnDataPass {
    private Toolbar mToolbar;
    ActionBarDrawerToggle mDrawerToggle;
    DrawerLayout mDrawerLayout;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    List<PickerImageMenu> listResource;
    ArrayList<String> listStickerAsset;
    String dir = "animal";
    StickerMenuAdapter adapter;
    StickerDownloadAdapter adapterRecycleSticker;
    String rootDirPath;
    StickerDownloadAdapter.OnDownload onDownload;
    StickerDownloadAdapter.OnDataPass onDataPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menusticker);
        mToolbar = (Toolbar)findViewById(R.id.appBar);
        loadMenuData();
        rootDirPath= SupportUtils.getRootDirPath(this)+"/sticker/";
        /**
         * recycleview menusticker
         */
        recyclerView = findViewById(R.id.drawerRecyclerView);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        listResource = new ArrayList<>();
        listStickerAsset = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new StickerMenuAdapter(listResource,this);
        recyclerView.setAdapter(adapter);
        /**
         * recycleview Sticker
         */
        onDataPass = new StickerDownloadAdapter.OnDataPass() {
            @Override
            public void onStickerSelected(String url) {
                Intent intent = new Intent();
                intent.putExtra("stickerpath", url);
                setResult(1, intent);
                finish();
            }
        };
        onDownload= new StickerDownloadAdapter.OnDownload() {
            @Override
            public void onDownloadCompleted(int position) {
                listStickerAsset=loadFile(getApplicationContext(),"sticker/"+dir);

                Collections.reverse(listStickerAsset);
                loadImageData(Const.RE_STICKER, dir);
                adapterRecycleSticker = new StickerDownloadAdapter(listStickerAsset,dir,getApplicationContext(),onDownload,onDataPass);
                recyclerView2.swapAdapter(adapterRecycleSticker,true);
            }
        };
        recyclerView2 =findViewById(R.id.rv_sticker);
        recyclerView2.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView2.setHasFixedSize(true);
        listStickerAsset=loadFile(this,"sticker/"+dir);
        Collections.reverse(listStickerAsset);
        adapterRecycleSticker = new StickerDownloadAdapter(listStickerAsset, dir, this,onDownload,onDataPass);
        recyclerView2.setAdapter(adapterRecycleSticker);
        loadImageData(Const.RE_STICKER, dir);

        /**
         * Load Sticker tu may
         */

        /**
         * Load Menu Data và Path Data
         */

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,  R.string.open_drawer, R.string.close_drawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //TODO Add some action here
                //Executed when drawer closes

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //TODO Add some action here
                //executes when drawer open
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public ArrayList<String> loadFile(Context context, String folder) {
        ArrayList<String> arrayList = new ArrayList<>();
        File file = new
                File(SupportUtils.getRootDirPath(context) + "/"+folder+"/");
        String[] list;
        list = file.list();
        if(list != null){
            for (String files : list) {
                if(!files.contains("ny")){
                    arrayList.add(file+"/"+files);
                }
            }
        }
        return arrayList;

    }


    public void swap(){
//        listStickerAsset = loadFile(this,"sticker/"+dir);
        adapterRecycleSticker = new StickerDownloadAdapter(listStickerAsset,dir,this,onDownload,onDataPass);
        recyclerView2.swapAdapter(adapterRecycleSticker,true);
    }


    public void loadMenuData(){
        final APIService service = APIClient.getClient();
        service.getMenuData(App.getApp().getPackageName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<PickerImageMenu>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.i("Api:", String.valueOf(d)+"subscribe");
                    }

                    @Override
                    public void onNext(List<PickerImageMenu> response) {
                        for(int i=0;i<response.size();i++){
                            File f = new File(rootDirPath,response.get(i).getDir());
                            if (!f.exists()) {
                                f.mkdirs();
                            }
                        }
                        listResource.addAll(response);

                        //   mToHostView.loadMenuSuccess(response);
                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.i("Api:", e.toString()+"eror");
                    }

                    @Override
                    public void onComplete() {
                        adapter.notifyDataSetChanged();
                        listStickerAsset=loadFile(getApplicationContext(),"sticker/"+dir);

                        // Updates UI with data
                        Log.i("Api:", "Thanh cong");

                    }
                });
    }

    public void loadImageData(final String type, final String dir) {
        /**
         *
         */
        final APIService service = APIClient.getClient();
        if (type.equals(Const.RE_STICKER)) {
            service.getImageData(App.getApp().getPackageName(), "sticker/" + dir)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<String>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                            Log.i("Api:", String.valueOf(d)+"subscribe2");
                        }

                        @Override
                        public void onNext(List<String> response) {
                            for (int i = 0; i <response.size() ; i++) {
                                if(!listStickerAsset.contains(rootDirPath+dir+"/"+response.get(i))){
                                    Log.d("testres",response.get(i));
                                    listStickerAsset.add(response.get(i));
                                }
                            }

//                            urlFromApi.addAll(response);
//                             mToHostView.loadImageSuccess(response,dir,type);
/// url sticker String url = "http://45.32.99.2/image/sticker/" + dir + "/frame/" + imageName.get(i);
                            Log.i("Api:", String.valueOf(response)+"next2");

                        }

                        @Override
                        public void onError(Throwable e) {

                            Log.i("Api:", e.toString()+"eorro2");
                        }

                        @Override
                        public void onComplete() {
                            Log.d("testres","finie" );
                            swap();
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
                            // Updates UI with data
                            Log.i("Api:", "Thanh cong");

                        }
                    });
        }


    }
    @Override
    public void onPassDir(String dir) {
        this.dir=dir;
        mDrawerLayout.closeDrawers();
        listStickerAsset = loadFile(this,"sticker/"+dir);
        loadImageData(Const.RE_STICKER,dir);
        adapterRecycleSticker = new StickerDownloadAdapter(listStickerAsset,dir,this,onDownload,onDataPass);
        recyclerView2.swapAdapter(adapterRecycleSticker,true);
    }
}

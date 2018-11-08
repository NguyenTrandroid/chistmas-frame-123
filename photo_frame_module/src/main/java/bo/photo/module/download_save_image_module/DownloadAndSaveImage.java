package bo.photo.module.download_save_image_module;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;



public class DownloadAndSaveImage {
    private String nameTask;
    private Context context;
    private OnPathImagePass onPathImagePass;
    private ImageLoader imageLoader;
    private ExecutorService executorService;
    private int count;

    public DownloadAndSaveImage(ArrayList<String> urilist,Context context,String nameTask) {
        this.onPathImagePass= (OnPathImagePass) context;
        this.nameTask=nameTask;
        this.context = context;
        count = urilist.size();
        this.imageLoader=getImageLoader();
        getExecutorService().execute(new DownloadImageRunnable(urilist));
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }
    public interface OnPathImagePass{
        void onDownloadSaveCompleted(String path,String Url,String nameTask);
        void onDownloadListCompleted(String nameTask);
    }
    public ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }
    public ImageLoader getImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        return imageLoader;
    }
    private class DownloadImageRunnable implements Runnable {

        private ArrayList<String> urilist;

        public DownloadImageRunnable(ArrayList<String> urilist) {
            this.urilist = urilist;
        }

        @Override
        public void run() {
            for (int i = 0; i <urilist.size() ; i++) {
                imageLoader.loadImage(urilist.get(i), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        count--;
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        count--;
                        onPathImagePass.onDownloadSaveCompleted(saveImage(loadedImage, System.currentTimeMillis() + "image"), imageUri, nameTask);
                        if(count==0){
                            onPathImagePass.onDownloadListCompleted(nameTask);
                        }
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        count--;
                    }
                });
            }
            }
        }
        private String saveImage(Bitmap finalBitmap, String image_name) {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = "/BirthDayFrame" + image_name+ ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            scanMedia(root+fname);
            return root+fname;
        }

}

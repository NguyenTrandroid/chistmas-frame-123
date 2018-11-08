package bo.photo.module.image_picker_module.furntion;

import android.content.Context;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;


public class ImageFileLoader {

    private Context context;
    private ExecutorService executorService;

    public ImageFileLoader(Context context) {
        this.context = context;
    }

    private final String[] projection = new String[]{
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME
    };

    public void loadDeviceImages(final boolean isFolderMode, final boolean includeVideo, final ArrayList<File> excludedImages, final ImageLoaderListener listener,boolean includePhoto,boolean includeSticker,boolean includeFrame,boolean includeFillter) {
        getExecutorService().execute(new ImageLoadRunnable(isFolderMode, includeVideo, excludedImages, listener,includePhoto,includeSticker,includeFrame,includeFillter));

    }
    public void loadDeviceSticker(final ArrayList<File> excludedImages, final ImageLoaderListener listener,String dir) {
        getExecutorService().execute(new ImageLoadRunnable(excludedImages, listener,dir));

    }
    public void loadDeviceFrame(final ArrayList<File> excludedImages, final ImageLoaderListener listener,String dirframe) {
        getExecutorService().execute(new ImageLoadRunnable(excludedImages, listener,dirframe));

    }


    public void abortLoadImages() {
        if (executorService != null) {
            executorService.shutdown();
            executorService = null;
        }
    }

    private ExecutorService getExecutorService() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

    private class ImageLoadRunnable implements Runnable {

        private boolean isFolderMode;
        private boolean includeVideo;
        private boolean includeSticker;
        private boolean includeFrame;
        private boolean includeFiller;
        private boolean includePhoto;
        private String dir;
        private ArrayList<File> exlucedImages;
        private ImageLoaderListener listener;

        public ImageLoadRunnable(boolean isFolderMode, boolean includeVideo, ArrayList<File> excludedImages,ImageLoaderListener listener,boolean includePhoto,boolean includeSticker,boolean includeFrame,boolean includeFiller) {
            this.isFolderMode = isFolderMode;
            this.includeVideo = includeVideo;
            this.exlucedImages = excludedImages;
            this.listener = listener;
            this.includeSticker=includeSticker;
            this.includeFrame=includeFrame;
            this.includeFiller=includeFiller;
            this.includePhoto=includePhoto;
        }
        public ImageLoadRunnable(ArrayList<File> excludedImages,ImageLoaderListener listener,String dir) {
            this.dir =dir;
            this.exlucedImages = excludedImages;
            this.listener = listener;
        }


        @Override
        public void run() {
            Cursor cursor;
            if (includeVideo) {
                String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE + " OR "
                        + MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
                cursor = context.getContentResolver().query(MediaStore.Files.getContentUri("external"), projection,
                        selection, null, MediaStore.Images.Media.DATE_ADDED);
            } else {
                cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                        null, null, MediaStore.Images.Media.DATE_ADDED);
            }

            if (cursor == null) {
                listener.onFailed(new NullPointerException());
                return;
            }

            List<Image> temp = new ArrayList<>();
            Map<String, Folder> folderMap = null;
            if (isFolderMode) {
                folderMap = new HashMap<>();
            }

            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(projection[1]));
                    String path = cursor.getString(cursor.getColumnIndex(projection[2]));
                    String bucket = cursor.getString(cursor.getColumnIndex(projection[3]));

                    File file = makeSafeFile(path);
                    if (file != null) {
                        if (exlucedImages != null && exlucedImages.contains(file))
                            continue;

                        Image image = new Image(id, name, path);
                        if(includePhoto==false){
                            if(!dir.isEmpty()){
                                if(!dir.equals("frame")){
                                    String root = Environment.getExternalStorageDirectory().toString()+"/sticker/"+dir;
                                    if(image.getPath().contains(root)){
                                        temp.add(image);
                                    }
                                }else if(dir.equals("frame")){
                                    String root = Environment.getExternalStorageDirectory().toString()+"/frame/";
                                    if(image.getPath().contains(root)){
                                        temp.add(image);
                                    }
                                }

                            }

                        }else {
                            temp.add(image);
                        }



                        if (folderMap != null) {
                            Folder folder = folderMap.get(bucket);
                            if (folder == null) {
                                folder = new Folder(bucket);
                                folderMap.put(bucket, folder);
                            }
                            folder.getImages().add(image);
                        }
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();

            /* Convert HashMap to ArrayList if not null */
            List<Folder> folders = null;
            if (folderMap != null) {
                folders = new ArrayList<>(folderMap.values());
            }
            listener.onImageLoaded(temp, folders);
        }
    }

    @Nullable
    private static File makeSafeFile(String path) {
        if (path == null || path.isEmpty()) {
            return null;
        }
        try {
            return new File(path);
        } catch (Exception ignored) {
            return null;
        }
    }

}

package christmas.frame.photoedittor.collage.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import christmas.frame.photoedittor.collage.App;
import christmas.frame.photoedittor.collage.R;

public class SaveImg {
    OnSave onSave;

    public SaveImg(OnSave onSave) {
        this.onSave = onSave;
    }

    public void saveImageToInternalStorage(Bitmap bitmapImage, String fileName) {
        try {
            ArrayList<String> a = saveToInternalStorage(bitmapImage, fileName);
            onSave.onSaveCompleted(a.get(0), a.get(1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> saveToInternalStorage(Bitmap bitmapImage, String fileName) throws IOException {
        ArrayList<String> a = new ArrayList<>();
        File storageDir = new File(Environment.getExternalStorageDirectory().toString().concat("/").concat(App.getAppContext().getResources().getString(R.string.app_name)));
        if (!storageDir.exists())
            storageDir.mkdirs();
        // Create imageDir
        File mypath = new File(storageDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            scanMedia(mypath.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        a.add(mypath.getAbsolutePath());
        a.add("null");
        return a;
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        App.getAppContext().sendBroadcast(scanFileIntent);
    }

    public interface OnSave {
        void onSaveCompleted(String path, String share);
    }
}

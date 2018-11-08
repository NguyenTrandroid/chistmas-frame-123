//package bo.photo.module.util;
//
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.InputStream;
//
//import birthdayframe.photoframe.photoeditor.App;
//import birthdayframe.photoframe.photoeditor.R;
//
///**
// * Created by admin on 9/12/18.
// */
//
//public class Support {
//    public static Drawable getDrawableFromUri(Uri uriImage) {
//        Drawable drawable;
//        try {
//            InputStream inputStream = App.getAppContext().getContentResolver().openInputStream(uriImage);
//            drawable = Drawable.createFromStream(inputStream, uriImage.toString());
//        } catch (FileNotFoundException e) {
//            drawable = App.getAppContext().getResources().getDrawable(R.drawable.demo1);
//        }
//        return drawable;
//    }
//
//    public static Bitmap decodeFile(File f) {
//        try {
//            // gỉai mã kích thước của tấm ảnh
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
//
//            // Kích thước mới của tấm ảnh
//            final int REQUIRED_SIZE=400;
//
//            // Tìm gía trị scale. Gía trị này cần phải là cơ số của 2.
//            int scale = 2;
//            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
//                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
//                scale *= 2;
//            }
//
//
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize = scale;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
//        } catch (FileNotFoundException e) {}
//        return null;
//    }
//}

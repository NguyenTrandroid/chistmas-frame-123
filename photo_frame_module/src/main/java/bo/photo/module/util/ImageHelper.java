package bo.photo.module.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageHelper {
    private Context context;

    public ImageHelper(Context context) {
        this.context = context;
    }

    public Drawable resize(Drawable image) {
        Bitmap b = ((BitmapDrawable)image).getBitmap();
        int width = b.getWidth();
        int height = b.getHeight();
        int newHeight = (height *500)/width;
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 500, newHeight, false);
        return new BitmapDrawable(context.getResources(), bitmapResized);
    }
}

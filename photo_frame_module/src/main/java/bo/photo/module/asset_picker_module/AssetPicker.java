package bo.photo.module.asset_picker_module;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

public class AssetPicker {
    private Context context;

    public AssetPicker(Context context) {
        this.context = context;
    }

    public Map<String, Typeface> getSystemTypeface() {
        Map<String, Typeface> systemTypeface = null;
        try {
            //Typeface typeface = Typeface.class.newInstance();
            Typeface typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL);
            Field f = Typeface.class.getDeclaredField("systemTypeface");
            f.setAccessible(true);
            systemTypeface = (Map<String, Typeface>) f.get(typeface);
            for (Map.Entry<String, Typeface> entry : systemTypeface.entrySet()) {
                Log.d("FontMap", entry.getKey() + " ---> " + entry.getValue() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return systemTypeface;
    }

    public ArrayList<String> listAssetFiles(String path) {

        String[] list;
        ArrayList<String> listtypeface = new ArrayList<>();
        try {
            list = context.getAssets().list(path);
            for (String file : list) {
                listtypeface.add(path + "/" + file);
            }
        } catch (IOException e) {
        }
        return listtypeface;
    }
}

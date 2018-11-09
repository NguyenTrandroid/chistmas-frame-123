package christmas.frame.photoedittor.collage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admin on 8/29/18.
 */

public class PickerImageMenu {


        @SerializedName("dir")
        @Expose
        private String dir;
        @SerializedName("name")
        @Expose
        private String name;

        private String url;


    public String getDir() {


        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        String path = "http://45.32.99.2/image/sticker/"+this.dir+"/avatar.png";

        return path;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

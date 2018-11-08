package bo.photo.module.image_picker_module.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by boss1088 on 8/22/16.
 */
public class Folder implements Parcelable {

    private String folderName;
    private ArrayList<Image> images;

    public Folder(String bucket) {
        folderName = bucket;
        images = new ArrayList<>();
    }

    protected Folder(Parcel in) {
        folderName = in.readString();
        images = in.createTypedArrayList(Image.CREATOR);
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel in) {
            return new Folder(in);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(folderName);
        parcel.writeTypedList(images);
    }
}

package bo.photo.module.image_picker_module.furntion;


import java.util.List;

import bo.photo.module.image_picker_module.model.Folder;
import bo.photo.module.image_picker_module.model.Image;

public interface ImageLoaderListener {
    void onImageLoaded(List<Image> images, List<Folder> folders);
    void onFailed(Throwable throwable);

}

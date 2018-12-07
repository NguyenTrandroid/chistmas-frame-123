package christmas.frame.photoedittor.collage.api;

import java.util.List;

import christmas.frame.photoedittor.collage.model.PickerImageMenu;
import io.reactivex.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by admin on 8/24/18.
 */

public interface APIService {
//    @POST("/image/api.php")
//    Observable<ImageData> getImageData(@Query("p") String packageName);

    @POST("/christmas_frame/api.php")
    Observable<List<String>> getImageData(@Query("p") String packageName, @Query("dir") String dir);

    @POST("/image/sticker/list.php")
    Observable<List<PickerImageMenu>> getMenuData(@Query("p") String packageName);




}

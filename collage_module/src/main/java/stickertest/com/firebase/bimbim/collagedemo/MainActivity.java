//package stickertest.com.firebase.bimbim.collagedemo;
//
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.graphics.drawable.BitmapDrawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.RelativeLayout;
//
//import com.xiaopo.flying.sticker.Sticker;
//import com.xiaopo.flying.sticker.StickerView;
//import com.xiaopo.flying.sticker.TextSticker;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import stickertest.com.firebase.bimbim.collagedemo.frame.FrameImageView;
//import stickertest.com.firebase.bimbim.collagedemo.frame.FramePhotoLayout;
//import stickertest.com.firebase.bimbim.collagedemo.model.TemplateItem;
//import stickertest.com.firebase.bimbim.collagedemo.template.PhotoLayout;
//import stickertest.com.firebase.bimbim.collagedemo.utils.ImageUtils;
//import stickertest.com.firebase.bimbim.collagedemo.utils.PhotoUtils;
//import stickertest.com.firebase.bimbim.collagedemo.utils.TemplateImageUtils;
//import stickertest.com.firebase.bimbim.collagedemo.utils.frame.FrameImageUtils;
//
//public class MainActivity extends AppCompatActivity implements FramePhotoLayout.OnQuickActionClickListener {
//    private static final int REQUEST_SELECT_PHOTO = 99;
//    private final float MAX_SPACE = ImageUtils.pxFromDp(App.getAppContext(), 30);
//    private final float MAX_CORNER = ImageUtils.pxFromDp(App.getAppContext(), 60);
//    private final float DEFAULT_SPACE = ImageUtils.pxFromDp(App.getAppContext(), 2);
//    private static final float MAX_SPACE_PROGRESS = 300.0f;
//    protected static final int RATIO_SQUARE = 0;
//    private static final float MAX_CORNER_PROGRESS = 200.0f;
//    protected static final int RATIO_GOLDEN = 2;
//
//    protected float mOutputScale = 1;
//
//    @BindView(R.id.collage_area)
//    RelativeLayout collageArea;
//    @BindView(R.id.sticker_view)
//    StickerView stickerView;
//    private FrameImageView mSelectedFrameImageView;
//    private FramePhotoLayout mFramePhotoLayout;
//    private ViewGroup mSpaceLayout;
//    //    private SeekBar mSpaceBar;
////    private SeekBar mCornerBar;
//    private float mSpace = DEFAULT_SPACE;
//    private float mCorner = 0;
//    //Background
//    private int mBackgroundColor = Color.GRAY;
//    private Bitmap mBackgroundImage;
//    private Uri mBackgroundUri = null;
//    protected int mLayoutRatio = RATIO_SQUARE;
//    private Bundle mSavedInstanceState;
//    TemplateItem templateItem;
//    private PhotoLayout mPhotoLayout;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//        mSavedInstanceState = savedInstanceState;
//        templateItem = new TemplateItem();
//
//
//     //   templateItem = FrameImageUtils.loadFrameImages(this).get(6);
//
//        templateItem = TemplateImageUtils.loadTemplates().get(6);
//        templateItem.getPhotoItemList().size();
//        templateItem.getPhotoItemList().get(0).imagePath = "/storage/emulated/0/Pictures/4K Wallpapers/nature092_uhd.jpg";
//       // templateItem.getPhotoItemList().get(1).imagePath = "/storage/emulated/0/Pictures/4K Wallpapers/nature092_uhd.jpg";
//
//        collageArea.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mOutputScale = ImageUtils.calculateOutputScaleFactor(collageArea.getWidth(), collageArea.getHeight());
//                buildLayoutTemplate(templateItem);
//                // remove listener
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    collageArea.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                } else {
//                    collageArea.getViewTreeObserver().removeGlobalOnLayoutListener(this);
//                }
//            }
//        });
//
//        //        buildLayout(templateItem);
//    }
//
//    private void loadFrameImages() {
//
//        TemplateImageUtils.loadTemplates();
//
//    }
//    private int getPhotoViewWidth() {
//        return collageArea.getWidth();
//    }
//
//    private int getPhotoViewHeight() {
//        return collageArea.getHeight();
//    }
//    public int[] calculateThumbnailSize(int imageWidth, int imageHeight) {
//        int[] size = new int[2];
//        float ratioWidth = ((float) imageWidth) / getPhotoViewWidth();
//        float ratioHeight = ((float) imageHeight) / getPhotoViewHeight();
//        float ratio = Math.max(ratioWidth, ratioHeight);
//        if (ratio == ratioWidth) {
//            size[0] = getPhotoViewWidth();
//            size[1] = (int) (imageHeight / ratio);
//        } else {
//            size[0] = (int) (imageWidth / ratio);
//            size[1] = getPhotoViewHeight();
//        }
//
//        return size;
//    }
//    protected void buildLayoutTemplate(TemplateItem templateItem) {
//        Bitmap backgroundImage = null;
//        if (mPhotoLayout != null) {
//            backgroundImage = mPhotoLayout.getBackgroundImage();
//            mPhotoLayout.recycleImages(false);
//        }
//
//        final Bitmap frameImage = PhotoUtils.decodePNGImage(this, templateItem.getTemplate());
//        int[] size = calculateThumbnailSize(frameImage.getWidth(), frameImage.getHeight());
//        //Photo Item item_quick_action must be descended by index before creating photo layout.
//        mPhotoLayout = new PhotoLayout(this, templateItem.getPhotoItemList(), frameImage);
//        mPhotoLayout.setBackgroundImage(backgroundImage);
//       // mPhotoLayout.setQuickActionClickListener(this);
//        mPhotoLayout.build(size[0], size[1], mOutputScale);
//
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size[0], size[1]);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        collageArea.removeAllViews();
//        collageArea.addView(mPhotoLayout, params);
//        //add sticker view
////        mContainerLayout.removeView(mPhotoView);
////        mContainerLayout.addView(mPhotoView, params);
//    }
//    private void buildLayout(TemplateItem item) {
//        mFramePhotoLayout = new FramePhotoLayout(this, item.getPhotoItemList());
//        mFramePhotoLayout.setQuickActionClickListener(this);
//        if (mBackgroundImage != null && !mBackgroundImage.isRecycled()) {
//            if (Build.VERSION.SDK_INT >= 16)
//                collageArea.setBackground(new BitmapDrawable(getResources(), mBackgroundImage));
//            else
//                collageArea.setBackgroundDrawable(new BitmapDrawable(getResources(), mBackgroundImage));
//        } else {
//            collageArea.setBackgroundColor(mBackgroundColor);
//        }
//
//        int viewWidth = collageArea.getWidth();
//        int viewHeight = collageArea.getHeight();
//        if (mLayoutRatio == RATIO_SQUARE) {
//            if (viewWidth > viewHeight) {
//                viewWidth = viewHeight;
//            } else {
//                viewHeight = viewWidth;
//            }
//        } else if (mLayoutRatio == RATIO_GOLDEN) {
//            final double goldenRatio = 1.61803398875;
//            if (viewWidth <= viewHeight) {
//                if (viewWidth * goldenRatio >= viewHeight) {
//                    viewWidth = (int) (viewHeight / goldenRatio);
//                } else {
//                    viewHeight = (int) (viewWidth * goldenRatio);
//                }
//            } else if (viewHeight <= viewWidth) {
//                if (viewHeight * goldenRatio >= viewWidth) {
//                    viewHeight = (int) (viewWidth / goldenRatio);
//                } else {
//                    viewWidth = (int) (viewHeight * goldenRatio);
//                }
//            }
//        }
//        mOutputScale = ImageUtils.calculateOutputScaleFactor(viewWidth, viewHeight);
//        mFramePhotoLayout.build(viewWidth, viewHeight, mOutputScale, mSpace, mCorner);
//        if (mSavedInstanceState != null) {
//            mFramePhotoLayout.restoreInstanceState(mSavedInstanceState);
//            mSavedInstanceState = null;
//        }
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(viewWidth, viewHeight);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        collageArea.removeAllViews();
//        collageArea.addView(mFramePhotoLayout, params);
////        //add sticker view
////        collageArea.removeView(mPhotoView);
////        collageArea.addView(mPhotoView, params);
//        //reset space and corner seek bars
////        mSpaceBar.setProgress((int) (MAX_SPACE_PROGRESS * mSpace / MAX_SPACE));
////        mCornerBar.setProgress((int) (MAX_CORNER_PROGRESS * mCorner / MAX_CORNER));
//
//        stickerView.addSticker(
//                new TextSticker(this)
////                        .setDrawable(ContextCompat.getDrawable(getApplicationContext(),
////                                R.drawable.bubble))
//                        .setText("dfsdff")
//                        .setMinTextSize(17)
//                      //  .setTypeface(typeface)
//                       // .setTextColor(color)
//                        .resizeText()
//                , Sticker.Position.TOP);
//
//        stickerView.addSticker(
//                new TextSticker(this)
////                        .setDrawable(ContextCompat.getDrawable(getApplicationContext(),
////                                R.drawable.bubble))
//                        .setText("dfsdff")
//                        .setMinTextSize(17)
//                        //  .setTypeface(typeface)
//                        // .setTextColor(color)
//                        .resizeText()
//                , Sticker.Position.TOP);
//
//        stickerView.addSticker(
//                new TextSticker(this)
////                        .setDrawable(ContextCompat.getDrawable(getApplicationContext(),
////                                R.drawable.bubble))
//                        .setText("dfsdff")
//                        .setMinTextSize(17)
//                        //  .setTypeface(typeface)
//                        // .setTextColor(color)
//                        .resizeText()
//                , Sticker.Position.TOP);
//
//
//    }
//
//    @Override
//    public void onEditActionClick(FrameImageView v) {
//
//    }
//
//    @Override
//    public void onChangeActionClick(FrameImageView v) {
//
//    }
//}

package christmas.frame.photoedittor.collage.showcastimage;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import bo.photo.module.util.TimeUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import christmas.frame.photoedittor.collage.App;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.customView.ZoomableImageView;


public class FragmentShowCastImage extends Fragment {


    @BindView(R.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R.id.rl_rate)
    RelativeLayout rlRate;
    @BindView(R.id.rl_delete)
    RelativeLayout rlDelete;
    Unbinder unbinder;
    Bundle bundle;
    ArrayList<String> a;
    OnDeleteImage onDeleteImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_showcastimage, null);
        RelativeLayout relativeLayout = view.findViewById(R.id.rl_main2);
        onDeleteImage= (OnDeleteImage) getActivity();
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });
        bundle = getArguments();
        ZoomableImageView touch = view.findViewById(R.id.ziv_show);
        touch.setImageBitmap(BitmapFactory.decodeFile(bundle.getString("path")));
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_share, R.id.rl_rate, R.id.rl_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_share:
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                Uri screenshotUri = getImageContentUri(getContext(),new File(bundle.getString("path")));
//                Log.d("testuri",screenshotUri+"");
//                intent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
//                intent.setType("image/*");
//                startActivity(Intent.createChooser(intent, "Share image via..."));
//                Intent share = new Intent(Intent.ACTION_SEND);
//                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(bundle.getString("path"))));
//                share.setType("image/*");
//                share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(share, "Share image File"));


                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                a = new ArrayList<>();
                Bitmap bitmap = BitmapFactory.decodeFile(bundle.getString("path"));
                try { a = saveToInternalStorage(bitmap, TimeUtils.dtFormat(new Date(), "yyyyMMddHHmmss") + ".jpg");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("testshare",a.get(1) );
                Log.d("testshare",a.get(0) );
                Uri screenshotUri = Uri.parse(a.get(1));
                share.putExtra(Intent.EXTRA_STREAM, screenshotUri);
                startActivityForResult(Intent.createChooser(share, "Share image via..."),1);
                break;
            case R.id.rl_rate:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=PackageName")));
                break;
            case R.id.rl_delete:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Do you want to delete this photo?");
                builder.setCancelable(false);
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleted(bundle.getString("path"));
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("testactivity","oke" );
        if(requestCode==1){
            Log.d("testactivity","oke2" );
            deleted(a.get(0));

            Log.d("testdelete",a.get(0) );
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private ArrayList<String> saveToInternalStorage(Bitmap bitmapImage, String fileName) throws IOException {
        ArrayList<String> a = new ArrayList<>();
        ContextWrapper cw = new ContextWrapper(App.getAppContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            fos.close();
        }
        String share = MediaStore.Images.Media.insertImage(App.getAppContext().getContentResolver(), mypath.getAbsolutePath(), mypath.getName(), fileName);
        a.add(mypath.getAbsolutePath());
        a.add(share);
        return a;
    }
    public void deleted(String path){
        File fdelete = new File(path);
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                scanMedia(path);
                onDeleteImage.onDeleted();
                getFragmentManager().popBackStack();
            } else {
                getFragmentManager().popBackStack();
            }
        }
        String[] retCol = { MediaStore.Audio.Media._ID };
        Cursor cur = getContext().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                retCol,
                MediaStore.Images.Media.TITLE + "='"+path.substring(path.lastIndexOf("/")+1)+"'", null, null
        );
        try {
            if (cur.getCount() == 0) {
                return;
            }
            cur.moveToFirst();
            int id = cur.getInt(cur.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            );
            int cnt = getContext().getContentResolver().delete(uri, null, null);
        }finally {
            cur.close();
        }
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        App.getAppContext().sendBroadcast(scanFileIntent);
    }
    public interface OnDeleteImage{
        void onDeleted();
    }


}
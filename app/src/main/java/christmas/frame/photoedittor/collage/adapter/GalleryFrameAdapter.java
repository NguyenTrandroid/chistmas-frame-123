package christmas.frame.photoedittor.collage.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;

import java.io.File;
import java.util.ArrayList;

import bo.photo.module.util.SupportUtils;
import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.frame.OnFrameSelect;
import christmas.frame.photoedittor.collage.frame.OnGalleryFrameSelect;


public class GalleryFrameAdapter extends RecyclerView.Adapter<GalleryFrameAdapter.ViewHolder> {
    ArrayList<String> listframelocal;
    Context context;
    OnDownload onDownload;
    String dir;
    String rootdir;
    int layout;
    OnGalleryFrameSelect onGalleryFrameSelect;


    public GalleryFrameAdapter(ArrayList<String> listframelocal, Context context, OnDownload onDownload, String dir, String rootdir, int layout) {
        this.listframelocal = listframelocal;
        this.context = context;
        this.onDownload = onDownload;
        this.dir = dir;
        this.rootdir = rootdir;
        this.layout = layout;
        onGalleryFrameSelect = (OnGalleryFrameSelect) context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final String rootDirPath = SupportUtils.getRootDirPath(context) + "/" + rootdir + "/";
        /**
         * Check xem file nào chưa down
         */
        holder.download.setVisibility(View.GONE);
        if (listframelocal.get(position).contains(rootDirPath)) {
            Log.d("testimage", listframelocal.get(position));
            Glide.with(context).load(listframelocal.get(position)).into(holder.photo);
        } else {
            Log.d("testimage", listframelocal.get(position));
            holder.download.setVisibility(View.VISIBLE);
            Glide.with(context).load("http://45.32.99.2/christmas_frame/" + dir + "/frame/" + listframelocal.get(position)).into(holder.photo);

        }
        /**
         * Download File
         */
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(context, config);
        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AAA", listframelocal.get(position));
                if (!listframelocal.get(position).contains(rootDirPath)) {
                    Log.d("BBB", "http://45.32.99.2/christmas_frame/" + dir + "/frame/" + listframelocal.get(position));
                    PRDownloader.download("http://45.32.99.2/christmas_frame/" + dir + "/frame/" + listframelocal.get(position), rootDirPath, listframelocal.get(position))
                            .build()
                            .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                                @Override
                                public void onStartOrResume() {

                                }
                            })
                            .setOnPauseListener(new OnPauseListener() {
                                @Override
                                public void onPause() {

                                }
                            })
                            .setOnCancelListener(new OnCancelListener() {
                                @Override
                                public void onCancel() {

                                }
                            })
                            .setOnProgressListener(new OnProgressListener() {
                                @Override
                                public void onProgress(Progress progress) {

                                }
                            })
                            .start(new OnDownloadListener() {
                                @Override
                                public void onDownloadComplete() {
                                    scanMedia(rootDirPath + listframelocal.get(position));
                                    onDownload.onDownloadCompleted();
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(context, "Failed To Download", Toast.LENGTH_SHORT).show();
                                }


                            });
                } else {
                    onGalleryFrameSelect.sendGalleryFrame(listframelocal.get(position));

                }
            }
        });
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }

    public interface OnDownload {
        void onDownloadCompleted();
    }

    @Override
    public int getItemCount() {
        return listframelocal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView download;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_photo);
            download = itemView.findViewById(R.id.iv_download);
        }
    }


}
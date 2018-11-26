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


public class StickerDownloadAdapter extends RecyclerView.Adapter<StickerDownloadAdapter.ViewHolder> {
    String dir;
    Context context;
    ArrayList<String> listStickerLocal;
    String rootDirPath;
    OnDownload onDownload;
    OnDataPass onDataPass;

    public StickerDownloadAdapter(ArrayList<String> listStickerLocal, String dir, Context context, OnDownload onDownload, OnDataPass onDataPass) {
        this.dir = dir;
        this.listStickerLocal = listStickerLocal;
        this.context = context;
        this.onDownload = onDownload;
        this.onDataPass = onDataPass;
        rootDirPath = SupportUtils.getRootDirPath(context) + "/sticker/" + dir;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_stickers, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        /**
         * Download
         */

        for (int i = 0; i < listStickerLocal.size(); i++) {
            Log.d("testss", listStickerLocal.get(i));
        }
        holder.download.setVisibility(View.GONE);
        if (listStickerLocal.get(position).contains(rootDirPath)) {
            Glide.with(context).load(listStickerLocal.get(position)).into(holder.sticker1);
        } else {
            holder.download.setVisibility(View.VISIBLE);
            Glide.with(context).load("http://45.32.99.2/image/sticker/" + dir + "/frame/" + listStickerLocal.get(position)).into(holder.sticker1);
            Log.d("AAA","http://45.32.99.2/image/sticker/" + dir + "/frame/" + listStickerLocal.get(position));
        }

        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(context, config);
        holder.sticker1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!listStickerLocal.get(position).contains("/sticker")) {
                    PRDownloader.download("http://45.32.99.2/image/sticker/" + dir + "/frame/" + listStickerLocal.get(position), rootDirPath, listStickerLocal.get(position))
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
                                    Toast.makeText(context, "Download Completed", Toast.LENGTH_SHORT).show();
                                    scanMedia(rootDirPath + listStickerLocal.get(position));
                                    onDownload.onDownloadCompleted(position);
                                }

                                @Override
                                public void onError(Error error) {
                                    Toast.makeText(context, "Failed To Download", Toast.LENGTH_SHORT).show();

                                }

                            });
                } else onDataPass.onStickerSelected(listStickerLocal.get(position));
//                dataPasser.onStickerSelected("http://45.32.99.2/image/sticker/"+dir+"/frame/"+urlFromApi.get(position));
            }
        });

//
//        Glide.with(context).load("http://45.32.99.2/image/sticker/"+dir+"/frame/"+urlFromApi.get(position)).into(holder.sticker1);
//        Picasso.get().load().fit().centerCrop().into(holder.sticker1);
    }

    private void scanMedia(String path) {
        File file = new File(path);
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }

    public interface OnDownload {
        void onDownloadCompleted(int position);
    }

    @Override
    public int getItemCount() {
        return listStickerLocal.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sticker1;
        ImageView download;

        public ViewHolder(View itemView) {
            super(itemView);
            sticker1 = itemView.findViewById(R.id.iv_sticker1);
            download = itemView.findViewById(R.id.iv_download);
        }
    }

    public interface OnDataPass {
        void onStickerSelected(String url);
    }


}
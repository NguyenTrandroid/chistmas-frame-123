package christmas.frame.photoedittor.collage.addphoto.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import christmas.frame.photoedittor.collage.R;
import christmas.frame.photoedittor.collage.addphoto.OnPhotoSelectt;
import christmas.frame.photoedittor.collage.model.Path;


public class PhotoListAdapter extends RecyclerView.Adapter<PhotoListAdapter.ViewHolder> {
    ArrayList<String> listResource;
    OnPhotoSelectt onPhotoSelect;
    int maxImage = 0;
    int countImageSelected = 0;
    Context context;
    int layout;
    ArrayList<Path> listPathSelected;

    public PhotoListAdapter(ArrayList<String> listResource, Context context, int layout, OnPhotoSelectt onPhotoSelect, int maxImage) {
        this(listResource, context, layout);
        this.onPhotoSelect = onPhotoSelect;
        this.maxImage = maxImage;
    }

    public PhotoListAdapter(ArrayList<String> listResource, Context context, int layout) {
        this.listResource = listResource;
        this.context = context;
        this.layout = layout;
        createHashMapCheckSelected(listResource);
        Log.d("test", listResource.size() + "");
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
        /**
         * Chuyen Photo Path qua PhotoFragment bang interface
         */

        holder.photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (maxImage == 0) {
                    listPathSelected.get(position).changeStatus();
                    Glide.with(context).clear(holder.check);
                    if (listPathSelected.get(position).isSelected()) {
                        Glide.with(context).load(R.drawable.ic_check_cam).into(holder.check);
                    }
                    passData(position);
                } else {
                    boolean isMaxImage = false;
                    listPathSelected.get(position).changeStatus();
                    Glide.with(context).clear(holder.check);
                    if (listPathSelected.get(position).isSelected()) {
                        Glide.with(context).load(R.drawable.ic_check_cam).into(holder.check);
                        countImageSelected++;
                        if (countImageSelected > maxImage) {
                            listPathSelected.get(position).changeStatus();
                            Toast.makeText(context, "Up to " + maxImage + " photo only", Toast.LENGTH_SHORT).show();
                            countImageSelected--;
                            isMaxImage = true;
                            Glide.with(context).clear(holder.check);
                        }
                    } else countImageSelected--;
                    if (onPhotoSelect != null && isMaxImage == false) {
                        onPhotoSelect.sendPhoto(listPathSelected.get(position).getPath(), false);
                    }

                }

            }
        });

        if (position == 0) {
            holder.camera.setVisibility(View.VISIBLE);
            holder.check.setVisibility(View.GONE);
        } else {
            holder.camera.setVisibility(View.GONE);
            holder.check.setVisibility(View.VISIBLE);

        }
        Log.d("test", "position: " + position + "  status: " + listPathSelected.get(position).isSelected());
        Glide.with(context).load(listPathSelected.get(position).getPath()).into(holder.photo);
        Glide.with(context).clear(holder.check);
        if (listPathSelected.get(position).isSelected()) {
            Glide.with(context).load(R.drawable.ic_check_cam).into(holder.check);
        }
    }

    @Override
    public int getItemCount() {
        return listPathSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView photo;
        private ImageView check;
        private ImageView camera;

        public ViewHolder(View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.iv_photo);
            check = itemView.findViewById(R.id.iv_check);
            camera = itemView.findViewById(R.id.iv_camera);
        }
    }


    public void passData(int position) {
        if (onPhotoSelect != null) {
            onPhotoSelect.sendPhoto(listPathSelected.get(position).getPath(), true);
        }
    }

    public void createHashMapCheckSelected(ArrayList<String> listResource) {
        listPathSelected = new ArrayList<>();
        for (int i = 0; i < listResource.size(); i++) {
            listPathSelected.add(new Path(false, listResource.get(i)));
        }
    }


}
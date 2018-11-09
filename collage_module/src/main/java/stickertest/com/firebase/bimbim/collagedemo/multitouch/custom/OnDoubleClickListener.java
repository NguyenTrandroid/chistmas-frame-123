package stickertest.com.firebase.bimbim.collagedemo.multitouch.custom;


import stickertest.com.firebase.bimbim.collagedemo.multitouch.controller.MultiTouchEntity;

public interface OnDoubleClickListener {
	public void onPhotoViewDoubleClick(PhotoView view, MultiTouchEntity entity);
	public void onBackgroundDoubleClick();
}

package stickertest.com.firebase.bimbim.collagedemo.frame;


import stickertest.com.firebase.bimbim.collagedemo.listener.OnFrameTouchListener;

public abstract class FrameTouch implements OnFrameTouchListener {
	private boolean mImageFrameMoving = false;

	public void setImageFrameMoving(boolean imageFrameMoving) {
		mImageFrameMoving = imageFrameMoving;
	}

	public boolean isImageFrameMoving() {
		return mImageFrameMoving;
	}

}

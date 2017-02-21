package com.jx.wxhb.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.avos.avoscloud.SaveCallback;

/**
 * Desc
 * Created by Jun on 2017/2/20.
 */

public class ImageLoaderUtil {

    public static void displayImage(String url){

    }

    public static void getImageByObjectId(String id,final OnImageCallback onImageCallback){
        AVFile.withObjectIdInBackground(id, new GetFileCallback<AVFile>() {
            @Override
            public void done(AVFile avFile, AVException e) {
                if (avFile != null) {
                    avFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (e != null){
                                onImageCallback.onError(e.getMessage());
                            } else {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                onImageCallback.onSuccess(bitmap,null);
                            }
                        }
                    });
                } else {
                    onImageCallback.onError(e.getMessage());
                }

            }
        });
    }

    public static void displayImageByObjectId(String id, final ImageView imageView){
        AVFile.withObjectIdInBackground(id, new GetFileCallback<AVFile>() {
            @Override
            public void done(AVFile avFile, AVException e) {
                if (avFile != null) {
                    avFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (e == null){
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                }

            }
        });
    }

    public static void displayImageByObjectId(String id, final ImageView imageView,final OnImageCallback onImageCallback){
        AVFile.withObjectIdInBackground(id, new GetFileCallback<AVFile>() {
            @Override
            public void done(AVFile avFile, AVException e) {
                if (avFile != null) {
                    avFile.getDataInBackground(new GetDataCallback() {
                        @Override
                        public void done(byte[] bytes, AVException e) {
                            if (e != null){
                                onImageCallback.onError(e.getMessage());
                            } else {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                imageView.setImageBitmap(bitmap);
                            }
                        }
                    });
                } else {
                    onImageCallback.onError(e.getMessage());
                }

            }
        });
    }

    public interface OnImageCallback{
        void onSuccess(Bitmap bitmap,String objectId);

        void onError(String msg);
    }

    public static void updateImageToCloud(String path){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(path.substring(path.lastIndexOf("/")+1), path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        UserUtil.updateUserAvatarId(file.getObjectId());
                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void updateImageToCloud(String filename, String path,final OnImageCallback onImageCallback){
        try {
            final AVFile file = AVFile.withAbsoluteLocalPath(filename, path);
            file.saveInBackground(new SaveCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        getImageByObjectId(file.getObjectId(), new OnImageCallback() {
                            @Override
                            public void onSuccess(Bitmap bitmap,String id) {
                                onImageCallback.onSuccess(bitmap,file.getObjectId());
                            }

                            @Override
                            public void onError(String msg) {
                                onImageCallback.onError(msg);
                            }
                        });

                    }
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}

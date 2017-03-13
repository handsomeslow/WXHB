package com.jx.wxhb.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.GetDataCallback;
import com.avos.avoscloud.GetFileCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static android.os.Environment.MEDIA_MOUNTED;

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

    public static void saveImageToGallery(Context context, Bitmap bitmap) {
        // 首先保存图片
        File appDir = new File(getCacheDirectory(context) + "/wxhb/");
        if (!appDir.exists()) {
            appDir.mkdirs();
        }

        String mImageFileName = System.currentTimeMillis() + ".png";
        String mImageFilePath = appDir.getAbsolutePath() + "/" + mImageFileName;

        long dateSeconds = System.currentTimeMillis() / 1000;

        // Save the screenshot to the MediaStore
        ContentValues values = new ContentValues();
        ContentResolver resolver = context.getContentResolver();
        values.put(MediaStore.Images.ImageColumns.DATA, mImageFilePath);
        values.put(MediaStore.Images.ImageColumns.TITLE, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DISPLAY_NAME, mImageFileName);
        values.put(MediaStore.Images.ImageColumns.DATE_ADDED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.DATE_MODIFIED, dateSeconds);
        values.put(MediaStore.Images.ImageColumns.MIME_TYPE, "image/png");
        Uri uri;
        try {
            uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } catch (Exception e){
            Toast.makeText(context,"没有权限",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            OutputStream out = resolver.openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            // update file size in the database
            values.clear();
            values.put(MediaStore.Images.ImageColumns.SIZE, new File(mImageFilePath).length());
            resolver.update(uri, values, null, null);

            Toast.makeText(context,"保存成功",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context,"保存失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    mImageFilePath, mImageFileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + appDir.getPath())));
    }

    /**
     * 获取总缓存目录
     * @param context
     * @return
     */
    public static File getCacheDirectory(Context context) {

        File appCacheDir = null;
        String externalStorageState;
        try {
            externalStorageState = Environment.getExternalStorageState();
        } catch (NullPointerException e) {
            externalStorageState = "";
        } catch (IncompatibleClassChangeError e) {
            externalStorageState = "";
        }
        if (MEDIA_MOUNTED.equals(externalStorageState) && hasExternalStoragePermission(context)) {
            appCacheDir = getExternalCacheDir(context);
        }
        if (appCacheDir == null) {
            appCacheDir = context.getCacheDir();
        }
        if (appCacheDir == null) {
            //notify user about the external storage ？
            String cacheDirPath = "/data/data/" + context.getPackageName() + "/cache/";
            //L.w("Can't define system cache directory! '%s' will be used.", cacheDirPath);
            appCacheDir = new File(cacheDirPath);
        }
        return appCacheDir;
    }

    private static boolean hasExternalStoragePermission(Context context) {
        int perm = context.checkCallingOrSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE");
        return perm == PackageManager.PERMISSION_GRANTED;
    }

    private static File getExternalCacheDir(Context context) {
        File dataDir = new File(new File(Environment.getExternalStorageDirectory(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                //L.w("Unable to create external cache directory");
                return null;
            }
            try {
                new File(appCacheDir, ".nomedia").createNewFile();
            } catch (IOException e) {
                //L.i("Can't create \".nomedia\" file in application external cache directory");
            }
        }
        return appCacheDir;
    }

}

package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.R;
import com.jx.wxhb.adapter.FullyGridLayoutManager;
import com.jx.wxhb.adapter.PhotosPickerAdapter;
import com.jx.wxhb.utils.CloudContentUtil;
import com.jx.wxhb.utils.ImageLoaderUtil;
import com.jx.wxhb.utils.UserUtil;
import com.luck.picture.lib.model.FunctionConfig;
import com.luck.picture.lib.model.LocalMediaLoader;
import com.luck.picture.lib.model.PictureConfig;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;
import com.yalantis.ucrop.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

public class PublishLuckyGroupActivity extends BaseActivity {

    @Bind(R.id.input_edit_text)
    EditText inputEditText;
//    @Bind(R.id.add_photo_image_view)
//    ImageView addPhotoImageView;
    @Bind(R.id.publish_button)
    Button publishButton;
    @Bind(R.id.photos_picker_view)
    RecyclerView photosPickerView;

    PhotosPickerAdapter adapter;
    private List<LocalMedia> photos;
    private int selectMode = FunctionConfig.MODE_MULTIPLE;
    private int maxSelectNum = 9;// 图片最大可选数量

    public static Intent newIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PublishLuckyGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_lucky_group_layout);
        ButterKnife.bind(this);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishLuckyGroupActivity.this,
                4, GridLayoutManager.VERTICAL, false);
        photosPickerView.setLayoutManager(manager);
        adapter = new PhotosPickerAdapter(PublishLuckyGroupActivity.this, onAddPicClickListener);
        adapter.setSelectMax(maxSelectNum);
        photosPickerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new PhotosPickerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                PictureConfig.getPictureConfig().externalPicturePreview(PublishLuckyGroupActivity.this, position, photos);
            }
        });
    }


    @OnClick(R.id.publish_button)
    public void onPublishClick() {
        if (!inputEditText.getText().toString().isEmpty()) {
            publishLuckyGroup(inputEditText.getText().toString());
        }

    }

    private void publishLuckyGroup(final String content) {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(final Subscriber<? super String> subscriber) {
                ImageLoaderUtil.updateImageToCloud((UserUtil.getLoginedUser().getUsername() + ".png"),
                        photos.get(0).getPath(),
                        new ImageLoaderUtil.OnImageCallback() {
                            @Override
                            public void onSuccess(Bitmap bitmap, String objectId) {
                                Toast.makeText(PublishLuckyGroupActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                subscriber.onNext(objectId);
                            }

                            @Override
                            public void onError(String msg) {
                                Toast.makeText(PublishLuckyGroupActivity.this, "上传失败" + msg, Toast.LENGTH_SHORT).show();
                                Log.d("jun", "onError: " + msg);
                            }
                        });
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(final String photo) {
                        AVObject group = new AVObject(CloudContentUtil.LUCKY_GROUP_TABLE);
                        group.put(CloudContentUtil.LUCKY_GROUP_CONTENT, content);
                        List<String> photos = new ArrayList<String>();
                        photos.add(photo);
                        group.put(CloudContentUtil.LUCKY_GROUP_PHOTOS, photos);
                        group.put(CloudContentUtil.LUCKY_GROUP_OWNER, AVUser.getCurrentUser());
                        group.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    Toast.makeText(PublishLuckyGroupActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(PublishLuckyGroupActivity.this, "发布失败，请重试！", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });



    }

//    @OnClick(R.id.add_photo_image_view)
//    public void onAddPhotosClick(){
//        IPicker.setLimit(3);
//        IPicker.open(getApplicationContext());
//        IPicker.setCropEnable(false);
//        IPicker.setOnSelectedListener(new IPicker.OnSelectedListener() {
//            @Override
//            public void onSelected(List<String> paths) {
//                if (photos != null){
//                    photos.clear();
//                }
//                photos = paths;
//            }
//        });
//    }

    /**
     * 删除图片回调接口
     */
    private PhotosPickerAdapter.onAddPicClickListener onAddPicClickListener = new PhotosPickerAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick(int type, int position) {
            switch (type) {
                case 0:
                    // 进入相册
                    /**
                     * type --> 1图片 or 2视频
                     * copyMode -->裁剪比例，默认、1:1、3:4、3:2、16:9
                     * maxSelectNum --> 可选择图片的数量
                     * selectMode         --> 单选 or 多选
                     * isShow       --> 是否显示拍照选项 这里自动根据type 启动拍照或录视频
                     * isPreview    --> 是否打开预览选项
                     * isCrop       --> 是否打开剪切选项
                     * isPreviewVideo -->是否预览视频(播放) mode or 多选有效
                     * ThemeStyle -->主题颜色
                     * CheckedBoxDrawable -->图片勾选样式
                     * cropW-->裁剪宽度 值不能小于100  如果值大于图片原始宽高 将返回原图大小
                     * cropH-->裁剪高度 值不能小于100
                     * isCompress -->是否压缩图片
                     * setEnablePixelCompress 是否启用像素压缩
                     * setEnableQualityCompress 是否启用质量压缩
                     * setRecordVideoSecond 录视频的秒数，默认不限制
                     * setRecordVideoDefinition 视频清晰度  Constants.HIGH 清晰  Constants.ORDINARY 低质量
                     * setImageSpanCount -->每行显示个数
                     * setCheckNumMode 是否显示QQ选择风格(带数字效果)
                     * setPreviewColor 预览文字颜色
                     * setCompleteColor 完成文字颜色
                     * setPreviewBottomBgColor 预览界面底部背景色
                     * setBottomBgColor 选择图片页面底部背景色
                     * setCompressQuality 设置裁剪质量，默认无损裁剪
                     * setSelectMedia 已选择的图片
                     * setCompressFlag 1为系统自带压缩  2为第三方luban压缩
                     * 注意-->type为2时 设置isPreview or isCrop 无效
                     * 注意：Options可以为空，默认标准模式
                     */
//                    String ws = et_w.getText().toString().trim();
//                    String hs = et_h.getText().toString().trim();
//
//                    if (!isNull(ws) && !isNull(hs)) {
//                        cropW = Integer.parseInt(ws);
//                        cropH = Integer.parseInt(hs);
//                    }
//
//                    if (!isNull(et_compress_width.getText().toString()) && !isNull(et_compress_height.getText().toString())) {
//                        compressW = Integer.parseInt(et_compress_width.getText().toString());
//                        compressH = Integer.parseInt(et_compress_height.getText().toString());
//                    }

                    //int selector = R.drawable.select_cb;
                    FunctionConfig config = new FunctionConfig();
                    config.setType(LocalMediaLoader.TYPE_IMAGE);
                    config.setCopyMode(FunctionConfig.COPY_MODEL_DEFAULT);
                    config.setCompress(true);
                    config.setEnablePixelCompress(true);
                    config.setEnableQualityCompress(true);
                    config.setMaxSelectNum(maxSelectNum);
                    config.setSelectMode(selectMode);
                    config.setShowCamera(true);
                    config.setEnablePreview(true);
                    config.setEnableCrop(true);
//                    config.setCropW(cropW);
//                    config.setCropH(cropH);
                    config.setCheckNumMode(true);
                    config.setCompressQuality(100);
                    config.setImageSpanCount(4);
//                    config.setSelectMedia(selectMedia);
//                    config.setCompressFlag(compressFlag);
//                    config.setCompressW(compressW);
//                    config.setCompressH(compressH);
//                    if (theme) {
//                        config.setThemeStyle(ContextCompat.getColor(PublishLuckyGroupActivity.this, R.color.blue));
//                        // 可以自定义底部 预览 完成 文字的颜色和背景色
//                        if (!isCheckNumMode) {
//                            // QQ 风格模式下 这里自己搭配颜色，使用蓝色可能会不好看
//                            config.setPreviewColor(ContextCompat.getColor(PublishLuckyGroupActivity.this, R.color.white));
//                            config.setCompleteColor(ContextCompat.getColor(PublishLuckyGroupActivity.this, R.color.white));
//                            config.setPreviewBottomBgColor(ContextCompat.getColor(PublishLuckyGroupActivity.this, R.color.blue));
//                            config.setBottomBgColor(ContextCompat.getColor(PublishLuckyGroupActivity.this, R.color.blue));
//                        }
//                    }
//                    if (selectImageType) {
//                        config.setCheckedBoxDrawable(selector);
//                    }

                    // 先初始化参数配置，在启动相册
                    PictureConfig.init(config);
                    PictureConfig.getPictureConfig().openPhoto(PublishLuckyGroupActivity.this, resultCallback);

                    break;
                case 1:
                    // 删除图片
                    photos.remove(position);
                    adapter.notifyItemRemoved(position);
                    break;
            }
        }
    };

    /**
     * 图片回调方法
     */
    private PictureConfig.OnSelectResultCallback resultCallback = new PictureConfig.OnSelectResultCallback() {
        @Override
        public void onSelectSuccess(List<LocalMedia> resultList) {
            photos = resultList;
            Log.i("callBack_result", photos.size() + "");
            if (photos != null) {
                adapter.setList(photos);
                adapter.notifyDataSetChanged();
            }
        }
    };
}

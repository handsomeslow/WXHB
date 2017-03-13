package com.jx.wxhb.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.utils.ImageLoaderUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * Desc
 * Created by Jun on 2017/3/10.
 */

public class PhotoViewFragment extends DialogFragment {
    public static String EXTRA_IMAGE_URL = "iamge_url";

    @Bind(R.id.photo_image_view)
    ImageView photoImageView;

    private String photoUrl;

    public static PhotoViewFragment newInstance(String imageUrl) {
        PhotoViewFragment fragment = new PhotoViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_IMAGE_URL, imageUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
        initExtraData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo_view_layout, null);
        ButterKnife.bind(this, view);
        if (!TextUtils.isEmpty(photoUrl)) {
            ImageLoaderUtil.displayImageByObjectId(photoUrl, photoImageView);
        } else {
            Toast.makeText(getActivity(), "无效图片", Toast.LENGTH_SHORT).show();
            dismiss();
        }
        return view;
    }

    private void initExtraData() {
        if (getArguments() != null) {
            photoUrl = getArguments().getString(EXTRA_IMAGE_URL);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(),"长按保存图片",Toast.LENGTH_SHORT).show();
        photoImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return onSavePhotoClick();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    //@OnLongClick(R.id.photo_image_view)
    public boolean onSavePhotoClick(){
        photoImageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = photoImageView.getDrawingCache(true).copy(Bitmap.Config.RGB_565,false);
        photoImageView.setDrawingCacheEnabled(false);
        Log.d("jun", "onSavePhotoClick: "+bitmap.getByteCount());
        ImageLoaderUtil.saveImageToGallery(getActivity(),bitmap);
        return true;
    }
}

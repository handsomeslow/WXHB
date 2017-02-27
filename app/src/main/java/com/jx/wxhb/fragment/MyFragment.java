package com.jx.wxhb.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.wxhb.BuildConfig;
import com.jx.wxhb.R;
import com.jx.wxhb.activity.LoginActivity;
import com.jx.wxhb.presenter.AdminContract;
import com.jx.wxhb.presenter.AdminPresenter;
import com.jx.wxhb.utils.ContentUtil;
import com.jx.wxhb.utils.ImageLoaderUtil;
import com.jx.wxhb.utils.UserUtil;
import com.liuguangqiang.ipicker.IPicker;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyFragment extends BaseFragment implements View.OnClickListener,
        IPicker.OnSelectedListener, AdminContract.View {
    private static final int REQUEST_CODE_LOGIN_SUCCESS = 0X9001;

    @Bind(R.id.user_pic_image_view)
    ImageView loginInBt;

    @Bind(R.id.save_img_btn)
    Button saveImgBtn;
    @Bind(R.id.name_text_view)
    TextView nameTextView;

    @Bind(R.id.refresh_wechat_news_button)
    ImageView refreshNewsButton;

    @Bind(R.id.admin_control_layout)
    LinearLayout adminControlLayout;

    private String title;

    private AdminPresenter adminPresenter;

    public static MyFragment newInstance(String tag) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ContentUtil.EXTRA_ARG_TITLE, tag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ContentUtil.EXTRA_ARG_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adminPresenter = new AdminPresenter(this);
        loginInBt.setOnClickListener(this);
        saveImgBtn.setOnClickListener(this);

        if (BuildConfig.DEBUG) {
            adminControlLayout.setVisibility(View.VISIBLE);
        }

        if (UserUtil.getLoginedUser() != null) {
            initUserInfoView();
        }
    }

    private void initUserInfoView() {
        getUserPic();
        nameTextView.setText(UserUtil.getLoginedUser().getUsername());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_pic_image_view:
                if (UserUtil.getLoginedUser() == null) {
                    loginIn();
                } else {
                    selectPic();
                }
                break;

        }
    }

    private void selectPic() {
        IPicker.setLimit(1);
        IPicker.setCropEnable(true);
        IPicker.open(getActivity());
        IPicker.setOnSelectedListener(this);

    }

    @Override
    public void onSelected(List<String> paths) {
        if (paths != null && paths.size() > 0) {
            Log.d("jun", "onSelected path:" + paths.get(0));
            ImageLoaderUtil.updateImageToCloud((UserUtil.getLoginedUser().getUsername() + ".png"),
                    paths.get(0),
                    new ImageLoaderUtil.OnImageCallback() {
                        @Override
                        public void onSuccess(Bitmap bitmap, String objectId) {
                            Toast.makeText(getActivity(), "上传成功", Toast.LENGTH_SHORT).show();
                            UserUtil.updateUserAvatarId(objectId);
                            initUserInfoView();
                        }

                        @Override
                        public void onError(String msg) {
                            Toast.makeText(getActivity(), "上传失败" + msg, Toast.LENGTH_SHORT).show();
                            Log.d("jun", "onError: " + msg);
                        }
                    });

        }
    }

    private void loginIn() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_LOGIN_SUCCESS);
    }

    private void getUserPic() {
        if (UserUtil.getUserAvatarId() != null) {
            ImageLoaderUtil.displayImageByObjectId(UserUtil.getUserAvatarId(), loginInBt);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("jun", "onActivityResult: " + requestCode);
        switch (requestCode) {
            case REQUEST_CODE_LOGIN_SUCCESS:
                initUserInfoView();
                break;
        }
    }

    @OnClick(R.id.refresh_wechat_news_button)
    public void onPushNewsClick() {
        adminPresenter.pushNewsToCloud();
    }

    @Override
    public void onPushNewsFinish(int count) {
        Toast.makeText(getActivity(),"更新了："+count,Toast.LENGTH_SHORT).show();
    }
}

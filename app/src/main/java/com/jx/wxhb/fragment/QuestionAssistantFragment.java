package com.jx.wxhb.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.jx.wxhb.R;
import com.jx.wxhb.service.QuestionFloatService;
import com.jx.wxhb.utils.AccessibilityUtil;
import com.jx.wxhb.utils.CheckFloatWindowUtil;
import com.jx.wxhb.widget.QuestionFloatView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class QuestionAssistantFragment extends BaseFragment {
    private static final String TAG = "AssistantFragment";


//    @Bind(R.id.start_float_button)
//    Button startFloatButton;


    @Bind(R.id.success_view)
    TextView successView;

    @Bind(R.id.error_view)
    TextView errorView;

    @Bind(R.id.open_assistant_image_view)
    ImageView openAssistantState;

    @Bind(R.id.float_tip_image_view)
    ImageView floatTipState;

    //private QuestionFloatView questionFloatView;

    public static QuestionAssistantFragment newInstance() {
        QuestionAssistantFragment fragment = new QuestionAssistantFragment();
        return fragment;
    }

    public QuestionAssistantFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_assistant_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        checkStatus();
    }

    private void checkStatus() {
        // 检查无障碍
        if (AccessibilityUtil.isAccessibilitySettingsOn()) {
            openAssistantState.setImageResource(R.drawable.icon_success);
        } else {
            openAssistantState.setImageResource(R.drawable.icon_error);
        }

        // 悬浮框
        if (CheckFloatWindowUtil.checkPermission(getActivity())) {
            floatTipState.setImageResource(R.drawable.icon_success);
        } else {
            floatTipState.setImageResource(R.drawable.icon_error);
        }

        showStatus(AccessibilityUtil.serviceIsRunning(".service.QHBAccessibilityService"));
    }

    private void showStatus(boolean isSuccess) {
        successView.setVisibility(isSuccess ? View.VISIBLE : View.GONE);
        errorView.setVisibility(!isSuccess ? View.VISIBLE : View.GONE);
    }

//    private boolean isOpenFloatView = true;
//    @OnClick(R.id.start_float_button)
//    void startFloat() {
////        Intent intent = new Intent(getActivity(), QuestionFloatService.class);
////        getActivity().startService(intent);
////        if (questionFloatView == null) {
////            questionFloatView = new QuestionFloatView(MyApplication.context);
////        } else {
////            questionFloatView.remove();
////        }
//        if (isOpenFloatView) {
//            if (Build.VERSION.SDK_INT > 24 && !CheckFloatWindowUtil.checkPermission(getActivity())) {
//                CheckFloatWindowUtil.applyPermission(getActivity());
//            } else {
//                Intent intent = new Intent(getActivity(), QuestionFloatService.class);
//                getActivity().startService(intent);
//            }
//        } else {
//            Intent intent = new Intent(getActivity(), QuestionFloatService.class);
//            getActivity().stopService(intent);
//
//        }
//        isOpenFloatView = !isOpenFloatView;
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 222) {
            if (grantResults[0]!= PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),"未获得权限，开启失败！",Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(getActivity(), QuestionFloatService.class);
                getActivity().startService(intent);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

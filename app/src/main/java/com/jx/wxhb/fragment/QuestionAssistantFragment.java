package com.jx.wxhb.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.jx.wxhb.MyApplication;
import com.jx.wxhb.R;
import com.jx.wxhb.widget.QuestionFloatView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class QuestionAssistantFragment extends BaseFragment {

    @Bind(R.id.start_float_button)
    Button startFloatButton;

    private QuestionFloatView questionFloatView;

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

    @OnClick(R.id.start_float_button)
    void startFloat() {
        Intent intent = new Intent(getActivity(), QuestionFloatView.class);
        getActivity().startService(intent);
//        if (questionFloatView == null) {
//            questionFloatView = new QuestionFloatView(MyApplication.context);
//        } else {
//            questionFloatView.remove();
//        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}

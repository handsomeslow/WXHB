package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.CommentAdapter;
import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.presenter.OfficialContract;
import com.jx.wxhb.presenter.OfficialPresenter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class OfficialFragment extends BaseFragment implements OfficialContract.View {

    private static final String ARG_PARAM_ID = "param_official_id";

    @Bind(R.id.name_text_view)
    TextView nameTextView;
    @Bind(R.id.desc_text_view)
    TextView descTextView;

    @Bind(R.id.comment_edit_view)
    EditText commentEditView;
    @Bind(R.id.add_comment_botton)
    Button addCommentBotton;
    @Bind(R.id.comment_list_view)
    RecyclerView commentListView;

    private String officialId;

    OfficialContract.Presenter presenter;

    CommentAdapter commentAdapter;

    public static OfficialFragment newInstance(String id) {
        OfficialFragment fragment = new OfficialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            officialId = getArguments().getString(ARG_PARAM_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        commentListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        presenter = new OfficialPresenter(this);
        presenter.pullOfficialData(officialId);
        presenter.pullCommentData(officialId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showOfficialView(OfficialInfo info) {
        nameTextView.setText(info.getName());
        descTextView.setText(info.getDesc());
    }

    @Override
    public void refreshCommentView(List<CommentInfo> commentInfoList) {
        if (commentAdapter == null){
            commentAdapter = new CommentAdapter(commentInfoList);
            commentListView.setAdapter(commentAdapter);
        } else {
            commentAdapter.refreshCommentInfoList(commentInfoList);
        }
    }

    @OnClick(R.id.add_comment_botton)
    public void addComment() {
        if (!TextUtils.isEmpty(commentEditView.getText().toString())) {
            presenter.addCommentData(commentEditView.getText().toString(), officialId);
        } else {
            Toast.makeText(getActivity(), "是不是忘记填内容里？", Toast.LENGTH_SHORT).show();
        }
    }
}

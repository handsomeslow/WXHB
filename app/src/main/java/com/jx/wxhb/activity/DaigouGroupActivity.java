package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.adapter.PushGroupAdapter;
import com.jx.wxhb.fragment.PhotoViewFragment;
import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.presenter.DaigouGroupContract;
import com.jx.wxhb.presenter.DaigouGroupPresenter;
import com.jx.wxhb.presenter.LuckyGroupContract;
import com.jx.wxhb.presenter.LuckyGroupPresenter;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DaigouGroupActivity extends BaseActivity implements DaigouGroupContract.View {

    @Bind(R.id.lucky_group_list_view)
    UltimateRecyclerView luckyGroupListView;
    @Bind(R.id.comment_edit_view)
    EditText commentEditView;
    @Bind(R.id.add_comment_botton)
    ImageView addCommentBotton;
    @Bind(R.id.add_comment_layout)
    LinearLayout addCommentLayout;

    private PushGroupAdapter adapter;

    private DaigouGroupContract.Presenter presenter;

    private String groupId;
    private int itemPosition;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DaigouGroupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lucky_group_layout);
        ButterKnife.bind(this);
        setTitle("代购集散地");
        showBackButton();
        initView();
        initAdapter();
        presenter = new DaigouGroupPresenter(this);
        presenter.pullGroupList();
    }

    private void initAdapter() {
        if (adapter == null) {
            adapter = new PushGroupAdapter(this);
            adapter.setOnClickListener(new PushGroupAdapter.OnClickListener() {
                @Override
                public void onAddComment(int position,String id) {
                    addCommentLayout.setVisibility(View.VISIBLE);
                    commentEditView.setFocusable(true);
                    commentEditView.setFocusableInTouchMode(true);
                    commentEditView.requestFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    groupId = id;
                    itemPosition = position;
                }

                @Override
                public void onPhotoView(int position, String image) {
                    PhotoViewFragment photoViewFragment = PhotoViewFragment.newInstance(image);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(photoViewFragment,image);
                    fragmentTransaction.commitAllowingStateLoss();
                    //photoViewFragment.showPhoto(getSupportFragmentManager(),image);
                }
            });
            luckyGroupListView.setAdapter(adapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.pullGroupList();
    }

    private void initView() {
        luckyGroupListView.setLayoutManager(new LinearLayoutManager(this));
        luckyGroupListView.displayDefaultFloatingActionButton(true);
        luckyGroupListView.getDefaultFloatingActionButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(PublishLuckyGroupActivity.newIntent(DaigouGroupActivity.this));
            }
        });
    }

    @Override
    public void refreshListsuccess(List<LuckyGroupInfo> groupInfoList) {
        adapter.setGroupList(groupInfoList);
    }

    @Override
    public void refreshListFaild() {

    }

    @Override
    public void loadMoreDataSuccess(List<LuckyGroupInfo> groupInfoList) {
        adapter.insertList(groupInfoList);
    }

    @Override
    public void loadMoreDataFaild() {

    }

    @Override
    public void refreshComment(List<CommentInfo> comments, int position) {
        adapter.changeItemComments(comments,position);
    }

    @OnClick(R.id.add_comment_botton)
    public void onFinishCommentClick(){
        String comment = commentEditView.getText().toString();
        if (!TextUtils.isEmpty(comment)){
            presenter.addComment(comment,groupId,itemPosition);
            addCommentLayout.setVisibility(View.GONE);
        } else {
            Toast.makeText(this,"评价失败，请重试！",Toast.LENGTH_SHORT).show();
        }
    }
}

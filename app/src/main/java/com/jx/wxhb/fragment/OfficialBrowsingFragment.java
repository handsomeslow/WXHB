package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.activity.PurchaseGroupActivity;
import com.jx.wxhb.model.Item;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.utils.ImageLoaderUtil;
import com.jx.wxhb.utils.UIUtils;
import com.jx.wxhb.widget.ReverseInterpolator;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 */
public class OfficialBrowsingFragment extends BaseFragment {
    private static final String ARG_PARAM_ITEM = "browsing_item";
    private static final String ARG_PARAM_POSITION = "is_frist";


    @Bind(R.id.more_layout)
    CardView moreLayout;
    @Bind(R.id.picture)
    ImageView picture;
    @Bind(R.id.picture_layout)
    CardView pictureLayout;
    @Bind(R.id.item_view)
    RelativeLayout itemView;
    @Bind(R.id.name_text_view)
    TextView nameTextView;
    @Bind(R.id.wxid_text_view)
    TextView wxidTextView;
    @Bind(R.id.desc_text_view)
    TextView descTextView;
    @Bind(R.id.comment_layout)
    LinearLayout commentLayout;

    private OfficialInfo item;
    private boolean isFrist;

    public static OfficialBrowsingFragment newInstance(OfficialInfo item, boolean position) {
        OfficialBrowsingFragment fragment = new OfficialBrowsingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PARAM_ITEM, item);
        args.putBoolean(ARG_PARAM_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            item = getArguments().getParcelable(ARG_PARAM_ITEM);
            isFrist = getArguments().getBoolean(ARG_PARAM_POSITION, false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_official_browsing_layout, container, false);
        view.getLayoutParams().width = UIUtils.getWidth() - UIUtils.dip2Px(20);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        if (isFrist) {
            itemView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    expandView(true);
                }
            }, 500);
        }
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(PurchaseGroupActivity.newIntent(getActivity(),item));
            }
        });
    }

    private void initView(){
        ImageLoaderUtil.displayImageByObjectId(item.getImage(),picture);
        nameTextView.setText(item.getName());
        wxidTextView.setText(String.format("微信号：%s",item.getWxId()));
        descTextView.setText(item.getDesc());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed() && isVisibleToUser) {
            expandView(true);
            upFromBottom();
        } else {
            collapseView(true);
            if (commentLayout!=null){
                commentLayout.setVisibility(View.GONE);
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void expandView(final boolean animate) {
        if (moreLayout == null) {
            return;
        }
        if (moreLayout.getVisibility() != View.VISIBLE) {
            moreLayout.setVisibility(View.VISIBLE);
            final Animation scaleMoreAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.reveal_more);
            if (!animate) {
                scaleMoreAnimation.setDuration(0);
            }
            moreLayout.startAnimation(scaleMoreAnimation);
            pictureLayout.animate()
                    .translationY(-getActivity().getResources().getDimensionPixelSize(R.dimen.picture_translate_distance))
                    .setDuration(scaleMoreAnimation.getDuration())
                    .setInterpolator(scaleMoreAnimation.getInterpolator())
                    .start();
        }
        upFromBottom();
    }

    private void collapseView(final boolean animate) {
        if (moreLayout == null) {
            return;
        }
        if (moreLayout.getVisibility() != View.INVISIBLE) {
            moreLayout.setVisibility(View.INVISIBLE);
            final Animation scaleMoreAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.reveal_more);
            if (!animate) {
                scaleMoreAnimation.setDuration(0);
            }
            pictureLayout.animate()
                    .translationY(0)
                    .setDuration(scaleMoreAnimation.getDuration())
                    .setInterpolator(scaleMoreAnimation.getInterpolator())
                    .start();
            scaleMoreAnimation.setInterpolator(new ReverseInterpolator(scaleMoreAnimation.getInterpolator()));
            moreLayout.startAnimation(scaleMoreAnimation);
        }
    }

    private void upFromBottom(){
        commentLayout.setVisibility(View.VISIBLE);
        Animation animation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_up);
        commentLayout.startAnimation(animation);
    }
}

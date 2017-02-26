package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.presenter.RandomFunnyContract;
import com.jx.wxhb.presenter.RandomFunnyPresenter;
import com.jx.wxhb.utils.ContentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 */
public class RandomFunnyFragment extends BaseFragment implements RandomFunnyContract.View {

    @Bind(R.id.one_pos)
    Button onePos;
    @Bind(R.id.two_pos)
    Button twoPos;
    @Bind(R.id.three_pos)
    Button threePos;
    @Bind(R.id.four_pos)
    Button fourPos;
    @Bind(R.id.five_pos)
    Button fivePos;
    @Bind(R.id.history_outcome_text_view)
    TextView historyOutcomeTextView;
    @Bind(R.id.winner_text_view)
    TextView winnerTextView;


    private String title;

    private RandomFunnyContract.Presenter presenter;

    public static RandomFunnyFragment newInstance(String title) {
        RandomFunnyFragment fragment = new RandomFunnyFragment();
        Bundle args = new Bundle();
        args.putString(ContentUtil.EXTRA_ARG_TITLE, title);
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
        View view = inflater.inflate(R.layout.fragment_random_funny_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().setTitle(title);

        presenter = new RandomFunnyPresenter(this);
        presenter.pullRandomFunnyData();
        presenter.pullRandomFunnyHistoryData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void refreshNoteView(int position, int count) {
        Toast.makeText(getActivity(),"refreshNoteView",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initHistoryFunnyView(String outcome, String winner) {
        historyOutcomeTextView.setText(outcome);
        winnerTextView.setText(winner);
    }

    @OnClick(R.id.one_pos)
    public void onOnePositionClick() {
        presenter.addBetInfo(0, 10);
    }

    @OnClick(R.id.two_pos)
    public void onTwoPositionClick() {
        presenter.addBetInfo(1, 10);
    }

    @OnClick(R.id.three_pos)
    public void onThreePositionClick() {
        presenter.addBetInfo(2, 10);
    }

    @OnClick(R.id.four_pos)
    public void onFourPositionClick() {
        presenter.addBetInfo(3, 10);
    }

    @OnClick(R.id.five_pos)
    public void onFivePositionClick() {
        presenter.addBetInfo(4, 10);
    }

}

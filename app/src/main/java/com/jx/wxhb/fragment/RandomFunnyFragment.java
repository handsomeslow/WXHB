package com.jx.wxhb.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jx.wxhb.R;
import com.jx.wxhb.presenter.RandomFunnyContract;
import com.jx.wxhb.presenter.RandomFunnyPresenter;
import com.jx.wxhb.service.PushRecevier;
import com.jx.wxhb.utils.ContentUtil;
import com.luolc.emojirain.EmojiRainLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;

/**
 */
public class RandomFunnyFragment extends BaseFragment implements RandomFunnyContract.View {


    @Bind(R.id.history_outcome_text_view)
    TextView historyOutcomeTextView;

    // 投注图标
    @Bind(R.id.actors_chart_view)
    ColumnChartView actorsChartView;

    @Bind(R.id.group_emoji_container)
    EmojiRainLayout groupEmojiContainer;

    @Bind(R.id.position_0)
    ImageView position0;
    @Bind(R.id.position_1)
    ImageView position1;
    @Bind(R.id.position_2)
    ImageView position2;
    @Bind(R.id.position_3)
    ImageView position3;
    @Bind(R.id.position_4)
    ImageView position4;

    @Bind(R.id.score)
    TextView score;
    @Bind(R.id.out_come_layout)
    LinearLayout outComeLayout;
    @Bind(R.id.next_round)
    TextView nextRound;

    private List<ImageView> postionsImageView;

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
        presenter = new RandomFunnyPresenter(this);
        presenter.pullRandomFunnyData();
        presenter.pullRandomFunnyHistoryData();
        groupEmojiContainer.addEmoji(R.drawable.emoj_hb);
        groupEmojiContainer.addEmoji(R.drawable.emoj_yb);
        initEvent();
        postionsImageView = new ArrayList<>();
        postionsImageView.add(position0);
        postionsImageView.add(position1);
        postionsImageView.add(position2);
        postionsImageView.add(position3);
        postionsImageView.add(position4);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void initView(){
        outComeLayout.setVisibility(View.GONE);
        for (int i =0;i<postionsImageView.size();++i){
            postionsImageView.get(i).setSelected(false);
        }
    }

    private void initEvent(){
        nextRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.pullRandomFunnyData();
                initView();
            }
        });
    }


    @Override
    public void refreshNoteView(int position, int count) {
        IntentFilter filter = new IntentFilter();
        filter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
        filter.addCategory("com.jx.wxhb");
        getActivity().registerReceiver(broadcastReceiver, filter);
        Set<String> tags = new HashSet<String>();
        tags.add("random_funny");
        JPushInterface.setTags(getActivity(), tags, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {

            }
        });
    }

    @Override
    public void initHistoryFunnyView(String outcome, String winner) {
        //historyOutcomeTextView.setText(outcome);
    }

    @OnClick(R.id.position_0)
    public void onOnePositionClick() {
        presenter.addBetInfo(0, 10);
    }

    @OnClick(R.id.position_1)
    public void onTwoPositionClick() {
        presenter.addBetInfo(1, 10);
    }

    @OnClick(R.id.position_2)
    public void onThreePositionClick() {
        presenter.addBetInfo(2, 10);
    }

    @OnClick(R.id.position_3)
    public void onFourPositionClick() {
        presenter.addBetInfo(3, 10);
    }

    @OnClick(R.id.position_4)
    public void onFivePositionClick() {
        presenter.addBetInfo(4, 10);
    }


    private int[] COLORS = {
            ChartUtils.COLOR_BLUE,
            ChartUtils.COLOR_GREEN,
            ChartUtils.COLOR_ORANGE,
            ChartUtils.COLOR_RED,
            ChartUtils.COLOR_VIOLET};

    private void generateColumChartView(List<Integer> list) {
        int subColumsNum = 1;
        int columsNum = list.size();
        List<SubcolumnValue> values;
        List<Column> columns = new ArrayList<>();
        List<AxisValue> axisValues = Arrays.asList(
                new AxisValue(0).setLabel("第1个门"),
                new AxisValue(1).setLabel("第2个门"),
                new AxisValue(2).setLabel("第3个门"),
                new AxisValue(3).setLabel("第4个门"),
                new AxisValue(4).setLabel("第5个门"));
        for (int i = 0; i < columsNum; i++) {
            values = new ArrayList<>();
            for (int j = 0; j < subColumsNum; j++) {
                values.add(new SubcolumnValue(list.get(i), COLORS[i]));
            }
            Column column = new Column(values);
            column.setHasLabels(true);
            column.setHasLabelsOnlyForSelected(false);
            columns.add(column);
        }
        ColumnChartData columnChartData = new ColumnChartData(columns);
        columnChartData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        columnChartData.setAxisYLeft(new Axis().setHasSeparationLine(true).setName("参与人数"));
        actorsChartView.setColumnChartData(columnChartData);
    }

    @Override
    public void refreshActorsView(List<Integer> list) {
        generateColumChartView(list);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("cn.jpush.android.intent.MESSAGE_RECEIVED")) {
                String type = intent.getStringExtra("cn.jpush.android.TITLE");
                String num = intent.getStringExtra("cn.jpush.android.CONTENT_TYPE");
                String id = intent.getStringExtra("cn.jpush.android.MESSAGE");
                if (type.equals("random_funny")) {
                    showWinnerView(num);
                }

            }
        }
    };

    private void showWinnerView(String numStr) {
        int num = Integer.valueOf(numStr);
        groupEmojiContainer.startDropping();
        score.setText(numStr);
        for (int i =0;i<postionsImageView.size();++i){
            postionsImageView.get(i).setSelected(true);
        }
        postionsImageView.get(num).setImageResource(R.drawable.red_packet_winner);
        // 5秒钟进入下一轮
        outComeLayout.setVisibility(View.VISIBLE);
    }
}

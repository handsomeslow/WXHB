package com.jx.wxhb.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArraySet;
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
import lecho.lib.hellocharts.view.PreviewColumnChartView;

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
    // 投注图标
    @Bind(R.id.actors_chart_view)
    ColumnChartView actorsChartView;


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
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void refreshNoteView(int position, int count) {
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


    private int[] COLORS = {ChartUtils.COLOR_BLUE,ChartUtils.COLOR_GREEN,ChartUtils.COLOR_ORANGE,ChartUtils.COLOR_RED,ChartUtils.COLOR_VIOLET};

    private void generateColumChartView(List<Integer> list){
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
        for (int i = 0;i<columsNum;i++){
            values = new ArrayList<>();
            for (int j = 0;j<subColumsNum;j++){
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


}

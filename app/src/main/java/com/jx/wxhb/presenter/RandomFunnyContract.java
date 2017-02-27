package com.jx.wxhb.presenter;

import java.util.List;

/**
 * Created by 徐俊 on 2017/2/25.
 */

public class RandomFunnyContract {
    public interface View {
        void refreshNoteView(int position, int count);

        void initHistoryFunnyView(String outcome, String winner);

        void refreshActorsView(List<Integer> list);
    }

    public interface Presenter {
        void pullRandomFunnyData();

        void pullRandomFunnyHistoryData();

        void addBetInfo(int position,int count);
    }
}

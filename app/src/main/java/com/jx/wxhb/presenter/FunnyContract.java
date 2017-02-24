package com.jx.wxhb.presenter;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class FunnyContract {
    public interface View {
        void refreshListView();

    }

    public interface Presenter {
        void pullOfficialData();
    }
}

package com.jx.wxhb.presenter;

/**
 * Desc
 * Created by Jun on 2017/2/21.
 */

public class AdminContract {
    public interface View{
        void onPushNewsFinish(int count);
    }

    public interface Presenter{
        void pushNewsToCloud();
    }
}

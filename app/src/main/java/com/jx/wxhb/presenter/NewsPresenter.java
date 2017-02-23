package com.jx.wxhb.presenter;

import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.utils.ContentUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/21.
 */

public class NewsPresenter implements NewsContract.Presenter {
    private static int NEWS_PAGE_COUNT = 15;

    List<HotNewInfo> newList;

    NewsContract.View view;

    private int pageNum = 0;

    public NewsPresenter(NewsContract.View view) {
        this.view = view;
        newList = new ArrayList<>();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ContentUtil.PULL_DATA_SUCCESS:
                    view.loadMoreDataSuccess(newList);
                    break;

                case ContentUtil.PULL_DATA_NON:
                    view.loadnoMoreData();
                    break;

                case ContentUtil.PULL_DATA_FAIL:
                    view.loadMoreDataFail();
                    break;
                case ContentUtil.REFRESH_NEWS_SUCCESS:
                    view.refreshDataSuccess(newList);

                case ContentUtil.REFRESH_NEWS_FAIL:
                    view.refreshDataFail();
            }
        }
    };

    // 上拉加载更多
    @Override
    public void pullNewsFromCloud() {
        AVQuery<AVObject> query = new AVQuery<>("WBNews");
        query.limit(NEWS_PAGE_COUNT);
        query.skip(pageNum * NEWS_PAGE_COUNT);
        query.orderByDescending("score");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size()<=0){
                    handler.sendEmptyMessage(ContentUtil.PULL_DATA_NON);
                    return;
                }
                for (AVObject object:list){
                    HotNewInfo info = new HotNewInfo();
                    info.setTitle(object.getString("title"));
                    info.setUrl(object.getString("url"));
                    info.setOfficial(object.getString("official"));
                    info.setScore(object.getDouble("score"));
                    info.setReadcount(object.getString("readCount"));
                    info.setLikecount(object.getString("likeCount"));
                    newList.add(info);
                }
                handler.sendEmptyMessage(ContentUtil.PULL_DATA_SUCCESS);
            }
        });
        pageNum++;

    }

    // 下拉刷新
    @Override
    public void refreshNewsFromCloud() {
        AVQuery<AVObject> query = new AVQuery<>("WBNews");
        query.limit(NEWS_PAGE_COUNT);
        query.skip(pageNum * NEWS_PAGE_COUNT);
        query.orderByDescending("score");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size()<=0){
                    handler.sendEmptyMessage(ContentUtil.REFRESH_NEWS_FAIL);
                    return;
                }
                for (AVObject object:list){
                    HotNewInfo info = new HotNewInfo();
                    info.setTitle(object.getString("title"));
                    info.setUrl(object.getString("url"));
                    info.setOfficial(object.getString("official"));
                    info.setScore(object.getDouble("score"));
                    info.setReadcount(object.getString("readCount"));
                    info.setLikecount(object.getString("likeCount"));
                    newList.add(info);
                }
                handler.sendEmptyMessage(ContentUtil.REFRESH_NEWS_SUCCESS);
            }
        });
    }
}

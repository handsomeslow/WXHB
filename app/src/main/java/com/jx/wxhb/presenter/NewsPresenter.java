package com.jx.wxhb.presenter;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.utils.ContentUtil;
import com.jx.wxhb.utils.HtmlDivUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/21.
 */

public class NewsPresenter implements NewsContract.Presenter {

    List<HotNewInfo> newList;

    NewsContract.View view;

    public NewsPresenter(NewsContract.View view) {
        this.view = view;
        newList = new ArrayList<>();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ContentUtil.MSG_REFRESH:
                    view.refreshListView(newList);
                    break;
            }
        }
    };

    @Override
    public void pullNewsFromCloud() {
        AVQuery<AVObject> query = new AVQuery<>("hot_wechat_news");
        query.limit(15);
        query.orderByDescending("updatedAt");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject object:list){
                    HotNewInfo info = new HotNewInfo();
                    info.setTitle(object.getString("title"));
                    info.setUrl(object.getString("url"));
                    info.setOfficial(object.getString("official"));
                    info.setScore(object.getDouble("score"));
                    info.setReadcount(object.getDouble("readcount"));
                    info.setLikecount(object.getDouble("likecount"));
                    newList.add(info);
                }
                handler.sendEmptyMessage(ContentUtil.MSG_REFRESH);
            }
        });
    }
}

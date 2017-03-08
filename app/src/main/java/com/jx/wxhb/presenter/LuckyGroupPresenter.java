package com.jx.wxhb.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.utils.CloudContentUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 徐俊 on 2017/3/5.
 */

public class LuckyGroupPresenter implements LuckyGroupContract.Presenter {
    List<LuckyGroupInfo> infoList;
    private LuckyGroupContract.View view;

    public LuckyGroupPresenter(LuckyGroupContract.View view) {
        this.view = view;
        infoList = new ArrayList<LuckyGroupInfo>();
    }

    @Override
    public void pullGroupList() {
        AVQuery<AVObject> groupQuery = new AVQuery<>(CloudContentUtil.LUCKY_GROUP_TABLE);
        groupQuery.include(CloudContentUtil.LUCKY_GROUP_OWNER);
        groupQuery.orderByDescending(CloudContentUtil.CREATE_DATE);
        groupQuery.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list == null || list.size()<=0){
                    return;
                }
                List<LuckyGroupInfo> infoList = new ArrayList<LuckyGroupInfo>();
                for (AVObject avObject:list){
                    LuckyGroupInfo info = new LuckyGroupInfo();
                    info.setId(avObject.getObjectId());
                    info.setContent(avObject.getString(CloudContentUtil.LUCKY_GROUP_CONTENT));
                    info.setOwer(avObject.getAVUser(CloudContentUtil.LUCKY_GROUP_OWNER));
                    info.setPublishDate(avObject.getCreatedAt());
                    info.setPhotoList(avObject.getList(CloudContentUtil.LUCKY_GROUP_PHOTOS));
                    infoList.add(info);
                }
                view.refreshListsuccess(infoList);
            }
        });
    }

    @Override
    public void publishGrouInfo() {

    }

    @Override
    public void addComment(final String content, final String id, final int position) {
        final AVObject group = AVObject.createWithoutData(CloudContentUtil.LUCKY_GROUP_TABLE, id);

        final AVObject comment = new AVObject(CloudContentUtil.LUCKY_GROUP_COMMENT_TABLE);
        comment.put(CloudContentUtil.COMMENT_CONTENT,content);
        comment.put(CloudContentUtil.COMMENT_OWNER, AVUser.getCurrentUser());
        comment.put(CloudContentUtil.LUCKY_GROUP_TABLE,group);
        AVObject.saveAllInBackground(Arrays.asList(comment), new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVRelation<AVObject> relation = group.getRelation(CloudContentUtil.COMMENT);
                relation.add(comment);
                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e !=null){
                            e.printStackTrace();
                            return;
                        }
                    }
                });
            }
        });
    }

    @Override
    public void refreshItemComment(String id, final int position) {
        AVObject groupTable = AVObject.createWithoutData(CloudContentUtil.LUCKY_GROUP_TABLE, id);
        AVRelation<AVObject> relation = groupTable.getRelation(CloudContentUtil.COMMENT);
        AVQuery<AVObject> query = relation.getQuery();
        // 关键代码，加上这一句返回对象，不加返回objectId
        query.orderByDescending(CloudContentUtil.CREATE_DATE);
        query.include(CloudContentUtil.COMMENT_OWNER);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                // list 是一个 AVObject 的 List，它包含所有当前 todoFolder 的 tags
                if (e != null) {
                    Log.d("jun", "refreshItemComment: " + e.getMessage());
                    return;
                }
                List<CommentInfo> comments = new ArrayList<CommentInfo>();
                for (AVObject object : list) {
                    CommentInfo info = new CommentInfo();
                    info.setContent(object.getString(CloudContentUtil.COMMENT_CONTENT));
                    try {
                        info.setName(object.getAVUser(CloudContentUtil.COMMENT_OWNER).getUsername());
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        info.setName("未注册用户");
                    }
                    info.setCreateTime(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(object.getCreatedAt()));
                    comments.add(info);
                }
                view.refreshComment(comments, position);
            }
        });
    }
}

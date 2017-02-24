package com.jx.wxhb.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.OfficialInfo;
import com.jx.wxhb.utils.CloudContentUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Desc
 * Created by Jun on 2017/2/24.
 */

public class OfficialPresenter implements OfficialContract.Presenter {
    OfficialContract.View view;

    public OfficialPresenter(OfficialContract.View view) {
        this.view = view;
    }

    @Override
    public void pullOfficialData(String id) {
        AVQuery<AVObject> officialQuery = new AVQuery<>(CloudContentUtil.PUSH_OFFICAIL);
        officialQuery.getInBackground(id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                Log.d("jun", "pullOfficialData: "+avObject.getString("name"));
                OfficialInfo info = new OfficialInfo();
                info.setName(avObject.getString("name"));
                info.setDesc(avObject.getString("desc"));
                view.showOfficialView(info);
            }
        });
    }

    @Override
    public void pullCommentData(String id) {
        AVObject todoFolder = AVObject.createWithoutData(CloudContentUtil.PUSH_OFFICAIL, id);
        AVRelation<AVObject> relation = todoFolder.getRelation("comments");
        AVQuery<AVObject> query = relation.getQuery();
        // 关键代码，加上这一句返回对象，不加返回objectId
        query.orderByDescending("createdAt");
        query.include("owner");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                // list 是一个 AVObject 的 List，它包含所有当前 todoFolder 的 tags
                if (e != null){
                    Log.d("jun", "pullCommentData: "+e.getMessage());
                    return;
                }
                List<CommentInfo> comments = new ArrayList<CommentInfo>();
                for (AVObject object:list){
                    CommentInfo info = new CommentInfo();
                    info.setContent(object.getString("content"));
                    try {
                        info.setName(object.getAVUser("owner").getUsername());
                    } catch (Exception exception){
                        exception.printStackTrace();
                        info.setName("未注册用户");
                    }
                    info.setCreateTime(new SimpleDateFormat("yyyy.mm.dd HH:MM").format(object.getCreatedAt()));
                    comments.add(info);
                }
                view.refreshCommentView(comments);
            }
        });
    }

    @Override
    public void addCommentData(String content, final String id) {
        final AVObject official = AVObject.createWithoutData(CloudContentUtil.PUSH_OFFICAIL, id);

        final AVObject comment = new AVObject("OfficialComment");
        comment.put("content",content);
        comment.put("owner",AVUser.getCurrentUser());
        comment.put("official",official);
//        official.getRelation("comments").add(comment);
//        official.saveInBackground();
//        comment.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if (e !=null){
//                    Log.d("jun", "addCommentData:"+e.getMessage());
//                    return;
//                }
//                pullCommentData(id);
//            }
//
//        });
        AVObject.saveAllInBackground(Arrays.asList(comment), new SaveCallback() {
            @Override
            public void done(AVException e) {
                AVRelation<AVObject> relation = official.getRelation("comments");
                relation.add(comment);
                official.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e !=null){
                            Log.d("jun", "addCommentData:"+e.getMessage());
                            return;
                        }
                        pullCommentData(id);
                    }
                });
            }
        });
    }
}

package com.jx.wxhb.presenter;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.jx.wxhb.model.CommentInfo;
import com.jx.wxhb.model.LuckyGroupInfo;
import com.jx.wxhb.utils.CloudContentUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

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
        groupQuery.include(CloudContentUtil.COMMENT);
        groupQuery.include(CloudContentUtil.COMMENT+"."+CloudContentUtil.COMMENT_OWNER);
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
//                    List<AVObject> commentList = avObject.getList(CloudContentUtil.COMMENT,AVObject.class);
//                    if (commentList!=null && commentList.size()>0){
//                        List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
//                        for (AVObject comment:commentList){
//                            CommentInfo commentInfo = new CommentInfo();
//                            commentInfo.setContent(comment.getString(CloudContentUtil.COMMENT_CONTENT));
//                            commentInfo.setName(comment.getAVUser(CloudContentUtil.COMMENT_OWNER).getUsername());
//                            commentInfo.setCreateTime(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(comment.getCreatedAt()));
//                            commentInfoList.add(commentInfo);
//                        }
//                        info.setCommentList(commentInfoList);
//                    }
                    infoList.add(info);
                    refreshItemComment(avObject.getObjectId(),infoList.size()-1);
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
        comment.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                group.addUnique(CloudContentUtil.COMMENT,comment);
                group.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e !=null){
                            e.printStackTrace();
                            return;
                        }
                        refreshItemComment(id,position);
                    }
                });
            }
        });
    }

    @Override
    public void refreshItemComment(String id, final int position) {
        AVObject groupTable = AVObject.createWithoutData(CloudContentUtil.LUCKY_GROUP_TABLE, id);
        groupTable.fetchIfNeededInBackground(CloudContentUtil.COMMENT, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                final List<AVObject> commentList = avObject.getList(CloudContentUtil.COMMENT,AVObject.class);
                if (commentList!=null && commentList.size()>0){
                    final List<CommentInfo> commentInfoList = new ArrayList<CommentInfo>();
                    for (final AVObject comment:commentList){
                        comment.getAVUser(CloudContentUtil.COMMENT_OWNER).fetchInBackground(new GetCallback<AVObject>() {
                            @Override
                            public void done(AVObject avUser, AVException e) {
                                CommentInfo commentInfo = new CommentInfo();
                                commentInfo.setContent(comment.getString(CloudContentUtil.COMMENT_CONTENT));
                                commentInfo.setName(avUser.getString("username"));
                                commentInfo.setCreateTime(new SimpleDateFormat("MM.dd HH:mm").format(comment.getCreatedAt()));
                                commentInfoList.add(commentInfo);
                                if (commentInfoList.size() == commentList.size()){
                                    view.refreshComment(commentInfoList, position);
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}

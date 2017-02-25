package com.jx.wxhb.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

/**
 * Created by 徐俊 on 2017/2/25.
 */

public class RandomFunnyPresenter implements RandomFunnyContract.Presenter {
    // private static final String[] ACTORS = {"actors_one","actors_two","actors_three","actors_four","actors_five"};
    RandomFunnyContract.View view;

    AVObject randomFunny;

    public RandomFunnyPresenter(RandomFunnyContract.View view) {
        this.view = view;
    }

    @Override
    public void addBetInfo(final int position,final int count) {
        if (randomFunny==null){
            return;
        }
        final AVObject randomFunnyNote = new AVObject("RandomFunnyNote");
        randomFunnyNote.put("count",count);
        randomFunnyNote.put("positopm",position);
        randomFunnyNote.put("user", AVUser.getCurrentUser());
        randomFunnyNote.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e!=null){
                    e.printStackTrace();
                    return;
                }
                AVRelation<AVObject> funnyRelation = randomFunny.getRelation("actors");
                funnyRelation.add(randomFunnyNote);
                randomFunny.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e!=null){
                            e.printStackTrace();
                            return;
                        }
                        Log.d("jun", "randomFunny.saveInBackground: sucess");
                        view.refreshNoteView(position,count);

                    }
                });
            }
        });

    }

    @Override
    public void pullRandomFunnyData() {
        AVQuery<AVObject> query = new AVQuery<>("RandomFunny");
        query.whereEqualTo("used",1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null){
                    e.printStackTrace();
                    return;
                }
                if (list != null && list.size()>0){
                    randomFunny = list.get(0);
                    Log.d("jun", "pullRandomFunnyData: sucess");
                }
            }
        });
    }

    @Override
    public void pullRandomFunnyHistoryData() {
        AVQuery<AVObject> query = new AVQuery<>("RandomFunny");
        query.limit(1);
        query.whereEqualTo("used",0);
        query.orderByDescending("createdAt");
        query.include("actors");
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e != null){
                    e.printStackTrace();
                    return;
                }
                if (list != null && list.size()>0){
                    AVObject hiostoryFunny = list.get(0);
                    String outCome = hiostoryFunny.getString("funnyname");
                    int funnynum = hiostoryFunny.getInt("funnynum");
                    String userName ="暂无信息";
                    AVRelation hiostoryFunnyNote = hiostoryFunny.getRelation("actors");
                    if (hiostoryFunnyNote != null){
                        AVQuery<AVObject> query1 = hiostoryFunnyNote.getQuery();
                        query1.include("user");
                        query1.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                AVUser user = list.get(0).getAVUser("user");
                                String userName = user.getUsername();
                                Log.d("jun", "pullRandomFunnyHistoryData: "+userName);
                            }
                        });
                        Log.d("jun", "pullRandomFunnyHistoryData: "+userName);
                    }
                    view.initHistoryFunnyView(outCome,userName);
                }
            }
        });
    }
}

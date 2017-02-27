package com.jx.wxhb.presenter;

import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVRelation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        Subscription subscription = Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(final Subscriber<? super AVObject> subscriber) {
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
                            subscriber.onNext(randomFunny);
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AVObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AVObject avObject) {
                        final AVObject randomFunnyNote = new AVObject("RandomFunnyNote");
                        randomFunnyNote.put("count",count);
                        randomFunnyNote.put("position",position);
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
                                        pullRandomFunnyData();
                                    }
                                });
                            }
                        });
                    }
                });


    }

    @Override
    public void pullRandomFunnyData() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<AVObject>() {
            @Override
            public void call(final Subscriber<? super AVObject> subscriber) {
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
                            subscriber.onNext(randomFunny);
                            subscriber.onCompleted();
                        }
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<AVObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(AVObject avObject) {
                        AVRelation<AVObject> funnyRelation = avObject.getRelation("actors");
                        AVQuery<AVObject> query = funnyRelation.getQuery();
                        query.include("user");
                        query.findInBackground(new FindCallback<AVObject>() {
                            @Override
                            public void done(List<AVObject> list, AVException e) {
                                if (e!=null){
                                    e.printStackTrace();
                                    return;
                                }
                                if (list!=null && list.size()>0){
                                    List<Integer> actorsCount = Arrays.asList(0,0,0,0,0);
                                    for (AVObject object : list){
                                        //参与信息
                                        int position = object.getInt("position");
                                        actorsCount.set(position,actorsCount.get(position)+1);
                                    }
                                    view.refreshActorsView(actorsCount);
                                }

                            }
                        });
                    }
                });
//        AVQuery<AVObject> query = new AVQuery<>("RandomFunny");
//        query.whereEqualTo("used",1);
//        query.findInBackground(new FindCallback<AVObject>() {
//            @Override
//            public void done(List<AVObject> list, AVException e) {
//                if (e != null){
//                    e.printStackTrace();
//                    return;
//                }
//                if (list != null && list.size()>0){
//                    randomFunny = list.get(0);
//                    Log.d("jun", "pullRandomFunnyData: sucess");
//                }
//            }
//        });
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
                                if (e != null){
                                    e.printStackTrace();
                                    return;
                                }
                                if (list!=null && list.size()>0){
                                    AVUser user = list.get(0).getAVUser("user");
                                    String userName = user.getUsername();
                                    Log.d("jun", "pullRandomFunnyHistoryData: "+userName);
                                }
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

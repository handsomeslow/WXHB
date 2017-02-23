package com.jx.wxhb.presenter;

import com.avos.avoscloud.AVObject;
import com.jx.wxhb.model.HotNewInfo;
import com.jx.wxhb.utils.ContentUtil;
import com.jx.wxhb.utils.HtmlDivUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Desc 已移至云端
 * Created by Jun on 2017/2/21.
 */

public class AdminPresenter implements AdminContract.Presenter {
    AdminContract.View view;

    public AdminPresenter(AdminContract.View view) {
        this.view = view;
    }

    @Override
    public void pushNewsToCloud() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    int count = 0;
                    // 以下字符串不可更改
                    Document document = Jsoup.connect(HtmlDivUtil.NEW_HTML_HOST).get();
                    //Elements elements = document.select("ant-table-tbody").select("tr");
                    Elements elements = document.select("ant-table-row.ant-table-row-level-0");
                    for (Element element : elements) {
                        Elements elementsTd = element.select("td");
                        AVObject newObject = new AVObject("hot_wechat_news");
                        newObject.put("title",elementsTd.get(0).text());
                        newObject.put("url",elementsTd.get(0).select("a.title-text").attr("href"));
                        newObject.put("official",elementsTd.get(1).text());
                        newObject.put("readcount",countToNumber(elementsTd.get(3).text()));
                        newObject.put("likecount",countToNumber(elementsTd.get(4).text()));
                        newObject.put("score",countToNumber(elementsTd.get(2).text()));
                        newObject.saveInBackground();
                        count++;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 网页字符转float
     * @return
     */
    private double countToNumber(String count){
        if (count.equals(HtmlDivUtil.MAX_COUNT)){
            return HtmlDivUtil.MAX_COUNT_FLOAT;
        }
        return Double.valueOf(count);
    }
}

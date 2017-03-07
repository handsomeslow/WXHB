package com.jx.wxhb.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.jx.wxhb.R;
import com.jx.wxhb.utils.ContentUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WebViewActivity extends BaseActivity {

    @Bind(R.id.web_view)
    WebView webView;

    String title;
    String url;

    public static Intent newIntent(Context context,String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context,WebViewActivity.class);
        intent.putExtra(ContentUtil.EXTRA_ARG_TITLE, title);
        intent.putExtra(ContentUtil.EXTRA_ARG_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view_layout);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(ContentUtil.EXTRA_ARG_URL) != null){
            url = getIntent().getStringExtra(ContentUtil.EXTRA_ARG_URL);
        }
        title = getIntent().getStringExtra(ContentUtil.EXTRA_ARG_TITLE) !=null ?
                getIntent().getStringExtra(ContentUtil.EXTRA_ARG_TITLE):"文章详情";
        setTitle(title);
        showBackButton();
        initView();
    }

    private void initView(){
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setBuiltInZoomControls(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100){
                    //Toast.makeText(WebViewActivity.this,"加载完成",Toast.LENGTH_SHORT).show();
                }
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}

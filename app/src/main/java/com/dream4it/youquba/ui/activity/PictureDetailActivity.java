package com.dream4it.youquba.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.dream4it.youquba.R;
import com.dream4it.youquba.data.PictureItemData;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;

/**
 * Created by meiming on 17-2-4.
 */

public class PictureDetailActivity extends BaseActivity {
    private PictureItemData mPictureItemData;

    @BindView(R.id.picture_detail_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.picture_detail_webview)
    WebView mWebView;

    @BindView(R.id.picture_detail_loading)
    AVLoadingIndicatorView mLoading;

    @BindView(R.id.picture_detail_progress)
    ProgressBar mProgressBar;
    @Override
    protected int initLayoutId() {
        return R.layout.activity_picture_detail;
    }

    @Override
    protected void initView() {
        initToolbar();
        initWebView();
    }

    @Override
    protected void initData() {
        mPictureItemData = getIntent().getParcelableExtra("picture_item_data");
    }

    private void initWebView() {
        final WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setBlockNetworkImage(true);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                mProgressBar.setProgress(newProgress);
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                }
            }

        });

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                //getContextView(view, url);
                super.onPageFinished(view, url);
                mLoading.hide();
                settings.setBlockNetworkImage(false);
            }
        });
        mWebView.loadUrl(mPictureItemData.getUrl());
    }

    private void getContextView(WebView view, String url) {
        String fun="javascript:function getClass(parent,sClass) { var aEle=parent.getElementsByTagName('div'); var aResult=[]; var i=0; for(i<0;i<aEle.length;i++) { if(aEle[i].className==sClass) { aResult.push(aEle[i]); } }; return aResult; } ";
        view.loadUrl(fun);
        String fun2="javascript:function hideOther() {getClass(document,'t100')[0].style.display='none'; " +
                "getClass(document,'nav')[0].style.display='none';" +
                "getClass(document,'seat')[0].style.display='none';" +
                "getClass(document,'l')[0].style.display='none'; " +
                "getClass(document,'hottu')[0].style.display='none';" +
                "getClass(document,'pl')[0].style.display='none';" +
                "getClass(document, 'r')[0].style.width='100%'}";
        view.loadUrl(fun2);
        view.loadUrl("javascript:hideOther();");
    }

    private void initToolbar() {
        String desc = mPictureItemData.getTitle();
        desc = desc.length() > 10 ? desc.substring(0, 10) + "..." : desc;
        mToolbar.setTitle(desc);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}

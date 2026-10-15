package com.sumoo.ajt124;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends Activity {
    private WebView webView;

    @Override
    @SuppressLint("SetJavaScriptEnabled") // Required by the bundled game; navigation is restricted to local assets below.
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView.setWebContentsDebuggingEnabled(false);

        webView = new WebView(this);
        webView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        WebSettings settings = webView.getSettings();
        // JavaScript is required for the bundled game. Do not load remote content in this WebView.
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        // The game is bundled locally. Keep access limited to its own asset files.
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setAllowUniversalAccessFromFileURLs(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            settings.setSafeBrowsingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient() {
            private boolean isAllowedLocalAssetUrl(String url) {
                return url != null && url.startsWith("file:///android_asset/");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return !isAllowedLocalAssetUrl(request.getUrl().toString());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return !isAllowedLocalAssetUrl(url);
            }
        });
        setContentView(webView);

        if (savedInstanceState == null) {
            webView.loadUrl("file:///android_asset/index.html");
        } else {
            webView.restoreState(savedInstanceState);
            if (webView.getUrl() == null) {
                webView.loadUrl("file:///android_asset/index.html");
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (webView != null) webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}

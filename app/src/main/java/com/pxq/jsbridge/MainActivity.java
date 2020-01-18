package com.pxq.jsbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

import com.pxq.jsbridge.core.JsBridge;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        JsBridge.bind(webView, new BridgeTest());
        setContentView(webView);
        webView.loadUrl("file:///android_asset/test.html");

    }
}

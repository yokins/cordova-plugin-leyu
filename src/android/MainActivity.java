package com.yokins;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebViewClient;
import android.widget.Toast;

import org.apache.cordova.*;

import aidl.com.leyu.LeyuService;

public class MainActivity extends CordovaActivity
{
    private CordovaWebView appView;
    private LeyuService leyuService;
    private boolean connected =false;
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            leyuService = LeyuService.Stub.asInterface(iBinder);
            if (leyuService != null) {
                connected = true;
            } else {
                connected = false;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setComponent(new
                ComponentName("com.android.settings", "com.android.settings.LeyuInterfaceService"));
        connected = this.bindService(intent , connection ,BIND_AUTO_CREATE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getBoolean("cdvStartInBackground", false)) {
            moveTaskToBack(true);
        }
        loadUrl(launchUrl);
        initView();
        attemptToBindService();
    }

    @Override
    public void onDestroy() {
        if(connection!=null) {
            unbindService(connection);
            connection=null;
        }
    }
    
    private void initView(){
        //mWebView = findViewById(R.id.web);
        // 得到设置属性的对象
        //WebSettings webSettings = mWebView.getSettings();
        WebSettings webSettings = appView.getSettings();
        // 使能JavaScript
        //webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptEnabled(true);
        // 载入页面：本地html资源文件
        //mWebView.loadUrl("file:///android_asset/sample.html");
        // WebViewClient 主要帮助WebView处理各种通知、请求事件的
        //mWebView.setWebViewClient(new WebViewClient());
        appView.setWebViewClient(new WebViewClient());
        // 传入一个Java对象和一个接口名,在JavaScript代码中用这个接口名代替这个对象,通过接口名调用Android接口的方法
        //mWebView.addJavascriptInterface(new MainActivity.MyJsInteration(this),"Android");
        appView.addJavascriptInterface(new MyJsInteration(this),"Android");
    }

    public  class  MyJsInteration {

        private Context mContext;
        MyJsInteration(Context context){
            mContext = context;
        }
        @JavascriptInterface //一定要写，不然h5调不到这个方法
        public void getTpEnable(){
            getEnable();
        }

        @JavascriptInterface //一定要写，不然h5调不到这个方法
        public void setTpEnable(boolean enable){
            setEnable(enable);
        }

        private void setEnable(boolean enable){
            try {
                leyuService.setTpEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        private void getEnable(){
            try {
                boolean enable = leyuService.getTpEnable();
                if(enable){
                    //Toast.makeText(mContext, "当前状态:禁止手指", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "enable finger");
                }else {
                    //Toast.makeText(mContext, "当前状态:允许手指", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "disable finger");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
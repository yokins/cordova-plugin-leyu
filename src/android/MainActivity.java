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
import android.webkit.WebView;
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
            Log.i(TAG, "yokins-log connected:" + connected);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
            Log.i(TAG, "yokins-log onServiceDisconnected:");
        }
    };

    private void attemptToBindService() {
        Intent intent = new Intent();
        intent.setComponent(new
                ComponentName("com.android.settings", "com.android.settings.LeyuInterfaceService"));
        connected = this.bindService(intent , connection ,BIND_AUTO_CREATE);
        Log.i(TAG, "yokins-log attemptToBindService:");
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
        Log.i(TAG, "yokins-log onCreate:");
    }

    @Override
    public void onDestroy() {
        if(connection!=null) {
            unbindService(connection);
            connection=null;
        }
    }
    
    private void initView(){
        WebView webView = (WebView) appView.getView();
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new MyJsInteration(this),"Android");
        Log.i(TAG, "yokins-log initView:");
    }

    public  class  MyJsInteration {

        private Context mContext;
        MyJsInteration(Context context){
            mContext = context;
        }

        @JavascriptInterface //一定要写，不然h5调不到这个方法
        public void getTpEnable(){
            getEnable();
            Log.i(TAG, "yokins-log getTpEnable:");
        }

        @JavascriptInterface //一定要写，不然h5调不到这个方法
        public void setTpEnable(boolean enable){
            setEnable(enable);
            Log.i(TAG, "yokins-log setTpEnable:");
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
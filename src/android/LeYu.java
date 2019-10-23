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

/**
 * This class echoes a string called from JavaScript.
 */
public class LeYu extends CordovaPlugin implements CordovaActivity {
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
        attemptToBindService();
    }

    @Override
    public void onDestroy() {
        if(connection!=null) {
            unbindService(connection);
            connection=null;
        }
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getTpEnable")) {
            this.getTpEnable(callbackContext);
            return true;
        }
        if (action.equals("setTpEnable")) {
            JSONObject jsonObject = args.getJSONObject(0);
            boolean enable = jsonObject.getBoolean("enable");
            this.setTpEnable(enable, callbackContext);
            return true;
        }
        return false;
    }

    public void getTpEnable(CallbackContext callbackContext){
        try {
            boolean enable = leyuService.getTpEnable();
            callbackContext.success("enable");
        } catch (RemoteException e) {
            callbackContext.error("获取乐愚状态失败！");
            e.printStackTrace();
        }
    }

    public void setTpEnable(boolean enable, CallbackContext callbackContext){
        try {
            leyuService.setTpEnable(enable);
            callbackContext.success();
        } catch (RemoteException e) {
            callbackContext.error("设置乐愚状态失败！");
            e.printStackTrace();
        }
    }
}

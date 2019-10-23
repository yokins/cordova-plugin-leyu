package com.yokins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import aidl.com.leyu.LeyuService;

/**
 * This class echoes a string called from JavaScript.
 */
public class LeYu extends CordovaPlugin {
    private static Context context;
    private LeyuService leyuService;
    private boolean connected = false;
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            leyuService = LeyuService.Stub.asInterface(iBinder); 
            if(leyuService!=null) {
                connected = true; 
            } else {
                connected =false; 
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
        connected = context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("openTp")) {
            this.attemptToBindService();
            return true;
        }
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
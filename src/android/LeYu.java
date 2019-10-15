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

    private void attemptToBindService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new
                ComponentName("com.android.settings", "com.android.settings.LeyuInterfaceService"));
        connected = context.bindService(intent, connection, context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        attemptToBindService();
    }

    @Override
    protected void onDestroy() {
        if(connection!=null) {
            unbindService(connection);
            connection=null;
        }
        super.onDestroy();
    }


    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        // if (action.equals("coolMethod")) {
        //     String message = args.getString(0);
        //     this.coolMethod(message, callbackContext);
        //     return true;
        // }
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

    // private void coolMethod(String message, CallbackContext callbackContext) {
    //     if (message != null && message.length() > 0) {
    //         callbackContext.success(message);
    //     } else {
    //         callbackContext.error("Expected one non-empty string argument.");
    //     }
    // }

    public void getTpEnable(CallbackContext callbackContext){
        try {
            boolean enable = leyuService.getTpEnable();
            callbackContext.success("获取乐愚状态成功！", enable);
        } catch (RemoteException e) {
            callbackContext.error("获取乐愚状态失败！");
            e.printStackTrace();
        }
    }

    public void setTpEnable(boolean enable, CallbackContext callbackContext){
        try {
            leyuService.setTpEnable(enable);
            callbackContext.success("设置乐愚状态成功！", enable);
        } catch (RemoteException e) {
            callbackContext.error("设置乐愚状态失败！");
            e.printStackTrace();
        }
    }



    // @Override
    // public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    //     if (action.equals("coolMethod")) {
    //         String message = args.getString(0);
    //         this.coolMethod(message, callbackContext);
    //         return true;
    //     }
    //     if (action.equals("getTpEnable")) {
    //         this.getTpEnable();
    //         return true;
    //     }
    //     if (action.equals("setTpEnable")) {
    //         this.setTpEnable(true);
    //         return true;
    //     }
    //     return false;
    // }

    // private LeyuService leyuService;
    // private boolean connected = false;
    // private ServiceConnection connection = new ServiceConnection() {

    //     @Override
    //     public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
    //         leyuService = LeyuService.Stub.asInterface(iBinder); 
    //         if(leyuService!=null) {
    //             connected = true; 
    //         } else {
    //             connected =false; 
    //         }
    //     }

    //     @Override
    //     public void onServiceDisconnected(ComponentName componentName) {
    //         connected = false; 
    //     }
    // };

    // private void attemptToBindService(Context context) {
    //     Intent intent = new Intent();
    //     intent.setComponent(new
    //             ComponentName("com.android.settings", "com.android.settings.LeyuInterfaceService"));
    //     connected = context.bindService(intent, connection, context.BIND_AUTO_CREATE);
    // }

    // private void coolMethod(String message, CallbackContext callbackContext) {
    //     if (message != null && message.length() > 0) {
    //         callbackContext.success(message);
    //     } else {
    //         callbackContext.error("Expected one non-empty string argument.");
    //     }
    // }

    // private void setTpEnable(boolean enable) {
    //     try {
    //         leyuService.setTpEnable(enable);
    //     } catch (RemoteException e) {
    //         e.printStackTrace();
    //     }
    // }

    // private boolean getTpEnable() {
    //     boolean enable = false;
    //     try {
    //         enable = leyuService.getTpEnable();
    //     } catch (RemoteException e) {
    //         e.printStackTrace();
    //     }
    //     return enable;
    // }
}

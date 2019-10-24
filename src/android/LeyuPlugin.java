package com.yokins;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;
import android.os.IBinder;
import android.os.RemoteException;
import android.content.ServiceConnection;
import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import aidl.com.leyu.LeyuService;

/**
 * This class echoes a string called from JavaScript.
 */
public class LeyuPlugin extends CordovaPlugin {

    private final String TAG = "LeyuPlugin";
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
            Log.i(TAG, "connected:"+connected);//lishunbo add
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            connected = false;
        }
    };

    private void attemptToBindService() {
        Log.i(TAG, "attemptToBindService:");//lishunbo add
        Intent intent = new Intent();
        intent.setComponent(new
                ComponentName("com.android.settings", "com.android.settings.LeyuInterfaceService"));
        connected = cordova.getActivity().bindService(intent , connection ,cordova.getActivity().BIND_AUTO_CREATE);
    }

    private void setTpEnable(boolean enable){
        Log.i(TAG, "setTpEnable:"+enable);//lishunbo add
        try {
            leyuService.setTpEnable(enable);
        } catch (RemoteException e) {
            Log.e(TAG, "setTpEnable-error:"+e);//lishunbo add
            e.printStackTrace();
        }
    }
    private void getTpEnable(){
        Log.i(TAG, "getTpEnable:");//lishunbo add
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
            Log.e(TAG, "getTpEnable-error:"+e);//lishunbo add
            e.printStackTrace();
        }
    }

    private void unbindService() {
        Log.i(TAG, "unbindService:");//lishunbo add
        cordova.getActivity().unbindService(connection);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        Log.i(TAG, "execute:"+action);//lishunbo add
        if("attemptToBindService".equals(action)){
            //String content = args.getString(0);
            attemptToBindService();
            callbackContext.success("finish");//如果不调用success回调，则js中successCallback不会执行
            return true;
        } else if ("unbindService".equals(action)) {
            unbindService();
            callbackContext.success("finish");//如果不调用success回调，则js中successCallback不会执行
            return true;
        } else if ("getTpEnable".equals(action)) {
            getTpEnable();
            callbackContext.success("finish");//如果不调用success回调，则js中successCallback不会执行
            return true;
        } else if ("setTpEnable".equals(action)) {
            boolean enabled = args.getBoolean(0);
            setTpEnable(enabled);
            callbackContext.success("finish");//如果不调用success回调，则js中successCallback不会执行
            return true;
        }
        return false;
    }

}
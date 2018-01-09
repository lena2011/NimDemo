package zaoren.demo.com.base.http;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.HashMap;
import java.util.Map;

import zaoren.demo.com.DemoCache;

import static zaoren.demo.com.constant.Constant.HEADER_CONTENT_TYPE;
import static zaoren.demo.com.constant.Constant.HEADER_KEY_APP_KEY;
import static zaoren.demo.com.constant.Constant.HEADER_USER_AGENT;

/**
 * Created by Administrator on 2018/1/8.
 */

public class BaseHttpClient {
    // result
    protected static final String RESULT_KEY_RES = "res";
    protected static final String RESULT_KEY_ERROR_MSG = "errmsg";
    protected static  final  String RESULT_KEY_MSG="msg";
    protected static final String RESULT_KEY_ROOM_ID = "roomid";
    protected static final String RESULT_KEY_AVTYPE = "avType";
    protected static final String RESULT_KEY_ORIENTATION = "orientation";
    protected static final String RESULT_KEY_LIVE = "live";
    protected static final String RESULT_KEY_PUSH_URL = "pushUrl";
    protected static final String RESULT_KEY_PULL_URL = "rtmpPullUrl";
    // code
    protected static final int RESULT_CODE_SUCCESS = 200;

    // request register
    protected static final String REQUEST_USER_NAME = "username";
    protected static final String REQUEST_NICK_NAME = "nickname";
    protected static final String REQUEST_PASSWORD = "password";


    public interface HttpCallback<T> {
        void onSuccess(T t);

        void onFailed(int code, String errorMsg);
    }

//    private  static  BaseHttpClient instance;
//
//    public static synchronized BaseHttpClient getInstance() {
//        if (instance==null){
//            instance=new BaseHttpClient();
//        }
//        return instance;
//    }
//
//    public BaseHttpClient() {
//        NimHttpClient.getInstance().init(DemoCache.getContext());
//    }

    protected  Map<String,String>  getHeaders(){
        Map<String, String> headers = new HashMap<>(1);
        String appKey = readAppKey();
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
        headers.put(HEADER_USER_AGENT, "nim_demo_android");
        headers.put(HEADER_KEY_APP_KEY, appKey);
        return  headers;
    }

    private String readAppKey() {
        try {
            ApplicationInfo appInfo = DemoCache.getContext().getPackageManager()
                    .getApplicationInfo(DemoCache.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

package zaoren.demo.com.im.http;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import zaoren.demo.com.DemoCache;
import zaoren.demo.com.base.http.BaseHttpClient;
import zaoren.demo.com.base.http.NimHttpClient;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.base.util.log.LogUtil;
import zaoren.demo.com.base.util.string.MD5;
import zaoren.demo.com.constant.BasicApi;

/**
 * Created by Administrator on 2017/12/27.
 */

public class RegisterHttpClient extends BaseHttpClient {
    private static final String TAG = "RegisterHttpClient";


    private RegisterHttpClient() {
        NimHttpClient.getInstance().init(DemoCache.getContext());
    }

    private static RegisterHttpClient instance;

    public static synchronized RegisterHttpClient getInstance() {
        if (instance == null) {
            instance = new RegisterHttpClient();
        }
        return instance;
    }


    /**
     * 向应用服务器创建账号（注册账号）
     * 由应用服务器调用WEB SDK接口将新注册的用户数据同步到云信服务器
     */
    public void register(String account, String nickName, String password, final HttpCallback<Void> callback) {
        String url = BasicApi.API_REGISTER_NAME;
        password = MD5.getStringMD5(password);
        try {
            nickName = URLEncoder.encode(nickName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_USER_NAME).append("=").append(account.toLowerCase()).append("&")
                .append(REQUEST_NICK_NAME).append("=").append(nickName).append("&")
                .append(REQUEST_PASSWORD).append("=").append(password);
        String bodyString = body.toString();

        DebugLog.i("body=" + bodyString);
        for (Map.Entry<String, String> entry : getHeaders().entrySet()) {

           DebugLog.i("Key = " + entry.getKey() + ", Value = " + entry.getValue());
        }
        NimHttpClient.getInstance().execute(url, getHeaders(), bodyString, new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, String errorMsg) {
                if (code != 200 || errorMsg != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (errorMsg != null ? errorMsg : "null"));
                    if (callback != null) {
                        callback.onFailed(code, errorMsg != null ? errorMsg : "null");
                    }
                    return;
                }

                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_RES);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        callback.onSuccess(null);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                }

            }
        });
    }

}

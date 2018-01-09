package zaoren.demo.com.im.config;

import android.content.Context;
import android.content.SharedPreferences;

import zaoren.demo.com.DemoCache;

/**
 * Created by Administrator on 2018/1/9.
 */

public class AuthPreferences {
    private static final String KEY_USER_ACCOUNT = "account";
    private static final String KEY_USER_TOKEN = "token";

    public static void saveUserAccount(String account) {
        saveString(KEY_USER_ACCOUNT, account);
    }

    public static void saveUserToken(String token) {
        saveString(KEY_USER_TOKEN, token);
    }

    public static String getUserAccount() {
        return getString(KEY_USER_ACCOUNT);
    }

    public static String getUserToken() {
        return getString(KEY_USER_TOKEN);
    }

    private static void saveString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        editor.commit();
    }

    private static String getString(String key) {
        return getSharedPreferences().getString(key, null);
    }

    static SharedPreferences getSharedPreferences() {
        return DemoCache.getContext().getSharedPreferences("demo", Context.MODE_PRIVATE);
    }
}

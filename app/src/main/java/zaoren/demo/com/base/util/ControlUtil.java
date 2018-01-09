package zaoren.demo.com.base.util;

import android.content.Context;
import android.content.Intent;

import zaoren.demo.com.MainActivity;
import zaoren.demo.com.entertainment.activity.LiveActivity;
import zaoren.demo.com.im.LoginActivity;
import zaoren.demo.com.im.RegisterActivity;

/**
 * Created by Administrator on 2018/1/9.
 */

public class ControlUtil {

    public static void go2Login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void go2Live(Context context) {
        Intent intent = new Intent(context, LiveActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void go2Mian(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void go2Register(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}

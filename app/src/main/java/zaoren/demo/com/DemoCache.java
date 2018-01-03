package zaoren.demo.com;

import android.content.Context;

/**
 * Created by Administrator on 2017/12/27.
 */

public class DemoCache {
private static Context mContext;

    public static Context getContext() {
        return mContext;
    }

    public static void setContext(Context context) {
        mContext = context.getApplicationContext();
    }
}

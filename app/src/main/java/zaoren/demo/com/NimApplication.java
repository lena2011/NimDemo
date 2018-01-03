package zaoren.demo.com;

import android.app.Application;

import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.util.NIMUtil;

/**
 * Created by Administrator on 2017/12/27.
 */

public class NimApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DemoCache.setContext(this);
        NIMClient.init(this,loginInfo(),null);
        if (NIMUtil.isMainProcess(this)) {

            initUIKit();
        }

    }

    private void initUIKit() {
        NimUIKit.init(this);
    }

    private LoginInfo loginInfo(){
        return null;
    }
}

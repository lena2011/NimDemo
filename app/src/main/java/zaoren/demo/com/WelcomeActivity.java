package zaoren.demo.com;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

import zaoren.demo.com.base.BaseActivity;
import zaoren.demo.com.base.util.ControlUtil;
import zaoren.demo.com.base.util.StringUtil;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.im.config.AuthPreferences;

/**
 * Created by Administrator on 2018/1/9.
 */

public class WelcomeActivity extends BaseActivity {
    private boolean firstEnter = true;

    private boolean customSplash = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_welcome);
        DebugLog.i("firstEnter="+firstEnter+"customSplash="+customSplash);
        if (!firstEnter) {
            onIntent();
        } else {
            showSplashView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DebugLog.i("onResume");
        if (firstEnter) {
            firstEnter = false;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (isAutoLogin()) {
                        onIntent();
                    } else {
                        ControlUtil.go2Login(WelcomeActivity.this);
                        finish();
                    }
                }
            };
            DebugLog.i("firstEnter="+firstEnter+"customSplash="+customSplash);
            if (customSplash) {
                new Handler().postDelayed(runnable, 1000);
            } else {
                runnable.run();
            }

        }
    }

    private void onIntent() {
        ControlUtil.go2Mian(WelcomeActivity.this);
    }

    private void showSplashView() {
        getWindow().setBackgroundDrawableResource(R.color.gray);
        customSplash = true;
    }

    private boolean isAutoLogin() {
        String account = AuthPreferences.getUserAccount();
        String token = AuthPreferences.getUserToken();
        return !StringUtil.isEmpty(account) && !StringUtil.isEmpty(token);
    }
}

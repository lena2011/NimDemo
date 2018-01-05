package zaoren.demo.com;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2018/1/5.
 */

public class BaseActivity extends AppCompatActivity {
    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected final Handler getHandler() {
        if (mHandler == null)
            mHandler = new Handler(getMainLooper());
        return mHandler;
    }
}

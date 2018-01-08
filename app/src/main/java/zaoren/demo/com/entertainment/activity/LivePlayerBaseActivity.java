package zaoren.demo.com.entertainment.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import zaoren.demo.com.base.BaseActivity;
import zaoren.demo.com.permission.MPermission;

/**
 * Created by Administrator on 2018/1/4.
 */

public abstract class LivePlayerBaseActivity extends BaseActivity {
    protected  final  int LIVE_PERMISSION_REQUEST_CODE=100;
    protected abstract int getActvityLayout();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActvityLayout());
        ButterKnife.bind(this);
    }

    protected static final String[] LIVE_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_PHONE_STATE
    };

    protected void requestPermissions() {
        MPermission.with(this).addRequestCode(LIVE_PERMISSION_REQUEST_CODE).permissions(LIVE_PERMISSIONS).request();
    }


}

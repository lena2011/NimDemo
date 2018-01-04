package zaoren.demo.com.entertainment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import zaoren.demo.com.R;
import zaoren.demo.com.permission.MPermission;

/**
 * Created by Administrator on 2017/12/28.
 */

public class LiveActivity extends LivePlayerBaseActivity {

    @Override
    protected int getActvityLayout() {
        return R.layout.act_live;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}

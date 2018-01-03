package zaoren.demo.com.entertainment;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import zaoren.demo.com.R;

/**
 * Created by Administrator on 2017/12/28.
 */

public class LiveActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_live);
    }
}

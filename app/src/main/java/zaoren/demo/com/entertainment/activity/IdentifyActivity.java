package zaoren.demo.com.entertainment.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zaoren.demo.com.R;

/**
 * Created by Administrator on 2017/12/27.
 */

public class IdentifyActivity extends AppCompatActivity {
    @BindView(R.id.master_btn)
    Button mMasterBtn;
    @BindView(R.id.audience_btn)
    Button mAudienceBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.act_identify);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.master_btn)
    void  master(){

    }
}

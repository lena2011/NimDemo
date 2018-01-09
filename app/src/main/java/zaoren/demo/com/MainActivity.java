package zaoren.demo.com;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import zaoren.demo.com.base.util.ControlUtil;
import zaoren.demo.com.entertainment.activity.LiveActivity;
import zaoren.demo.com.im.LoginActivity;
import zaoren.demo.com.im.RegisterActivity;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.register)
    TextView mRegister;
    @BindView(R.id.live)
    TextView mLive;
    @BindView(R.id.audio)
    TextView mAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.this.startActivity(intent);
            }
        });

        mLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ControlUtil.go2Live(MainActivity.this);
            }
        });
    }
}

package zaoren.demo.com.im;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import zaoren.demo.com.R;
import zaoren.demo.com.base.http.BaseHttpClient;
import zaoren.demo.com.im.http.RegisterHttpClient;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.username)
    EditText mUsername;
    @BindView(R.id.nickname)
    EditText mNickname;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.register)
    Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(mUsername.getText().toString(), mNickname.getText().toString(), mPassword.getText().toString());

            }
        });
    }

    private void register(String username, String nickname, String password) {
        RegisterHttpClient.getInstance().register(username, nickname, password, new BaseHttpClient.HttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                switchMode();  // 切换回登录
//                loginAccountEdit.setText(account);
//                loginPasswordEdit.setText(password);


//                DialogMaker.dismissProgressDialog();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(RegisterActivity.this, getString(R.string.register_failed, String.valueOf(code), errorMsg), Toast.LENGTH_SHORT)
                        .show();

            }
        });
    }

}

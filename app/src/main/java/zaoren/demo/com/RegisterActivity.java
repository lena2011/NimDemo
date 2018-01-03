package zaoren.demo.com;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nim.uikit.common.activity.UI;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;

import zaoren.demo.com.contact.ContactHttpClient;

public class RegisterActivity extends UI {
    private EditText mUsername;
    private EditText mNickname;
    private EditText mPassword;
    private Button mRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mUsername=findView(R.id.username);
        mNickname=findView(R.id.nickname);
        mPassword=findView(R.id.password);
        mRegister=findView(R.id.register);

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(mUsername.getText().toString(),mNickname.getText().toString(),mPassword.getText().toString());

            }
        });
    }

    private void register(String username, String nickname, String password) {
        ContactHttpClient.getInstance().register(username, nickname, password, new ContactHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
//                switchMode();  // 切换回登录
//                loginAccountEdit.setText(account);
//                loginPasswordEdit.setText(password);


//                DialogMaker.dismissProgressDialog();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(RegisterActivity.this, getString(R.string.register_failed, String.valueOf(code), errorMsg), Toast.LENGTH_SHORT)
                        .show();

                DialogMaker.dismissProgressDialog();
            }
        });
    }

}

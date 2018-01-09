package zaoren.demo.com.im;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zaoren.demo.com.DemoCache;
import zaoren.demo.com.R;
import zaoren.demo.com.base.util.ControlUtil;
import zaoren.demo.com.base.util.log.DebugLog;
import zaoren.demo.com.base.util.string.MD5;
import zaoren.demo.com.im.config.AuthPreferences;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.sign)
    Button mSign;
    @BindView(R.id.register)
    TextView mRegister;
    private AbortableFuture<LoginInfo> loginRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

    }


    @OnClick(R.id.sign)
    void sign() {
        attemptLogin();
    }

    @OnClick(R.id.register)
    void register() {
        ControlUtil.go2Register(this);

    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Store values at the time of the login attempt.
        String email = mAccount.getText().toString();
        final String password = mPassword.getText().toString();

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        final String account = email;
        final String token = tokenFromPassword(password);
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                //todo 登录成功
                DebugLog.i(param.getAccount());
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);
                ControlUtil.go2Mian(LoginActivity.this);
                finish();

            }

            @Override
            public void onFailed(int code) {
                if (code == 302 || code == 404) {
                    Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败" + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {

            }
        });
    }

    /**
     * 保存用户信息
     *
     * @param account
     * @param token
     */
    private void saveLoginInfo(String account, String token) {
        AuthPreferences.saveUserAccount(account);
        AuthPreferences.saveUserToken(token);
    }


    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(this);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey)
                || "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }


    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


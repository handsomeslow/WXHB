package com.jx.wxhb.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.jx.wxhb.R;
import com.jx.wxhb.activity.BaseActivity;
import com.jx.wxhb.model.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.login_progress)
    ProgressBar loginProgress;
    @Bind(R.id.phone_view_text)
    EditText phoneViewText;
    @Bind(R.id.password)
    EditText password;
    @Bind(R.id.sign_in_button)
    Button signInButton;
    @Bind(R.id.get_msg_code)
    Button getMsgCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_layout);
        ButterKnife.bind(this);

        signInButton.setOnClickListener(this);
        getMsgCodeButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                AVUser.signUpOrLoginByMobilePhoneInBackground(phoneViewText.getText().toString(),
                        password.getText().toString(), new LogInCallback<AVUser>() {
                            @Override
                            public void done(AVUser avUser, AVException e) {
                                if (null == e){
                                    loginSuccess(avUser);
                                    return;
                                }
                                Log.d("jun", "get code: "+e.getMessage());
                                loginFail(e.getCode());
                            }
                        });
                break;

            case R.id.get_msg_code:
                AVOSCloud.requestSMSCodeInBackground(phoneViewText.getText().toString(), new RequestMobileCodeCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e){
                            Toast.makeText(LoginActivity.this,"验证码发生成功，请及时输入！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.d("jun", "get code: "+e.getMessage());
                        Toast.makeText(LoginActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                break;


        }
    }

    private void loginSuccess(final AVUser avUser){
        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
        finish();
//        Realm realm = Realm.getDefaultInstance();
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                UserInfo userInfo = realm.createObject(UserInfo.class);
//                userInfo.setId(avUser.getObjectId());
//                userInfo.setName(avUser.getUsername());
//                userInfo.setEmail(avUser.getEmail());
//                userInfo.setPhone(avUser.getMobilePhoneNumber());
//                userInfo.setAvatar(avUser.getObjectId());
//                userInfo.setPhoneVerifiy(avUser.isMobilePhoneVerified());
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
//                finish();
//            }
//        }, new Realm.Transaction.OnError() {
//            @Override
//            public void onError(Throwable error) {
//                Toast.makeText(LoginActivity.this, "登录失败，请重试！", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

    private void loginFail(int code){
        String message = null;
        switch (code){
            case 603:
                message = "无效的短信验证码";
                break;
            case 210:
                message = "密码错误！";
                break;
            case 601:
                message = "验证码条数已达到上限！";
                break;
        }
        Toast.makeText(LoginActivity.this,"登录失败，"+ message,Toast.LENGTH_SHORT).show();
    }
}


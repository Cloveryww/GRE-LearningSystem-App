package com.example.work;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import android.content.Intent;
import android.os.Message;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.os.Handler;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = "LoginActivity";
    private String UserID="";
    private LinearLayout mMyLogin;
    private EditText mUserET;
    private EditText mPassWordET;
    private Button mRegisterBtn;
    private Button mLoginBtn;
    private ProgressBar mLoadingPB;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 200){//登陆成功，转到MainActivity
                UserID=msg.obj.toString();
                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                intent.putExtra("UserID",UserID);
                toast("登陆成功");
                startActivity(intent);
                finish();
            }
            else if(msg.what==201)//登录失败，密码错误
            {
                mPassWordET.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("密码错误，请输入正确的密码！");
            }else if(msg.what==202)//登录失败，账号不存在
            {
                mUserET.setText("");
                mPassWordET.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("不存在此用户名，请输入正确的用户名或重新注册用户！");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();

    }
    private void initView(){
        mMyLogin=(LinearLayout) findViewById(R.id.Mylogin);
        this.setAlphaAnimation(mMyLogin);
        mUserET=(EditText) findViewById(R.id.user_edit);
        mPassWordET=(EditText) findViewById(R.id.password_edit);
        mRegisterBtn=(Button)findViewById(R.id.register_btn);
        mLoginBtn=(Button)findViewById(R.id.login_btn);
        mLoadingPB=(ProgressBar)findViewById(R.id.login_pb);
        return;
    }
    private void setAlphaAnimation(View v) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(5000);
        v.startAnimation(aa);
    }
    private void initListener() {
        this.mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterRegisterActivity();
            }
        });
        this.mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingPB.setVisibility(View.VISIBLE);
                beginLogin();
            }
        });
    }
    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void enterRegisterActivity(){
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }
    private  void beginLogin(){
        if(mUserET.getText().toString().equals("")||mPassWordET.getText().toString().equals(""))
        {
            mLoadingPB.setVisibility(View.GONE);
            toast("用户名和密码不能为空，请输入用户名和密码！");
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String result=new Util().login(mUserET.getText().toString(),mPassWordET.getText().toString());
                    Message msg=Message.obtain();
                    if(result.equals("0"))//密码错误，重新输入
                    {
                        msg.what=201;
                    }
                    else if(result.equals("-1")) //账号错误，重新输入
                    {
                        msg.what=202;
                    }
                    else//登陆成功，跳转到首页
                    {
                        msg.what = 200;
                        msg.obj = result;//返回userid
                    }
                    handler.sendMessage(msg);

                }
            }).start();
        }
    }

}

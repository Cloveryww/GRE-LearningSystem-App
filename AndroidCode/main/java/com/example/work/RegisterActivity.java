package com.example.work;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class RegisterActivity extends AppCompatActivity {

    private static String TAG = "RegisterActivity";
    private LinearLayout mMyRegister;
    private EditText mUserET;
    private EditText mPassWordET1;
    private EditText mPassWordET2;
    private Button mSubmitBtn;
    private ProgressBar mLoadingPB;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 200){//注册成功，转到LoginActivity
                toast("注册成功");
                finish();
            }
            else if(msg.what==201)
            {
                mUserET.setText("");
                mPassWordET1.setText("");
                mPassWordET2.setText("");
                mLoadingPB.setVisibility(View.GONE);
                toast("该用户名已经被注册过了，请更换用户名重新注册！");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        initListener();
    }
    private void initView(){
        mMyRegister=(LinearLayout) findViewById(R.id.MyRegister);
        this.setAlphaAnimation(mMyRegister);
        mUserET=(EditText) findViewById(R.id.R_user);
        mPassWordET1=(EditText) findViewById(R.id.R_password_input1);
        mPassWordET2=(EditText) findViewById(R.id.R_password_input2);
        mSubmitBtn=(Button)findViewById(R.id.R_submitbtn);
        mLoadingPB=(ProgressBar)findViewById(R.id.R_register_pb);

    }
    private void setAlphaAnimation(View v) {
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(5000);
        v.startAnimation(aa);
    }
    private void initListener() {
        this.mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingPB.setVisibility(View.VISIBLE);
                beginRegister();
            }
        });
    }
    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private void enterRegisterActivity(){

    }
    private  void beginRegister(){
        if(mUserET.getText().toString().equals("")||mPassWordET1.getText().toString().equals("")||mPassWordET2.getText().toString().equals(""))
        {
            mLoadingPB.setVisibility(View.GONE);
            toast("用户名和密码不能为空，请输入用户名和密码！");
        }
        else if(!mPassWordET1.getText().toString().equals(mPassWordET2.getText().toString()))
        {
            mLoadingPB.setVisibility(View.GONE);
            mPassWordET1.setText("");
            mPassWordET2.setText("");
            toast("两次输入的密码不一致，请重新输入！");
        }
        else
        {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Message msg=Message.obtain();
                    if(new Util().register(mUserET.getText().toString(),mPassWordET1.getText().toString()))//注册成功，跳转到登陆界面
                    {
                        msg.what = 200;
                    }
                    else//注册失败，用户名已经被注册过了，重新注册
                    {
                        msg.what=201;
                    }
                    handler.sendMessage(msg);

                }
            }).start();
        }
    }
}

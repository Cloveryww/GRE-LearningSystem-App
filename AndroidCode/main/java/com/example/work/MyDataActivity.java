package com.example.work;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class MyDataActivity extends AppCompatActivity {

    private ImageView back_btn;
    private TextView userId_tv;
    private Button change_password_btn;
    private EditText et_passwordold;
    private EditText et_passwordnew;
    private String UserId;
    private String UserAccount;
    JSONObject resultJSONObject=null;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    userId_tv.setText(UserAccount);
                    Toast.makeText(MyDataActivity.this,"个人信息加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(MyDataActivity.this, "网络错误，个人信息加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(MyDataActivity.this,"密码修改成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(MyDataActivity.this, "网络错误，密码修改失败", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(MyDataActivity.this,"网络错误，操作失败",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data);
        UserId=getIntent().getStringExtra("UserId");
        initView();
        initViewContent();
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        change_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                if(et_passwordold.getText().toString().equals("")||et_passwordnew.getText().toString().equals(""))
                {
                    Toast.makeText(MyDataActivity.this, "密码栏不能为空，请重新输入密码", Toast.LENGTH_SHORT).show();
                }
                else if(!et_passwordold.getText().toString().equals(resultJSONObject.getString("USPASSWORD")))
                {
                    et_passwordold.setText("");
                    Toast.makeText(MyDataActivity.this, "旧密码错误，请输入正确的旧密码", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(new Util().change_User_password(UserId,et_passwordnew.getText().toString()))
                            {
                                Message msg = Message.obtain();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                                Message msg = Message.obtain();
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
                }catch(JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void initView()
    {
        back_btn=(ImageView)findViewById(R.id.InmyData_backbtn);
        userId_tv=(TextView)findViewById(R.id.InmyData_user_tx);
        change_password_btn=(Button)findViewById(R.id.InmyData_changepassword_btn);
        et_passwordold=(EditText)findViewById(R.id.InmyData_password_old_et);
        et_passwordnew=(EditText)findViewById(R.id.InmyData_password_new_et);
    }
    public void initViewContent()
    {
        //获取用户信息
        new Thread(new Runnable() {
            @Override
            public void run() {
                resultJSONObject=new Util().upload_User_Info(UserId);
                if(resultJSONObject==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                else
                {
                    try {
                        UserAccount = resultJSONObject.getString("USACCOUNT");
                        Message msg = Message.obtain();
                        msg.what = 0;
                        handler.sendMessage(msg);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

}

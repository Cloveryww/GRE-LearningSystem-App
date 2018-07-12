package com.example.work;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.work.R.id.chronometer;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;
    private String UserID;
    private Button btnStart;
    private Button btnStop;
    private Button btnRest;
    private Chronometer chronometer;

    private TextView PaperBank;
    private TextView MyPaperBank;
    private TextView MyWordBank;

    private ImageView btn_calendar;
    private ImageView btn_userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UserID=getIntent().getStringExtra("UserID");
        initChronometer();
        initEnter();

    }
    void initEnter()
    {
        btn_calendar=(ImageView)findViewById(R.id.btn_calendar);
        btn_userInfo=(ImageView)findViewById(R.id.btn_UserInfo);
        PaperBank=(TextView)findViewById(R.id.PaperBank);
        MyPaperBank=(TextView)findViewById(R.id.MyPaperBank);
        MyWordBank=(TextView)findViewById(R.id.MyWordBank);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });
        btn_userInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(MainActivity.this,MyDataActivity.class);
            intent.putExtra("UserId",UserID);
            startActivity(intent);
        }
    });
        PaperBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,PaperBankActivity.class);
                intent.putExtra("UserId",UserID);
                startActivity(intent);
            }
        });
        MyPaperBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MyPaperBankActivity.class);
                intent.putExtra("UserId",UserID);
                startActivity(intent);
            }
        });
        MyWordBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MyWordBankActivity.class);
                intent.putExtra("UserId",UserID);
                startActivity(intent);
            }
        });
    }
    void initChronometer()
    {
        chronometer = (Chronometer) findViewById(R.id.Mychronometer);
        int xiaoshi = (int) ((SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000 / 60);
        chronometer.setFormat("0"+String.valueOf(xiaoshi)+":%s");
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        btnRest = (Button) findViewById(R.id.btn_reset);
        btnStart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // 停止计时
                chronometer.stop();
            }
        });
        // 重置
        btnRest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                chronometer.setBase(SystemClock.elapsedRealtime());
                chronometer.stop();
            }
        });
    }


}

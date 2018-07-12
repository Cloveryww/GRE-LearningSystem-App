package com.example.work;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.item.item_paper_content;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    private String PaperId;
    private String PaperName;
    private String UserId;

    private boolean isAnswered=false;
    private int cur_quesNo;
    private HashSet<String> MyAns;
    private TextView QuesTigan;
    private CheckBox[] checkoptions=null;
    private Button nextQues_btn;
    private ImageView back_btn;
    private TextView tv_title;
    private ArrayList<item_paper_content> paperDate=null;
    private JSONObject PaperJsonResult;
    private JSONArray JsonContent;
    private JSONObject JsonPreAnswers;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(QuestionActivity.this,"下载卷纸失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    initfirstOne();
                    Toast.makeText(QuestionActivity.this, "下载卷纸成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(QuestionActivity.this,"试卷已完成，上传答案成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(QuestionActivity.this, "试卷已完成，网络错误，上传答案失败，请重新提交", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(QuestionActivity.this, "不知道咋回事", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        Intent intent=getIntent();
        PaperId=intent.getStringExtra("PaperId");
        PaperName=intent.getStringExtra("PaperName");
        UserId=intent.getStringExtra("UserId");
        cur_quesNo=0;
        initView();
        initPaperData();
       // initfirstOne();

    }
    private void initView()
    {
        QuesTigan=(TextView)findViewById(R.id.Inquestion_content);
        MyAns=new HashSet<>();
        checkoptions=new CheckBox[9];
        checkoptions[0]=(CheckBox) findViewById(R.id.Inquestion_op1);
        checkoptions[1]=(CheckBox) findViewById(R.id.Inquestion_op2);
        checkoptions[2]=(CheckBox) findViewById(R.id.Inquestion_op3);
        checkoptions[3]=(CheckBox) findViewById(R.id.Inquestion_op4);
        checkoptions[4]=(CheckBox) findViewById(R.id.Inquestion_op5);
        checkoptions[5]=(CheckBox) findViewById(R.id.Inquestion_op6);
        checkoptions[6]=(CheckBox) findViewById(R.id.Inquestion_op7);
        checkoptions[7]=(CheckBox) findViewById(R.id.Inquestion_op8);
        checkoptions[8]=(CheckBox) findViewById(R.id.Inquestion_op9);
        for(int i=0;i<9;i++)
        {
            checkoptions[i].setOnClickListener(this);
        }
        nextQues_btn=(Button)findViewById(R.id.Inquestion_next);
        back_btn=(ImageView)findViewById(R.id.Inquestion_backbtn);
        tv_title=(TextView)findViewById(R.id.Inquestion_title);
        tv_title.setText(PaperName);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        nextQues_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dosomething
                List<String> templist = new ArrayList<String>(MyAns);
                Collections.sort(templist);
                String tempstr="";
                for(String x :templist)
                {
                    tempstr+=x;
                }
                paperDate.get(cur_quesNo).setMyAns(tempstr);//保存自己的答案
                MyAns.clear();
                for(int i=0;i<9;i++)
                {
                    checkoptions[i].setChecked(false);
                    checkoptions[i].setSelected(false);
                }
                if(cur_quesNo<9)
                {
                    cur_quesNo++;
                    update_ques();//切换到下一题
                }
                else//后面没有题了
                {
                    //向服务器发送做题结果并跳转到对答案页面
                    final JSONArray user_ans = new JSONArray();
                    try {
                        JSONObject temp=new JSONObject();
                        for (int i = 0; i < 10; i++) {
                            temp.put("ANS" + Integer.toString(i + 1), paperDate.get(i).getMyAns());
                        }
                        user_ans.put(temp);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            if(new Util().upload_User_Paper_Ans(UserId,PaperId,user_ans))//成功了
                            {
                                Message msg = Message.obtain();
                                msg.what = 2;
                                handler.sendMessage(msg);

                                Intent intent=new Intent(QuestionActivity.this,QuestionPaperreviewActivity.class);
                                intent.putExtra("paperData",paperDate);
                                intent.putExtra("UserId", UserId);
                                intent.putExtra("PaperId", PaperId);
                                intent.putExtra("PaperName", PaperName);
                                startActivity(intent);
                                finish();
                            }
                            else//失败了
                            {
                                Message msg = Message.obtain();
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        if(checkoptions[0].isChecked()) MyAns.add("A");
        if(checkoptions[1].isChecked()) MyAns.add("B");
        if(checkoptions[2].isChecked()) MyAns.add("C");
        if(checkoptions[3].isChecked()) MyAns.add("D");
        if(checkoptions[4].isChecked()) MyAns.add("E");
        if(checkoptions[5].isChecked()) MyAns.add("F");
        if(checkoptions[6].isChecked()) MyAns.add("G");
        if(checkoptions[7].isChecked()) MyAns.add("H");
        if(checkoptions[8].isChecked()) MyAns.add("I");
    }
    private void initPaperData()
    {
        paperDate=new ArrayList<item_paper_content>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                PaperJsonResult = new Util().upload_One_papercontent(UserId,PaperId);
                if(PaperJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 0;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        JsonContent = new JSONArray(PaperJsonResult.getString("content"));
                        System.out.println(PaperJsonResult.toString());
                        if(PaperJsonResult.getString("Done").equals("Yes"))//之前已经做过这套题了
                        {
                            JsonPreAnswers=new JSONArray(PaperJsonResult.getString("mypreAns")).getJSONObject(0);
                            isAnswered=true;
                        }
                        for (int i = 0; i < JsonContent.length(); i++)
                        {
                            JSONObject getJsonObj = JsonContent.getJSONObject(i);//获取json数组中的第一项
                            item_paper_content bean=new item_paper_content(i+1,getJsonObj.getString("PATYPE"+Integer.toString(i+1)),getJsonObj.getString("QUID"),
                                    getJsonObj.getString("QUCON"));
                            switch(getJsonObj.getString("PATYPE"+Integer.toString(i+1)))
                            {
                                case "93":
                                    {
                                        for(int j=0;j<9;j++)
                                        {
                                            bean.addQ_Opeions(getJsonObj.getString("QUOPT"+(char)('A'+j)));
                                        }
                                        for(int j=0;j<3;j++)
                                        {
                                            bean.addQ_Ans(getJsonObj.getString("QUANS"+Integer.toString(j+1)));
                                        }
                                        break;
                                    }
                                case "62":
                                {
                                    for(int j=0;j<6;j++)
                                    {
                                        bean.addQ_Opeions(getJsonObj.getString("QUOPT"+(char)('A'+j)));
                                    }
                                    for(int j=0;j<2;j++)
                                    {
                                        bean.addQ_Ans(getJsonObj.getString("QUANS"+Integer.toString(j+1)));
                                    }
                                    break;
                                 }
                                 case "51":
                                {
                                    for(int j=0;j<5;j++)
                                    {
                                        bean.addQ_Opeions(getJsonObj.getString("QUOPT"+(char)('A'+j)));
                                    }
                                    bean.addQ_Ans(getJsonObj.getString("QUANS"+Integer.toString(1)));
                                    break;
                                }
                                default:
                                    break;

                            }
                            paperDate.add(bean);
                        }
                        Message msg = Message.obtain();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return;
    }
    private void setpreAns(String preAns)
    {
        char  t;
        for(int i=0;i<preAns.length();i++)
        {
            t=preAns.charAt(i);
            checkoptions[t-'A'].setChecked(true);
        }
    }
    private void initfirstOne()
    {
        if(paperDate.get(cur_quesNo).getQ_Type().equals("93"))
        {
            for(int i=0;i<9;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
            checkoptions[5].setText("F. "+paperDate.get(cur_quesNo).getQ_Options(5));
            checkoptions[6].setText("G. "+paperDate.get(cur_quesNo).getQ_Options(6));
            checkoptions[7].setText("H. "+paperDate.get(cur_quesNo).getQ_Options(7));
            checkoptions[8].setText("I. "+paperDate.get(cur_quesNo).getQ_Options(8));
        }
        else if(paperDate.get(cur_quesNo).getQ_Type().equals("62"))
        {
            for(int i=0;i<6;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            for(int i=6;i<9;i++)
            {
                checkoptions[i].setVisibility(View.GONE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
            checkoptions[5].setText("F. "+paperDate.get(cur_quesNo).getQ_Options(5));
        }
        else//是51
        {
            for(int i=0;i<5;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            for(int i=5;i<9;i++)
            {
                checkoptions[i].setVisibility(View.GONE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
        }
        //将第一题先赋值
        if(isAnswered)//之前做过了
        {
            try {
                setpreAns(JsonPreAnswers.getString("ANS" + Integer.toString(cur_quesNo + 1)));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }
    private void update_ques()
    {
        if(paperDate.get(cur_quesNo).getQ_Type().equals("93"))
        {
            for(int i=0;i<9;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
            checkoptions[5].setText("F. "+paperDate.get(cur_quesNo).getQ_Options(5));
            checkoptions[6].setText("G. "+paperDate.get(cur_quesNo).getQ_Options(6));
            checkoptions[7].setText("H. "+paperDate.get(cur_quesNo).getQ_Options(7));
            checkoptions[8].setText("I. "+paperDate.get(cur_quesNo).getQ_Options(8));
        }
        else if(paperDate.get(cur_quesNo).getQ_Type().equals("62"))
        {
            for(int i=0;i<6;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            for(int i=6;i<9;i++)
            {
                checkoptions[i].setVisibility(View.GONE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
            checkoptions[5].setText("F. "+paperDate.get(cur_quesNo).getQ_Options(5));
        }
        else//是51
        {
            for(int i=0;i<5;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            for(int i=5;i<9;i++)
            {
                checkoptions[i].setVisibility(View.GONE);
            }
            QuesTigan.setText(Integer.toString(cur_quesNo+1)+". "+paperDate.get(cur_quesNo).getQ_Content());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
        }
        if(isAnswered)//之前做过了
        {
            try {
                setpreAns(JsonPreAnswers.getString("ANS" + Integer.toString(cur_quesNo + 1)));
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

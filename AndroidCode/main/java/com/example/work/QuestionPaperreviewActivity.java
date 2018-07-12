package com.example.work;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.work.item.WrapContentListview;
import com.example.work.item.item_comment;
import com.example.work.item.item_paper_content;
import com.example.work.item.item_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class QuestionPaperreviewActivity extends AppCompatActivity {

    private String PaperId;
    private String PaperName;
    private String UserId;
    private int cur_quesNo;


    private TextView QuesTigan;
    private TextView[] checkoptions=null;
    private TextView nextQues_btn;
    private ImageView back_btn;
    private TextView tv_title;
    private TextView tv_add2Mybank;
    private TextView tv_correct_ans;
    private TextView tv_user_ans;
    private WrapContentListview comment_list;
    private EditText et_mycomment;
    private Button btn_addComment;

    private ArrayList<item_comment> commentDataList;
    private ArrayList<item_paper_content> paperDate=null;
    private Comment_Adapter myAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"添加到我的错题库成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this, "该错题已经在错题库中了", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"已经到达最后一题",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                   comment_list.setAdapter(myAdapter);
                   // myAdapter.notifyDataSetChanged();
                    Toast.makeText(QuestionPaperreviewActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 4:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"你已经赞过了",Toast.LENGTH_SHORT).show();
                }
                break;
                case 5:
                {
                    comment_list.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                    Toast.makeText(QuestionPaperreviewActivity.this,"评论区更新成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 6:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"网络错误，评论区更新失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 7:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"不能删除别人的评论",Toast.LENGTH_SHORT).show();
                }
                break;
                case 8://删除一条评论
                {
                    comment_list.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                }
                break;
                case 9:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"网络错误，删除评论失败",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(QuestionPaperreviewActivity.this, "不知道咋回事", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_paperreview);
        Intent intent=getIntent();
        PaperId=intent.getStringExtra("PaperId");
        PaperName=intent.getStringExtra("PaperName");
        UserId=intent.getStringExtra("UserId");
        paperDate=intent.getParcelableArrayListExtra("paperData");
       // System.out.println("+++++++++++++++"+paperDate.toString());

        cur_quesNo=0;
        initView();
        initfirstOne();
        myAdapter=new Comment_Adapter(this,commentDataList);
        comment_list.setAdapter(myAdapter);

    }
    private void initView()
    {
        et_mycomment=(EditText)findViewById(R.id.Inquestionpaperreview_Comment_et);
        btn_addComment=(Button)findViewById(R.id.Inquestionpaperreview_add_coment_btn);
        tv_correct_ans=(TextView)findViewById(R.id.Inquestionpaperreview_corroct_ans);
        tv_user_ans=(TextView)findViewById(R.id.Inquestionpaperreview_user_ans);

        QuesTigan=(TextView)findViewById(R.id.Inquestionpaperreview_content);
        checkoptions=new TextView[9];
        checkoptions[0]=(TextView) findViewById(R.id.Inquestionpaperreview_op1);
        checkoptions[1]=(TextView) findViewById(R.id.Inquestionpaperreview_op2);
        checkoptions[2]=(TextView) findViewById(R.id.Inquestionpaperreview_op3);
        checkoptions[3]=(TextView) findViewById(R.id.Inquestionpaperreview_op4);
        checkoptions[4]=(TextView) findViewById(R.id.Inquestionpaperreview_op5);
        checkoptions[5]=(TextView) findViewById(R.id.Inquestionpaperreview_op6);
        checkoptions[6]=(TextView) findViewById(R.id.Inquestionpaperreview_op7);
        checkoptions[7]=(TextView) findViewById(R.id.Inquestionpaperreview_op8);
        checkoptions[8]=(TextView) findViewById(R.id.Inquestionpaperreview_op9);

        nextQues_btn=(TextView)findViewById(R.id.Inquestionpaperreview_next);
        back_btn=(ImageView)findViewById(R.id.Inquestionpaperreview_backbtn);
        tv_title=(TextView)findViewById(R.id.Inquestionpaperreview_title);
        tv_add2Mybank=(TextView)findViewById(R.id.Inquestionpaperreview_addMyBank);
        tv_title.setText(PaperName);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String myComment=et_mycomment.getText().toString();
                if(myComment.equals(""))
                {
                    Toast.makeText(QuestionPaperreviewActivity.this,"评论内容不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                }
                {
                    SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                    final Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                    final item_comment item=new item_comment("-1",formatter.format(curDate),myComment,0,UserId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result=new Util().add_one_Comment(item,paperDate.get(cur_quesNo).getQ_ID());
                            if(result==null)
                            {
                                Message msg = Message.obtain();
                                msg.what = 6;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                                item.setCo_Id(result);
                                commentDataList.add(item);
                                Message msg = Message.obtain();
                                msg.what = 5;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                }
            }
        });
        nextQues_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dosomething
                if(cur_quesNo<9)
                {
                    cur_quesNo++;
                    update_ques();//切换到下一题
                }
                else//后面没有题了
                {
                    Message msg = Message.obtain();
                    msg.what = 2;
                    handler.sendMessage(msg);
                }
            }
        });
        tv_add2Mybank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(new Util().upload_add_My_paper_bank(UserId,paperDate.get(cur_quesNo).getQ_ID(),paperDate.get(cur_quesNo).getMyAns()))
                        {
                            Message msg = Message.obtain();
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                        else
                        {
                            Message msg = Message.obtain();
                            msg.what = 1;
                            handler.sendMessage(msg);
                        }
                    }
                }).start();
            }
        });

        //评论模块相关
        comment_list=(WrapContentListview)findViewById(R.id.Inquestionpaperreview_comentlist);
        commentDataList=new ArrayList<item_comment>();
        initListCreator();
        comment_list.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://delete
                        // open
                        if(commentDataList.get(position).getCo_From().equals(UserId))
                        {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (new Util().delete_one_Comment(commentDataList.get(position).getCo_Id())) {
                                        commentDataList.remove(position);
                                        Message msg = Message.obtain();
                                        msg.what = 8;
                                        handler.sendMessage(msg);
                                    } else {
                                        Message msg = Message.obtain();
                                        msg.what = 9;
                                        handler.sendMessage(msg);
                                        //Message msg = Message.obtain();
                                        // msg.what = 8;
                                        // handler.sendMessage(msg);
                                    }
                                }
                            }).start();

                        }
                        else
                        {
                            Message msg = Message.obtain();
                            msg.what = 7;
                            handler.sendMessage(msg);
                        }
                        break;
                    // case 1:
                    // delete
                    // break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

    }
    private void initListCreator()//初始化ListView的滑动效果
    {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete_forever_black_24dp);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

// set creator
        comment_list.setMenuCreator(creator);
        comment_list.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void initfirstOne()
    {
        //将第一题先赋值
        QuesTigan.setText("1. "+paperDate.get(0).getQ_Content());
        tv_correct_ans.setText(paperDate.get(0).getQ_correctAns());
        tv_user_ans.setText(paperDate.get(0).getMyAns());

        if(paperDate.get(cur_quesNo).getQ_Type().equals("93"))
        {
            for(int i=0;i<9;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
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
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
        }

        get_Comments(paperDate.get(cur_quesNo).getQ_ID());
        return;
    }
    private void get_Comments(final String Q_Id)
    {
        commentDataList.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                JSONArray commentJSONarray=new Util().upload_Comments(Q_Id);
                if(commentJSONarray==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 6;
                    handler.sendMessage(msg);
                }
                else
                {
                    System.out.println(commentJSONarray.toString());
                    try {
                        for(int i=0;i<commentJSONarray.length();i++)
                        {
                            JSONObject getJsonObj = commentJSONarray.getJSONObject(i);//获取json数组中的第一项
                            commentDataList.add(new item_comment(getJsonObj.getString("COID"),getJsonObj.getString("COTIME"),
                                        getJsonObj.getString("COCON"),getJsonObj.getInt("CONICE"),getJsonObj.getString("COFROM")));
                        }
                        Message msg = Message.obtain();
                        msg.what = 5;
                        handler.sendMessage(msg);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
            tv_correct_ans.setText(paperDate.get(cur_quesNo).getQ_correctAns());
            tv_user_ans.setText(paperDate.get(cur_quesNo).getMyAns());
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
            tv_correct_ans.setText(paperDate.get(cur_quesNo).getQ_correctAns());
            tv_user_ans.setText(paperDate.get(cur_quesNo).getMyAns());
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
            tv_correct_ans.setText(paperDate.get(cur_quesNo).getQ_correctAns());
            tv_user_ans.setText(paperDate.get(cur_quesNo).getMyAns());
            checkoptions[0].setText("A. "+paperDate.get(cur_quesNo).getQ_Options(0));
            checkoptions[1].setText("B. "+paperDate.get(cur_quesNo).getQ_Options(1));
            checkoptions[2].setText("C. "+paperDate.get(cur_quesNo).getQ_Options(2));
            checkoptions[3].setText("D. "+paperDate.get(cur_quesNo).getQ_Options(3));
            checkoptions[4].setText("E. "+paperDate.get(cur_quesNo).getQ_Options(4));
        }
        get_Comments(paperDate.get(cur_quesNo).getQ_ID());

    }
    public class Comment_Adapter extends BaseAdapter {
        private List<item_comment> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Comment_Adapter(Context context, List<item_comment> list) {
            mList = list;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        //ListView需要显示的数据数量
        public int getCount() {
            return mList.size();
        }

        @Override
        //指定的索引对应的数据项
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        //指定的索引对应的数据项ID
        public long getItemId(int position) {
            return position;
        }

        @Override
        //返回每一项的显示内容
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_comment, null);

                //对viewHolder的属性进行赋值
                viewHolder.co_from = (TextView) convertView.findViewById(R.id.InComment_From);
                viewHolder.co_time = (TextView) convertView.findViewById(R.id.InComment_Time);
                viewHolder.co_content = (TextView) convertView.findViewById(R.id.InComment_content);
                viewHolder.co_nice_num = (TextView) convertView.findViewById(R.id.InComment_NiceNum);
                viewHolder.co_nice=(ImageView)convertView.findViewById(R.id.InComment_Nice_im);

                viewHolder.co_nice.setTag(position);


                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            item_comment bean = mList.get(position);

            // 设置控件的数据
            viewHolder.co_from.setText(bean.getCo_From());
            viewHolder.co_time.setText(bean.getCo_Time());
            viewHolder.co_content.setText(bean.getCo_Content());
            viewHolder.co_nice_num.setText(Integer.toString(bean.getCo_Nice_num()));
            viewHolder.co_nice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int po=(Integer)view.getTag();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                            Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                            if(new Util().nice_one_Comment(UserId,mList.get(po).getCo_Id(),formatter.format(curDate)))
                            {
                                //viewHolder.co_nice_num.setText(Integer.toString(mList.get(po).getCo_Nice_num()+1));
                                mList.get(po).nice_add_one();
                                Message msg = Message.obtain();
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                                Message msg = Message.obtain();
                                msg.what = 4;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                }
            });

            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public TextView co_from;
            public TextView co_time;
            public TextView co_content;
            public TextView co_nice_num;
            public ImageView co_nice;
        }
    }
}

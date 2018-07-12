package com.example.work;

import android.content.Context;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionReviewActivity extends AppCompatActivity {
    private String UserId;
    private String QuestionId;

    private TextView QuesTigan;
    private TextView[] checkoptions=null;
    private ImageView back_btn;
    private TextView tv_title;
    private TextView tv_correct_ans;
    private TextView tv_user_ans;
    private WrapContentListview comment_list;
    private EditText et_mycomment;
    private Button btn_addComment;

    private ArrayList<item_comment> commentDataList;
    private item_paper_content QuestionDate;
    private Comment_Adapter myAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    comment_list.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                    Toast.makeText(QuestionReviewActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(QuestionReviewActivity.this,"已经赞过了",Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    comment_list.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                    Toast.makeText(QuestionReviewActivity.this,"评论区更新成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(QuestionReviewActivity.this,"网络错误，评论区更新失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 4:
                {
                    initQuestionData();
                    Toast.makeText(QuestionReviewActivity.this,"题目信息加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 5:
                {
                    Toast.makeText(QuestionReviewActivity.this,"题目信息加载失败",Toast.LENGTH_SHORT).show();
                }
                break;
                case 6:
                {
                    Toast.makeText(QuestionReviewActivity.this,"不能删除别人的评论",Toast.LENGTH_SHORT).show();
                }
                break;
                case 7://删除一条评论
                {
                    comment_list.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                }
                break;
                case 8:
                {
                    Toast.makeText(QuestionReviewActivity.this,"网络错误，删除评论失败",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(QuestionReviewActivity.this, "不知道咋回事", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_review);
        UserId=getIntent().getStringExtra("UserId");
        QuestionId=getIntent().getStringExtra("QuestionId");


        initView();
        load_info();
        myAdapter=new Comment_Adapter(this,commentDataList);
        comment_list.setAdapter(myAdapter);
    }
    private void initView()
    {
        et_mycomment=(EditText)findViewById(R.id.Inquestionreview_Comment_et);
        btn_addComment=(Button)findViewById(R.id.Inquestionreview_add_coment_btn);
        tv_correct_ans=(TextView)findViewById(R.id.Inquestionreview_corroct_ans);
        tv_user_ans=(TextView)findViewById(R.id.Inquestionreview_user_ans);

        QuesTigan=(TextView)findViewById(R.id.Inquestionreview_content);
        checkoptions=new TextView[9];
        checkoptions[0]=(TextView) findViewById(R.id.Inquestionreview_op1);
        checkoptions[1]=(TextView) findViewById(R.id.Inquestionreview_op2);
        checkoptions[2]=(TextView) findViewById(R.id.Inquestionreview_op3);
        checkoptions[3]=(TextView) findViewById(R.id.Inquestionreview_op4);
        checkoptions[4]=(TextView) findViewById(R.id.Inquestionreview_op5);
        checkoptions[5]=(TextView) findViewById(R.id.Inquestionreview_op6);
        checkoptions[6]=(TextView) findViewById(R.id.Inquestionreview_op7);
        checkoptions[7]=(TextView) findViewById(R.id.Inquestionreview_op8);
        checkoptions[8]=(TextView) findViewById(R.id.Inquestionreview_op9);

        back_btn=(ImageView)findViewById(R.id.Inquestionreview_backbtn);
        tv_title=(TextView)findViewById(R.id.Inquestionreview_title);

        tv_title.setText(QuestionId+"号题目");
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
                et_mycomment.setText("");
                if(myComment.equals(""))
                {
                    Toast.makeText(QuestionReviewActivity.this,"评论内容不能为空，请重新输入",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                    final Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                    final item_comment item=new item_comment("-1",formatter.format(curDate),myComment,0,UserId);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String result=new Util().add_one_Comment(item,QuestionId);
                            if(result==null)
                            {
                                Message msg = Message.obtain();
                                msg.what = 3;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                                item.setCo_Id(result);
                                commentDataList.add(item);
                                Message msg = Message.obtain();
                                msg.what = 2;
                                handler.sendMessage(msg);
                            }
                        }
                    }).start();

                }
            }
        });
        //评论模块相关
        comment_list=(WrapContentListview) findViewById(R.id.Inquestionreview_comentlist);
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
                                        msg.what = 7;
                                        handler.sendMessage(msg);
                                    } else {
                                        Message msg = Message.obtain();
                                        msg.what = 7;
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
                            msg.what = 6;
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
    private void load_info()
    {
        //将题目信息下载下来
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject resultJSONObject = new Util().upload_one_myquestion_Info(UserId, QuestionId);
                    if(resultJSONObject==null)
                    {
                        Message msg = Message.obtain();
                        msg.what = 5;
                        handler.sendMessage(msg);
                    }
                    else {
                        QuestionDate = new item_paper_content(1, resultJSONObject.getString("PATYPE"), resultJSONObject.getString("QUID"),
                                resultJSONObject.getString("QUCON"));
                        QuestionDate.setMyAns(resultJSONObject.getString("QUUSANS"));//用户的答案
                        switch (resultJSONObject.getString("PATYPE")) {
                            case "93": {
                                for (int j = 0; j < 9; j++) {
                                    QuestionDate.addQ_Opeions(resultJSONObject.getString("QUOPT" + (char)('A' + j)));
                                }
                                for (int j = 0; j < 3; j++) {
                                    QuestionDate.addQ_Ans(resultJSONObject.getString("QUANS" + Integer.toString(j + 1)));
                                }
                                break;
                            }
                            case "62": {
                                for (int j = 0; j < 6; j++) {
                                    QuestionDate.addQ_Opeions(resultJSONObject.getString("QUOPT" + (char)('A' + j)));
                                }
                                for (int j = 0; j < 2; j++) {
                                    QuestionDate.addQ_Ans(resultJSONObject.getString("QUANS" + Integer.toString(j + 1)));
                                }
                                break;
                            }
                            case "51": {
                                for (int j = 0; j < 5; j++) {
                                    QuestionDate.addQ_Opeions(resultJSONObject.getString("QUOPT" + (char)('A' + j)));
                                }
                                QuestionDate.addQ_Ans(resultJSONObject.getString("QUANS" + Integer.toString(1)));
                                break;
                            }
                            default:
                                break;

                        }
                        Message msg = Message.obtain();
                        msg.what = 4;
                        handler.sendMessage(msg);
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
    private void initQuestionData()
    {
        QuesTigan.setText(QuestionDate.getQ_Content());
        tv_correct_ans.setText(QuestionDate.getQ_correctAns());
        tv_user_ans.setText(QuestionDate.getMyAns());

        if(QuestionDate.getQ_Type().equals("93"))
        {
            for(int i=0;i<9;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            checkoptions[0].setText("A. "+QuestionDate.getQ_Options(0));
            checkoptions[1].setText("B. "+QuestionDate.getQ_Options(1));
            checkoptions[2].setText("C. "+QuestionDate.getQ_Options(2));
            checkoptions[3].setText("D. "+QuestionDate.getQ_Options(3));
            checkoptions[4].setText("E. "+QuestionDate.getQ_Options(4));
            checkoptions[5].setText("F. "+QuestionDate.getQ_Options(5));
            checkoptions[6].setText("G. "+QuestionDate.getQ_Options(6));
            checkoptions[7].setText("H. "+QuestionDate.getQ_Options(7));
            checkoptions[8].setText("I. "+QuestionDate.getQ_Options(8));
        }
        else if(QuestionDate.getQ_Type().equals("62"))
        {
            for(int i=0;i<6;i++)
            {
                checkoptions[i].setVisibility(View.VISIBLE);
            }
            for(int i=6;i<9;i++)
            {
                checkoptions[i].setVisibility(View.GONE);
            }
            checkoptions[0].setText("A. "+QuestionDate.getQ_Options(0));
            checkoptions[1].setText("B. "+QuestionDate.getQ_Options(1));
            checkoptions[2].setText("C. "+QuestionDate.getQ_Options(2));
            checkoptions[3].setText("D. "+QuestionDate.getQ_Options(3));
            checkoptions[4].setText("E. "+QuestionDate.getQ_Options(4));
            checkoptions[5].setText("F. "+QuestionDate.getQ_Options(5));
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
            checkoptions[0].setText("A. "+QuestionDate.getQ_Options(0));
            checkoptions[1].setText("B. "+QuestionDate.getQ_Options(1));
            checkoptions[2].setText("C. "+QuestionDate.getQ_Options(2));
            checkoptions[3].setText("D. "+QuestionDate.getQ_Options(3));
            checkoptions[4].setText("E. "+QuestionDate.getQ_Options(4));
        }

        get_Comments(QuestionId);
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
                    msg.what = 3;
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
                            System.out.println("+++++++++++++add"+i);
                        }
                        Message msg = Message.obtain();
                        msg.what = 2;
                        handler.sendMessage(msg);
                    }catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
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
                //System.out.println("+++++++++++第"+position+"个评论已生成");

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
            //System.out.println("+++++++++++第"+position+"个评论点击事件已添加");
            viewHolder.co_nice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int po=(Integer)view.getTag();
                    //viewHolder.co_nice_num.setText(Integer.toString(mList.get(po).getCo_Nice_num()+1));
                    //mList.get(po).nice_add_one();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
                            Date curDate=new Date(System.currentTimeMillis());//获取当前时间
                            if(new Util().nice_one_Comment(UserId,mList.get(po).getCo_Id(),formatter.format(curDate)))
                            {
                                mList.get(po).nice_add_one();
                                //System.out.println("+++++++++++你成功点击了第"+po+"个评论的赞");
                                Message msg = Message.obtain();
                                msg.what = 0;
                                handler.sendMessage(msg);
                            }
                            else
                            {
                               // System.out.println("+++++++++++你失败点击了第"+po+"个评论的赞");
                                Message msg = Message.obtain();
                                msg.what = 1;
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

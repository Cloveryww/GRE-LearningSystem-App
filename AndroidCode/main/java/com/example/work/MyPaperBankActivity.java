package com.example.work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.work.item.WrapContentListview;
import com.example.work.item.item_my_question;
import com.example.work.item.item_paper_content;
import com.example.work.item.item_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyPaperBankActivity extends AppCompatActivity {

    private String UserId;
    private ImageView back_btn;
    private WrapContentListview myquestionList;
    private ArrayList<item_my_question> myquestionIDsDataList;
    private JSONArray questionIdsJsonResult;
    private Question_Adapter myAdapter;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    Toast.makeText(MyPaperBankActivity.this,"我的试题库加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(MyPaperBankActivity.this, "网络错误，我的试题库加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2://删除一个错题
                {
                    myquestionList.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                }
                break;
                case 3:
                {
                    Toast.makeText(MyPaperBankActivity.this,"网络错误，删除试题失败",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(MyPaperBankActivity.this,"网络错误，操作失败",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_paper_bank);
        UserId=getIntent().getStringExtra("UserId");
        myquestionList=(WrapContentListview)findViewById(R.id.Inmypaperbank_questionlist);
        back_btn=(ImageView)findViewById(R.id.Inmypaperbank_backbtn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Question_Adapter(this,myquestionIDsDataList);
        myquestionList.setAdapter(myAdapter);

        myquestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent=new Intent(MyPaperBankActivity.this,QuestionReviewActivity.class);
                intent.putExtra("QuestionId",myquestionIDsDataList.get(position).getQuestionId());
                intent.putExtra("UserId",UserId);
                startActivity(intent);
            }
        });

        //删除试题相关
        initListCreator();
        myquestionList.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://delete
                        // open
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (new Util().delete_one_myquestion(UserId,myquestionIDsDataList.get(position).getQuestionId())) {
                                    myquestionIDsDataList.remove(position);
                                    Message msg = Message.obtain();
                                    msg.what = 2;
                                    handler.sendMessage(msg);
                                } else {
                                    Message msg = Message.obtain();
                                    msg.what = 3;
                                    handler.sendMessage(msg);
                                    //Message msg = Message.obtain();
                                    // msg.what = 8;
                                    // handler.sendMessage(msg);
                                }
                            }
                        }).start();

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
        myquestionList.setMenuCreator(creator);
        myquestionList.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void initDataList()
    {
        myquestionIDsDataList= new ArrayList<item_my_question>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                questionIdsJsonResult = new Util().upload_User_questionIds(UserId);
                if(questionIdsJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < questionIdsJsonResult.length(); i++) {
                            JSONObject getJsonObj = questionIdsJsonResult.getJSONObject(i);//获取json数组中的第一项
                            myquestionIDsDataList.add(new item_my_question(getJsonObj.getString("QUESID")));
                        }
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
    public class Question_Adapter extends BaseAdapter {
        private List<item_my_question> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Question_Adapter(Context context, List<item_my_question> list) {
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
            ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_my_question, null);

                //对viewHolder的属性进行赋值
                viewHolder.question_No=(TextView)convertView.findViewById(R.id.InMyquestion_No);
                viewHolder.question_id = (TextView) convertView.findViewById(R.id.InMyquestion_Name);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            item_my_question bean = mList.get(position);

            // 设置控件的数据
            viewHolder.question_No.setText(Integer.toString(position+1)+".");
            viewHolder.question_id.setText(bean.getQuestionId()+"号题目");
            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public TextView question_No;
            public TextView question_id;

        }
    }
}

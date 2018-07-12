package com.example.work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.work.item.item_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyWordBankActivity extends AppCompatActivity {
    private String UserId;
    private ImageView back_btn;
    private TextView btn_add;

    private com.baoyz.swipemenulistview.SwipeMenuListView list_words;
    private ArrayList<item_word> mData = null;
    private JSONArray wordsJsonResult;
    private Word_Adapter myAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(MyWordBankActivity.this,"我的GRE单词本加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(MyWordBankActivity.this, "网络错误，我的GRE单词本加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    list_words.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                    Toast.makeText(MyWordBankActivity.this, "单词库更新成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(MyWordBankActivity.this, "单词库更新失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case 4://删除一个单词
                {
                    list_words.setAdapter(myAdapter);
                    //myAdapter.notifyDataSetChanged();
                }
                break;
                case 5:
                {
                    Toast.makeText(MyWordBankActivity.this,"网络错误，删除单词失败",Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(MyWordBankActivity.this,"网络错误，操作失败",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_word_bank);
        UserId=getIntent().getStringExtra("UserId");
        list_words=(com.baoyz.swipemenulistview.SwipeMenuListView) findViewById(R.id.Inmywordbank_wordlist);
        btn_add=(TextView)findViewById(R.id.Inmywordbank_addbtn);
        back_btn=(ImageView)findViewById(R.id.Inmywordbank_backbtn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Word_Adapter(this,mData);
        list_words.setAdapter(myAdapter);

        list_words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
               //编辑单词
                final View dialogEdit=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_new_word,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_new_word_spell) ;//新建相册的时候EditText
                final EditText new_et2 = dialogEdit.findViewById(R.id.dialog_new_word_chinese);//新建相册的时候EditText
                new_et1.setText(mData.get(position).getSpell());
                new_et2.setText(mData.get(position).getChinese());
                new AlertDialog.Builder(MyWordBankActivity.this).setTitle("编辑单词")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEdit)
                        .setPositiveButton("编辑", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String spell = new_et1.getText().toString();
                                        String chinese=new_et2.getText().toString();
                                        if (spell.equals("")||chinese.equals("")) {
                                            Toast.makeText(getApplicationContext(), "编辑单词失败，英文拼写和中文释义不能为空,请重新编辑", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            edit_new_word(spell,chinese,position);//和添加新单词相同的函数，由服务器查重
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View dialogEdit=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_new_word,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_new_word_spell) ;//新建相册的时候EditText
                final EditText new_et2 = dialogEdit.findViewById(R.id.dialog_new_word_chinese);//新建相册的时候EditText
                new AlertDialog.Builder(MyWordBankActivity.this).setTitle("添加新单词")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(dialogEdit)
                        .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String spell = new_et1.getText().toString();
                                        String chinese=new_et2.getText().toString();
                                        if (spell.equals("")||chinese.equals("")) {
                                            Toast.makeText(getApplicationContext(), "新单词添加失败，英文拼写和中文释义不能为空,请重新编辑", Toast.LENGTH_SHORT).show();
                                        }
                                        else {

                                            add_new_word(spell,chinese);
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });

        //删除单词相关
        initListCreator();
        list_words.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(final int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0://delete
                        // open
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    if (new Util().delete_one_word(UserId,mData.get(position).getSpell())) {
                                        mData.remove(position);
                                        Message msg = Message.obtain();
                                        msg.what = 4;
                                        handler.sendMessage(msg);
                                    } else {
                                        Message msg = Message.obtain();
                                        msg.what = 5;
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
        list_words.setMenuCreator(creator);
        list_words.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
    private void  edit_new_word(String spell,String chinese,int position){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        item_word temp=new item_word(spell,chinese, formatter.format(curDate));
        mData.get(position).setChinese(chinese);
        mData.get(position).setSpell(spell);
        mData.get(position).setAddTime(formatter.format(curDate));
        Boolean re=new Util().add_one_word_to_MyWordbank(UserId,temp);
        if(re)//新单词添加成功
        {
            Message msg = Message.obtain();
            msg.what = 2;
            //成功了更新界面
            handler.sendMessage(msg);
        }//新单词添加失败
        else {
            Message msg = Message.obtain();
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }
    private void  add_new_word(String spell,String chinese){
        SimpleDateFormat formatter =new SimpleDateFormat("yyyy.MM.dd  HH:mm:ss");
        Date curDate=new Date(System.currentTimeMillis());//获取当前时间
        item_word temp=new item_word(spell,chinese, formatter.format(curDate));
        Boolean re=new Util().add_one_word_to_MyWordbank(UserId,temp);
        if(re)//新单词添加成功
        {
            Message msg = Message.obtain();
            msg.what = 2;
            //成功了更新界面
            mData.add(temp);
            handler.sendMessage(msg);
        }//新单词添加失败
        else {
            Message msg = Message.obtain();
            msg.what = 3;
            handler.sendMessage(msg);
        }
    }
    private void initDataList()
    {
        mData = new ArrayList<item_word>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                wordsJsonResult = new Util().upload_MyWords(UserId);
                if(wordsJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < wordsJsonResult.length(); i++) {
                            JSONObject getJsonObj = wordsJsonResult.getJSONObject(i);//获取json数组中的第一项
                            mData.add(new item_word(getJsonObj.getString("UWENG"),getJsonObj.getString("UWCHI"),getJsonObj.getString("UWTIME")));
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
    public class Word_Adapter extends BaseAdapter {
        private List<item_word> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Word_Adapter(Context context, List<item_word> list) {
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
                convertView = mInflater.inflate(R.layout.item_word, null);

                //对viewHolder的属性进行赋值
                viewHolder.word_no = (TextView) convertView.findViewById(R.id.word_no);
                viewHolder.word_spell = (TextView) convertView.findViewById(R.id.word_spell);
                viewHolder.word_chinese = (TextView) convertView.findViewById(R.id.word_chinese);
                viewHolder.word_addTime = (TextView) convertView.findViewById(R.id.word_addTime);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            item_word bean = mList.get(position);

            // 设置控件的数据
            viewHolder.word_no.setText(Integer.toString(position+1)+".");
            viewHolder.word_spell.setText(bean.getSpell());
            viewHolder.word_chinese.setText(bean.getChinese());
            viewHolder.word_addTime.setText(bean.getAddTime());
            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public TextView word_no;
            public TextView word_spell;
            public TextView word_chinese;
            public TextView word_addTime;
        }
    }
}


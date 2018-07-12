package com.example.work;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.work.item.item_word;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PaperWordListActivity extends AppCompatActivity {

    private String UserId;
    private String PaperId;
    private String PaperName;
    private ImageView back_btn;
    private TextView btn_begin;
    private TextView tv_title;

    private ListView list_words;
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
                    Toast.makeText(PaperWordListActivity.this,"本试卷单词加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    Toast.makeText(PaperWordListActivity.this, "网络错误，本试卷单词加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
                case 2:
                {
                    Toast.makeText(PaperWordListActivity.this, "我的单词库更新成功", Toast.LENGTH_SHORT).show();
                }
                break;
                case 3:
                {
                    Toast.makeText(PaperWordListActivity.this, "我的单词库更新失败", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                {
                    Toast.makeText(PaperWordListActivity.this,"网络错误，操作失败",Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_word_list);
        Intent intent=getIntent();
        UserId=intent.getStringExtra("UserId");
        PaperId=intent.getStringExtra("PaperId");
        PaperName=intent.getStringExtra("PaperName");

        list_words=(ListView)findViewById(R.id.Inpaperwordlist_wordlist);
        btn_begin=(TextView)findViewById(R.id.Inpaperwordlist_beginbtn);
        back_btn=(ImageView)findViewById(R.id.Inpaperwordlist_backbtn);
        tv_title=(TextView)findViewById(R.id.Inpaperwordlist_title);
        tv_title.setText("单词表");
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        list_words.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //编辑单词
                final View dialogEdit=(LinearLayout) getLayoutInflater().inflate(R.layout.dialog_new_word,null);
                final EditText new_et1 =dialogEdit.findViewById(R.id.dialog_new_word_spell) ;//新建相册的时候EditText
                final EditText new_et2 = dialogEdit.findViewById(R.id.dialog_new_word_chinese);//新建相册的时候EditText
                new_et1.setText(mData.get(position).getSpell());
                new_et2.setText(mData.get(position).getChinese());
                new AlertDialog.Builder(PaperWordListActivity.this).setTitle("添加到我的生词库")
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
                                            Toast.makeText(getApplicationContext(), "添加单词失败，英文拼写和中文释义不能为空,请重新添加", Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            add_new_word(spell,chinese);//和添加新单词相同的函数，由服务器查重
                                        }
                                    }
                                }).start();

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
            }
        });
        initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Word_Adapter(this,mData);
        list_words.setAdapter(myAdapter);

        btn_begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(PaperWordListActivity.this,QuestionActivity.class);
                intent.putExtra("PaperId",PaperId);
                intent.putExtra("PaperName",PaperName);
                intent.putExtra("UserId",UserId);
                startActivity(intent);
            }
        });
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
                wordsJsonResult = new Util().upload_PaperWords(PaperId);
                System.out.println("+++++++++++++"+wordsJsonResult.toString());
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
                            mData.add(new item_word(getJsonObj.getString("WOENG"),getJsonObj.getString("WOCHI"),""));
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
                convertView = mInflater.inflate(R.layout.item_word_notime, null);

                //对viewHolder的属性进行赋值
                viewHolder.word_no = (TextView) convertView.findViewById(R.id.word_no2);
                viewHolder.word_spell = (TextView) convertView.findViewById(R.id.word_spell2);
                viewHolder.word_chinese = (TextView) convertView.findViewById(R.id.word_chinese2);
               // viewHolder.word_addTime = (TextView) convertView.findViewById(R.id.word_addTime);

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
           // viewHolder.word_addTime.setText(bean.getAddTime());
            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public TextView word_no;
            public TextView word_spell;
            public TextView word_chinese;
           // public TextView word_addTime;
        }
    }
}

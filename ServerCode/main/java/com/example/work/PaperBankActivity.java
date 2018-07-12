package com.example.work;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.work.item.item_paper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PaperBankActivity extends AppCompatActivity {
    private String UserId;
    private ImageView back_btn;

    private ListView list_paper;
    private ArrayList<item_paper> mData = null;
    private JSONArray papersJsonResult;
    private  Paper_Adapter myAdapter;
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(PaperBankActivity.this,"GRE试卷列表加载成功",Toast.LENGTH_SHORT).show();
                }
                break;
                case 1:
                {
                    myAdapter.notifyDataSetChanged();
                    Toast.makeText(PaperBankActivity.this, "网络错误，GRE试卷列表加载失败", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_bank);
        UserId=getIntent().getStringExtra("UserId");
        back_btn=(ImageView)findViewById(R.id.Inpaperbank_backbtn);
        list_paper=(ListView)findViewById(R.id.Inpaperbank_paperlist);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        initDataList();//初始化mData
        //设置ListView的数据适配器
        myAdapter=new Paper_Adapter(this,mData);
        list_paper.setAdapter(myAdapter);

        list_paper.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(PaperBankActivity.this,PaperWordListActivity.class);
                intent.putExtra("PaperId",mData.get(position).getpaperId());
                intent.putExtra("PaperName","GRE考卷");
                intent.putExtra("UserId",UserId);
                startActivity(intent);
            }
        });
    }
    private void initDataList()
    {
        mData = new ArrayList<item_paper>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                papersJsonResult = new Util().upload_Papers();
                if(papersJsonResult==null)
                {
                    Message msg = Message.obtain();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
                else {//初始化mData
                    try {
                        for (int i = 0; i < papersJsonResult.length(); i++) {
                            JSONObject getJsonObj = papersJsonResult.getJSONObject(i);//获取json数组中的第一项
                            mData.add(new item_paper(getJsonObj.getString("PAID"),getJsonObj.getString("PATIME")));
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
    public class Paper_Adapter extends BaseAdapter {
        private List<item_paper> mList;//数据源
        private LayoutInflater mInflater;//布局装载器对象

        // 通过构造方法将数据源与数据适配器关联起来
        // context:要使用当前的Adapter的界面对象
        public Paper_Adapter(Context context, List<item_paper> list) {
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
            Paper_Adapter.ViewHolder viewHolder;
            //如果view未被实例化过，缓存池中没有对应的缓存
            if (convertView == null) {
                viewHolder = new Paper_Adapter.ViewHolder();
                // 由于我们只需要将XML转化为View，并不涉及到具体的布局，所以第二个参数通常设置为null
                convertView = mInflater.inflate(R.layout.item_paper, null);

                //对viewHolder的属性进行赋值
                viewHolder.paper_name = (TextView) convertView.findViewById(R.id.paperName);

                //通过setTag将convertView与viewHolder关联
                convertView.setTag(viewHolder);
            }else{//如果缓存池中有对应的view缓存，则直接通过getTag取出viewHolder
                viewHolder = (Paper_Adapter.ViewHolder) convertView.getTag();
            }
            // 取出gallery_entity对象
            item_paper bean = mList.get(position);

            // 设置控件的数据
            viewHolder.paper_name.setText(bean.getpaperDate()+"GRE考卷");
            return convertView;
        }
        // ViewHolder用于缓存控件，三个属性分别对应item布局文件的三个控件
        class ViewHolder{
            public TextView paper_name;
        }
    }
}

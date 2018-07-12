package com.example.work.item;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

/**
 * Created by 杨旺旺 on 2017/12/13.
 */

public class item_paper_content implements Parcelable {
    private int Q_No;
    private String Q_Type;//0是93，1是62，2是51
    private String Q_ID;
    private String Q_Content;
    private ArrayList<String> Q_Options;
    private ArrayList<String> Q_Ans;
    private String Q_correctAns;
    private String MyAns;

    @Override
    public int describeContents()
    {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel out,int flags)
    {
        out.writeInt(Q_No);
        out.writeString(Q_Type);
        out.writeString(Q_ID);
        out.writeString(Q_Content);
        out.writeStringList(Q_Options);
        out.writeStringList(Q_Ans);
        out.writeString(Q_correctAns);
        out.writeString(MyAns);
    }
    public static final Parcelable.Creator<item_paper_content> CREATOR =new Parcelable.Creator<item_paper_content>()
    {
        @Override
        public item_paper_content createFromParcel(Parcel in)
        {
            return new item_paper_content(in);
        }
        @Override
        public item_paper_content[] newArray(int size)
        {
            return new item_paper_content[size];
        }
    };
    private item_paper_content (Parcel in)
    {
        Q_No=in.readInt();
        Q_Type=in.readString();
        Q_ID=in.readString();
        Q_Content=in.readString();
        Q_Options=new ArrayList<String>();
        in.readStringList(Q_Options);
        Q_Ans=new ArrayList<String>();
        in.readStringList(Q_Ans);
        Q_correctAns=in.readString();
        MyAns=in.readString();
    }
    public item_paper_content(int Q_No,String Q_Type,String Q_ID,String Q_Content)
    {
        this.Q_No=Q_No;
        this.Q_Type=Q_Type;
        this.Q_ID=Q_ID;
        this.Q_Content=Q_Content;
        this.Q_Options=new ArrayList<String>();
        this.Q_Ans=new ArrayList<String>();
    }
    public int getQ_No(){return this.Q_No;}

    public String getQ_Type(){return this.Q_Type;}

    public String getQ_ID() {return this.Q_ID;}

    public String getQ_Content() {return this.Q_Content;}

    public String getQ_Options(int index){return this.Q_Options.get(index);}

    public String getQ_Ans(int index){return this.Q_Ans.get(index);}

    public String getQ_correctAns()
    {
        String ans="";
        for(String x: this.Q_Ans)
        {
            ans=ans+x;
        }
        return  ans;
    }

    public String getMyAns(){return  this.MyAns;}

    public void addQ_Opeions(String option){this.Q_Options.add(option);}

    public void addQ_Ans(String ans){this.Q_Ans.add(ans);}

    public void setMyAns(String myans){this.MyAns=myans;}
    public void setQ_correctAns(String i)
    {
        this.Q_correctAns=i;
    }

}


package com.example.work.item;

/**
 * Created by 杨旺旺 on 2017/12/19.
 */

public class item_comment {
    private String co_Id;
    private String co_Time;
    private String co_Content;
    private int co_Nice_num;
    private String co_From;

    public item_comment(String co_Id, String co_Time, String co_Content, int co_Nice_num, String co_From) {
        this.co_Id = co_Id;
        this.co_Time = co_Time;
        this.co_Content = co_Content;
        this.co_Nice_num = co_Nice_num;
        this.co_From = co_From;
    }
    public String getCo_Id() {
        return co_Id;
    }

    public void nice_add_one()
    {
        co_Nice_num=co_Nice_num+1;
        return;
    }

    public void setCo_Id(String co_Id) {
        this.co_Id = co_Id;
    }

    public String getCo_Time() {
        return co_Time;
    }

    public void setCo_Time(String co_Time) {
        this.co_Time = co_Time;
    }

    public String getCo_Content() {
        return co_Content;
    }

    public void setCo_Content(String co_Content) {
        this.co_Content = co_Content;
    }

    public int getCo_Nice_num() {
        return co_Nice_num;
    }

    public void setCo_Nice_num(int co_Nice_num) {
        this.co_Nice_num = co_Nice_num;
    }

    public String getCo_From() {
        return co_From;
    }

    public void setCo_From(String co_From) {
        this.co_From = co_From;
    }
}

package com.example.work.item;

/**
 * Created by 杨旺旺 on 2017/12/8.
 */

public class item_paper {
    private String paperId;
    private String paperDate;
    public item_paper(String ipaperId,String ipaperDate) {
        this.paperId = ipaperId;
        this.paperDate=ipaperDate;
    }

    public String getpaperId() {
        return  paperId;
    }

    public void setpaperId(String ipaperId) {
        this.paperId = ipaperId;
    }

    public String getpaperDate() {
        return paperDate;
    }

    public void setpaperDate(String ipaperDate) {
        this.paperDate = ipaperDate;
    }


}

package com.example.work.item;

/**
 * Created by 杨旺旺 on 2017/12/14.
 */

public class item_word {
   // private String Id;
    private String spell;
    private String chinese;
    private String addTime;

    public item_word(String ispell,String ichinese,String iaddTime) {
       // this.Id=iId;
        this.spell = ispell;
        this.chinese=ichinese;
        this.addTime=iaddTime;
    }

   // public String getId() {
      //  return  Id;
   // }

   // public void setId(String iId) {
     //   this.Id = iId;
  //  }

    public String getSpell() {
        return  spell;
    }

    public void setSpell(String ispell) {
        this.spell = ispell;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String ichinese) {
        this.chinese = ichinese;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String iaddTime) {
        this.addTime = iaddTime;
    }
}

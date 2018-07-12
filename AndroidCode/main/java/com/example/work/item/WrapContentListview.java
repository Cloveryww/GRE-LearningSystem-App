package com.example.work.item;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 杨旺旺 on 2017/12/21.
 */

public class WrapContentListview extends com.baoyz.swipemenulistview.SwipeMenuListView{
    public WrapContentListview(Context context) {
        super(context);
    }

    public WrapContentListview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentListview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

package com.marche.tickerbar;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by Patrick on 11/06/2014.
 */
public class HorizontalListeningScrollView extends HorizontalScrollView {

    private Handler h;
    private int direction = 1;

    public HorizontalListeningScrollView(Context context) {
        super(context);
    }

    public HorizontalListeningScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HorizontalListeningScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setListener(Handler h){
        this.h = h;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        View view = (View) getChildAt(getChildCount()-1);
        view.requestLayout();
        view.invalidate();
        int diff = (view.getRight()-(getWidth() + getScrollX()));
        if( diff <= 0 ){
            if(h != null){
                h.sendEmptyMessage(1);
                invalidate();
            }
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }


}

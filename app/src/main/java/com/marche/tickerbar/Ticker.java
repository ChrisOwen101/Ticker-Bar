package com.marche.tickerbar;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Created by Chris Owen on 25/09/2015.
 */
public class Ticker extends FrameLayout{

    public static final int LEFT = 1;
    public static final int RIGHT = 2;

    private int speed = 4;
    private int direction = 1;

    private boolean addBlankSpace = true;
    private boolean keepOrder = true;

    private TextView text;
    private ImageView textBackground;
    private HorizontalListeningScrollView horizontalScrollView;

    private int count = 0;

    private boolean isRunning = false;

    private List<String> messages = new ArrayList<String>();

    private Handler h;
    private Runnable r;

    public Ticker(Context context) {
        super(context);
        init();
    }

    public Ticker(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Ticker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.ui_ticker, this);

        text = (TextView) this.findViewById(R.id.text);

        horizontalScrollView = (HorizontalListeningScrollView) this.findViewById(R.id.horizontalScrollView);

        horizontalScrollView.setListener(new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                cancelTickers();

                showNext();
                return false;
            }
        }));
        textBackground = (ImageView) this.findViewById(R.id.textBackground);

        h = new Handler();

        text.setSelected(true);
    }

    public void setText(String text){
        if(addBlankSpace){
            String blankString = "                                                                                                                        ";
            this.text.setText(blankString + text + blankString);
        } else {
            this.text.setText(text);
        }

        horizontalScrollView.invalidate();
    }

    public void setMessages(List<String> messages){
        this.messages = messages;
    }

    public void setMessages(String[] messages){
        this.messages = Arrays.asList(messages);
    }

    public void start(){
        if(!isRunning){

            if(!keepOrder){
                Collections.shuffle(messages);
            }

            isRunning = true;
            countdown();
        }
    }

    public void pause(){
        if(isRunning){
            isRunning = false;
            cancelTickers();
        }
    }

    public void stop(){
        if(isRunning){
            isRunning = false;
            cancelTickers();
            text.setText("");
        }
    }

    public boolean isRunning(){
        return isRunning;
    }

    public void setTextColour(int color){
        text.setTextColor(color);
    }

    public void setBackgroundColour(int color){
        textBackground.setBackgroundColor(color);
    }

    public void setTextColour(String color){
        text.setTextColor(Color.parseColor(color));
    }

    public void setBackgroundColour(String color){
        textBackground.setBackgroundColor(Color.parseColor(color));
    }

    public void setDirection(int direction){
        this.direction = direction;
        horizontalScrollView.setDirection(direction);
    }

    public void setTypeFace(Typeface tf){
        this.text.setTypeface(tf);
    }

    public void setAddBlankSpace(boolean addBlankSpace){
        this.addBlankSpace = addBlankSpace;
    }

    private void cancelTickers() {
        if (h != null && r != null) {
            h.removeCallbacks(r);
        }
    }

    private void countdown(){
        cancelTickers();

        horizontalScrollView.invalidate();
        horizontalScrollView.scrollTo(0,0);

        final int delay = 8;

        r = new Runnable() {
            @Override
            public void run() {
                if(direction == LEFT){
                    horizontalScrollView.scrollBy(speed,0);
                    h.postDelayed(this, delay);
                } else if(direction == RIGHT){
                    horizontalScrollView.scrollBy(0,speed);
                    h.postDelayed(this, delay);
                }
            }
        };

        h.postDelayed(r, delay);
    }

    private void showNext(){
        AlphaAnimation alpha = new AlphaAnimation(1,0);
        alpha.setDuration(500);
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                setText(messages.get(count));

                AlphaAnimation alpha = new AlphaAnimation(0, 1);
                alpha.setDuration(500);
                text.startAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        text.startAnimation(alpha);

        countdown();

        count++;

        if(!keepOrder) {
            if (count >= messages.size()) {
                Collections.shuffle(messages);
                count = 0;
            }
        }
    }

}

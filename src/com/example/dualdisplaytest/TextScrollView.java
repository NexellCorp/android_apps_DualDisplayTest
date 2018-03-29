package com.example.dualdisplaytest;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class TextScrollView extends TextView  {
    public final static String TAG = TextScrollView.class.getSimpleName();

    private long  lTimerInterval = 10;

    private float step = 0f;
    private float y = 0f;
    private float temp_view_plus_text_length = 0.0f;
    private float temp_view_plus_two_text_length = 0.0f;
    public boolean isStarting = false;
    private Paint paint = null;
    private String text = "";


    private float speed = 1.0f;
    private int textColor = 0xFFFFFFFF;

    private int nViewWidth = 720;
    // private int nViewHeight = 30;
    private int nViewHeight = 80;

    private Rect mRectSrc = new Rect();

    private Bitmap mBitmap = null;

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int color) {
        this.textColor = color;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public TextScrollView(Context context) {
        super(context);
        initView();

    }

    public TextScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public TextScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();

    }

    private void initView() {
        //	setOnClickListener(this);
        mRectSrc.left = 0;
        mRectSrc.right = mRectSrc.left + nViewWidth;
        // mRectSrc.top = 100;
        mRectSrc.top = 0;
        mRectSrc.bottom = mRectSrc.top + nViewHeight;

    }

    public void init() {
        paint = super.getPaint();

        text = getText().toString();

        step = 0;
        y = getTextSize() + getPaddingTop();
        paint.setColor(textColor);
        if (mBitmap == null) {
            mBitmap = textAsBitmap(text, 20);
            // mBitmap = textAsBitmap(text, 50);
        }
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what > 0) {
                    if ( isStarting )
                    {
                        invalidate(mRectSrc);
                    }
                } else {
                }
            }
        };
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 10;
                handler.sendMessage(msg);
            }
        }, 200, lTimerInterval);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (mBitmap != null)
        {
            canvas.drawBitmap(mBitmap, nViewWidth-step, y - 14, paint);
        }
        step += speed;
        if (step > (temp_view_plus_text_length))
            step = 0;
    }

    public Bitmap textAsBitmap(String text, float textSize) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float nTextLength = paint.measureText(text);

        int width = (int) (paint.measureText(text) + 0.5f); // round
        float baseline = (int) (-paint.ascent() + 0.5f); // ascent() is negative
        int height = (int) (baseline + paint.descent() + 0.5f );
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        temp_view_plus_text_length = nViewWidth + nTextLength;
        temp_view_plus_two_text_length = nViewWidth + nTextLength * 2 ;

        Log.v("hemm",  "temp_view_plus_text_length = " + temp_view_plus_text_length +
                ", width = " + width + ", temp_view_plus_two_text_length = " + temp_view_plus_two_text_length );

        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }

    public void startScroll() {
        isStarting = true;
        invalidate(mRectSrc);
    }

    public void stopScroll() {
        isStarting = false;
        invalidate(mRectSrc);
    }
}

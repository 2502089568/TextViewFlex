package com.example.zz.textviewflex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final int SHRINK_UP_STATE = 1;// 收起状态
    private static final int SPREAD_STATE = 2;// 展开状态
    private static int mState = SHRINK_UP_STATE;//默认收起状态

    private TextView mContentText;// 展示文本内容
    private RelativeLayout mShowMore;// 展示更多
    private ImageView mImageSpread;// 展开
    private ImageView mImageShrinkUp;// 收起

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initData();
    }

    private void initView() {
        mContentText = (TextView) findViewById(R.id.text_content);
        mShowMore = (RelativeLayout) findViewById(R.id.show_more);
        mImageSpread = (ImageView) findViewById(R.id.spread);
        mImageShrinkUp = (ImageView) findViewById(R.id.shrink_up);
    }

    private void initData() {
        mContentText.setText(R.string.txt_info);
        //mContentText.setText(R.string.txt_info1);
        //mContentText.setText(R.string.txt_info2);
        //设置最大显示3行
        mContentText.setHeight(mContentText.getLineHeight()*3);
        ViewTreeObserver vto = mContentText.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int m = mContentText.getLineCount();
                Log.d("zzzz", String.valueOf(mContentText.getLineCount()));
                if(m >3){
                    mShowMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mClick(m);
                        }
                    });
                } else{
                    mShowMore.setVisibility(View.GONE);
                }
                return true;
            }
        });

    }

    public void mClick(int m) {
        //布局中可以不用maxLine会控制行高，可直接设置TextView的高度为指定行数*行高
        final int deltaValue;//默认高度，即前边由maxLine确定的高度
        final int startValue = mContentText.getHeight();//起始高度
        int durationMillis = 350;
            if (mState == SPREAD_STATE) {//展开状态
                deltaValue=mContentText.getLineHeight()*3-startValue;
                RotateAnimation animation = new RotateAnimation(180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setDuration(durationMillis);
                mImageSpread.startAnimation(animation);
                mState = SHRINK_UP_STATE;
               /* mContentText.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                mContentText.requestLayout();
                mImageShrinkUp.setVisibility(View.GONE);
                mImageSpread.setVisibility(View.VISIBLE);
                mState = SHRINK_UP_STATE;*/
            } else if (mState == SHRINK_UP_STATE) {//收起状态
                deltaValue = mContentText.getLineHeight()*mContentText.getLineCount()-startValue;
                RotateAnimation animation = new RotateAnimation(0,180, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                animation.setDuration(durationMillis);
                animation.setFillAfter(true);//设为true之后，界面会停留在动画播放完时的界面。默认和setFillBefore 动画结束后，View会回到原来的位置
                mImageSpread.startAnimation(animation);
                mState = SPREAD_STATE;
                /*mContentText.setMaxLines(Integer.MAX_VALUE);
                mContentText.requestLayout();
                mImageShrinkUp.setVisibility(View.VISIBLE);
                mImageSpread.setVisibility(View.GONE);
                mState = SPREAD_STATE;*/
            }else{deltaValue = startValue;}

        Animation animation=new Animation() {
            //根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mContentText.setHeight((int)(startValue+ deltaValue *interpolatedTime));
            }
        };
            animation.setDuration(durationMillis);
            mContentText.startAnimation(animation);
    }
}

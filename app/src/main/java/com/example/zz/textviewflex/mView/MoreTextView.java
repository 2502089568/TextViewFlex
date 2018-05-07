package com.example.zz.textviewflex.mView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zz.textviewflex.R;

public class MoreTextView extends LinearLayout {

    protected TextView contentView; //文本正文
    protected ImageView expandView; //展开按钮

    //对应styleable中的属性
    protected int textColor;
    protected float textSize;
    protected int maxLine;
    protected String text;

    //默认属性值
    public int defaultTextColor =Color.argb(3,3,3,3);
    public int defaultTextSize = 12;
    public int defaultLine = 3;

    public MoreTextView(Context context, AttributeSet attrs) {
        super(context);
        initalize();//初始化并添加View。初始化TextView和ImageView,并添加到MoretextView中去。
        initWithAttrs(context,attrs);//取值并设置。利用attrs从xml布局中取我们配置好的text/textSize/textColor/maxLine等属性的属性值，并设置到View上去。
        bindListener();//绑定点击事件并设置动画。 给当前MoreTextView设置点击事件，实现点击折叠和展开。
    }

    private void initalize() {
        setOrientation(VERTICAL);//垂直布局
        setGravity(Gravity.RIGHT);//右对齐
        contentView = new TextView(getContext());
        addView(contentView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        expandView = new ImageView(getContext());
        int padding = dip2px(getContext(),5);//将dip或dp值转换为px值，保证尺寸大小不变
        expandView.setPadding(padding,padding,padding,padding);
        expandView.setImageResource(R.drawable.ic_details_more);
        LinearLayout.LayoutParams a = new LinearLayout.LayoutParams(80,80);
        addView(expandView,a);
    }

    protected void initWithAttrs(Context context,AttributeSet attrs){
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.MoreTextStyle);
        int textColor = a.getColor(R.styleable.MoreTextStyle_textColor,defaultTextColor);//后面为默认值
        textSize = a.getDimensionPixelSize(R.styleable.MoreTextStyle_textSize,defaultTextSize);
        maxLine = a.getInt(R.styleable.MoreTextStyle_maxLine,defaultLine);
        text = a.getString(R.styleable.MoreTextStyle_text);
        bindTextView(textColor,textSize,maxLine,text);
        a.recycle();
    }
    //绑定到textView
    protected void bindTextView(int color,float size,final int line,String text){
        contentView.setTextColor(color);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);
        contentView.setText(text);
        contentView.setHeight(contentView.getLineHeight() * line);
        post(new Runnable() {//前面已讲，不再赘述
            @Override
            public void run() {
                expandView.setVisibility(contentView.getLineCount() > line ? View.VISIBLE : View.GONE);

            }
        });
    }

    //点击展开与折叠，不再赘述
    protected void bindListener(){
        setOnClickListener(new View.OnClickListener() {
            boolean isExpand;
            @Override
            public void onClick(View v) {
                isExpand = !isExpand;
                contentView.clearAnimation();
                final int deltaValue;
                final int startValue = contentView.getHeight();
                int durationMillis = 350;
                if (isExpand) {
                    deltaValue = contentView.getLineHeight() * contentView.getLineCount() - startValue;
                    RotateAnimation animation = new RotateAnimation(0, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandView.startAnimation(animation);
                } else {
                    deltaValue = contentView.getLineHeight() * maxLine - startValue;
                    RotateAnimation animation = new RotateAnimation(180, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    expandView.startAnimation(animation);
                }
                Animation animation = new Animation() {
                    protected void applyTransformation(float interpolatedTime, Transformation t) {
                        contentView.setHeight((int) (startValue + deltaValue * interpolatedTime));
                    }
                };
                animation.setDuration(durationMillis);
                contentView.startAnimation(animation);
            }
        });
    }

    public TextView getTextView(){
        return contentView;
    }

    public void setText(CharSequence charSequence){
        contentView.setText(charSequence);
    }

    public static int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
}

package com.hehuidai.customview.one.animtextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;

/**
 * @ProjectName: CustomViewDemo
 * @Package: com.hehuidai.customview.one.animtextview
 * @ClassName: TextAnim
 * @Description: java类作用描述 ：
 * @Author: 作者名：lml
 * @CreateDate: 2019/7/16 9:30
 * @UpdateUser: 更新者：
 * @UpdateDate: 2019/7/16 9:30
 * @UpdateRemark: 更新说明：
 * @Version: 1.0
 */

public class TextAnim extends View {
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String mText = "布鲁克S25";
    private Path mDst = new Path();
    private float mCurrentProcess = 0F;
    private int mTextWidth = 0;
    private float mTextSize = 80F;
    private int heightCount = 1;//行数
    private ArrayList<PathMeasure> mPathMeasureList = new ArrayList();
    private PathMeasure mPathMeasure;
    private int mPathMeasureIndex = 0;//PathMeasure的Index
    private void init(){
        mPaint.setStrokeWidth(2F);
        mPaint.setColor(Color.BLUE);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle( Paint.Style.STROKE);
    }
    public TextAnim(Context context) {
        this(context,null);
        init();
    }

    public TextAnim(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public TextAnim(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    void setText(String text){
        //重置了路径
        mDst.reset();
        mText = text;
        Path tempPath = new Path();
        mTextWidth = (int) mPaint.measureText(mText);
        heightCount =(int) (mPaint.measureText(mText) / getWidth() )+ 1;
        mPathMeasureList.clear();
        for (int i = 0;i<heightCount ;i++){
            mPaint.getTextPath(mText,mText.length()/3 * i,mText.length() * (i+1),0F,mTextSize,tempPath);
            mPathMeasureList.add(new PathMeasure(tempPath,false));
        }
        requestLayout();//重新布局
        mPathMeasureIndex = 0;
        mPathMeasure = mPathMeasureList.get(mPathMeasureIndex);
        startAnim();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec),measuredHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Path tempPath = new Path();
        mTextWidth = (int) mPaint.measureText(mText);
        heightCount = (int) (mPaint.measureText(mText) / w )+ 1;
        mPathMeasureList.clear();
        LogUtils.eTag("测试","mTextWidth:"+mTextWidth+"===width"+w+"====heightCount"+heightCount);
        for (int i = 0;i<heightCount ;i++){
            mPaint.getTextPath(mText,mText.length()/3 * i,mText.length() * (i+1),0F,mTextSize,tempPath);
            mPathMeasureList.add(new PathMeasure(tempPath,false));
        }
        mPathMeasureIndex = 0;
        mPathMeasure = mPathMeasureList.get(mPathMeasureIndex);
        startAnim();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathMeasure.getSegment(0F,mPathMeasure.getLength()*mCurrentProcess,mDst,true);//获取路径片段
        canvas.drawPath(mDst,mPaint);//绘制路径
        if(mCurrentProcess>=1){//每当mCurrentProcess的值超过1的时候就换下一段封闭的路径
            if(mPathMeasure.nextContour()){
                startAnim();
            }else if(mPathMeasureIndex<mPathMeasureList.size()){
                mPathMeasureIndex++;
                mPathMeasure = mPathMeasureList.get(mPathMeasureIndex);
            }
        }
    }


    /**
     * 开始动画
     */
    void startAnim(){
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0f, 1f);
        ofFloat.setDuration((long) (mPathMeasure.getLength() / 120 *1000));
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentProcess = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ofFloat.start();
    }
    /**
     * 测量宽
     *
     * @param widthMeasureSpec
     */
    private int measureWidth(int widthMeasureSpec) {
        int result;
        int specMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int specSize = View.MeasureSpec.getSize(widthMeasureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = mTextWidth;
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
     * 测量高
     *
     * @param heightMeasureSpec
     */
    private int measuredHeight(int heightMeasureSpec) {
        int result;
        int specMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int specSize = View.MeasureSpec.getSize(heightMeasureSpec);
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) ((mPaint.getFontMetrics().bottom - mPaint.getFontMetrics().top)*heightCount);
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }
}

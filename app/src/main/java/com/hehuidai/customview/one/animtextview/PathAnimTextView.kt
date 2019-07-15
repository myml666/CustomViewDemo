package com.hehuidai.customview.one.animtextview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one.animtextview
 * @ClassName:      PathAnimTextView
 * @Description:     java类作用描述 ：
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/15 14:21
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 14:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class PathAnimTextView: View {
    private val mPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mText = "布鲁克S25"
    private val mDst:Path = Path()
    private var mCurrentProcess = 0F
    private var mTextWidth:Int = 0
    private var mTextSize:Float = 80F
    private var heightCount:Int = 1//行数
    private val mPathMeasureList:ArrayList<PathMeasure> = ArrayList()
    init {
        mPaint.strokeWidth = 2F
        mPaint.color = Color.BLUE
        mPaint.textSize = mTextSize
        mPaint.style = Paint.Style.STROKE
        val tempPath = Path()
        mTextWidth = mPaint.measureText(mText).toInt()
        heightCount = (mPaint.measureText(mText) / width ).toInt()+ 1
        for (i in 0..heightCount){
            mPaint.getTextPath(mText,mText.length/3 * i,mText.length * (i+1),0F,mTextSize,tempPath)
            mPathMeasureList.add(PathMeasure(tempPath,false))
        }
    }
    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun setText(text:String){
        //重置了路径
        mDst.reset()
        mText = text
        val tempPath = Path()
        mTextWidth = mPaint.measureText(mText).toInt()
        heightCount = (mPaint.measureText(mText) / width ).toInt()+ 1
        mPathMeasureList.clear()
        for (i in 0..heightCount){
            mPaint.getTextPath(mText,mText.length/3 * i,mText.length * (i+1),0F,mTextSize,tempPath)
            mPathMeasureList.add(PathMeasure(tempPath,false))
        }
        requestLayout()//重新布局
        startAnim()
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec),measuredHeight(heightMeasureSpec))
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startAnim()
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mPathMeasure.getSegment(0F,mPathMeasure.length*mCurrentProcess,mDst,true)//获取路径片段
        canvas!!.drawPath(mDst,mPaint)//绘制路径
        if(mCurrentProcess>=1){//每当mCurrentProcess的值超过1的时候就换下一段封闭的路径
            if(mPathMeasure.nextContour()){
               startAnim()
            }
        }
    }

    /**
     * 开始动画
     */
    fun startAnim(){
        val ofFloat = ValueAnimator.ofFloat(0f, 1f)
        ofFloat.duration = (mPathMeasure.length / 120 *1000).toLong()
        ofFloat.addUpdateListener {
            mCurrentProcess = it.animatedValue as Float
            invalidate()
        }
        ofFloat.start()
    }
    /**
     * 测量宽
     *
     * @param widthMeasureSpec
     */
    private fun measureWidth(widthMeasureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val specSize = View.MeasureSpec.getSize(widthMeasureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = mTextWidth
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }

    /**
     * 测量高
     *
     * @param heightMeasureSpec
     */
    private fun measuredHeight(heightMeasureSpec: Int): Int {
        var result: Int
        val specMode = View.MeasureSpec.getMode(heightMeasureSpec)
        val specSize = View.MeasureSpec.getSize(heightMeasureSpec)
        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize
        } else {
            result = (mPaint.fontMetrics.bottom - mPaint.fontMetrics.top).toInt()*heightCount
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
}
package com.hehuidai.customview.animtextview2

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.animtextview2
 * @ClassName:      AnimTextView2
 * @Description:     java类作用描述 ：逐字显示动画效果
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/25 16:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/25 16:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class AnimTextView2:View {
    private val mPaint: Paint
    private var mTextSize:Float = 30F//文字大小
    private var mTextColor:Int = Color.RED//文字颜色
    private var mSingleTextAnimDuration:Int = 300//单个字符的动画时间
    private var mTextStrokWidth:Float = 2F//文字画笔宽度
    private val mTextLineCountList: ArrayList<Int> = ArrayList()//存储每行字符长度的集合
    private var mText:String = "我是动画文字"
    private var mAnimProgress:Int = 0//动画进度值
    private var mTextLine:Int = 1//文字动画到第几行
    private var mAnimator:ValueAnimator? = null
    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.textSize = mTextSize
        mPaint.color = mTextColor
        mPaint.strokeWidth = mTextStrokWidth
//        mPaint.typeface = Typeface.createFromAsset(context.assets, "Muyao-Softbrush.ttf")//设置文字字体
    }
    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(View.MeasureSpec.getSize(widthMeasureSpec),measuredHeight(heightMeasureSpec))
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
            result = ((mPaint.fontMetrics.bottom-mPaint.fontMetrics.top)*mTextLine+getBaseline(mPaint)).toInt()
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
    /**
     * 设置动画的文字
     */
    fun setAnimText(animText:String){
        mText = animText
        calcStrlineCount()
        requestLayout()//重新计算控件高度
    }
    /**
     * 开始动画
     */
    fun startAnim(){
        mAnimator = ValueAnimator.ofInt(1, mText.length)
        mAnimator!!.interpolator = LinearInterpolator()//匀速动画
        mAnimator!!.duration = (mSingleTextAnimDuration * mText.length).toLong()
        mAnimator!!.addUpdateListener {
            mAnimProgress = it.animatedValue as Int
            invalidate()
        }
        mAnimator!!.start()
    }
    /**
     * 获取到换行字符索引的集合以及文字的行数
     */
    fun calcStrlineCount() {
        val textWidths = FloatArray(mText.length + 1)
        mPaint.getTextWidths(mText, 0, mText.length, textWidths)
        mTextLineCountList.clear()//清空集合
        mTextLineCountList.add(0)//添加元素0方便我们在画文字的时候的操作
        var tempTextWidthSum = 0f
        for (i in 0..(textWidths.size - 1)) {
            tempTextWidthSum += textWidths[i]
            if (tempTextWidthSum >width ) {
                mTextLineCountList.add(i)//存储字符的索引值
                tempTextWidthSum = textWidths[i]//因为当前已经比控件宽度大了所以在重新计算的时候需要将这个字符的宽度加进去（因为这已经是第二行了）
            }
        }
        mTextLine = if (mPaint.measureText(mText)/width>(mPaint.measureText(mText)/width).toInt()){//计算出文字的行数（如果文字的宽度除以控件的宽度是整数的话，就说明文字行数为文字除以控件宽度的值，反之则文字的行数还得再加1）
            (mPaint.measureText(mText)/width).toInt()+1
        }else{
            (mPaint.measureText(mText)/width).toInt()
        }
    }

    /**
     * 计算绘制文字时的基线到中轴线的距离
     *
     * @param p
     * @param centerY
     * @return 基线和centerY的距离
     */
    private fun getBaseline(p: Paint): Float {
        val fontMetrics = p.fontMetrics
        return (fontMetrics.descent - fontMetrics.ascent) / 2 - fontMetrics.descent
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(mTextLineCountList.size>0){
            for(i in 1..mTextLine){//遍历行数（行数从1开始）
                if(mAnimProgress>mTextLineCountList[i-1]){
                    canvas!!.drawText(mText,mTextLineCountList[i-1],mAnimProgress,0f,(mPaint.fontMetrics.bottom-mPaint.fontMetrics.top)*i,mPaint)
                }
            }
        }
    }
}
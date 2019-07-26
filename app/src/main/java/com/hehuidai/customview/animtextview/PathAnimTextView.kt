package com.hehuidai.customview.animtextview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import com.hehuidai.customview.R

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.animtextview
 * @ClassName:      PathAnimTextView
 * @Description:     java类作用描述 ：动画文字
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/15 14:21
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 14:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class PathAnimTextView : View {
    companion object {
        const val SPEED_FAST:Int = 0//快速
        const val SPEED_SLOW:Int = 1//慢速
        const val SPEED_MEDIUM:Int = 2//中速
    }
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mText = "Animation Text"
    private var mAnimSpeed = SPEED_MEDIUM//动画速度(默认中速)
    private val mDst: Path = Path()//存储用于展示动画的路径（即PathMeasure截取的路径片段）
    private var mTextColor:Int = Color.BLUE//文字的颜色
    private var mCurrentProcess = 0F//动画的进度
    private var mTextWidth: Int = 1//文字宽度
    private var mTextSize: Float = 80F//文字大小
    private var heightCount: Int = 1//文字的行数
    private var mTextStrokWidth = 2f//文字画笔的宽度
    private var mTextTypeFace = ""
    private var mTextLineMargen = 0f
    private val mPathMeasureList: ArrayList<PathMeasure> = ArrayList()
    private var mPathMeasure: PathMeasure? = null
    private var isWrapContent: Boolean = false//是否是自适应
    private var mPathMeasureIndex: Int = 0//PathMeasure的Index
    private var mAnimation:ValueAnimator? = null//用来存储动画对象，必要时需要停止动画
    private val mTextLineCountList: ArrayList<Int> = ArrayList()//存储每行字符长度的集合
    constructor(context: Context) : super(context){
        initParams(null)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initParams(attrs)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initParams(attrs)
    }
    /**
     * 加载控件参数
     */
    @SuppressLint("Recycle")
    fun initParams(attrs: AttributeSet?){
        if(attrs!=null){
            val ta = context.obtainStyledAttributes(attrs, R.styleable.PathAnimTextView)
            mTextColor = ta.getColor(R.styleable.PathAnimTextView_animTextColor, Color.BLUE)
            mTextSize = ta.getFloat(R.styleable.PathAnimTextView_animTextSize,mTextSize)
            mTextStrokWidth = ta.getFloat(R.styleable.PathAnimTextView_animTextStrokWidth,mTextStrokWidth)
            val animText = ta.getString(R.styleable.PathAnimTextView_animText)
            if(!TextUtils.isEmpty(animText)){
                mText = animText
            }
            val  typeValue = TypedValue()
            ta.getValue(R.styleable.PathAnimTextView_animSpeed, typeValue)
            mAnimSpeed = typeValue.data
            val animTextTypeFace = ta.getString(R.styleable.PathAnimTextView_animTextTypeFace)
            if(!TextUtils.isEmpty(animTextTypeFace)){
                mTextTypeFace = animTextTypeFace
            }
            mTextLineMargen = ta.getFloat(R.styleable.PathAnimTextView_animTextLineMargen,mTextLineMargen)
        }
        mPaint.strokeWidth = mTextStrokWidth
        mPaint.color = mTextColor
        mPaint.textSize = mTextSize
        if(!TextUtils.isEmpty(mTextTypeFace)){
            mPaint.typeface = Typeface.createFromAsset(context.assets, mTextTypeFace)
        }
        mPaint.style = Paint.Style.STROKE//这里必须为STROKE否则文字会变得不清楚
        mTextWidth = mPaint.measureText(mText).toInt()//计算出初始状态的字符串宽度
    }

    /**
     * 设置文字
     */
    fun setText(text: String) {
        mText = text
    }

    /**
     * 计算每行的字符的个数
     */
    fun calcStrlineCount() {
        val textWidths = FloatArray(mText.length + 1)
        mPaint.getTextWidths(mText, 0, mText.length, textWidths)
        mTextLineCountList.clear()//清空集合
        var tempTextWidthSum = 0f
        var tempViewWidth = 0
        tempViewWidth = if(isWrapContent){
            if(mTextWidth>(parent as ViewGroup).width){
                (parent as ViewGroup).width
            }else{
                mTextWidth
            }
        }else{
            width
        }
        for (i in 0..(textWidths.size - 1)) {
            tempTextWidthSum += textWidths[i]
            if (tempTextWidthSum >tempViewWidth ) {
                mTextLineCountList.add(i)//存储字符的索引值
                tempTextWidthSum = textWidths[i]//因为当前已经比控件宽度大了所以在重新计算的时候需要将这个字符的宽度加进去（因为这已经是第二行了）
            }
        }
        mTextLineCountList.add(mText.length)//将最后的字符索引加进去，不然会缺字
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
//        当与窗口分离的时候取消动画
        if(mAnimation!=null){
            mAnimation!!.cancel()
        }
    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measuredHeight(heightMeasureSpec))
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initTextPath()
    }

    /**
     * 开始动画（用于手动开启）
     */
    fun startTextAnim(){
        mTextWidth = mPaint.measureText(mText).toInt()
        requestLayout()
        initTextPath()
        startAnim()
    }
    /**
     * 加载文字路径并且开启动画
     */
    private fun initTextPath(){
        //重置了路径
        mDst.reset()
        val tempPath = Path()
        calcStrlineCount()
        heightCount = mTextLineCountList.size
        mPathMeasureList.clear()
        val textTopMargen = (height - mTextSize*heightCount-(mPaint.fontMetrics.ascent - mPaint.fontMetrics.top)*2)/heightCount
        for (i in 0..(heightCount - 1)) {
            if (i > 0) {
                mPaint.getTextPath(mText, mTextLineCountList[i - 1], mTextLineCountList[i], 0F, mTextSize * (i + 1)+textTopMargen*i, tempPath)
            } else {
                mPaint.getTextPath(mText, 0, mTextLineCountList[i], 0F, mTextSize * (i + 1), tempPath)
            }
            mPathMeasureList.add(PathMeasure(tempPath, false))
        }
        mPathMeasureIndex = 0
        mPathMeasure = mPathMeasureList[mPathMeasureIndex]
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if(mPathMeasure!=null){
            mPathMeasure!!.getSegment(0F, mPathMeasure!!.length * mCurrentProcess, mDst, true)//获取路径片段
            canvas!!.drawPath(mDst, mPaint)//绘制路径
            if (mCurrentProcess >= 1) {//每当mCurrentProcess的值超过1的时候就换下一段封闭的路径
                if (mPathMeasure!!.nextContour()) {//如果当前的PathMeasure还有闭合的路径则跳转到下一个路径
                    startAnim()
                } else if (mPathMeasureIndex < mPathMeasureList.size - 1) {//如果当前的PathMeasure没有闭合的路径则跳转到下一个PathMeasure
                    mPathMeasureIndex++
                    mPathMeasure = mPathMeasureList[mPathMeasureIndex]
                    startAnim()
                }
            }
        }
    }

    /**
     * 开始动画
     */
    private fun startAnim() {
        mAnimation = ValueAnimator.ofFloat(0f, 1f)
        //根据路径长度计算出动画时间
        when(mAnimSpeed) {//这里是根据不同的设置来计算速度
            SPEED_FAST -> mAnimation!!.duration = (mPathMeasure!!.length / 600 * 1000).toLong()
            SPEED_SLOW -> mAnimation!!.duration = (mPathMeasure!!.length / 200 * 1000).toLong()
            SPEED_MEDIUM -> mAnimation!!.duration = (mPathMeasure!!.length / 400 * 1000).toLong()
        }
        mAnimation!!.addUpdateListener {
            mCurrentProcess = it.animatedValue as Float
            invalidate()
        }
        mAnimation!!.start()
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
            isWrapContent = true
            result = mTextWidth//自适应状态下将控件的宽度设置为字符串的宽度
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
            result = ((mPaint.fontMetrics.bottom-mPaint.fontMetrics.top)*heightCount+mTextLineMargen*(heightCount-1)).toInt()
            if (specMode == View.MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize)
            }
        }
        return result
    }
}
package com.hehuidai.customview.one.histogramview

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hehuidai.customview.R


/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one
 * @ClassName:      HistogramView
 * @Description:     java类作用描述 ：直方图控件
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/8 15:28
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/8 15:28
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class HistogramView : View {
    private var paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mPaintText:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMargen: Float = 40F//直方图的边距
    private var mCsysPath: Path = Path()//坐标系的路径
    private var mRectList:ArrayList<HistogramRectItem>? = null//存储矩形形状的集合
    private var mDescTextSize:Float = 20F//每个柱状的文字描述的文字大小
    private var mIsOpenAnim:Boolean = false //是否开启动画
    private var mHistogramList:ArrayList<RectF>? = null //存储柱状图的集合

    private var mRectColor:Int = Color.BLUE //柱形图的颜色
    private var mAxisColor:Int = Color.BLUE //轴线的颜色
    private var mRectDescTextColor:Int = Color.BLUE //柱形图文字描述的颜色
    init {
        paint.strokeWidth = 3F
        paint.color = mAxisColor
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.style = Paint.Style.STROKE

        mPaintText.textSize = mDescTextSize
        mPaintText.textAlign = Paint.Align.CENTER
        mPaintText.strokeWidth = 3F
        mPaintText.color = mRectDescTextColor
    }

    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
        initAttr(attrs)

    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        initAttr(attrs)
    }
    /**
     * 加载自定义属性
     */
    @SuppressLint("Recycle")
    fun initAttr(attrs: AttributeSet?){
        val ta = context.obtainStyledAttributes(attrs, R.styleable.HistogramView)
        mAxisColor = ta.getColor(R.styleable.HistogramView_axisColor, mAxisColor)
        mRectColor = ta.getColor(R.styleable.HistogramView_rectColor,mRectColor)
        mRectDescTextColor = ta.getColor(R.styleable.HistogramView_rectDescTextColor,mRectDescTextColor)
        mDescTextSize = ta.getFloat(R.styleable.HistogramView_rectTextSize,mDescTextSize)
        mPaintText.textSize = mDescTextSize
    }
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initCsysPath()
    }

    /**
     * 加载X、Y轴路径
     */
    private fun initCsysPath(){
        mCsysPath.reset()//重置路径
//      ==========画X轴的箭头===========
        mCsysPath.moveTo(mMargen - 10, mMargen + 10)
        mCsysPath.lineTo(mMargen, mMargen)
        mCsysPath.lineTo(mMargen + 10, mMargen + 10)
        mCsysPath.moveTo(mMargen, mMargen)
//      ================================
//      ==========画X轴=================
        mCsysPath.lineTo(mMargen,height - mMargen)
//      ================================
//      ==========画Y轴=================
        mCsysPath.lineTo(width - mMargen,height - mMargen)
//      ================================
//      ==========画Y轴箭头=============
        mCsysPath.lineTo(width - mMargen - 10,height - mMargen - 10)
        mCsysPath.moveTo(width - mMargen,height - mMargen)
        mCsysPath.lineTo(width - mMargen - 10,height - mMargen + 10)
//      ================================
    }

    /**
     * 设置轴线颜色
     */
    fun setAxisColor(color:Int){
        mAxisColor = color
        invalidate()//重绘
    }
    /**
     * 设置柱形图颜色
     */
    fun setRectColor(color:Int){
        mRectColor = color
        invalidate()//重绘
    }
    /**
     * 设置柱形图底部描述文字颜色
     */
    fun setRectDescTextColor(color:Int){
        mPaintText.color = color
        invalidate()//重绘
    }
    /**
     * 设置柱形图底部描述文字大小
     */
    fun setDescTextSize(textSize:Float){
        mDescTextSize = textSize
        mPaintText.textSize = mDescTextSize
        for(histioramitem in mRectList!!){
            histioramitem.pointF!!.y = height - mMargen + mDescTextSize //重新设置文字距离轴线的距离
        }
        invalidate()//重绘
    }
    /**
     * 加载柱形图路径
     * @param rectList 柱形图的数据
     * @param maxVal 柱形图的最大高度
     * @param isOpenAnim 是否开启动画
     * @param animDuration 动画的时长 不开启动画填0即可
     */
    fun initRectPath(rectList:List<HistogramRectItem>,maxVal:Float,isOpenAnim:Boolean,animDuration:Long){
        mIsOpenAnim = isOpenAnim
        mRectList = rectList as ArrayList<HistogramRectItem>
        val yLength = width - (mMargen*2)//Y轴的长度
        val xLength = height - (mMargen*2)//X轴的长度
        val yRealUseLength = yLength - (20*(rectList.size+1))//Y轴实际可用长度为总长度减去各个柱形的间隔20
        val rectItemWidth = yRealUseLength/rectList.size//每个柱形图的宽度
        val partHeight = xLength/maxVal//一段柱形图的高度
        if(mHistogramList == null){
            mHistogramList = ArrayList()
        }
        mHistogramList!!.clear()//清空柱形图
        //将计算出来的柱形图添加到路径当中
        if(mIsOpenAnim){//判断是否是开启动画的状态
            for ((i,rect) in mRectList!!.withIndex()){
                val left = mMargen + 20 + 20 * i + i * rectItemWidth
                val top = height -  partHeight * rect.rectValue - mMargen
                val right = left + rectItemWidth
                val bottom = height - mMargen
                val rectF = RectF(left,top,right,bottom)
                rect.pointF = PointF(left+rectItemWidth/2,bottom + mDescTextSize)
                mHistogramList!!.add(rectF)
//              设置柱形图的动画
                val valueAnimator = ValueAnimator.ofFloat(height - mMargen,top)
                valueAnimator.duration = animDuration
                valueAnimator.addUpdateListener {
                    val value:Float = it.animatedValue as Float
                    rectF.top = value
                    invalidate()
                }
                valueAnimator.start()
            }
        }else{
            for ((i,rect) in mRectList!!.withIndex()){
                val left = mMargen + 20 + 20 * i + i * rectItemWidth
                val top = height -  partHeight * rect.rectValue - mMargen
                val right = left + rectItemWidth
                val bottom = height - mMargen
                val rectF = RectF(left,top,right,bottom)
                rect.pointF = PointF(left+rectItemWidth/2,bottom + mDescTextSize)
                mHistogramList!!.add(rectF)
            }
            invalidate()
        }
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.style = Paint.Style.STROKE
        paint.color = mAxisColor //将画笔颜色设置为轴线颜色
        canvas.drawPath(mCsysPath, paint)
        paint.style = Paint.Style.FILL
        paint.color = mRectColor //将画笔颜色设置为柱形图颜色
        if(mHistogramList!=null){
            for(rect in mHistogramList!!){
                canvas.drawRect(rect,paint)
            }
        }
//        画柱状图文字描述
        if(mRectList!=null){
            for(histogramitem in mRectList!!){
                canvas.drawText(histogramitem.rectText,histogramitem.pointF!!.x,histogramitem.pointF!!.y,mPaintText)
            }
        }
    }
}
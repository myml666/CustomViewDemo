package com.hehuidai.customview.one.shadownview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.blankj.utilcode.util.ConvertUtils

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one.shadownview
 * @ClassName:      ShadownLayout
 * @Description:     java类作用描述 ：阴影布局
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/10 15:40
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/10 15:40
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ShadownLayout:RelativeLayout {
    private var mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMargen:Float = ConvertUtils.dp2px(8F).toFloat()//留出8dp的阴影位置
    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)//关闭硬件加速
        mPaint.setShadowLayer(mMargen,0F,0F, Color.parseColor("#ff0000"))//设置画笔的阴影
        mPaint.color = Color.parseColor("#ffffff")//设置画笔的颜色
        mPaint.style = Paint.Style.FILL//设置画笔样式为填充
    }
    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    override fun dispatchDraw(canvas: Canvas?) {
        canvas!!.drawRoundRect(RectF(mMargen,mMargen, width-mMargen,height-mMargen),mMargen,mMargen,mPaint)//在绘制子控件之前绘制阴影，否则会遮住子控件
        super.dispatchDraw(canvas)
    }
}
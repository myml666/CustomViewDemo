package com.hehuidai.customview.one.shadownview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.blankj.utilcode.util.ConvertUtils
import com.hehuidai.customview.R

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one.shadownview
 * @ClassName:      ShadownView
 * @Description:     java类作用描述 ：阴影控件
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/10 14:21
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/10 14:21
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ShadownView:View {
    private var mPaint:Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mMargen:Float = ConvertUtils.dp2px(15F).toFloat()
    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        mPaint.setShadowLayer(10F,0F,0F, Color.parseColor("#ff0000"))
        mPaint.color = Color.parseColor("#ffffff")
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 1F
    }
    constructor(context: Context) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas!!.drawRoundRect(RectF(mMargen,mMargen, width-mMargen,height-mMargen),10F,10F,mPaint)
    }
}
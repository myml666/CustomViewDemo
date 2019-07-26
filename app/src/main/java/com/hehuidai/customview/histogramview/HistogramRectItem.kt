package com.hehuidai.customview.histogramview

import android.graphics.PointF

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.histogramview
 * @ClassName:      HistogramRectItem
 * @Description:     java类作用描述 ：柱形图的实体类
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/8 16:17
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/8 16:17
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class HistogramRectItem {
    var rectValue:Float = 0f
    var rectText:String = ""
    var pointF:PointF? = null
    constructor()
    constructor(rectValue:Float,rectText:String){
        this.rectValue = rectValue
        this.rectText = rectText
    }
}
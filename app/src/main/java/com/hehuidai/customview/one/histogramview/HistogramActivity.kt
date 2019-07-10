package com.hehuidai.customview.one.histogramview

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.hehuidai.customview.R
import kotlinx.android.synthetic.main.activity_histogram.*
import java.util.ArrayList

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one.histogramview
 * @ClassName:      HistogramActivity
 * @Description:     java类作用描述 ：
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/10 14:32
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/10 14:32
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class HistogramActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_histogram)
        Handler().postDelayed({
            hisview.initRectPath(getRectList(),100F,false,5000)
        },100)
        hisview.setOnClickListener {
            hisview.setRectColor(Color.RED)
            hisview.setDescTextSize(30F)
            hisview.setRectDescTextColor(Color.GREEN)
            hisview.initRectPath(getRectList2(),100F,true,5000)
        }
    }

    /**
     * 获取矩形集合
     */
    private fun getRectList():List<HistogramRectItem>{
        val rectList: ArrayList<HistogramRectItem> = ArrayList()
        rectList.add(HistogramRectItem(90F,"大海"))
        rectList.add(HistogramRectItem(70F,"假数据"))
        rectList.add(HistogramRectItem(60F,"健健身"))
        rectList.add(HistogramRectItem(50F,"看看姐姐"))
        rectList.add(HistogramRectItem(30F,"太阳花"))
        return rectList
    }
    private fun getRectList2():List<HistogramRectItem>{
        val rectList: ArrayList<HistogramRectItem> = ArrayList()
        rectList.add(HistogramRectItem(50F,"大海"))
        rectList.add(HistogramRectItem(66F,"假数据"))
        rectList.add(HistogramRectItem(46F,"健健身"))
        rectList.add(HistogramRectItem(80F,"看看姐姐"))
        rectList.add(HistogramRectItem(57F,"太阳花"))
        return rectList
    }
}
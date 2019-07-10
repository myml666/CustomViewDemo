package com.hehuidai.customview

import android.content.Intent
import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.hehuidai.customview.one.histogramview.HistogramActivity
import com.hehuidai.customview.one.histogramview.HistogramRectItem
import com.hehuidai.customview.one.shadownview.ShadownActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bt_histogram.setOnClickListener {
            //跳转柱状图控件
            startActivity(Intent(this@MainActivity,HistogramActivity::class.java))
        }
        bt_shadown.setOnClickListener {
            //跳转阴影布局
            startActivity(Intent(this@MainActivity,ShadownActivity::class.java))
        }
    }
}

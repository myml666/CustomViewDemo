package com.hehuidai.customview

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.hehuidai.customview.animtextview.AnimTextViewActivity
import com.hehuidai.customview.animtextview2.AnimTextView2Activity
import com.hehuidai.customview.histogramview.HistogramActivity
import com.hehuidai.customview.shadownview.ShadownActivity
import kotlinx.android.synthetic.main.activity_main.*

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
        bt_animtext.setOnClickListener {
            //跳转文字动画
            startActivity(Intent(this@MainActivity,AnimTextViewActivity::class.java))
        }
        bt_animtext2.setOnClickListener {
            //跳转文字动画2
            startActivity(Intent(this@MainActivity, AnimTextView2Activity::class.java))
        }
    }
}

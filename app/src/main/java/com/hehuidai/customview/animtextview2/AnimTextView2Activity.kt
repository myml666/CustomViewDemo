package com.hehuidai.customview.animtextview2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hehuidai.customview.R
import kotlinx.android.synthetic.main.activity_animtextview2.*

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.animtextview2
 * @ClassName:      AnimTextView2Activity
 * @Description:     java类作用描述 ：
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/25 16:49
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/25 16:49
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class AnimTextView2Activity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animtextview2)
        bt_startanim.setOnClickListener {
            atv.setAnimText("我是动画文字啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦哈哈哈哈")
            atv.startAnim()
        }
    }
}
package com.hehuidai.customview.animtextview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.hehuidai.customview.R
import kotlinx.android.synthetic.main.activity_animtextview.*

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.animtextview
 * @ClassName:      AnimTextViewActivity
 * @Description:     java类作用描述 ：
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/15 14:34
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/15 14:34
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class AnimTextViewActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animtextview)
        bt_startanim.setOnClickListener {
            patv.startTextAnim()
            patv2.startTextAnim()
            patv3.startTextAnim()
        }
    }
}
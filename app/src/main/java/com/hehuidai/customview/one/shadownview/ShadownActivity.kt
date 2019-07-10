package com.hehuidai.customview.one.shadownview

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.blankj.utilcode.util.ToastUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.hehuidai.customview.R
import kotlinx.android.synthetic.main.activity_shadown.*

/**
 *
 * @ProjectName:    CustomViewDemo
 * @Package:        com.hehuidai.customview.one.histogramview
 * @ClassName:      HistogramActivity
 * @Description:     java类作用描述 ：阴影控件展示Activity
 * @Author:         作者名：lml
 * @CreateDate:     2019/7/10 14:32
 * @UpdateUser:     更新者：
 * @UpdateDate:     2019/7/10 14:32
 * @UpdateRemark:   更新说明：
 * @Version:        1.0
 */
class ShadownActivity:AppCompatActivity() {
    private var mAdapter:BaseQuickAdapter<String,BaseViewHolder>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shadown)
        initAdapter()
    }

    /**
     * 加载Adapter
     */
    fun initAdapter(){
        activity_shadown_rv.layoutManager = LinearLayoutManager(this)
        mAdapter = object : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_shadown,getListDatas()) {
            override fun convert(helper: BaseViewHolder, item: String) {
                helper.setText(R.id.item_shadown_tv,item)
            }
        }
        mAdapter!!.setOnItemClickListener { adapter, view, position ->
            ToastUtils.showShort("$position")
        }
        activity_shadown_rv.adapter = mAdapter!!
    }
    /**
     * 生成测试数据
     */
    private fun getListDatas():ArrayList<String>{
        val dataList:ArrayList<String> = ArrayList();
        for(i in 1..20){
            dataList.add("我是数据$i")
        }
        return dataList
    }
}
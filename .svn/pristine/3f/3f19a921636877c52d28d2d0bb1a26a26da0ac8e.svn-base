package com.eohi.haixin.ui.work.my.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemProcessUnitBinding
import com.eohi.haixin.ui.work.my.model.MyProcessingUnitModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/18 14:10
 */
class ProcessingUnitAdapter(mContext: Activity, listDatas: ArrayList<MyProcessingUnitModel>) :
    BaseAdapter<ItemProcessUnitBinding, MyProcessingUnitModel>(mContext, listDatas) {
    override fun convert(v: ItemProcessUnitBinding, t: MyProcessingUnitModel, position: Int) {
        v.tvBh.text = t.SCXBH
        v.tvMc.text = t.SCXMC
    }
}
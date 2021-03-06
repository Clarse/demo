package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutAdjustmentItemBinding
import com.eohi.haixin.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/21 10:38
 */
class AdjustmentItemAdapter(mContext: Activity, listDatas: ArrayList<ItemInfo>) :
    BaseAdapter<LayoutAdjustmentItemBinding, ItemInfo>(mContext, listDatas) {
    override fun convert(v: LayoutAdjustmentItemBinding, t: ItemInfo, position: Int) {
        v.tvTmh.text = t.tmh
        v.tvWpmc.text = t.WPMC
        v.tvGg.text = t.GGMS
        v.tvBeforeNum.text = t.ZSL
        v.tvAfterNum.text = t.tzhsl
    }

}
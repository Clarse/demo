package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutInItemBinding
import com.eohi.haixin.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/6 10:50
 */
class InItemlistAdapter(mContext: Activity, listDatas: ArrayList<ItemInfo>) :
    BaseAdapter<LayoutInItemBinding, ItemInfo>(mContext, listDatas) {
    override fun convert(v: LayoutInItemBinding, t: ItemInfo, position: Int) {
        v.tvTmh.text = t.tmh
        v.tvWpmc.text = t.WPMC
        v.tvDw.text = t.ZDW
        v.tvGg.text = t.GGMS
        v.tvNum.text = t.ZSL
    }

}
package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutInItemBinding
import com.eohi.haixin.ui.work.model.WxkgInfoModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/25 15:57
 */
class WxItemlistAdapter(mContext: Activity, listDatas: ArrayList<WxkgInfoModel>) :
    BaseAdapter<LayoutInItemBinding, WxkgInfoModel>(mContext, listDatas) {
    override fun convert(v: LayoutInItemBinding, t: WxkgInfoModel, position: Int) {
        v.tvTmh.text = t.lzkkh
        v.tvWpmc.text = t.wpmc
        v.tvDw.text = ""
        v.tvGg.text = t.gg
        v.tvNum.text = t.sybzs.toString()

    }
}
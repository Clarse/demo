package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.PopItemBinding

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/6 14:54
 */
class DialogSelectAdapter(mContext: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<PopItemBinding, String>(mContext, listDatas) {
    override fun convert(v: PopItemBinding, t: String, position: Int) {
        v.btn.text = t
    }
}
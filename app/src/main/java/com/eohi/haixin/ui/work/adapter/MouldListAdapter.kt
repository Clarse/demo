package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemMouldListBinding

class MouldListAdapter(mContext: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemMouldListBinding, String>(mContext, listDatas) {

    override fun convert(v: ItemMouldListBinding, t: String, position: Int) {

    }

}
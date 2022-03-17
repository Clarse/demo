package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutClxhItemBinding
import com.eohi.haixin.ui.work.model.MaterialModel
import com.eohi.haixin.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/8 17:21
 */
class AddConsumablesAdapter(mContext: Activity, listDatas: ArrayList<MaterialModel>) :
    BaseAdapter<LayoutClxhItemBinding, MaterialModel>(mContext, listDatas) {
    override fun convert(v: LayoutClxhItemBinding, t: MaterialModel, position: Int) {
        v.tvBzs.text = t.clbzh
        v.tvWpmc.text = t.wpmc
        v.etSl.setText(t.sysl.toString())
        v.tvDelete.clicks {
            onItemClick?.let {
                it(position)
            }
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

    fun onItemClick(itemClick: (Int) -> Unit) {
        this.onItemClick = itemClick
    }

}
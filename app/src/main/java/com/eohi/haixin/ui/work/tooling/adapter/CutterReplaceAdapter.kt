package com.eohi.haixin.ui.work.tooling.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemCutterBinding
import com.eohi.haixin.ui.work.tooling.model.Items
import com.eohi.haixin.widget.clicks

class CutterReplaceAdapter(mContext: Activity, listDatas: ArrayList<Items>) :
    BaseAdapter<ItemCutterBinding, Items>(mContext, listDatas) {
    private lateinit var changeListener: ChangeListener

    override fun convert(v: ItemCutterBinding, t: Items, position: Int) {
        v.tvDjmc.text = t.DJMC
        v.tvGgxh.text = t.GGXH
        v.tvPh.text = t.PH
        v.tvBzsmsycs.text = t.BZSMSYCS
        v.tvYwgsl.text = t.YWGSL
        v.tvScghsj.text = t.GHSJ
        v.btnChange.clicks {
            changeListener.change(position)
        }
    }

    fun setChangeListener(changeListener: ChangeListener) {
        this.changeListener = changeListener
    }

    interface ChangeListener {
        fun change(position: Int)
    }

}
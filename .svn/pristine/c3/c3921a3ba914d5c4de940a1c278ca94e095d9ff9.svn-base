package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemEquipmentCheckBinding
import com.eohi.haixin.ui.work.equipment.model.EquipmentCheckItem

class EquipmentCheckAdapter(
    mContext: Activity,
    listDatas: ArrayList<EquipmentCheckItem>,
    onmChecked: ((Int, String) -> Unit)?
) :
    BaseAdapter<ItemEquipmentCheckBinding, EquipmentCheckItem>(mContext, listDatas) {
    private var onChecked: ((Int, String) -> Unit)? = onmChecked
    override fun convert(v: ItemEquipmentCheckBinding, t: EquipmentCheckItem, position: Int) {
        v.name.text = t.inspecProjectName
        v.con.text = "点检方法：" + t.checkmethod
        v.djbz.text = "点检标准：" + t.checkstandard
        v.rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_good -> {
                    onChecked?.let {
                        it(position, "良好")
                    }
                }
                R.id.rb_bad -> {
                    onChecked?.let {
                        it(position, "异常")
                    }
                }
            }
        }
    }
}
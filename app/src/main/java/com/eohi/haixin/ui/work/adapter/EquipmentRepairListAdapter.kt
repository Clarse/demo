package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.text.Html
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemEquipmentMaintenanceScanListBinding
import com.eohi.haixin.ui.work.equipment.model.Fault

class EquipmentRepairListAdapter(
    mContext: Activity,
    listDatas: ArrayList<Fault>
) : BaseAdapter<ItemEquipmentMaintenanceScanListBinding, Fault>(mContext, listDatas) {
    override fun convert(v: ItemEquipmentMaintenanceScanListBinding, t: Fault, position: Int) {
        // TODO: 2021/7/18  
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>设备编号:</font><font color='#666666'>"
                        + t.SBBH + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>设备名称：</font><font color='#666666'>"
                        + t.SBMC + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.sbxh.text = Html.fromHtml(
                "<font color='#333333'>设备型号：</font><font color='#666666'>"
                        + t.XH + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
            v.szdz.text = Html.fromHtml(
                "<font color='#333333'>所在地址：</font><font color='#666666'>"
                        + t.GSMC + "</font>", Html.FROM_HTML_MODE_LEGACY
            )
//            v.wbsj.text = Html.fromHtml(
//                "<font color='#333333'>预定维保时间：</font><font color='#666666'>"
//                        + t.BCJHWBRQ + "</font>", Html.FROM_HTML_MODE_LEGACY
//            )
        } else {
            v.sbbh.text = Html.fromHtml(
                "<font color='#333333'>设备编号:</font><font color='#666666'>"
                        + t.SBBH + "</font>"
            )
            v.sbmc.text = Html.fromHtml(
                "<font color='#333333'>设备名称：</font><font color='#666666'>"
                        + t.SBMC + "</font>"
            )
            v.sbxh.text = Html.fromHtml(
                "<font color='#333333'>设备型号：</font><font color='#666666'>"
                        + t.XH + "</font>"
            )
            v.szdz.text = Html.fromHtml(
                "<font color='#333333'>所在地址：</font><font color='#666666'>"
                        + t.GSMC + "</font>"
            )
//            v.wbsj.text = Html.fromHtml(
//                "<font color='#333333'>预定维保时间：</font><font color='#666666'>"
//                        + t.BCJHWBRQ + "</font>"
//            )
        }
    }
}
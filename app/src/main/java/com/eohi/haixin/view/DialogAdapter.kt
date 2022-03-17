package com.eohi.haixin.view

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemDialogBinding

class DialogAdapter(context: Activity, listDatas: ArrayList<String>) :
    BaseAdapter<ItemDialogBinding, String>(context, listDatas) {

    override fun convert(v: ItemDialogBinding, t: String, position: Int) {
        v.singleItem.text = t
    }


}
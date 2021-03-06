package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemMergeListBinding
import com.eohi.haixin.ui.work.model.CardInfoModel
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/28 11:10
 */
class MergeDetailAdapter(mContext: Activity, listDatas: ArrayList<CardInfoModel>) :
    BaseAdapter<ItemMergeListBinding, CardInfoModel>(mContext, listDatas) {
    override fun convert(v: ItemMergeListBinding, t: CardInfoModel, position: Int) {
        v.tvLzkkh.text = t.LZKKH
        v.tvRwdh.text = t.RWDH
        v.tvCpmc.text = t.WPMC
        v.tvCpdm.text = t.WPH
        v.tvCllh.text = t.CLLH
        v.tvRcllh.text = t.RCLLH
        v.tvScph.text = t.SCPH
        v.tvKpsl.text = t.BZS.toString()
        v.tvBgsl.text = t.BGSL.toString()
        v.tvTsztms.text = t.TSZTMS
        v.btnGys.clicks {
            onDetele?.let {
                it(position)
            }
        }
        val textwatch = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    if (s.toString().toInt() > t.BZS) {
                        ToastUtil.showToast(mContext, "合卡数量不能大于卡片数量")
                        return
                    }
                    onEdit?.let {
                        it(position, s.toString().toInt())
                    }
                }
            }
        }

        v.tvGys.removeTextChangedListener(textwatch)
        v.tvGys.setText(t.HKSL.toString())
        v.tvGys.addTextChangedListener(textwatch)

    }

    var onDetele: ((Int) -> Unit)? = null
    var onEdit: ((Int, Int) -> Unit)? = null

    fun setDetele(onDetele: ((Int) -> Unit)) {
        this.onDetele = onDetele
    }

    fun setEdit(onEdit: ((Int, Int) -> Unit)?) {
        this.onEdit = onEdit
    }


}
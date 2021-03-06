package com.eohi.haixin.ui.work.quality.unqualified.adapter

import android.app.Activity
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemMxBinding
import com.eohi.haixin.ui.work.model.Items
import com.eohi.haixin.widget.clicks

/**
 *@author: YH
 *@date: 2022/1/17
 *@desc: 明细适配器
 */
class MxAdapter(mContext: Activity, list: ArrayList<Items>) :
    BaseAdapter<ItemMxBinding, Items>(mContext, list) {

    private lateinit var deleteListener: DeleteListener
    private lateinit var blyyListener: BlyyListener
    private lateinit var scryListener: ScryListener
    private lateinit var slListener: SlListener

    override fun convert(v: ItemMxBinding, t: Items, position: Int) {
        v.tvBhgyy.text = t.blyy
        v.etSl.setText(t.blsl.toString())
        v.tvScry.text = t.scrid + "/" + t.scr
        v.tvBhgyy clicks {
            blyyListener.setBlyy(position)
        }

        if (v.etSl.tag is TextWatcher) {
            v.etSl.removeTextChangedListener(v.etSl.tag as TextWatcher)
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (v.etSl.hasFocus()) {
                    s?.let {
                        if (TextUtils.isEmpty(s.toString())) {
                            slListener.setSl(position, "0")
                        } else {
                            slListener.setSl(position, s.toString())
                        }
                    }
                }
            }
        }
        v.etSl.addTextChangedListener(textWatcher)
        v.etSl.tag = textWatcher

        v.tvScry clicks {
            scryListener.setScry(position)
        }
        v.tvCz clicks {
            deleteListener.delete(position)
        }
    }

    fun setDeleteListener(deleteListener: DeleteListener) {
        this.deleteListener = deleteListener
    }

    fun setBlyyListener(blyyListener: BlyyListener) {
        this.blyyListener = blyyListener
    }

    fun setScryListener(scryListener: ScryListener) {
        this.scryListener = scryListener
    }

    fun setSlListener(slListener: SlListener) {
        this.slListener = slListener
    }

    interface DeleteListener {
        fun delete(position: Int)
    }

    interface BlyyListener {
        fun setBlyy(position: Int)
    }

    interface ScryListener {
        fun setScry(position: Int)
    }

    interface SlListener {
        fun setSl(position: Int, s: String)
    }

}
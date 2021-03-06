package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemLzkSublistBinding
import com.eohi.haixin.ui.work.model.LZKSubListModel
import com.eohi.haixin.ui.work.process.card.CirculationCardDetailActivity
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.widget.clicks

class LzkSubAdapter(mContext: Activity, listDatas: ArrayList<LZKSubListModel>) :
    BaseAdapter<ItemLzkSublistBinding, LZKSubListModel>(mContext, listDatas) {

    lateinit var jdListener: JdListener

    override fun convert(v: ItemLzkSublistBinding, t: LZKSubListModel, position: Int) {
        v.tvGx.text = t.GXMS
        v.tvJgdy.text = t.JGDYMC
        v.tvHgsl.text = t.HGSL.toString()
        v.tvBhgsl.text = t.BHGSL.toString()
        v.tvBc.text = t.BC
        v.tvCzg.text = t.SCRXM
        v.tvBgrq.text = t.BGRQ
        v.tvJyy.text = t.ZJRY
        v.tvJyhgsl.text = t.JYHGSL.toString()
        v.tvJybhgsl.text = t.JYBHGSL.toString()
        v.tvJyri.text = t.JYRQ

        v.tvJdsl.text = t.JDSL.toString()
        v.tvJdr.text = t.JDRXM
        v.tvJdsj.text = t.JDSJ

        if (CirculationCardDetailActivity.dqgxh != null) {
            if (CirculationCardDetailActivity.dqgxh == t.GXH) {
                v.container.setBackgroundColor(R.color.circle_point.asColor())
                v.tvGx.text = ">>".plus(t.GXMS)
            }
        }

        v.tvDetail clicks {
            val view = LayoutInflater.from(mContext).inflate(R.layout.dialog_lzkmx, null, false)
            view.findViewById<TextView>(R.id.tv_gx).text = t.GXMS
            view.findViewById<TextView>(R.id.tv_hgsl).text = t.HGSL.toString()
            view.findViewById<TextView>(R.id.tv_bhgsl).text = t.BHGSL.toString()
            view.findViewById<TextView>(R.id.tv_bc).text = t.BC
            view.findViewById<TextView>(R.id.tv_czg).text = t.SCRXM
            view.findViewById<TextView>(R.id.tv_bgrq).text = t.BGRQ
            view.findViewById<TextView>(R.id.tv_jyy).text = t.ZJRY
            view.findViewById<TextView>(R.id.tv_jyhgsl).text = t.JYHGSL.toString()
            view.findViewById<TextView>(R.id.tv_jybhgsl).text = t.JYBHGSL.toString()
            view.findViewById<TextView>(R.id.tv_jyrq).text = t.JYRQ
            view.findViewById<TextView>(R.id.tv_jdsl).text = t.DQBZNSL.toString()
            view.findViewById<TextView>(R.id.tv_jdr).text = t.JDRXM
            view.findViewById<TextView>(R.id.tv_jdsj).text = t.JDSJ
            view.findViewById<TextView>(R.id.tv_bz).text = t.BZ
            val dialog = AlertDialog.Builder(mContext).setView(view).create()

            dialog.show()
        }
        v.tvJd clicks {
            jdListener.jd(position)
        }
    }

    interface JdListener {
        fun jd(position: Int)
    }

    fun setListener(jdListener: JdListener) {
        this.jdListener = jdListener
    }

}
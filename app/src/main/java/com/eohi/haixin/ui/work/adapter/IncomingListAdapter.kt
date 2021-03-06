package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.content.Intent
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemIncomgingListBinding
import com.eohi.haixin.ui.work.model.IncomingListModel
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.haixin.widget.clicks

class IncomingListAdapter(mContext: Activity, listData: ArrayList<IncomingListModel>) :
    BaseAdapter<ItemIncomgingListBinding, IncomingListModel>(mContext, listData) {

    override fun convert(v: ItemIncomgingListBinding, t: IncomingListModel, position: Int) {
        v.tvRwbh.text = t.RWDH
        v.tvCgddh.text = t.CGDDH
        v.tvDhtzd.text = t.DHTZDH
        v.tvGysjc.text = t.GYSJC
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.WPMC
        v.tvGgms.text = t.GGMS
        v.tvDhsl.text = t.DHSL
        v.tvCjsj.text = t.CJRQ

        v.btnCheck clicks {
            val intent = Intent(mContext, IncomingCheckActivity::class.java).apply {
                putExtra("RWDH", t.RWDH)
                putExtra("cgddh", t.CGDDH)
            }
            mContext.startActivity(intent)
        }

    }

}
package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemIncomgingHistoryListBinding
import com.eohi.haixin.ui.work.model.IncomingListModel
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.haixin.ui.work.quality.incoming.IncomingDetailActivity
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.widget.clicks

class IncomingHistoryListAdapter(mContext: Activity, listData: ArrayList<IncomingListModel>) :
    BaseAdapter<ItemIncomgingHistoryListBinding, IncomingListModel>(mContext, listData) {

    override fun convert(v: ItemIncomgingHistoryListBinding, t: IncomingListModel, position: Int) {
        v.tvRwbh.text = t.RWDH
        v.tvCgddh.text = t.CGDDH
        v.tvTzdh.text = t.DHTZDH
        v.tvGys.text = t.GYSJC
        v.tvWpmc.text = t.WPMC
        v.tvCjsj.text = t.CJRQ
        if (!TextUtils.isEmpty(t.JYJG)) {
            if (t.JYJG == "1") {
                v.tvCheckState.text = "合格"
                v.tvCheckState.setTextColor(R.color.qualified.asColor())
            } else {
                v.tvCheckState.text = "不合格"
                v.tvCheckState.setTextColor(R.color.unqualified.asColor())
            }
        }
        v.tvDetail clicks {
            val intent = Intent(mContext, IncomingDetailActivity::class.java).apply {
                putExtra("RWDH", t.RWDH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify clicks {
            val intent = Intent(mContext, IncomingCheckActivity::class.java).apply {
                putExtra("RWDH", t.RWDH)
                putExtra("type", "modify")
                putExtra("cgddh", t.CGDDH)
            }
            mContext.startActivity(intent)
        }
    }

}
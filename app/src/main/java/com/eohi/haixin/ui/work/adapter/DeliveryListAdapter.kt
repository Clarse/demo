package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemDeliveryListBinding
import com.eohi.haixin.ui.work.model.DeliveryCheckListResult
import com.eohi.haixin.ui.work.quality.delivery.DeliveryCheckActivity
import com.eohi.haixin.ui.work.quality.delivery.DeliveryDetailActivity
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.widget.clicks

class DeliveryListAdapter(mContext: Activity, listData: ArrayList<DeliveryCheckListResult>) :
    BaseAdapter<ItemDeliveryListBinding, DeliveryCheckListResult>(mContext, listData) {

    override fun convert(v: ItemDeliveryListBinding, t: DeliveryCheckListResult, position: Int) {
        v.tvJydh.text = t.JYDH
        v.tvWpmc.text = t.WPMC
        v.tvGgms.text = t.GG
        v.tvJysj.text = t.JYSJ
        if (!TextUtils.isEmpty(t.JYJG)) {
            if (t.JYJG == "1") {
                v.tvJyjg.text = "合格"
                v.tvCheckState.text = "合格"
                v.tvCheckState.setTextColor(R.color.qualified.asColor())
            } else {
                v.tvJyjg.text = "不合格"
                v.tvCheckState.text = "不合格"
                v.tvCheckState.setTextColor(R.color.unqualified.asColor())
            }
        }
        v.tvDetail clicks {
            val intent = Intent(mContext, DeliveryDetailActivity::class.java).apply {
                putExtra("GDH", t.GDH)
                putExtra("DJH", t.JYDH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify clicks {
            val intent = Intent(mContext, DeliveryCheckActivity::class.java).apply {
                putExtra("DJH", t.JYDH)
                putExtra("type", "modify")
                putExtra("GDH", t.GDH)
            }
            mContext.startActivity(intent)
        }
    }

}
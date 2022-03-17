package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemFirstHistoryListBinding
import com.eohi.haixin.ui.work.model.FirstCheckListResult
import com.eohi.haixin.ui.work.quality.first.FirstCheckActivity
import com.eohi.haixin.ui.work.quality.first.FirstDetailActivity
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.widget.clicks

class FirstHistoryListAdapter(mContext: Activity, listData: ArrayList<FirstCheckListResult>) :
    BaseAdapter<ItemFirstHistoryListBinding, FirstCheckListResult>(mContext, listData) {

    override fun convert(v: ItemFirstHistoryListBinding, t: FirstCheckListResult, position: Int) {
        v.tvRwbh.text = t.RWBH
        v.tvGdh.text = t.SCGDH
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.WPMC
        v.tvCjsj.text = t.CJSJ
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
            val intent = Intent(mContext, FirstDetailActivity::class.java).apply {
                putExtra("gdh", t.SCGDH)
                putExtra("rwbh", t.RWBH)
            }
            mContext.startActivity(intent)
        }
        v.tvModify clicks {
            val intent = Intent(mContext, FirstCheckActivity::class.java).apply {
                putExtra("gdh", t.SCGDH)
                putExtra("rwbh", t.RWBH)
                putExtra("type", "modify")
            }
            mContext.startActivity(intent)
        }
    }

}
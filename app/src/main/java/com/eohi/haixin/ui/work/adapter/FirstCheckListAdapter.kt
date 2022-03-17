package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import android.content.Intent
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.ItemFirstCheckListBinding
import com.eohi.haixin.ui.work.model.FirstCheckListResult
import com.eohi.haixin.ui.work.quality.first.FirstCheckActivity
import com.eohi.haixin.widget.clicks

class FirstCheckListAdapter(mContext: Activity, listData: ArrayList<FirstCheckListResult>) :
    BaseAdapter<ItemFirstCheckListBinding, FirstCheckListResult>(mContext, listData) {

    override fun convert(v: ItemFirstCheckListBinding, t: FirstCheckListResult, position: Int) {
        v.tvRwbh.text = t.RWBH
        v.tvGdh.text = t.SCGDH
        v.tvWph.text = t.WPH
        v.tvWpmc.text = t.WPMC
        v.tvGgms.text = t.GGMS
        v.tvSl.text = t.SL
        v.tvCjsj.text = t.CJSJ

        v.btnCheck clicks {
            val intent = Intent(mContext, FirstCheckActivity::class.java).apply {
                putExtra("gdh", t.SCGDH)
                putExtra("rwbh", t.RWBH)
            }
            mContext.startActivity(intent)
        }

    }

}
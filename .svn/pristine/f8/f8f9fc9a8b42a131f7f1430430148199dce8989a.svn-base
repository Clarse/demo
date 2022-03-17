package com.eohi.haixin.ui.work.abnormal

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutAbnormalCallBinding
import com.eohi.haixin.ui.work.model.AbnormalCallModel
import com.eohi.haixin.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/1/19 16:44
 */
class AbnormalCallAdapter(mContext: Activity, listdata: ArrayList<AbnormalCallModel>) :
    BaseAdapter<LayoutAbnormalCallBinding, AbnormalCallModel>(
        mContext, listdata
    ) {
    override fun convert(v: LayoutAbnormalCallBinding, t: AbnormalCallModel, position: Int) {
        v.tvYclx.text = t.yjlx
        v.tvYcdj.text = t.yjdjmc
        v.tvJgdy.text = t.GZZXMC
        v.tvScsb.text = t.SBMC
        v.tvHjr.text = t.yjtjr
        v.tvYcsj.text = t.yjsj
        v.tvYcnr.text = t.yjnr
        v.tvClr.text = t.CLR
        v.tvClsj.text = t.CLSJ
        v.tvClms.text = t.CLSM
        v.tvStatus.text = t.YJZT
        when (t.YJZT) {
            "待处理" -> {
                v.tvStatus.setTextColor(Color.RED)
                v.btnResponse.visibility = View.VISIBLE
                v.btnDeal.visibility = View.VISIBLE
                setVisibility(v, View.GONE)
            }
            "已响应" -> {
                v.tvStatus.setTextColor(Color.parseColor("#FFA500"))
                v.btnResponse.visibility = View.GONE
                v.btnDeal.visibility = View.VISIBLE
                setVisibility(v, View.GONE)
            }
            "已处理" -> {
                v.tvStatus.setTextColor(Color.GREEN)
                v.btnResponse.visibility = View.GONE
                v.btnDeal.visibility = View.GONE
                setVisibility(v, View.VISIBLE)
            }
        }

        v.btnResponse.clicks {
            onResponseclick?.apply {
                this(position)
            }
        }
        v.btnDeal.clicks {
            onDealClick?.let {
                it(position)
            }
        }

    }

    private var onResponseclick: ((Int) -> Unit)? = null
    fun setResponseClick(onResponseclick: ((Int) -> Unit)?) {
        this.onResponseclick = onResponseclick
    }

    private var onDealClick: ((Int) -> Unit)? = null
    fun setOnDealClick(onDealClick: ((Int) -> Unit)?) {
        this.onDealClick = onDealClick
    }


    private fun setVisibility(v: LayoutAbnormalCallBinding, visible: Int) {
        v.tvClrText.visibility = visible
        v.tvClr.visibility = visible
        v.tvClsj.visibility = visible
        v.tvClsjText.visibility = visible
        v.tvClms.visibility = visible
        v.tvClmsText.visibility = visible
    }

}
package com.eohi.haixin.ui.work.process.coordination

import android.graphics.drawable.ColorDrawable
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.haixin.App
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentCoordinationStartBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.model.WxkgInfoModel
import com.eohi.haixin.ui.work.process.viewmodel.CoordinationViewModel
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/17 13:59
 */
class CoordinationStartFragment :
    BaseFragment<CoordinationViewModel, FragmentCoordinationStartBinding>() {
    var cardNo: String = ""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.gxList.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                var list = ArrayList<String>()
                var gxhlist = ArrayList<String>()
                for (i in it.indices) {
                    list.add(it[i].gxms)
                    gxhlist.add(it[i].gxh)
                }
                showListPopulWindow(v.etGx, list.toTypedArray(), gxhlist.toTypedArray())
            }

        })
    }

    override fun initView() {
    }

    override fun initClick() {
        v.etGx.clicks {
            vm.getGxlist(cardNo)
        }
    }

    override fun initData() {
        v.tvUsername.text = username
        v.tvDate.text = DateUtil.data
    }

    override fun lazyLoadData() {

    }

    fun setData(data: WxkgInfoModel) {
        v.tvWph.text = data.wph
        v.tvWpmc.text = data.wpmc
        v.tvGg.text = data.gg
        v.tvSl.text = data.sybzs.toString()
        v.etGx.text = data.gxms
        cardNo = data.lzkkh
    }

    private fun showListPopulWindow(
        mEditText: TextView,
        list: Array<String>,
        gxhlist: Array<String>
    ) {
        val listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(
            ArrayAdapter(
                mContext,
                R.layout.pop_item,
                list
            )
        ) //???android???????????????????????????????????????
        listPopupWindow.anchorView = mEditText //???????????????????????????????????????mEditText?????????
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        )
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //?????????????????????
            mEditText.text = list[i] //?????????????????????????????????EditText???
            listPopupWindow.dismiss() //????????????????????????????????????
            App.post(EventMessage(EventCode.REFRESH, cardNo, gxhlist[i], 0))
        }
        listPopupWindow.show() //???ListPopWindow????????????
    }


}
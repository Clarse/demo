package com.eohi.haixin.ui.work.abnormal

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityAbnormalCallBinding
import com.eohi.haixin.ui.work.abnormal.viewmodel.AbnormalCallViewModel
import com.eohi.haixin.ui.work.model.AbnormalCallModel
import com.eohi.haixin.ui.work.model.AbnormalCallSubmitModel
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.showShortToast
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.view.DialogAbnormalCall
import com.eohi.haixin.widget.clicks

class AbnormalCallActivity : BaseActivity<AbnormalCallViewModel, ActivityAbnormalCallBinding>() {
    private var page: Int = 1
    private lateinit var madapter: AbnormalCallAdapter
    private lateinit var list: ArrayList<AbnormalCallModel>
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
    }

    override fun initClick() {
        v.ivBack.clicks {
            finish()
        }
        v.refreshLayout.setOnRefreshListener {
            page = 1
            vm.getAbnormalList(companyNo, page, 10)
        }
        v.refreshLayout.setOnLoadMoreListener {
            page++
            vm.getAbnormalList(companyNo, page, 10)
        }
    }

    override fun initData() {
        list = ArrayList()
        madapter = AbnormalCallAdapter(this, list)
        madapter.setResponseClick {
            val model = AbnormalCallSubmitModel("", list[it].djlsh, 1, accout, username)
            vm.submit(model)
        }
        madapter.setOnDealClick { index ->
            val dialog = DialogAbnormalCall(mContext)
            dialog.setOnOkClick {
                val model = AbnormalCallSubmitModel(it, list[index].djlsh, 2, accout, username)
                vm.submit(model)
            }
            dialog.show()
        }

        v.mRecyclerView.let {
            it.layoutManager = LinearLayoutManager(mContext)
            it.adapter = madapter
        }
        vm.getAbnormalList(companyNo, page, 10)

    }

    override fun initVM() {
        vm.abnormallist.observe(this, Observer {
            v.refreshLayout.finishRefresh()
            v.refreshLayout.finishLoadMore()
            if (it.isNotEmpty()) {
                if (page == 1) {
                    list.clear()
                }
                list.addAll(it)
                madapter.notifyDataSetChanged()
            }
        })

        vm.result.observe(this, Observer {
            showShortToast("???????????????")
            page = 1
            vm.getAbnormalList(companyNo, page, 10)
        })
    }


}
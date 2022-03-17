package com.eohi.haixin.ui.work.quality.first

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityFirstListBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.adapter.FirstCheckListAdapter
import com.eohi.haixin.ui.work.model.FirstCheckListResult
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.widget.clicks
import java.util.*

class FirstCheckListActivity : BaseActivity<FirstCheckViewModel, ActivityFirstListBinding>() {

    private lateinit var hashMap: HashMap<String, String>
    private var page: Int = 1
    private lateinit var list: ArrayList<FirstCheckListResult>
    private lateinit var adapter: FirstCheckListAdapter
    private lateinit var popView: View
    private lateinit var etGdh: EditText
    private lateinit var etWpmc: EditText
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private var startDate: Date? = null
    private var endDate: Date? = null
    private lateinit var popupWindow: PopupWindow

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        hashMap = HashMap()
        list = ArrayList()

        popView = LayoutInflater.from(this).inflate(R.layout.pop_first_filter, null, false)
        etGdh = popView.findViewById(R.id.et_gdh)
        etWpmc = popView.findViewById(R.id.et_wpmc)
        tvStartTime = popView.findViewById(R.id.tv_start_time)
        tvEndTime = popView.findViewById(R.id.tv_end_time)
        tvStartTime clicks {
            startDate = DateUtil.chooseStartDate(mContext, tvStartTime, startDate, endDate)
        }
        tvEndTime clicks {
            endDate = DateUtil.chooseEndDate(mContext, tvEndTime, startDate, endDate)
        }
        popView.findViewById<Button>(R.id.btn_reset) clicks {
            etGdh.setText("")
            etWpmc.setText("")
            tvStartTime.text = ""
            tvEndTime.text = ""
        }
        popView.findViewById<Button>(R.id.btn_search) clicks {
            list.clear()
            getList()
            popupWindow.dismiss()
        }

        adapter = FirstCheckListAdapter(this, list)
        v.mRecyclerView.layoutManager = LinearLayoutManager(mContext)
        v.mRecyclerView.adapter = adapter

        getList()

        v.refreshLayout.setOnRefreshListener {
            page = 1
            getList()
        }
        v.refreshLayout.setOnLoadMoreListener {
            page++
            getList()
        }

    }

    private fun initMap() {
        hashMap["pageindex"] = page.toString()
        hashMap["pagesize"] = "10"
        hashMap["gsh"] = companyNo
        hashMap["ksrq"] = tvStartTime.text.toString()
        hashMap["jsrq"] = tvEndTime.text.toString()
        hashMap["wpmc"] = etWpmc.text.toString()
        hashMap["scgdh"] = etGdh.text.toString()
    }

    private fun getList() {
        initMap()
        vm.getFirstCheckList(hashMap)
        v.refreshLayout.finishRefresh()
        v.refreshLayout.finishLoadMore()
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.tvHistory clicks {
            startActivity(Intent(this, FirstHistoryListActivity::class.java))
        }
        v.ivSearch clicks {
            popupWindow = PopupWindow(
                popView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                isTouchable = true
                isFocusable = true
                isOutsideTouchable = true
/*                animationStyle = R.style.anim_pop_filter*/
            }
            popupWindow.showAsDropDown(v.consTitle, 0, 1)
        }
    }

    override fun initData() {

    }

    override fun initVM() {
        vm.firstCheckList.observe(this) {
            if (page == 1) list.clear()
            it.let { it1 -> list.addAll(it1) }
            adapter.notifyDataSetChanged()
        }
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            page = 1
            getList()
        }
    }

}
package com.eohi.haixin.ui.work.equipment

import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.api.ApiService
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivityEquipmentRepairListBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.adapter.EquipmentMaintainListAdapter
import com.eohi.haixin.ui.work.equipment.model.MaintainListModel
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.utils.retrofit.ApiErrorModel
import com.eohi.haixin.utils.retrofit.FatherList
import com.eohi.haixin.utils.retrofit.MyCallBack
import com.eohi.haixin.utils.retrofit.RetrofitUtil
import com.eohi.haixin.view.SpacesItemDecoration
import com.eohi.haixin.widget.clicks

/**
 * 设备维修
 */
class EquipmentRepairListActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentRepairListBinding>() {

    var adapter: EquipmentMaintainListAdapter? = null
    var listDatas: ArrayList<MaintainListModel>? = null
    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        listDatas = ArrayList()
        adapter = EquipmentMaintainListAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        adapter!!.itemClick {
            val intent = Intent(this, EquipmentRepairActivity::class.java)
            intent.putExtra("model", listDatas!![it])
            startActivity(intent)
        }
    }

    private fun setTaskFaultList() {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .getEupMaintainlist(accout)
            .enqueue(object : MyCallBack<FatherList<MaintainListModel>>() {
                override fun success(t: FatherList<MaintainListModel>?) {
                    listDatas?.clear()
                    listDatas?.addAll(t?.list!!)
                    adapter?.notifyDataSetChanged()
                    v.tvSl.text = "当前待维修设备数：" + listDatas?.size
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    override fun initClick() {
        v.ivBack.clicks { finish() }
    }

    override fun initData() {
        setTaskFaultList()
    }

    override fun initVM() {
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.REFRESH) {
            setTaskFaultList()
        }
    }

}
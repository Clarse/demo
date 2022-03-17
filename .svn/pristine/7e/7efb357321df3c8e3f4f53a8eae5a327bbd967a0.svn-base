package com.eohi.haixin.ui.work.equipment

import android.Manifest
import android.content.Intent
import android.text.TextUtils
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.api.ApiService
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivityEquipmentFaultConfirmListBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.adapter.EquipmentFaultConfirmListAdapter
import com.eohi.haixin.ui.work.equipment.model.Fault
import com.eohi.haixin.utils.Constant
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.utils.retrofit.ApiErrorModel
import com.eohi.haixin.utils.retrofit.FatherList
import com.eohi.haixin.utils.retrofit.MyCallBack
import com.eohi.haixin.utils.retrofit.RetrofitUtil
import com.eohi.haixin.view.SpacesItemDecoration
import com.eohi.haixin.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions


/**
 * 故障确认
 */
class EquipmentFaultConfirmListActivity :
    BaseActivity<BaseViewModel, ActivityEquipmentFaultConfirmListBinding>() {

    private var apiurl by Preference("ApiUrl", "http://122.51.182.66:3019/")
    var adapter: EquipmentFaultConfirmListAdapter? = null
    var listDatas: ArrayList<Fault>? = null

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun onBarcodeEvent(result: String) {
        v.etScgx.setText(result.trim { it <= ' ' })
    }

    override fun initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.white))
        StatusBarUtil.darkMode(this, true)
        listDatas = ArrayList()
        adapter = EquipmentFaultConfirmListAdapter(mContext, listDatas!!)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter
        v.rc.addItemDecoration(SpacesItemDecoration(0, 0, 20, 20))
        adapter!!.itemClick {
            startActivity(
                Intent(mContext, EquipmentFaultConfirmActivity::class.java).putExtra(
                    "sbbh",
                    listDatas!![it].SBBH
                )
                    .putExtra("SBMC", listDatas!![it].SBMC)
                    .putExtra("SBBJBH", listDatas!![it].SBBJBH)
                    .putExtra("XH", listDatas!![it].XH)
                    .putExtra("SBDQSZWZ", listDatas!![it].SBDQSZWZ)
                    .putExtra("PJSWH", listDatas!![it].PJSWH)
                    .putExtra("GZBM", listDatas!![it].GZBM)
                    .putExtra("GZMC", listDatas!![it].GZMC)
                    .putExtra("XDWCGS", listDatas!![it].XDWCGS)
                    .putExtra("JJCD", listDatas!![it].JJCD)
                    .putExtra("GZQK", listDatas!![it].GZQK)
            )
        }
        setTaskFaultList()
    }

    private fun setTaskFaultList() {
        RetrofitUtil.builder.baseUrl(apiurl)
            .build()
            .create(ApiService::class.java)
            .setTaskFaultList(accout).enqueue(object : MyCallBack<FatherList<Fault>>() {
                override fun success(t: FatherList<Fault>?) {
                    v.refreshLayout.finishRefresh()
                    listDatas?.clear()
                    listDatas?.addAll(t?.list!!)
                    adapter?.notifyDataSetChanged()
                    v.tvScxx.text = "当前待确认记录数：" + listDatas?.size
                }

                override fun failure(apiErrorModel: ApiErrorModel?) {
                }
            })
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.btnScgx clicks {
            if (TextUtils.isEmpty(v.etScgx.text.toString())) {
                setTaskFaultList()
            } else {
                val list = listDatas?.filter { it.SBBH.contains(v.etScgx.text.toString()) }
                listDatas?.clear()
                listDatas?.addAll(list!!)
                adapter?.notifyDataSetChanged()
                v.tvScxx.text = "当前待确认记录数：" + listDatas?.size
            }
        }
        v.ivKwh.clicks {
            checkCameraPermissions()
        }
    }


    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.etScgx.setText(result)
                }
            }
        }
    }

    override fun handleEvent(msg: EventMessage) {
        super.handleEvent(msg)
        when (msg.code) {
            EventCode.REFRESH -> {
                setTaskFaultList()
            }
        }
    }

    override fun initData() {
        v.refreshLayout.setOnRefreshListener {
            setTaskFaultList()
        }
    }

    override fun initVM() {
    }
}
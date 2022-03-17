package com.eohi.haixin.ui.work.tooling.cutter

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityCutterReplaceBinding
import com.eohi.haixin.ui.work.tooling.adapter.CutterReplaceAdapter
import com.eohi.haixin.ui.work.tooling.model.CutterReplacePostBean
import com.eohi.haixin.ui.work.tooling.model.Items
import com.eohi.haixin.ui.work.tooling.viewmodel.CutterReplaceViewModel
import com.eohi.haixin.utils.Constant
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.view.DialogCutter
import com.eohi.haixin.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import org.json.JSONException
import org.json.JSONObject
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/*
* 刀具交换
* */
class CutterReplace : BaseActivity<CutterReplaceViewModel, ActivityCutterReplaceBinding>(),
    EasyPermissions.PermissionCallbacks, CutterReplaceAdapter.ChangeListener {

    private lateinit var adapter: CutterReplaceAdapter
    private lateinit var list: ArrayList<Items>
    private lateinit var dialogCutter: DialogCutter
    private lateinit var sbbh: String

    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.mRecyclerView.layoutManager = LinearLayoutManager(this)
        v.mRecyclerView.adapter = adapter
    }

    override fun initClick() {
        v.tvSbm clicks {
            checkCameraPermissions()
        }
        v.ivBack clicks {
            finish()
        }
    }

    override fun initData() {
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
        list = ArrayList()
        adapter = CutterReplaceAdapter(this, list)
        adapter.setChangeListener(this)
    }

    override fun change(position: Int) {
        dialogCutter = DialogCutter(this, object : DialogCutter.MyListener {
            override fun refreshActivity(ph: String) {
                val items = list[position]
                val model = CutterReplacePostBean(
                    companyNo,
                    sbbh,
                    items.DJBH,
                    ph,
                    accout,
                    username,
                    DateUtil.audioTime,
                    DateUtil.data
                )
                vm.postCutterReplace(model)
                dialogCutter.dismiss()
            }
        })
        dialogCutter.show()
    }

    override fun initVM() {
        vm.scanResult.observe(this) {
            v.tvSbmc.text = it[0].SBMC
            list.addAll(it[0].items)
            adapter.notifyDataSetChanged()
        }
        vm.postResult.observe(this) {
            list.clear()
            vm.getCutterReplaceList(companyNo, sbbh)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    try {
                        //String转JSONObject
                        val jsonstr = JSONObject(result)
                        //取数据
                        sbbh = jsonstr["data"].toString()
                        v.tvSbm.text = sbbh
                        vm.getCutterReplaceList(companyNo, sbbh)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    //开始扫码
    private fun startScan() {
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
    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            startScan()
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        startScan()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(Constant.SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Constant.SCANACTION) {
                sbbh = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvSbm.text = sbbh
                vm.getCutterReplaceList(companyNo, sbbh)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onBarcodeEvent(result: String) {
        sbbh = result.trim { it <= ' ' }
        v.tvSbm.text = sbbh
        vm.getCutterReplaceList(companyNo, sbbh)
    }
}
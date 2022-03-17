package com.eohi.haixin.ui.work.process.card

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
import com.eohi.haixin.databinding.ActivityCirculationCardDetailBinding
import com.eohi.haixin.ui.work.adapter.LzkSubAdapter
import com.eohi.haixin.ui.work.model.LZKSubListModel
import com.eohi.haixin.ui.work.process.viewmodel.CirculationCardViewModel
import com.eohi.haixin.utils.Constant
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.view.DialogLzk
import com.eohi.haixin.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/*
* 流转卡详情 2022.01.26
* */
class CirculationCardDetailActivity :
    BaseActivity<CirculationCardViewModel, ActivityCirculationCardDetailBinding>(),
    LzkSubAdapter.JdListener {

    private var cardno = ""
    private lateinit var list: ArrayList<LZKSubListModel>
    private lateinit var adapter: LzkSubAdapter

    companion object {
        var dqgxh = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        adapter = LzkSubAdapter(this, list)
        adapter.setListener(this)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

//        vm.getLzkxx(cardno)
//        vm.getSubList(cardno)
//        vm.getCLXH(cardno)
//        vm.getGx(cardno)
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.ivScan.clicks {
            checkCameraPermissions()
        }
    }

    override fun initData() {
        v.tvCzr.text = username
        v.tvCjrq.text = DateUtil.audioTime
    }

    override fun initVM() {
        vm.lzkxqResult.observe(this) {
            if (it.size > 0) {
                it[0].apply {
                    dqgxh = ZXBGGX
                    v.tvRwdh.text = RWDH
                    v.tvScph.text = SCPH
                    v.tvScsl.text = BZS
                    v.tvWpmc.text = "(".plus(WPH).plus(")").plus(WPMC)
                    v.tvCpgg.text = GG
                    list.addAll(items)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        vm.jdResult.observe(this) {
            list.clear()
            vm.getLzkxq(companyNo, cardno)
        }
//        vm.subListResult.observe(this) {
//            if (it.size > 0) {
//                list.addAll(it)
//                adapter.notifyDataSetChanged()
//            }
//        }
//        vm.clxhResult.observe(this) {
//            if (it.size > 0) {
//                it[0].apply {
//                    v.tvClpc.text = clph
//                    v.tvGys.text = gysmc
//                    v.tvClgg.text = gg
//                    v.tvClwph.text = clwph
//                    v.tvClmc.text = clmc
//                    v.tvClth.text = clth
//                    v.tvXhsl.text = xhsl
//                }
//            }
//        }
//        vm.gxResult.observe(this) {
//            if (it.size > 0) {
//                it[0].apply {
//                    v.tvGxh.text = gxh
//                    v.tvGxmc.text = gxms
//                    v.tvBgrmc.text = bgrxm
//                    v.tvBgsl.text = bgsl
//                    v.tvBgsj.text = bgsj
//                }
//            }
//        }
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


    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                cardno = result
                v.etLzkkh.setText(cardno)
                vm.getLzkxq(companyNo, cardno)
//                vm.getSubList(cardno)
//                vm.getCLXH(cardno)
//                vm.getGx(cardno)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)!!.trim { it <= ' ' }
                    cardno = result
                    v.etLzkkh.setText(cardno)
                    vm.getLzkxq(companyNo, cardno)
//                    vm.getSubList(cardno)
//                    vm.getCLXH(cardno)
//                    vm.getGx(cardno)
                }

            }
        }
    }

    override fun onBarcodeEvent(result: String) {
        cardno = result.trim { it <= ' ' }
        v.etLzkkh.setText(cardno)
        vm.getLzkxq(companyNo, cardno)
//        vm.getSubList(cardno)
//        vm.getCLXH(cardno)
//        vm.getGx(cardno)
    }

    override fun jd(position: Int) {
        val dialog = DialogLzk(this, object : DialogLzk.PositiveListener {
            override fun positiveClick(jdsl: Int) {
                vm.jd(LzkJdModel(cardno, list[position].GXH, jdsl, accout, username))
            }
        })
        dialog.show()
        dialog.setLzkkh(cardno)
        dialog.setJdgx(list[position].GXMS)
        if (position == 0) {
            dialog.setJdsl(list[position].BZS.toString())
        } else if (position > 0) {
            dialog.setJdsl(list[position - 1].DQGXLJBGSL.toString())
        }
    }

}
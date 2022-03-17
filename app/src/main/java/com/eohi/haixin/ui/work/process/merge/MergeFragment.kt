package com.eohi.haixin.ui.work.process.merge

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentScanMergeBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.process.viewmodel.MergeCardViewModel
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.ToastUtil
import org.greenrobot.eventbus.EventBus

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/17 10:15
 */
class MergeFragment : BaseFragment<MergeCardViewModel, FragmentScanMergeBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.cardinfo.observe(this, Observer {
            if (it.isNotEmpty()) {
                v.tvLzkkh.text = it[0].LZKKH
                v.tvRwdh.text = it[0].RWDH
                v.tvCpmc.text = it[0].WPMC
                v.tvCpdm.text = it[0].WPH
                v.tvCllh.text = it[0].CLLH
                v.tvRcllh.text = it[0].RCLLH
                v.tvScph.text = it[0].SCPH
                v.tvKpsl.text = it[0].BZS.toString()
                v.tvBgsl.text = it[0].BGSL.toString()
                v.tvTsztms.text = it[0].TSZTMS
                v.etSl.setText(it[0].BZS.toString())
                EventBus.getDefault().post(EventMessage(EventCode.DATA, it[0].LZKKH, "", 0, it[0]))
            }
        })
    }

    override fun initView() {
        initHoneyScan()
    }

    override fun initClick() {
    }

    override fun initData() {
        v.tvCzr.text = username
        v.tvRq.text = DateUtil.data

        v.etSl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.isNotEmpty()) {
                    if (s.toString().toInt() > v.tvKpsl.text.toString().toInt()) {
                        ToastUtil.showToast(mContext, "合卡数量不能大于卡片数量")
                        return
                    }
                    EventBus.getDefault().post(
                        EventMessage(
                            EventCode.REFRESH,
                            v.tvLzkkh.text.toString(),
                            "",
                            s.toString().toInt()
                        )
                    )
                }
            }

        })

    }

    override fun lazyLoadData() {
    }

    //扫码结果
    override fun onBarcodeResult(result: String) {
        v.etLzk.setText(result)
        vm.getCardInfo(companyNo, result)
    }

    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter("com.android.server.scannerservice.broadcast.haixin")
        intentFilter.priority = Int.MAX_VALUE
        activity!!.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "com.android.server.scannerservice.broadcast.haixin") {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etLzk.setText(result)
                vm.getCardInfo(companyNo, result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity!!.unregisterReceiver(scanReceiver)
        super.onPause()
    }

}
package com.eohi.haixin.ui.work.picking.outbound

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.eohi.haixin.App
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentPickingOutboundBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.main.model.ItemInfo
import com.eohi.haixin.ui.main.model.KwModel
import com.eohi.haixin.ui.main.model.WarehouseInfo
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/14 16:32
 */
class PickingOutboundFragment :
    BaseFragment<PickingOutboundViewModel, FragmentPickingOutboundBinding>() {
    private var username by Preference("username", "")
    private var ishardware = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo>? = null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: List<KwModel>? = null
    private var kwlist: ArrayList<String?> = ArrayList()
    override fun isNeedEventBus(): Boolean {
        return false
    }


    override fun initVM() {
        vm.whlist.observe(this, Observer {
            warehouseInfolist = it
            cklist.clear()
            for (i in warehouseInfolist!!.indices) {
                if (null != warehouseInfolist!![i].CKMC)
                    cklist.add(warehouseInfolist!![i].CKMC)
            }
            dialogList()
        })

        vm.kwlist.observe(this, Observer {
            mkwList = it
            kwlist.clear()
            for (i in mkwList!!.indices) {
                if (null != mkwList!![i].kwmc)
                    kwlist.add(mkwList!![i].kwmc)
            }
//            dialogkwList();
        })

        vm.itemlist.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.etZysl.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                v.etFzdw.text = it[0].fzjldw
                val item = ItemInfo()
                item.cktype = ishardware //仓库类型
                item.tmh = it[0].tmh
                item.FSL = it[0].fzsl.toString()
                item.GGMS = it[0].gg
                item.WPMC = it[0].wpmc
                item.wph = it[0].wph
                item.ZSL = it[0].sl.toString()
                item.ckh = v.etCkh.text.toString()
                item.kwh = v.etKwh.text.toString()
                item.gdh = v.etGdh.text.toString()
                item.jldh = v.etJld.text.toString()
                App.post(EventMessage(EventCode.DATA, "", "", 0, item))
            } else {
                errorResult(ErrorResult(1001, "数据为空！", true))
            }
        })

    }

    override fun initView() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
        initHoneyScan()
        v.etCkh.setOnClickListener(View.OnClickListener {
            if (warehouseInfolist != null && warehouseInfolist!!.isNotEmpty()) {
                dialogList()
            } else {
                vm.getCklist()
            }
        })
        v.etKwh.setOnClickListener {
            if (mkwList != null && mkwList!!.isNotEmpty()) {
                dialogkwList()
            }
        }


    }


    private fun dialogList() {
        if (cklist.size == 0) return
        var items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etCkh.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                //是AGV
                if (model.SFAGVCK.equals("true")) {
                    ishardware = 2
                    v.etKwh.setText("自动分配库位")
                } else {
                    ishardware = 1
                    vm.getKwlist(model.CKH)
                }
            }
        }
        builder.create().show()
    }


    private fun dialogkwList() {
        if (kwlist.size == 0) return
        var items = kwlist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etKwh.setText(mkwList!![which].kwh)
        }
        builder.create().show()
    }


    override fun initClick() {
        v.etZysl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etTmh.text.isNotEmpty()) {
                    App.post(
                        EventMessage(
                            EventCode.REFRESH,
                            v.etTmh.text.toString(),
                            s.toString(),
                            0,
                            null
                        )
                    )
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        v.etFzsl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etTmh.text.isNotEmpty()) {
                    App.post(
                        EventMessage(
                            EventCode.FSLREFRESH,
                            v.etTmh.text.toString(),
                            s.toString(),
                            0,
                            null
                        )
                    )
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

    }

    override fun initData() {
    }

    override fun lazyLoadData() {
    }


    override fun onResume() {
        // 注册广播接收器
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        activity!!.registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION && isVisibleToUser) {
                if (0 == ishardware) {
                    ToastUtil.showToast(mContext, "请先选择仓库")
                    return
                }
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                if (v.etGdh.isFocused) {
                    v.etGdh.setText(result)
                    v.etTmh.isFocusable = true
                    v.etTmh.requestFocus()
                } else if (v.etTmh.isFocused) {
                    v.etTmh.setText(result)
                    vm.getItemInfo(result, v.etCkh.text.toString(), v.etKwh.text.toString())
                } else if (v.etKwh.isFocused) {
                    v.etKwh.setText(result)
                    v.etGdh.requestFocus()
                }

            }
        }
    }

    override fun onPause() {
        //取消广播注册
        activity!!.unregisterReceiver(scanReceiver)
        super.onPause()
    }

    override fun onBarcodeResult(result: String) {
        if (isVisibleToUser) {
            if (0 == ishardware) {
                ToastUtil.showToast(mContext, "请先选择仓库")
                return
            }
            when {
                v.etGdh.isFocused -> {
                    v.etGdh.setText(result)
                    v.etTmh.isFocusable = true
                    v.etJld.requestFocus()
                }
                v.etTmh.isFocused -> {
                    v.etTmh.setText(result)
                    vm.getItemInfo(result, v.etCkh.text.toString(), v.etKwh.text.toString())
                }
                v.etKwh.isFocused -> {
                    v.etKwh.setText(result)
                    v.etGdh.requestFocus()
                }
                v.etJld.isFocused -> {
                    v.etJld.setText(result)
                    v.etTmh.requestFocus()
                }
            }

        }

    }

}
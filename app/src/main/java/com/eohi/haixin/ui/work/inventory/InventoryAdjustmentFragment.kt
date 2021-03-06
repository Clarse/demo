package com.eohi.haixin.ui.work.inventory

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.text.Editable
import android.text.TextWatcher
import androidx.lifecycle.Observer
import com.eohi.haixin.App
import com.eohi.haixin.R
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentInventoryAdjustmentBinding
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
 * @date :2021/5/21 9:38
 */
class InventoryAdjustmentFragment :
    BaseFragment<InventoryAdjustmentViewModel, FragmentInventoryAdjustmentBinding>() {
    private var username by Preference("username", "")
    private var ishardware = 0//是否是五金
    private var warehouseInfolist: ArrayList<WarehouseInfo>? = null
    private var cklist: ArrayList<String?> = ArrayList()
    private var mkwList: List<KwModel>? = null
    private var kwlist: ArrayList<String?> = ArrayList()
    var ckh: String? = null
    private var isAdd: Boolean = true
    private var isfzAdd: Boolean = true
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
//            dialogkwList()
        })

        vm.itemlist.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                v.tvWph.text = it[0].wph
                v.tvWpmc.text = it[0].wpmc
                v.tvGg.text = it[0].gg
                v.tvNumber.text = it[0].sl.toString()
                v.tvFzsl.text = it[0].fzsl.toString()

                val item = ItemInfo()
                item.tmh = it[0].tmh
                item.FSL = it[0].fzsl.toString()
                item.GGMS = it[0].gg
                item.WPMC = it[0].wpmc
                item.wph = it[0].wph
                item.ZSL = it[0].sl.toString()
                item.ckh = v.etStartck.text.toString()
                item.kwh = v.etStartkw.text.toString()
                item.tzhsl = v.tvAdjustmentNumber.text.toString()
                App.post(EventMessage(EventCode.DATA, "", "", 0, item))
            } else {
                errorResult(ErrorResult(1001, "数据为空！", true))
            }

        })


    }

    override fun initView() {
        initHoneyScan()
        v.etSl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etSl.text.isNotEmpty()) {
                    if (isAdd) {
                        if (v.tvNumber.text.isNotEmpty()) {
                            val reF = v.tvNumber.text.toString().toFloat() + v.etSl.text.toString()
                                .toFloat()
                            v.tvAdjustmentNumber.text = reF.toString()
                            App.post(
                                EventMessage(
                                    EventCode.REFRESH,
                                    v.etTmh.text.toString(),
                                    reF.toString(),
                                    0,
                                    null
                                )
                            )
                        }
                    } else {
                        if (v.tvNumber.text.isNotEmpty()) {
                            val reF = v.tvNumber.text.toString().toFloat() - v.etSl.text.toString()
                                .toFloat()
                            v.tvAdjustmentNumber.text = reF.toString()
                            App.post(
                                EventMessage(
                                    EventCode.REFRESH,
                                    v.etTmh.text.toString(),
                                    reF.toString(),
                                    0,
                                    null
                                )
                            )
                        }
                    }
                } else {
                    v.tvAdjustmentNumber.text = v.tvNumber.text
                }

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }


        })

        v.etFzsl.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (v.etFzsl.text.isNotEmpty()) {
                    if (isfzAdd) {
                        if (v.tvFzsl.text.isNotEmpty()) {
                            val reF = v.tvFzsl.text.toString().toFloat() + v.etFzsl.text.toString()
                                .toFloat()
                            v.tvTzhfzsl.text = reF.toString()
                            App.post(
                                EventMessage(
                                    EventCode.FSLREFRESH,
                                    v.etTmh.text.toString(),
                                    "-" + v.etFzsl.text.toString(),
                                    0,
                                    null
                                )
                            )
                        }

                    } else {
                        if (v.tvFzsl.text.isNotEmpty()) {
                            val reF = v.tvFzsl.text.toString().toFloat() - v.etFzsl.text.toString()
                                .toFloat()
                            v.tvTzhfzsl.text = reF.toString()
                            App.post(
                                EventMessage(
                                    EventCode.FSLREFRESH,
                                    v.etTmh.text.toString(),
                                    v.etFzsl.text.toString(),
                                    0,
                                    null
                                )
                            )
                        }
                    }
                } else {
                    v.tvTzhfzsl.text = v.tvFzsl.text
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })


        v.rg.setOnCheckedChangeListener { group, checkedId ->
            v.etSl.setText("")
            isAdd = when (checkedId) {
                R.id.btn_0 -> true
                R.id.btn_1 -> false
                else -> true
            }
        }
        v.rgFz.setOnCheckedChangeListener { group, checkedId ->
            v.etFzsl.setText("")
            isfzAdd = when (checkedId) {
                R.id.btn_fz_add -> true
                R.id.btn_fz_remove -> false
                else -> true
            }
        }


    }

    override fun initClick() {
        v.btnStartck.setOnClickListener {
            vm.getCklist()
        }

        v.btnStartkw.setOnClickListener {
            if (null != ckh) {
                if (kwlist.size > 0) {
                    dialogkwList()
                } else {
                    vm.getKwlist(ckh)
                }
            }
        }


    }

    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
    }


    private fun dialogList() {
        if (cklist.size == 0) return
        val items = cklist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etStartck.text = warehouseInfolist!![which].CKH
            if (null != warehouseInfolist) {
                val model = warehouseInfolist!![which]
                ckh = model.CKH
                ishardware = if (model.SFAGVCK.equals("true")) {
                    2
                } else {
                    1
                }
                vm.getKwlist(model.CKH)
            }
        }
        builder.create().show()
    }


    private fun dialogkwList() {
        if (kwlist.size == 0) return
        val items = kwlist.toTypedArray()
        val builder = AlertDialog.Builder(context, 3)
        builder.setItems(items) { dialog, which ->
            dialog.dismiss()
            v.etStartkw.setText(mkwList!![which].kwh)
        }
        builder.create().show()
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
                if (v.etStartck.text.isEmpty()) {
                    ToastUtil.showToast(mContext, "仓库号不能为空")
                    return
                }
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when {
                    v.etTmh.isFocused -> {
                        v.etSl.setText("")
                        v.etFzsl.setText("")
                        v.tvTzhfzsl.text = ""
                        v.tvAdjustmentNumber.text = ""
                        v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getItemInfo(
                            result,
                            v.etStartck.text.toString(),
                            v.etStartkw.text.toString()
                        )
                    }
                    v.etStartkw.isFocused -> {
                        v.etStartkw.setText(result)
                        v.etTmh.requestFocus()
                    }
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
            if (v.etStartck.text.isEmpty()) {
                ToastUtil.showToast(mContext, "仓库号不能为空")
                return
            }
            when {
                v.etTmh.isFocused -> {
                    v.etSl.setText("")
                    v.etFzsl.setText("")
                    v.tvTzhfzsl.text = ""
                    v.tvAdjustmentNumber.text = ""
                    v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getItemInfo(result, v.etStartck.text.toString(), v.etStartkw.text.toString())
                }
                v.etStartkw.isFocused -> {
                    v.etStartkw.setText(result)
                    v.etTmh.requestFocus()
                }
            }

        }
    }

}
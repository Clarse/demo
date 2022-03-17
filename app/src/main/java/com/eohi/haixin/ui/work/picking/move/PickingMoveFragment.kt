package com.eohi.haixin.ui.work.picking.move

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
import com.eohi.haixin.databinding.FragmentPickingMoveBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.main.model.ItemInfo
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 14:20
 */
class PickingMoveFragment : BaseFragment<PickingMoveViewModel, FragmentPickingMoveBinding>() {
    private var username by Preference("username", "")
    var startCkh: String? = null
    var startkwh: String? = null
    var endCkh: String? = null
    var endKwh: String? = null
    var type = ""
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.cklist.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                startCkh = it[0].ckh
                startkwh = it[0].kwh
            }
        })
        vm.endcklist.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                endCkh = it[0].ckh
                endKwh = it[0].kwh
            }
        })
        vm.itemlist.observe(this, Observer {
            try {
                if (!it.isNullOrEmpty()) {
                    v.tvWph.text = it[0].wph
                    v.tvWpmc.text = it[0].wpmc
                    v.tvGg.text = it[0].gg
                    v.etZysl.text = Editable.Factory.getInstance().newEditable(it[0].sl.toString())
                    v.etFzsl.setText(it[0].fzsl.toString())
                    v.etFzdw.text = it[0].fzjldw
                    val item = ItemInfo()
                    item.tmh = it[0].tmh
                    item.FSL = it[0].fzsl.toString()
                    item.GGMS = it[0].gg
                    item.WPMC = it[0].wpmc
                    item.wph = it[0].wph
                    item.ZSL = it[0].sl.toString()
                    item.fckh = startCkh
                    item.fkwh = startkwh
                    item.ckh = endCkh
                    item.kwh = endKwh
                    item.ZDW = it[0].fzjldw
                    if ("agvxsfh" == type) {
                        item.fhtzdh = v.etGdh.text.toString()
                    } else {
                        item.gdh = v.etGdh.text.toString()
                    }
                    App.post(EventMessage(EventCode.DATA, "", "", 0, item))
                } else {
                    errorResult(ErrorResult(1001, "数据为空！", true))
                }
            } catch (e: Exception) {

            }

        })


    }

    override fun initView() {
        initHoneyScan()
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

    override fun initClick() {
    }

    override fun initData() {
        v.tvUser.text = username
        v.tvDate.text = DateUtil.data
        if ("work" == type) {
            v.etGdh.visibility = View.GONE
            v.tvGdhText.visibility = View.GONE
        } else {
            v.etGdh.visibility = View.VISIBLE
            v.tvGdhText.visibility = View.VISIBLE
            if ("agvxsfh" == type)
                v.tvGdhText.text = "发货通知单号"
        }

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
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                when {
                    v.etStartPoint.isFocused -> {
                        v.etStartPoint.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getCklist(result)
                        v.etEndPoint.requestFocus()
                    }
                    v.etEndPoint.isFocused -> {
                        v.etEndPoint.text = Editable.Factory.getInstance().newEditable(result)
                        vm.getEndList(result)
                        if (v.etGdh.visibility == View.VISIBLE) {
                            v.etGdh.requestFocus()
                        } else {
                            v.etTmh.requestFocus()
                        }

                    }
                    v.etGdh.isFocused -> {
                        v.etGdh.setText(result)
                        v.etTmh.requestFocus()
                    }
                    v.etTmh.isFocused -> {
                        v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                        if (null != startCkh) {
                            vm.getItemInfo(result, startCkh!!, startkwh!!)
                        } else {
                            ToastUtil.showToast(mContext, "请先扫描站点码")
                        }

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
            when {
                v.etStartPoint.isFocused -> {
                    v.etStartPoint.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getCklist(result)
                    v.etEndPoint.requestFocus()
                }
                v.etEndPoint.isFocused -> {
                    v.etEndPoint.text = Editable.Factory.getInstance().newEditable(result)
                    vm.getEndList(result)
                    if (v.etGdh.visibility == View.VISIBLE) {
                        v.etGdh.requestFocus()
                    } else {
                        v.etTmh.requestFocus()
                    }

                }
                v.etGdh.isFocused -> {
                    v.etGdh.setText(result)
                    v.etTmh.requestFocus()
                }
                v.etTmh.isFocused -> {
                    v.etTmh.text = Editable.Factory.getInstance().newEditable(result)
                    if (null != startCkh) {
                        vm.getItemInfo(result, startCkh!!, startkwh!!)
                    } else {
                        ToastUtil.showToast(mContext, "请先扫描站点码")
                    }

                }
            }
        }

    }


}
package com.eohi.haixin.ui.work.purchasein

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentInstorageDetailBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.main.model.ItemInfo
import com.eohi.haixin.ui.work.adapter.InItemlistAdapter
import com.eohi.haixin.ui.work.model.AgvSubmitModel
import com.eohi.haixin.ui.work.model.Data
import com.eohi.haixin.ui.work.model.InstorageModel
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.view.DialogAgv
import com.eohi.haixin.view.DialogAutoEnd
import com.eohi.haixin.view.DialogStartToEnd

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/12 10:06
 */
class InstorageDetailFragment :
    BaseFragment<InstorageDetailViewModel, FragmentInstorageDetailBinding>() {
    private var accout by Preference<String>("userid", "")
    private var username by Preference("username", "")
    private var companyno by Preference("companyNo", "")
    private var adpater: InItemlistAdapter? = null
    private var listdata: ArrayList<ItemInfo>? = null
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        vm.wjresult.observe(this, Observer {
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        })

        vm.mrkresult.observe(this, Observer {
            if (it.code == 1000) {
                vm.subAgvType("040")
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        })

        vm.agvtype.observe(this, Observer {
            if (it.code == 1000) {
                val type = it.data!!.list!![0]
                if (type.QDLX == 1 && type.ZDLX == 0) { //启点自动分配
                    val dialog = DialogAgv(mContext, activity, "采购入库")
                    dialog.onOkClick {
                        dialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", "", it, "采购入库", "", 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 1) {
                    val autoEndDialog = DialogAutoEnd(mContext, activity, "采购入库")
                    autoEndDialog.onOkClick {
                        autoEndDialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", it, "", "采购入库", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    autoEndDialog.show()
                } else if (type.QDLX == 0 && type.ZDLX == 0) {
                    val dialog = DialogStartToEnd(mContext, activity, "采购入库")
                    dialog.onOkClick { s, s2 ->
                        dialog.dismiss()
                        var agvsub = AgvSubmitModel(
                            companyno, accout,
                            "040", s, s2, "采购入库", insmodel!!.swh, 0, ""
                        )
                        vm.agvTaskadd(agvsub)
                    }
                    dialog.show()
                } else {

                }

            } else {
                ToastUtil.showToast(mContext, it.msg)
            }

        })

        vm.agvresult.observe(this, Observer {
            if (it.code == 1000) {
                ToastUtil.showToast(mContext, it.msg)
                listdata?.clear()
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, it.msg)
            }
        })

    }

    override fun initView() {
        listdata = ArrayList()
        adpater = InItemlistAdapter(mContext, listdata!!)
        v.rc.run {
            layoutManager = LinearLayoutManager(mContext)
            this.adapter = adpater
        }
        adpater?.itemLongClick { view, i ->
            val popup = PopupMenu(context!!, view) //第二个参数是绑定的那个view
            //获取菜单填充器
            val inflater = popup.menuInflater
            //填充菜单
            inflater.inflate(R.menu.listmenu, popup.menu)
            //绑定菜单项的点击事件
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.navigation_practice -> {
                        listdata?.removeAt(i)
                        adpater?.notifyDataSetChanged()
                    }
                }
                false
            }
            popup.show()

        }

    }

    var insmodel: InstorageModel? = null
    override fun initClick() {
        v.brnOk.setOnClickListener {
            if (listdata!!.isEmpty())
                return@setOnClickListener
            var mlistData = ArrayList<Data>()
            for (i in listdata!!.indices) {
                var data = Data(listdata!![i].FSL, listdata!![i].tmh, listdata!![i].ZSL)
                mlistData.add(data)
            }

            insmodel = InstorageModel(
                mlistData, username, "",
                accout, "040", companyno, listdata!![0].ckh!!,
                "", "", "", "", "", "", ""
            )

            if (listdata!![0].cktype == 2) { //非五金
                vm.getOrderNoAgvSubmit("TMCGRK", insmodel!!)

            } else if (listdata!![0].cktype == 1) { //五金仓库
                insmodel!!.tkwh = listdata!![0].kwh!!
                vm.getOrderNo("TMCGRK", insmodel!!)
            }

        }
    }

    override fun initData() {

    }

    override fun lazyLoadData() {
    }

    override fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.DATA) {
            val item = msg.obj as ItemInfo
            var isexit = false
            for (index in listdata!!.indices) {
                if (item.tmh == listdata!![index].tmh)
                    isexit = true
            }
            if (!isexit) {
                listdata?.add(item)
                adpater?.notifyDataSetChanged()
            } else {
                ToastUtil.showToast(mContext, "条码已存在！")
            }

        } else if (msg.code == EventCode.REFRESH) {
            for (index in listdata!!.indices) {
                if (msg.msg == listdata!![index].tmh) {
                    listdata!![index].ZSL = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        }
    }

}
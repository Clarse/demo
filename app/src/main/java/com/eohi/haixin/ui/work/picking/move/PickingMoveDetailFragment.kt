package com.eohi.haixin.ui.work.picking.move

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentPickingMoveDetailBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.main.model.ItemInfo
import com.eohi.haixin.ui.work.adapter.InItemlistAdapter
import com.eohi.haixin.ui.work.model.Data
import com.eohi.haixin.ui.work.model.InstorageModel
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 14:55
 */
class PickingMoveDetailFragment :
    BaseFragment<PickingMoveDetailViewModel, FragmentPickingMoveDetailBinding>() {
    private var accout by Preference<String>("userid", "")
    private var username by Preference("username", "")
    private var adpater: InItemlistAdapter? = null
    private var listdata: ArrayList<ItemInfo>? = null
    var ywlx = "010"
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
                mlistData,
                username,
                "",
                accout,
                ywlx,
                companyNo,
                listdata!![0].ckh!!,
                listdata!![0].kwh!!,
                "",
                "",
                listdata!![0].fckh!!,
                listdata!![0].fkwh!!,
                "",
                listdata!![0].gdh!!
            )
            vm.submit("TMYK", insmodel!!)
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
        } else if (msg.code == EventCode.FSLREFRESH) {
            for (index in listdata!!.indices) {
                if (msg.msg == listdata!![index].tmh) {
                    listdata!![index].FSL = msg.arg1
                    break
                }
            }
            adpater?.notifyDataSetChanged()
        }
    }


}
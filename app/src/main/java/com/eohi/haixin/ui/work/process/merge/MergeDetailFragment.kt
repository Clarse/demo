package com.eohi.haixin.ui.work.process.merge

import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.FragmentScanMergeDetailBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.adapter.MergeDetailAdapter
import com.eohi.haixin.ui.work.model.CardInfoModel
import com.eohi.haixin.view.DialogAlert
import org.greenrobot.eventbus.EventBus

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/17 10:40
 */
class MergeDetailFragment : BaseFragment<BaseViewModel, FragmentScanMergeDetailBinding>() {
    lateinit var adapter: MergeDetailAdapter
    lateinit var listdata: ArrayList<CardInfoModel>
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
    }

    override fun initView() {
    }

    override fun initClick() {
    }

    override fun initData() {
        listdata = ArrayList()
        adapter = MergeDetailAdapter(mContext, listdata)
        v.rc.let {
            it.layoutManager = LinearLayoutManager(mContext)
            it.adapter = adapter
        }

        adapter.setDetele {
            val dialog = DialogAlert(mContext, "确定要删除吗？")
            dialog.setOnOkClick {
                listdata.removeAt(it)
                adapter.notifyDataSetChanged()
                EventBus.getDefault().post(EventMessage(EventCode.DATA_DETELE, "", "", it))
            }
            dialog.show()
        }
        adapter.setEdit { i, i2 ->
            listdata[i].HKSL = i2
            EventBus.getDefault().post(EventMessage(EventCode.SLREFRESH, listdata[i].LZKKH, "", i2))
        }

    }

    override fun lazyLoadData() {
    }

    override fun handleEvent(msg: EventMessage) {
        when (msg.code) {
            EventCode.DATA -> {
                var b = true
                for (i in listdata.indices) {
                    if (msg.msg == listdata[i].LZKKH) {
                        b = false
                        break
                    }
                }
                if (b) {
                    val item = msg.obj as CardInfoModel
                    item.HKSL = item.BZS
                    listdata.add(0, item)
                    adapter.notifyDataSetChanged()
                    EventBus.getDefault().post(EventMessage(EventCode.DATA_RESULT, "", "", 0, item))
                }
            }
            EventCode.REFRESH -> {
                for (i in listdata.indices) {
                    if (msg.msg == listdata[i].LZKKH) {
                        listdata[i].HKSL = msg.arg2
                        adapter.notifyDataSetChanged()
                        break
                    }
                }
                EventBus.getDefault().post(EventMessage(EventCode.SLREFRESH, msg.msg, "", msg.arg2))
            }
            EventCode.DATA_SUBMITRESULT -> {
                listdata.clear()
                adapter.notifyDataSetChanged()
            }

        }


    }


}
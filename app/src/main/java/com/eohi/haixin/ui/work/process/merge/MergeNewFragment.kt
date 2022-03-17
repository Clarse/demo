package com.eohi.haixin.ui.work.process.merge

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentScanMergeNewBinding
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.work.model.CardInfoModel
import com.eohi.haixin.ui.work.model.CardItem
import com.eohi.haixin.ui.work.model.CradMergeSubModel
import com.eohi.haixin.ui.work.process.viewmodel.MergeCardViewModel
import com.eohi.haixin.utils.QRCodeUtil
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.widget.clicks
import org.greenrobot.eventbus.EventBus

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/17 10:56
 */
class MergeNewFragment : BaseFragment<MergeCardViewModel, FragmentScanMergeNewBinding>() {
    lateinit var listdata: ArrayList<CardInfoModel>
    override fun isNeedEventBus(): Boolean {
        return true
    }

    override fun initVM() {
        vm.subresult.observe(this, Observer {
            listdata.clear()
            EventBus.getDefault().post(EventMessage(EventCode.DATA_SUBMITRESULT))
            ToastUtil.showToast(mContext, "流转卡合卡成功")
            if (it.isNotEmpty()) {
                v.consCard.visibility = View.VISIBLE
                v.ivScan.setImageBitmap(QRCodeUtil.createImage(it[0].XLZKKH))
                v.tvTitle.text = it[0].LZKLX
                v.tvTransfercard.text = it[0].XLZKKH
                v.tvOrderNumber.text = it[0].RWDH
                v.tvProductName.text = it[0].WPMC
                v.tvSpecificationModel.text = it[0].GGMS
                v.tvProductCode.text = it[0].WPH
                v.tvUnitMeasurement.text = it[0].JLDW
                v.tvProductionQuantity.text = it[0].SCSL.toString()
                v.tvProductionBatch.text = it[0].SCPH
                v.tvMaterialheatNumber.text = it[0].CLLH
                v.tvHeattreatmentNumber.text = it[0].RCLLH

                val items = it[0].items
                var gx = StringBuffer()
                for (i in items.indices) {
                    gx.append("□" + items[i].GXMS + "  ")
                }
                v.tvProcess.text = gx.toString()
                v.tvSpecialState.text = it[0].TSZTMS
            }

        })
    }

    override fun initView() {
    }

    override fun initClick() {
        v.btnSub.clicks {
            if (listdata.isNotEmpty()) {
                if (v.etGx.text.isEmpty()) {
                    ToastUtil.showToast(mContext, "流转卡类型不能为空！")
                    return@clicks
                }

                val items = ArrayList<CardItem>()
                listdata.onEach {
                    items.add(CardItem(it.BZS, it.LZKKH, it.DjLsh, it.HKSL))
                }
                val model = CradMergeSubModel(
                    v.tvKpsl.text.toString().toInt(),
                    v.tvCllh.text.toString(),
                    companyNo,
                    accout,
                    username,
                    items,
                    v.etGx.text.toString(),
                    v.tvRcllh.text.toString(),
                    v.tvRwdh.text.toString(),
                    v.tvTsztms.text.toString()
                )
                vm.submitMerge(model)
            } else {
                ToastUtil.showToast(mContext, "请先扫描流转卡！")
            }
        }
    }

    override fun initData() {
        listdata = ArrayList()
        v.etGx.clicks {
            showListPopulWindow(v.etGx, arrayOf("外协流转卡", "生产流转卡"))
        }
    }


    private fun showListPopulWindow(mEditText: TextView, list: Array<String>) {
        val listPopupWindow = ListPopupWindow(mContext)
        listPopupWindow.setAdapter(
            ArrayAdapter(
                mContext,
                R.layout.pop_item,
                list
            )
        ) //用android内置布局，或设计自己的样式
        listPopupWindow.anchorView = mEditText //以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.isModal = true
        listPopupWindow.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    mContext,
                    R.color.white
                )
            )
        )
        listPopupWindow.setOnItemClickListener { adapterView, view, i, l ->
            //设置项点击监听
            mEditText.text = list[i] //把选择的选项内容展示在EditText上
            listPopupWindow.dismiss() //如果已经选择了，隐藏起来
        }
        listPopupWindow.show() //把ListPopWindow展示出来
    }

    override fun lazyLoadData() {
    }

    override fun handleEvent(msg: EventMessage) {
        when (msg.code) {
            EventCode.DATA_RESULT -> {
                val t = msg.obj as CardInfoModel
                v.tvRwdh.text = t.RWDH
                v.tvCpmc.text = t.WPMC
                v.tvCpdm.text = t.WPH
                v.tvCllh.text = t.CLLH
                v.tvRcllh.text = t.RCLLH
                v.tvScph.text = t.SCPH
                v.tvKpsl.text = t.HKSL.toString()
                v.tvTsztms.setText(t.TSZTMS)
                listdata.add(t)
                sendSum()
            }
            EventCode.SLREFRESH -> {
                for (i in listdata.indices) {
                    if (msg.msg == listdata[i].LZKKH) {
                        listdata[i].HKSL = msg.arg2
                        break
                    }
                }
                sendSum()
            }

            EventCode.DATA_DETELE -> {
                listdata.removeAt(msg.arg2)
                sendSum()
            }

        }
    }

    private fun sendSum() {
        var sum = 0
        listdata.forEach {
            sum += it.HKSL
        }
        v.tvKpsl.text = sum.toString()
    }

}
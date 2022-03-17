package com.eohi.haixin.ui.main

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.databinding.FragmentAgvBinding
import com.eohi.haixin.ui.main.agv.AgvOperationActivity
import com.eohi.haixin.ui.main.agv.pick.AgvSendingActivity
import com.eohi.haixin.ui.main.agv.production.SurplusMaterialsActivity
import com.eohi.haixin.ui.main.agv.sales.AgvSalesActivity
import com.eohi.haixin.ui.main.model.Menu
import com.eohi.haixin.ui.main.model.PointReleaseModel
import com.eohi.haixin.ui.main.model.Secondcd
import com.eohi.haixin.ui.main.model.Threecd
import com.eohi.haixin.ui.work.adapter.ImageViewAdapter
import com.eohi.haixin.ui.work.model.AgvSubmitModel
import com.eohi.haixin.ui.work.model.ImageViewModel
import com.eohi.haixin.ui.work.picking.move.PickingMoveActivity
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.view.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 10:09
 */
class AgvFragment : BaseFragment<AgvViewModel, FragmentAgvBinding>() {
    private var accout by Preference<String>("userid", "")
    private var firstmenus: ArrayList<Menu>? by Preference("menus", null)
    lateinit var agvmenus: ArrayList<Secondcd>
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
        vm.linelist.observe(this, Observer {
            var title: String = "货架至产线"
            val dialog = DialogtoLine(mContext, activity, title, it)
            dialog.onOkClick { s, s2 ->
                dialog.dismiss()
                var agvsub = AgvSubmitModel(
                    companyNo, accout,
                    "020", s, "", title, "", 0, s2
                )
                vm.agvTaskadd(agvsub)
            }
            dialog.show()
        })


//        vm.linelistrk.observe(this, Observer {
//            var title: String = v.tvBhqCx.text.toString()
//            title = title.replace("\r|\n".toRegex(), "")
//            val  dialog = DialogGoodsToline(mContext,activity,it)
//            dialog.onOkClick { s ->
//                dialog.dismiss()
//                var agvsub = AgvSubmitModel(companyno,accout,
//                    "020","","",title,"",0,s)
//                vm.agvTaskadd(agvsub)
//            }
//            dialog.show()
//        })

        vm.bhglinelist.observe(this, Observer {
            var title: String = "不合格品至产线"
            val dialog = DialogtoLine(mContext, activity, title, it)
            dialog.onOkClick { s, s2 ->
                dialog.dismiss()
                var agvsub = AgvSubmitModel(
                    companyNo, accout,
                    "029", s, "", title, "", 0, s2
                )
                vm.agvTaskadd(agvsub)
            }
            dialog.show()
        })



        vm.agvresult.observe(this, Observer {
            vm.dismissLoading()
            ToastUtil.showToast(mContext, it.msg)
        })

        vm.agvpoint.observe(this, Observer {
            ToastUtil.showToast(mContext, it.msg)
        })

    }

    override fun initView() {
        for (i in firstmenus!!.indices) {
            if (firstmenus!![i].cdbh == "D02") {
                agvmenus = (firstmenus!![i].secondcd as ArrayList<Secondcd>?)!!
                break
            }
        }

        var cgdhjyrk: ArrayList<Threecd>? = null
        var wxrk: ArrayList<Threecd>? = null
        var scll: ArrayList<Threecd>? = null
        var wxfl: ArrayList<Threecd>? = null
        var wgjyrk: ArrayList<Threecd>? = null
        var xsfh: ArrayList<Threecd>? = null
        var jcyw: ArrayList<Threecd>? = null


        var cgdhjyrkUserd = false
        var wxrkUsed = false
        var scllUsed = false
        var wxflUsed = false
        var wgjyrkUsed = false
        var xsfhUsed = false
        var jcywUsed = false

        for (i in agvmenus.indices) {
            when (agvmenus[i].cdbh1) {
                "D0201" -> {
                    cgdhjyrk = agvmenus[i].threecd as ArrayList<Threecd>
                    cgdhjyrkUserd = agvmenus[i].ifyqx1 == 1
                }
                "D0202" -> {
                    wxrk = agvmenus[i].threecd as ArrayList<Threecd>
                    wxrkUsed = agvmenus[i].ifyqx1 == 1
                }
                "D0203" -> {
                    scll = agvmenus[i].threecd as ArrayList<Threecd>
                    scllUsed = agvmenus[i].ifyqx1 == 1
                }
                "D0204" -> {
                    wxfl = agvmenus[i].threecd as ArrayList<Threecd>
                    wxflUsed = agvmenus[i].ifyqx1 == 1
                }
                "D0205" -> {
                    wgjyrk = agvmenus[i].threecd as ArrayList<Threecd>
                    wgjyrkUsed = agvmenus[i].ifyqx1 == 1
                }
                "D0206" -> {
                    xsfh = agvmenus[i].threecd as ArrayList<Threecd>
                    xsfhUsed = agvmenus[i].ifyqx1 == 1
                }
                "D0208" -> {
                    jcyw = agvmenus[i].threecd as ArrayList<Threecd>
                    jcywUsed = agvmenus[i].ifyqx1 == 1
                }

            }
        }


        //采购入库
        if (cgdhjyrkUserd) {
            initCgrk(cgdhjyrk)
        } else {
            v.llCgrk.visibility = View.GONE
        }

        //外协入库
        if (wxrkUsed) {
            initWxrk(wxrk)
        } else {
            v.llWxrk.visibility = View.GONE
        }

        //生产领料
        if (scllUsed) {
            initScll(scll)
        } else {
            v.llScll.visibility = View.GONE
        }

        //外协发料
        if (wxflUsed) {
            initWxfl(wxfl)
        } else {
            v.llWxfl.visibility = View.GONE
        }


        //完工检验入库
        if (wgjyrkUsed) {
            initWgjyrk(wgjyrk)
        } else {
            v.consWgjyrk.visibility = View.GONE
        }

        //销售发货
        if (xsfhUsed) {
            initXsfh(xsfh)
        } else {
            v.llXsfh.visibility = View.GONE
        }

        //基础业务
        if (jcywUsed) {
            initJcyw(jcyw)
        } else {
            v.consJcyw.visibility = View.GONE
        }


        //空货架至 模具库
        v.tvKhjMjk.setOnClickListener {
            var title: String = v.tvKhjMjk.text.toString()
            title = title.replace("\r|\n".toRegex(), "")
            val dialog = DialogAgv(mContext, activity, title)
            dialog.onOkClick {
                addAgvTask("004", "", it, title)
            }
            dialog.show()
        }

        //呼叫空AGV 小车
        v.tvHjagv.setOnClickListener {
            var title: String = v.tvHjagv.text.toString()
            title = title.replace("\r|\n".toRegex(), "")
            val dialog = DialogAgv(mContext, activity, title)
            dialog.onOkClick {
                addAgvTask("002", "", it, title)
            }
            dialog.show()
        }


        //产线磨模 具入库
        // 起点： 产线模具站点（扫码）
        // 终点： 模具站点码（自动分配）
        v.tvCxmork.setOnClickListener {
            var title: String = v.tvCxmork.text.toString()
            title = title.replace("\r|\n".toRegex(), "")

            val autoEndDialog = DialogAutoEnd(mContext, activity, title)
            autoEndDialog.onOkClick {
//                addAgvTask("006",it,"",title)
            }
            autoEndDialog.show()
        }


    }

    //基础业务
    private fun initJcyw(jcyw: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in jcyw!!.indices) {
            when (jcyw[i].cdbh2) {
                "D020801" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_qt_ptby, "普通搬运"))
                }
                "D020802" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_qt_zdsf, "站点释放"))
                }
                "D020803" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_qt_wlzy, "物料转移"))
                }
                "D020804" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_fh_kzb, "调空栈板"))
                }
                "D020805" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_khj_hs, "空货架\n回收"))
                }
                "D020806" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_czbhs, "空栈板\n回收"))
                }
                "D020807" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_blhj_fjq, "备料货架\n至分拣区"))
                }
                "D020808" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_dkhj, "调空货架"))
                }
                "D020809" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_zbzy, "栈板转移"))
                }
                "D020810" -> {
                    if (jcyw[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_hjzy, "货架转移"))
                }
            }
        }
        list.add(ImageViewModel(R.mipmap.agv_error, "AGV异常\n任务处理"))


        val adapter = ImageViewAdapter(mContext, list)
        v.rcJcyw.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_qt_ptby -> {
                    var title: String = "普通搬运"
                    val dialog = DialogStartToEnd(mContext, activity, title)
                    dialog.onOkClick { s, s2 ->
//                addAgvTask("010",s,s2,title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_qt_zdsf -> {
                    val dialog = DialogPointRelease(mContext, activity)
                    dialog.onOkClick {
                        dialog.dismiss()
                        var model = PointReleaseModel(it)
                        vm.releasePoint(model)
                    }
                    dialog.show()
                }
                R.mipmap.agv_qt_wlzy -> {
                    var title: String = "物料转移"
                    val dialog = DialogStartToEnd(mContext, activity, title)
                    dialog.onOkClick { s, s2 ->
                        addAgvTask("010", s, s2, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_fh_kzb -> {
                    var title: String = "调空栈板"
                    val autoEndDialog = DialogAgv(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("002", "", it, title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_khj_hs -> {
                    var title: String = "空货架回收"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("008", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_czbhs -> {
                    var title: String = "空栈板回收"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("006", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_blhj_fjq -> {
                    var title: String = "备料货架至分拣区"
                    title = title.replace("\r|\n".toRegex(), "")
                    val dialog = DialogAgv(mContext, activity, title)
                    dialog.onOkClick {
                        addAgvTask("021", "", it, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_dkhj -> {
                    var title: String = "调空货架"
                    val dialog = DialogAgv(mContext, activity, title)
                    dialog.onOkClick { s: String ->
                        addAgvTask("004", "", s, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_zbzy -> {
                    var title: String = "栈板转移"
                    val dialog = DialogStartToEnd(mContext, activity, title)
                    dialog.onOkClick { s, s2 ->
                        addAgvTask("042", s, s2, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_hjzy -> {
                    var title: String = "货架转移"
                    val dialog = DialogStartToEnd(mContext, activity, title)
                    dialog.onOkClick { s, s2 ->
                        addAgvTask("047", s, s2, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_error -> {

                }

            }

        }

    }


    //销售发货
    private fun initXsfh(xsfh: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in xsfh!!.indices) {
            when (xsfh[i].cdbh2) {
                "D020601" -> {
                    if (xsfh[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_jccp_fhq, "拣出成品\n至发货区"))
                }
                "D020602" -> {
                    if (xsfh[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_fhq_yt, "发货区\n至月台"))
                }
                "D020603" -> {
                    if (xsfh[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_jcblp, "拣出不良\n品返回"))
                }
                "D020604" -> {
                    if (xsfh[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_xsfhfjzy, "销售发货\n分拣转移"))
                }
                "D020605" -> {
                    if (xsfh[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_xsfh, "销售发货"))
                }

            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcXsfl.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_jccp_fhq -> {
                    var title: String = "拣出成品至发货区"
                    val dialog = DialogAutoEnd(mContext, activity, title)
                    dialog.onOkClick { s: String ->
                        addAgvTask("033", s, "", title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_fhq_yt -> {
                    var title: String = "发货区至月台"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("034", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_jcblp -> {
                    var title: String = "拣出不良品返回"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
//                addAgvTask("006",it,"",title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_xsfhfjzy -> {
                    val intent = Intent(mContext, PickingMoveActivity::class.java)
                    intent.putExtra("type", "agvxsfh")
                    startActivity(intent)
                }
                R.mipmap.agv_xsfh -> {
                    startActivity(Intent(mContext, AgvSalesActivity::class.java))
                }
            }

        }
    }


    //完工检验入库
    private fun initWgjyrk(wgjyrk: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in wgjyrk!!.indices) {
            when (wgjyrk[i].cdbh2) {
                "D020506" -> {
                    if (wgjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_oqc_cp, "OQC至\n成品仓"))
                }
                "D020502" -> {
                    if (wgjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_cp_fqc, "成品至\nFQC区"))
                }
                "D020503" -> {
                    if (wgjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_bhg_cx, "不合格品\n至产线"))
                }
                "D020504" -> {
                    if (wgjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_hgp_oqc, "合格品至\nOQC区"))
                }
            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcWgrk.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_oqc_cp -> {
                    val intent = Intent(mContext, AgvOperationActivity::class.java)
                    intent.putExtra("type", "oqccp")
                    startActivity(intent)
                }
                R.mipmap.agv_cp_fqc -> {
                    var title: String = "成品至FQC区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("028", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_bhg_cx -> {
                    vm.getLinebhg(companyNo)
                }
                R.mipmap.agv_hgp_oqc -> {
                    var title: String = "合格品至OQC区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("030", it, "", title)
                    }
                    autoEndDialog.show()
                }

            }


        }


    }

    //外协发料
    private fun initWxfl(wxfl: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in wxfl!!.indices) {
            when (wxfl[i].cdbh2) {
                "D020401" -> {
                    if (wxfl[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_kczb_ck, "库存栈板\n至仓库"))
                }
                "D020402" -> {
                    if (wxfl[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_xzb_wx, "栈板至外\n协待发区"))
                }
                "D020403" -> {
                    if (wxfl[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_wxll, "外协领料\n分拣转移"))
                }
                "D020404" -> {
                    if (wxfl[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_wxfl, "外协发料"))
                }

            }
        }


        val adapter = ImageViewAdapter(mContext, list)
        v.rcWxfl.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_kczb_ck -> {
                    var title: String = "库存栈板至仓库"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("025", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_xzb_wx -> {
                    var title: String = "栈板至外协待发区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("026", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_wxll -> {
                    val intent = Intent(mContext, PickingMoveActivity::class.java)
                    intent.putExtra("type", "agvwxll")
                    startActivity(intent)
                }
                R.mipmap.agv_wxfl -> {
                    startActivity(Intent(mContext, AgvSendingActivity::class.java))
                }
            }

        }

    }

    private fun initScll(scll: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in scll!!.indices) {
            when (scll[i].cdbh2) {
                "D020301" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_kc_ck, "库存栈板\n至仓库"))
                }
                "D020302" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_scll_hjcx, "货架至\n产线"))
                }
                "D020303" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_hj_blq, "货架至\n备料区"))
                }
                "D020304" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_scll_cxyl, "产线余料退\n至退料存储\n区"))
                }
                "D020305" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_scll_xsbhg, "上线不合格\n材料退至上\n线不合格区"))
                }
                "D020306" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(
                            ImageViewModel(
                                R.mipmap.agv_scll_bhgzc,
                                "上线不合格\n退料区至原\n材料不合格\n暂存区"
                            )
                        )
                }
                "D020307" -> {
                    if (scll[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.work_llyk, "栈板转移"))
                }

            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcScll.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_kc_ck -> {
                    var title: String = "库存栈板至仓库"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("019", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_scll_hjcx -> {
                    vm.getLinelist(companyNo)
                }
                R.mipmap.agv_hj_blq -> {
                    var title: String = "货架至备料区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("021", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_scll_cxyl -> {
                    val intent = Intent(mContext, SurplusMaterialsActivity::class.java)
                    intent.putExtra("type", "022")
                    startActivity(intent)
                }
                R.mipmap.agv_scll_xsbhg -> {
                    val intent = Intent(mContext, SurplusMaterialsActivity::class.java)
                    intent.putExtra("type", "023")
                    startActivity(intent)
                }
                R.mipmap.agv_scll_bhgzc -> {
                    var title: String = "上线不合格退料区至原材料不合格暂存区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("024", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.work_llyk -> {
                    val intent = Intent(mContext, PickingMoveActivity::class.java)
                    intent.putExtra("type", "agv")
                    startActivity(intent)
                }

            }

        }

    }

    private fun initWxrk(wxrk: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in wxrk!!.indices) {
            when (wxrk[i].cdbh2) {
                "D020201" -> {
                    if (wxrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_yt_wx, "月台至外协\n进料检区"))
                }
                "D020202" -> {
                    if (wxrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_wx_cpk, "外协进料检\n区至成品库"))
                }

            }
        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcWxrk.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }
        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_yt_wx -> {
                    var title: String = "月台至外协进料检区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("015", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_wx_cpk -> {
                    val intent = Intent(mContext, AgvOperationActivity::class.java)
                    intent.putExtra("type", "wxjl")
                    startActivity(intent)
                }

            }
        }

    }


    //采购到货入库
    private fun initCgrk(cgdhjyrk: ArrayList<Threecd>?) {
        var list = ArrayList<ImageViewModel>()
        for (i in cgdhjyrk!!.indices) {
            when (cgdhjyrk[i].cdbh2) {
                "D020101" -> {
                    if (cgdhjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_yt_ljq, "月台至进\n料检区"))
                }
                "D020102" -> {
                    if (cgdhjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_bhg_thq, "不合格品\n至退货区"))
                }
                "D020103" -> {
                    if (cgdhjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_hgp_ylq, "不合格品退\n货至月台"))
                }
                "D020104" -> {
                    if (cgdhjyrk[i].ifyqx2 == 1)
                        list.add(ImageViewModel(R.mipmap.agv_kzb_yt, "原材料检验\n合格入库"))
                }
            }

        }

        val adapter = ImageViewAdapter(mContext, list)
        v.rcCgrk.let {
            it.layoutManager = GridLayoutManager(mContext, 4)
            it.adapter = adapter
        }

        adapter.onNewItemClick {
            when (it) {
                R.mipmap.agv_yt_ljq -> {
                    var title: String = "月台至进料检区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("011", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_bhg_thq -> {
                    var title: String = "不合格品至退货区"
                    val autoEndDialog = DialogAutoEnd(mContext, activity, title)
                    autoEndDialog.onOkClick {
                        addAgvTask("012", it, "", title)
                    }
                    autoEndDialog.show()
                }
                R.mipmap.agv_hgp_ylq -> {
                    var title: String = "不合格品退货至月台"
                    val dialog = DialogStartToEnd(mContext, activity, title)
                    dialog.onOkClick { s, s2 ->
                        addAgvTask("013", s, s2, title)
                    }
                    dialog.show()
                }
                R.mipmap.agv_kzb_yt -> {
                    val intent = Intent(mContext, AgvOperationActivity::class.java)
                    intent.putExtra("type", "yljy")
                    startActivity(intent)
                }

            }
        }


    }

    private fun addAgvTask(ywlx: String, qd: String, zd: String, ly: String) {
        var agvsub = AgvSubmitModel(
            companyNo, accout,
            ywlx, qd, zd, ly, "", 0, ""
        )
        vm.agvTaskadd(agvsub)
    }


    override fun initClick() {
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
    }


}
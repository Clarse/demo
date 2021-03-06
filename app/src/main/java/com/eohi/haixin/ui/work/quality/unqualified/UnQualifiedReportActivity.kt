package com.eohi.haixin.ui.work.quality.unqualified

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityUnqualifiedReportBinding
import com.eohi.haixin.ui.work.model.BlxxBean
import com.eohi.haixin.ui.work.model.GxModel
import com.eohi.haixin.ui.work.model.Items
import com.eohi.haixin.ui.work.model.UnQualifiedPostModel
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.haixin.ui.work.quality.unqualified.adapter.MxAdapter
import com.eohi.haixin.utils.Constant.REQUEST_CODE_SCAN_03
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.showAlertDialog
import com.eohi.haixin.utils.Extensions.showShortToast
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.view.DialogEqu
import com.eohi.haixin.view.ListDialog
import com.eohi.haixin.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

/*
* 检验登记
* */
class UnQualifiedReportActivity :
    BaseActivity<UnQualifiedReportViewModel, ActivityUnqualifiedReportBinding>(),
    EasyPermissions.PermissionCallbacks, MxAdapter.DeleteListener, MxAdapter.BlyyListener,
    MxAdapter.ScryListener, MxAdapter.SlListener {

    private var cardno = ""
    private var equNO = ""
    private var gxno = ""//工序号
    private var gxtxh = ""//工序体序号
    private var lzkkh = ""//流转卡卡号
    private var jgdybh = ""//加工单元
    private lateinit var dialogGx: ListDialog
    private var listGxModel = ArrayList<GxModel>()
    private var listGx = ArrayList<String>()
    private var listGxH = ArrayList<String>()
    private var listGxtxh = ArrayList<String>()
    private lateinit var dialogPerson: ListDialog
    private lateinit var dialogZjry: ListDialog
    private var listPerson = ArrayList<String>()
    private var listPersonBh = ArrayList<String>()
    private var listZjry = ArrayList<String>()
    private var listZjryBh = ArrayList<String>()
    private var listEquipment = ArrayList<String>()
    private var listEquipmentshow = ArrayList<String>()
    private var listSbbh = ArrayList<String>()
    private lateinit var dialogEquipment: DialogEqu
    private lateinit var dialogJgdy: ListDialog
    private var listJgdy = ArrayList<String>()
    private var listJgdybh = ArrayList<String>()
    private var scrid = "0" //生产人员工号
    private var scr = "" //生产人姓名
    private var zjrid = "0" //质检人员工号
    private var zjr = "" //质检人姓名
    private var blxxList = ArrayList<BlxxBean>()
    private var mxList = ArrayList<Items>()
    private lateinit var mxAdapter: MxAdapter
    private var hashMap = HashMap<String, String>()
    private var ryPosition = 0
    private var zs = 0
    private var temp = 0
    private var slPosition = 0
    private var tag = 0

    companion object {
        const val REQUEST_RY = 0x1111
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun onBarcodeEvent(result: String) {
        when {
            v.tvLzkbh.isFocused -> {
                v.tvLzkbh.setText(result)
                cardno = result.trim { it <= ' ' }
                vm.getGx(result.trim { it <= ' ' })
            }
            v.etScry.isFocused -> {
                tag = 0
                vm.findPerson(companyNo, result)
            }
            v.etZjy.isFocused -> {
                tag = 1
                vm.findJYRY(companyNo, result)
            }
        }
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.tvCzy.text = username
        v.tvRq.text = DateUtil.audioTime
        v.etZjy.setText(username)
        zjrid = accout
        vm.getBlxx()
        vm.getPerson(companyNo, "")
        vm.getZjry(companyNo, "")
    }

    override fun initClick() {
        v.ivBack clicks {
            finish()
        }
        v.tvLzkbh clicks {
            checkCameraPermissions(1)
        }
        v.btnScgx clicks {
            if (cardno.isEmpty()) {
                showShortToast("请扫码获取流转卡信息")
            } else {
                dialogGx.show()
                dialogGx.refreshList(listGx)
                dialogGx.hideSearch()
            }
        }
        v.btnScsb clicks {
            if (jgdybh.isEmpty()) {
                showShortToast("请先选择加工单元！")
                return@clicks
            }
            dialogEquipment = DialogEqu(this, listEquipmentshow, object : DialogEqu.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvScsb.text = listEquipment[position]
                    equNO = listSbbh[position]
                    v.tvJgdy.text = listJgdy[position]
                    jgdybh = listJgdybh[position]
                }
            }).apply {
                setOnEqu {
                    vm.getSblist(companyNo, accout, jgdybh)
                }
                setOnAllequ {
                    getAll()
                }
            }
            vm.getSblist(companyNo, accout, jgdybh)
        }
        v.btnJgdy clicks {
            if (cardno.isEmpty()) {
                showShortToast("请扫码获取流转卡信息")
            } else {
                vm.getJgdy(v.tvLzkbh.text.toString(), gxno, equNO)
            }
        }
        v.btnPost clicks {
            when {
                v.tvLzkbh.text.toString().isEmpty() -> {
                    showShortToast("请扫码获取流转卡信息")
                }
                v.tvScgx.text.toString().isEmpty() -> {
                    showShortToast("请选择生产工序")
                }
                v.tvJgdy.text.toString().isEmpty() -> {
                    showShortToast("请选择加工单元")
                }
                v.tvScsb.text.toString().isEmpty() -> {
                    showShortToast("请选择生产设备")
                }
                v.etScry.text.toString().isEmpty() -> {
                    showShortToast("请选择生产人员")
                }
                v.etZjy.text.toString().isEmpty() -> {
                    showShortToast("请选择质检人员")
                }
                mxList.size > 0 && !checkItems() -> {

                }
                v.etBhgsl.text.toString().toInt() < 0 -> {
                    showShortToast("不合格数量不能小于0")
                }
                zs != v.etBhgsl.text.toString().toInt() -> {
                    showShortToast("不合格明细数量和总不合格数量不一致请修改确认")
                }
                else -> {
                    val unQualifiedPostModel = UnQualifiedPostModel().apply {
                        gsh = companyNo
                        jgdybh = this@UnQualifiedReportActivity.jgdybh
                        jgdymc = v.tvJgdy.text.toString()
                        lzkkh = this@UnQualifiedReportActivity.lzkkh
                        sbbh = equNO
                        sbmc = v.tvScsb.text.toString()
                        bhgzsl = v.etBhgsl.text.toString().toInt()
                        gxbh = gxno
                        gxmc = v.tvScgx.text.toString()
                        scrid = this@UnQualifiedReportActivity.scrid
                        scr = this@UnQualifiedReportActivity.scr
                        cjrid = accout
                        cjr = username
                        zjr = this@UnQualifiedReportActivity.zjr
                        zjrid = this@UnQualifiedReportActivity.zjrid
                        gxtxh = this@UnQualifiedReportActivity.gxtxh
                        hgsl = v.etHgsl.text.toString()
                        bz = v.etBz.text.toString()
                        items = mxList
                    }
                    vm.post(unQualifiedPostModel)
                }
            }
        }
        v.btnScry clicks {
            tag = 0
            dialogPerson.apply {
                show()
                refreshList(listPerson)
                hideSearch()
            }
        }
        v.btnZjy clicks {
            tag = 1
            dialogZjry.apply {
                show()
                refreshList(listZjry)
                hideSearch()
            }
        }
        v.ivScryScan clicks {
            tag = 0
            checkCameraPermissions(REQUEST_CODE_SCAN_03)
        }
        v.ivZjyScan clicks {
            tag = 1
            checkCameraPermissions(REQUEST_CODE_SCAN_03)
        }
        v.btnAdd clicks {
            val item = Items()
            if (v.etScry.text.toString() != "") {
                item.scrid = scrid
                item.scr = scr
            }
            mxList.add(item)
            mxAdapter.notifyItemChanged(mxList.size - 1)
        }
    }

    private fun checkItems(): Boolean {
        mxList.forEachIndexed { index, items ->
            if (items.blyy == "") {
                showShortToast("请选择第${index + 1}行的不良原因")
                return false
            }
            if (items.scr == "") {
                showShortToast("请扫码第${index + 1}行的生产人员")
                return false
            }
        }
        return true
    }

    private fun initMap() {
        hashMap["companycode"] = companyNo
        hashMap["equname"] = ""
        hashMap["userid"] = accout
    }

    private fun getAll() {
        initMap()
        vm.getAllEquipmentList(hashMap)
    }

    override fun initData() {
        mxAdapter = MxAdapter(this, mxList).apply {
            setDeleteListener(this@UnQualifiedReportActivity)
            setBlyyListener(this@UnQualifiedReportActivity)
            setScryListener(this@UnQualifiedReportActivity)
            setSlListener(this@UnQualifiedReportActivity)
        }
        v.rvMx.apply {
            layoutManager = LinearLayoutManager(this@UnQualifiedReportActivity)
            adapter = mxAdapter
        }
        dialogGx = ListDialog(this, "生产工序", listGx, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvScgx.text = listGx[position]
                vm.getLzkbg(companyNo, cardno, listGxH[position])
                gxno = listGxH[position]
                gxtxh = listGxtxh[position]
            }
        })
        dialogJgdy = ListDialog(this, "加工单元", listJgdy, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.tvJgdy.text = listJgdy[position]
                jgdybh = listJgdybh[position]
            }
        })
        dialogPerson = ListDialog(this, "人员选择", listPerson, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.etScry.setText(listPerson[position])
                scrid = listPersonBh[position]
                scr = listPerson[position].split("/")[1]
            }
        })
        dialogZjry = ListDialog(this, "人员选择", listZjry, object : ListDialog.MyListener {
            override fun refreshActivity(position: Int) {
                v.etZjy.setText(listPerson[position])
                zjrid = listZjryBh[position]
                zjr = listZjry[position].split("/")[1]
            }
        })
    }

    override fun initVM() {
        vm.lzkbg.observe(this) {
            it[0].apply {
                if (JGDYMC.isNotEmpty()) {
                    v.tvJgdy.text = JGDYMC
                }
                if (JGDYBH.isNotEmpty()) {
                    jgdybh = JGDYBH
                }
                if (SBMC.isNotEmpty()) {
                    v.tvScsb.text = SBMC
                }
                if (SBBH.isNotEmpty()) {
                    equNO = SBBH
                }
                if (SCRXM.isNotEmpty()) {
                    scr = SCRXM
                    v.etScry.setText(SCRXM)
                }
                if (SCRID.isNotEmpty()) {
                    scrid = SCRID
                }
                v.etBhgsl.setText(BHGSL.toString())
                v.etHgsl.setText(DQGXLJBGSL.toString())
            }
        }
        vm.jgdylist.observe(this) { list ->
            listJgdy.clear()
            listJgdybh.clear()
            list.onEach {
                listJgdybh.add(it.jgdybh)
                listJgdy.add(it.jgdymc)
            }
            dialogJgdy.show()
            dialogJgdy.refreshList(listJgdy)
        }
        vm.perPerson.observe(this) {
            if (it.isNotEmpty()) {
                mxList[ryPosition].scrid = it[0].ygbh
                mxList[ryPosition].scr = it[0].ygxm
                mxAdapter.notifyItemChanged(ryPosition)
            }
        }
        vm.person.observe(this) {
            if (it.isNotEmpty()) {
                if (tag == 0) {
                    v.etScry.setText(it[0].ygbh.plus("/").plus(it[0].ygxm))
                    scrid = it[0].ygbh
                    scr = it[0].ygxm
                } else if (tag == 1) {
                    v.etZjy.setText(it[0].ygbh.plus("/").plus(it[0].ygxm))
                    zjrid = it[0].ygbh
                    zjr = it[0].ygxm
                }
            }
        }
        vm.sblist.observe(this) { list ->
            if (list.isNotEmpty()) {
                listEquipment.clear()
                listEquipmentshow.clear()
                listSbbh.clear()
                listJgdy.clear()
                listJgdybh.clear()
                list.onEach {
                    listSbbh.add(it.sbbh)
                    listEquipment.add(it.sbmc)
                    listEquipmentshow.add(it.sbbh + "/" + it.sbmc)
                    listJgdy.add(it.scxmc)
                    listJgdybh.add(it.scxbh)
                }
                dialogEquipment.show()
                dialogEquipment.refreshList(listEquipmentshow)

            }
        }
        vm.equipmentList.observe(this) { list ->
            if (list.isNotEmpty()) {
                listEquipment.clear()
                listEquipmentshow.clear()
                listSbbh.clear()
                listJgdy.clear()
                listJgdybh.clear()
                list.onEach {
                    listSbbh.add(it.sbbh)
                    listEquipment.add(it.sbmc)
                    listEquipmentshow.add(it.sbbh + "/" + it.sbmc)
                    listJgdy.add(it.scxmc)
                    listJgdybh.add(it.scxbh)
                }
                dialogEquipment.refreshList(listEquipmentshow)
            }
        }
        vm.postResult.observe(this) {
            showShortToast("登记成功")
            finish()
        }
        vm.personResult.observe(this) { it ->
            listPerson.clear()
            listPersonBh.clear()
            it.onEach {
                listPerson.add(it.ygbh + "/" + it.ygxm)
                listPersonBh.add(it.ygbh)
            }
        }
        vm.zjryResult.observe(this) { it ->
            listZjry.clear()
            listZjryBh.clear()
            it.onEach {
                listZjry.add(it.ygbh + "/" + it.ygxm)
                listZjryBh.add(it.ygbh)
            }
        }
        vm.gxResult.observe(this) { it ->
            listGxModel.addAll(it)
            listGx.clear()
            it.onEach {
                listGx.add(it.gxms)
                listGxH.add(it.gxh)
                listGxtxh.add(it.txh.toString())
            }
            vm.getLzkDetail(
                cardno
            )
        }
        vm.blxxResult.observe(this) {
            blxxList.addAll(it)
        }
        vm.lzkDetailResult.observe(this) {
            it[0].apply {
                v.tvCpmc.text = wpmc
                v.tvCpth.text = th
                v.tvXqsl.text = bzs
                this@UnQualifiedReportActivity.lzkkh = lzkkh
                if (zxbggx != null && zxbggx.isNotEmpty()) {
                    listGxModel.forEach {
                        if (it.gxh == zxbggx) {
                            v.tvScgx.text = it.gxms
                        }
                    }
                    vm.getLzkbg(companyNo, cardno, zxbggx)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                1 -> {
                    v.tvLzkbh.setText(data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!)
                    cardno = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trim { it <= ' ' }
                    vm.getGx(cardno)
                }
                REQUEST_CODE_SCAN_03 -> {
                    val result =
                        data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trim { it <= ' ' }
                    if (tag == 0) {
                        vm.findPerson(companyNo, result)
                    } else if (tag == 1) {
                        vm.findJYRY(companyNo, result)
                    }
                }
                REQUEST_RY -> {
                    val result =
                        data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trim { it <= ' ' }
                    vm.findPerPerson(companyNo, result)
                }
            }
        }
    }

    @AfterPermissionGranted(IncomingCheckActivity.RC_CAMERA)
    private fun checkCameraPermissions(code: Int) {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, code)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), IncomingCheckActivity.RC_CAMERA, *perms
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        val intent = Intent(this, ScannerActivity::class.java)
        intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
        intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
        startActivityForResult(intent, 1)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("确定", "重要提示", "请去设置里开启权限！") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
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
                val result =
                    intent.getStringExtra("scannerdata")!!.toString().trim { it <= ' ' }
                when {
                    v.tvLzkbh.isFocused -> {
                        v.tvLzkbh.setText(result)
                        cardno = result
                        vm.getGx(result)
                    }
                    v.etScry.isFocused -> {
                        tag = 0
                        vm.findPerson(companyNo, result)
                    }
                    v.etZjy.isFocused -> {
                        tag = 1
                        vm.findJYRY(companyNo, result)
                    }
                }
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

    //删除指定位置数据
    override fun delete(position: Int) {
        showAlertDialog("确定", "重要提示", "是否删除") {
            zs -= mxList[position].blsl
//            v.etBhgsl.setText(zs.toString())
            mxList.removeAt(position)
            mxAdapter.notifyDataSetChanged()
        }
    }

    override fun setBlyy(position: Int) {
        val tempList = ArrayList<String>()
        blxxList.forEach {
            tempList.add(it.XXSM)
        }
        ListDialog(this, "不良现象", tempList, object : ListDialog.MyListener {
            override fun refreshActivity(i: Int) {
                mxList[position].blbm = blxxList[i].XXBM
                mxList[position].blyy = blxxList[i].XXSM
                mxAdapter.notifyItemChanged(position)
            }
        }).apply {
            show()
            hideSearch()
        }
    }

    override fun setScry(position: Int) {
        ryPosition = position
        checkCameraPermissions(REQUEST_RY)
    }

    override fun setSl(position: Int, s: String) {
        if (slPosition != position) {
            slPosition = position
            temp = 0
        }
        mxList[position].blsl = s.toInt()
        zs -= temp
        zs += s.toInt()
        temp = s.toInt()
//        v.etBhgsl.setText(zs.toString())
    }

}
package com.eohi.haixin.ui.work.quality.finish

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.donkingliang.imageselector.utils.ImageSelector
import com.eohi.haixin.App.Companion.postPhoto
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityFinishCheckBinding
import com.eohi.haixin.event.Event
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.plusimage.PlusImageActivity
import com.eohi.haixin.ui.work.adapter.ImageAdapter
import com.eohi.haixin.ui.work.adapter.ZjxmAdapter
import com.eohi.haixin.ui.work.model.BlxxBean
import com.eohi.haixin.ui.work.model.BtBean
import com.eohi.haixin.ui.work.model.CommonPostModel
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity.Companion.RC_CAMERA
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity.Companion.REQUEST_CODE
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.gone
import com.eohi.haixin.utils.Extensions.show
import com.eohi.haixin.utils.Extensions.showAlertDialog
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.view.ListDialog
import com.eohi.haixin.view.MultiListDialog
import com.eohi.haixin.widget.clicks
import com.example.qrcode.Constant
import com.example.qrcode.ScannerActivity
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class FinishCheckActivity : BaseActivity<FinishViewModel, ActivityFinishCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var adapter: ZjxmAdapter
    private lateinit var list: ArrayList<BtBean>
    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()
    private lateinit var filterGdhList: ArrayList<String>
    private lateinit var allGdhList: ArrayList<String>
    private lateinit var gdDialog: ListDialog
    private var model = CommonPostModel()
    private var total = "100"
    private var djh = ""
    private var jydh = ""
    private var wph = ""
    private var blxxList = ArrayList<BlxxBean>()
    private var postBlxxList = ArrayList<BlxxBean>()

    companion object {
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun onBarcodeEvent(result: String) {
        v.tvTmh.text = result
        vm.getDetailTMH(
            companyNo,
            result.trim { it <= ' ' }
        )
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        if (type == "modify") {
            v.tvTitle.text = "??????????????????"
            v.tvYjsl.gone()
            v.etJysl.gone()
            v.tvTmh.gone()
            v.etTmh.gone()
            v.btnGdh.isEnabled = false
            v.etHgsl.isEnabled = false
            v.tvGdh.isEnabled = false
            vm.getFinishCheckDetail(companyNo, jydh, djh)
        } else {
            getGDList()
        }

        vm.getBlxx()

        adapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }
        }, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        v.etJysl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                v.etHgsl.text = s
            }

        })

        v.etBhgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (type == "modify") {
                    if (!TextUtils.isEmpty(v.etBhgsl.text.toString())) {
                        val num: String
                        val tmp = s.toString()
                        num = if (tmp.endsWith(".")) {
                            val a = tmp.substring(0, tmp.length - 1)
                            (total.toDouble() - a.toDouble()).toString()
                        } else {
                            (total.toDouble() - tmp.toDouble()).toString()
                        }
                        if (num.toDouble() >= 0) {
                            v.etHgsl.setText(num)
                        }
                    } else {
                        v.etHgsl.setText(total)
                    }
                } else {
                    if (TextUtils.isEmpty(v.etJysl.text)) {
                        Toast.makeText(this@FinishCheckActivity, "????????????????????????", Toast.LENGTH_SHORT)
                            .show()
                    } else if (!TextUtils.isEmpty(v.etBhgsl.text.toString())) {
                        val num =
                            (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                                .toDouble()).toString()
                        if (num.toDouble() >= 0) {
                            v.etHgsl.setText(num)
                        }
                    } else {
                        v.etHgsl.setText(v.etJysl.text.toString())
                    }
                }
            }

        })

    }

    private fun getGDList() {
        vm.getFinishCheckGDHList(companyNo)
    }

    private fun onTextResult(i: Int, s: String) {
        list[i].JYZ = s
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.tvTmh clicks {
            checkCameraPermissions()
        }
        v.btnPost clicks {
            if (type == "modify") {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etHgsl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var tpwjm = ""
                        postPhoto.forEach {
                            tpwjm += "$it,"
                        }
                        model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.DJH = djh
                        model.GSH = companyNo
                        model.WPH = wph
                        model.WPMC = v.tvWpmc.text.toString()
                        model.GG = v.tvGgms.text.toString()
                        model.JYJG = if (v.pass.isChecked) "1" else "2"
                        model.HGSL = v.etHgsl.text.toString()
                        model.BHGSL = v.etBhgsl.text.toString()
                        model.GDH = v.tvGdh.text.toString()
                        model.JYSL = v.etJysl.text.toString()
                        model.JYMS = v.etJyms.text.toString()
                        model.TMH = v.etTmh.text.toString()
                        model.DATA = list
                        model.BLYY = postBlxxList
                        model.JYYID = accout
                        model.JYSJ = v.tvRq.text.toString()
                        vm.modifyFinishCheck(model)
                    }
                }
            } else {
                when {
                    TextUtils.isEmpty(v.tvGdh.text) -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etHgsl.text.toString() == "" -> {
                        Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    v.etBhgsl.text.toString() == "" -> {
                        Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etHgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    (v.etJysl.text.toString().toDouble() - v.etBhgsl.text.toString()
                        .toDouble()) < 0 -> {
                        Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        var tpwjm = ""
                        postPhoto.forEach {
                            tpwjm += "$it,"
                        }
                        model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                        model.DJH = djh
                        model.GSH = companyNo
                        model.WPH = wph
                        model.WPMC = v.tvWpmc.text.toString()
                        model.GG = v.tvGgms.text.toString()
                        model.JYJG = if (v.pass.isChecked) "1" else "2"
                        model.HGSL = v.etHgsl.text.toString()
                        model.BHGSL = v.etBhgsl.text.toString()
                        model.GDH = v.tvGdh.text.toString()
                        model.JYSL = v.etJysl.text.toString()
                        model.JYMS = v.etJyms.text.toString()
                        model.TMH = v.etTmh.text.toString()
                        model.DATA = list
                        model.BLYY = postBlxxList
                        model.JYYID = accout
                        model.JYSJ = v.tvRq.text.toString()
                        vm.postFinishCheck(model)
                    }
                }
            }
        }
        v.btnGdh clicks {
            gdDialog = ListDialog(this, "????????????", filterGdhList, object : ListDialog.MyListener {
                override fun refreshActivity(position: Int) {
                    v.tvGdh.text = filterGdhList[position]
                    vm.getDetailGDH(companyNo, filterGdhList[position])
                }
            })
            gdDialog.onSearchClick { s ->
                filterGdhList.clear()
                if (TextUtils.isEmpty(s)) {
                    filterGdhList.addAll(allGdhList)
                } else {
                    allGdhList.forEach {
                        if (it.contains(s)) {
                            filterGdhList.add(it)
                        }
                    }
                }
                gdDialog.refreshList(filterGdhList)
            }
            gdDialog.show()
        }
        //??????1 ????????? 0
        v.rg.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pass -> {
                    showBlxx(false)
                }
                R.id.unPass -> {
                    showBlxx(true)
                }
            }
        }
        v.tvBlxx clicks {
            val tempList = ArrayList<String>()
            blxxList.forEach {
                tempList.add(it.XXSM)
            }
            val dialog =
                MultiListDialog(this, "????????????", tempList, object : MultiListDialog.MyListener {
                    override fun refreshActivity(listPosition: ArrayList<Int>) {
                        postBlxxList.clear()
                        var str = ""
                        listPosition.forEach {
                            str += blxxList[it].XXSM + ","
                            postBlxxList.add(blxxList[it])
                        }
                        v.tvBlxx.text = str.substring(0, str.length - 1)
                    }
                })
            dialog.show()
            dialog.hideSearch()
        }
    }

    private fun showBlxx(isShow: Boolean) {
        if (isShow) {
            v.llBlxx.show()
        } else {
            v.llBlxx.gone()
        }
    }

    override fun initData() {
        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime

        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }
        if (intent.hasExtra("GDH")) {
            jydh = intent.getStringExtra("GDH")
        }
        if (intent.hasExtra("DJH")) {
            djh = intent.getStringExtra("DJH")
        }

        list = ArrayList()
        filterGdhList = ArrayList()
        allGdhList = ArrayList()

        postPhoto.clear()

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    override fun initVM() {
        vm.blxxList.observe(this) {
            blxxList.addAll(it)
        }
        vm.gdhList.observe(this) { it ->
            it.forEach {
                allGdhList.add(it.GDH)
                filterGdhList.add(it.GDH)
            }
        }

        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
        }

        vm.response.observe(this) {
            if (it[0].returncode == 1000) {
                Toast.makeText(this, it[0].returnmsg, Toast.LENGTH_SHORT).show()
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
            }
        }
        vm.detailmodel.observe(this) { it ->
            if (it.data.list.size > 0) {
                if (type == "modify") {

                    v.tvGdh.text = it.data.list[0].GDH

                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS

                    total = if (type == "modify") {
                        (it.data.list[0].HGSL.toDouble() + it.data.list[0].BHGSL.toDouble()).toString()
                    } else {
                        it.data.list[0].JYSL
                    }

                    var str = ""
                    it.data.BLYY.onEach {
                        str += it.XXSM + ","
                    }
                    v.tvBlxx.text = str.substring(0, str.length - 1)
                    postBlxxList = it.data.BLYY

                    v.etHgsl.setText(it.data.list[0].HGSL)
                    v.etBhgsl.setText(it.data.list[0].BHGSL)

                    if (it.data.list[0].JYJG == "1") {
                        v.pass.isChecked = true
                    } else if (it.data.list[0].JYJG == "2") {
                        v.unPass.isChecked = true
                    }

                    if (it.data.list[0].TPWJM != "") {
                        it.data.list[0].TPWJM.trim().split(",").forEach {
                            postPhoto.add(it)
                            mPicList.add("$apiurl/apidefine/image?filename=$it")
                        }
                    }

                    imgAdapter?.notifyDataSetChanged()

                    v.etJyms.setText(it.data.list[0].JYMS)
                    v.tvCzy.text = it.data.list[0].JYYXM
                    v.tvRq.text = it.data.list[0].JYSJ

                } else {
                    if (it.data.list[0].GDH != null) {
                        v.tvGdh.text = it.data.list[0].GDH
                    }

                    djh = it.data.list[0].DJH

                    wph = it.data.list[0].WPH
                    v.tvWpmc.text = it.data.list[0].WPMC
                    v.tvGgms.text = it.data.list[0].GGMS
                }

                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
                    list.clear()
                    list.addAll(it.data.BT)
                    adapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
    }

    @AfterPermissionGranted(RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            val intent = Intent(this, ScannerActivity::class.java)
            intent.putExtra(Constant.EXTRA_IS_ENABLE_SCAN_FROM_PIC, true)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_WIDTH, window.decorView.width / 2)
            intent.putExtra(Constant.EXTRA_SCANNER_FRAME_HEIGHT, window.decorView.width / 2)
            startActivityForResult(intent, 1)
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), RC_CAMERA, *perms
            )
        }
    }

    //????????????
    private fun viewPluImg(position: Int) {
        val intent =
            Intent(this, PlusImageActivity::class.java)
        intent.putStringArrayListExtra(
            PlusImageActivity.IMG_LIST,
            mPicList
        )
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

    private fun takeCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
                ToastUtil.showToast(
                    mContext,
                    "???????????????" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "??????"
                )
                return
            }
            //??????????????????
            ImageSelector.builder()
                .useCamera(true) // ????????????????????????
                .setSingle(false) //??????????????????
                .setViewImage(true) //??????????????????????????????,????????????true
                .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
                .start(this, REQUEST_CODE) // ????????????

        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.permission_camera),
                REQUEST_CODE,
                *perms
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && data != null) {
            val images = data.getStringArrayListExtra(ImageSelector.SELECT_RESULT)
            mPicList.addAll(images)
            imgAdapter?.notifyDataSetChanged()
            images?.forEach {
                val file = File(it)

                val requestBody: RequestBody =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file)

                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "filename",
                        file.name,
                        requestBody
                    )
                    .addFormDataPart("busclass", "1")
                    .addFormDataPart("acesstype", "1")
                    .addFormDataPart("conmap", "wangongjianyan")
                    .addFormDataPart("workorderno", djh)
                    .build()

                vm.getResult(body)
            }
        } else if (requestCode == PlusImageActivity.REQUEST_CODE_MAIN && resultCode == PlusImageActivity.RESULT_CODE_VIEW_IMG) {
            //?????????????????????????????????
            val toDeletePicList =
                data!!.getStringArrayListExtra(PlusImageActivity.IMG_LIST) //???????????????????????????
            mPicList.clear()
            mPicList.addAll(toDeletePicList)
            imgAdapter?.notifyDataSetChanged()
        } else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                v.tvTmh.text = data?.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!
                val tmh = data.getStringExtra(Constant.EXTRA_RESULT_CONTENT)!!.trim { it <= ' ' }
                vm.getDetailTMH(
                    companyNo,
                    tmh
                )
            }
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("??????", "????????????", "??????????????????????????????") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (mPicList.size >= IncomingCheckActivity.MAX_SELECT_PIC_NUM) {
            ToastUtil.showToast(
                mContext,
                "???????????????" + IncomingCheckActivity.MAX_SELECT_PIC_NUM + "??????"
            )
            return
        }
        //??????????????????
        ImageSelector.builder()
            .useCamera(true) // ????????????????????????
            .setSingle(false) //??????????????????
            .setViewImage(true) //??????????????????????????????,????????????true
            .setMaxSelectCount(IncomingCheckActivity.MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
            .start(this, IncomingCheckActivity.REQUEST_CODE) // ????????????
    }

    override fun onResume() {
        // ?????????????????????
        val intentFilter = IntentFilter(SCANACTION)
        intentFilter.priority = Int.MAX_VALUE
        registerReceiver(scanReceiver, intentFilter)
        super.onResume()
    }

    var scanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == SCANACTION) {
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.tvTmh.text = result
                vm.getDetailTMH(
                    companyNo,
                    result
                )
            }
        }
    }

    override fun onPause() {
        //??????????????????
        unregisterReceiver(scanReceiver)
        super.onPause()
    }

}
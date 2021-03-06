package com.eohi.haixin.ui.work.quality.incoming

import android.Manifest
import android.content.Intent
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
import com.eohi.haixin.databinding.ActivityIncomingCheckBinding
import com.eohi.haixin.event.Event
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.ui.plusimage.PlusImageActivity
import com.eohi.haixin.ui.work.adapter.ImageAdapter
import com.eohi.haixin.ui.work.adapter.ZjxmAdapter
import com.eohi.haixin.ui.work.model.BtBean
import com.eohi.haixin.ui.work.model.CommonPostModel
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.gone
import com.eohi.haixin.utils.Extensions.show
import com.eohi.haixin.utils.Extensions.showAlertDialog
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.widget.clicks
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import pub.devrel.easypermissions.EasyPermissions
import java.io.File

class IncomingCheckActivity : BaseActivity<IncomingViewModel, ActivityIncomingCheckBinding>(),
    EasyPermissions.PermissionCallbacks {

    var imgAdapter: ImageAdapter? = null
    private lateinit var adapter: ZjxmAdapter
    private lateinit var list: ArrayList<BtBean>
    private lateinit var hashMap: HashMap<String, String>
    private var model = CommonPostModel()
    private var rwbh = ""
    private var djh = ""
    private var cgddh = ""
    private var total = "100"
    var mPicList = ArrayList<String>()

    companion object {
        const val MAX_SELECT_PIC_NUM = 3
        const val REQUEST_CODE = 0x00000011
        const val RC_CAMERA = 0X01
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        adapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }

        }, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

        v.etBhgsl.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                v.etJssl.setText(v.etBhgsl.text.toString())

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
                        v.etHgsl.text = num
                    }
                } else {
                    v.etHgsl.text = total
                }
            }

        })

        initMap()

        when (type) {
            "modify" -> {
                v.tvTitle.text = "??????????????????"
                vm.getDetail(hashMap)
            }
            else -> {
                vm.getDetailModel(hashMap)
            }
        }

    }

    private fun onTextResult(i: Int, s: String) {
        list[i].JYZ = s
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
        v.imageAdd clicks {
            takeCameraPermissions()
        }
        v.btnPost clicks {
            when {
                v.etBhgsl.text.toString() == "" -> {
                    Toast.makeText(this, "????????????????????????", Toast.LENGTH_SHORT).show()
                }
                v.etJssl.text.toString() == "" -> {
                    Toast.makeText(this, "?????????????????????", Toast.LENGTH_SHORT).show()
                }
                (total.toDouble() - v.etBhgsl.text.toString().toDouble()) < 0 -> {
                    Toast.makeText(this, "???????????????????????????????????????", Toast.LENGTH_SHORT).show()
                }
                (total.toDouble() - v.etJssl.text.toString().toDouble()) < 0 -> {
                    Toast.makeText(this, "????????????????????????????????????", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    var tpwjm = ""
                    postPhoto.forEach {
                        tpwjm += "$it,"
                    }
                    model.TPWJM = if (tpwjm == "") "" else tpwjm.substring(0, tpwjm.length - 1)
                    model.DJH = djh
                    model.CGDDH = cgddh
                    model.ZJRWDH = rwbh
                    model.GSH = companyNo
                    model.WPH = v.tvWph.text.toString()
                    model.WPMC = v.tvWpmc.text.toString()
                    model.GG = v.tvGgms.text.toString()
                    model.JYJG = if (v.pass.isChecked) "1" else "2"
                    model.HGSL = v.etHgsl.text.toString()
                    model.BHGSL = v.etBhgsl.text.toString()
                    model.JSSL = v.etJssl.text.toString()
                    model.SQMS = v.etJyms.text.toString()
                    model.DATA = list
                    model.JYYID = accout
                    model.JYSJ = v.tvRq.text.toString()
                    if (type == "modify") {
                        vm.modify(model)
                    } else {
                        vm.submit(model)
                    }
                }
            }
        }
    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["rwbh"] = rwbh
    }

    override fun initData() {

        v.tvCzy.text = accout
        v.tvRq.text = DateUtil.audioTime

        hashMap = HashMap()
        list = ArrayList()

        if (intent.hasExtra("RWDH")) {
            rwbh = intent.getStringExtra("RWDH")
        }
        if (intent.hasExtra("cgddh")) {
            cgddh = intent.getStringExtra("cgddh")
        }
        if (intent.hasExtra("type")) {
            type = intent.getStringExtra("type")
        }

        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
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

    override fun initVM() {
        vm.incomingDetail.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.tvWph.text = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvRwbh.text = it.data.list[0].RWBH
                v.etJyms.setText(it.data.list[0].SQMS)
                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ

                if (it.data.list[0].TPWJM != "") {
                    it.data.list[0].TPWJM.trim().split(",").forEach {
                        postPhoto.add(it)
                        mPicList.add("$apiurl/apidefine/image?filename=$it")
                    }
                }

                imgAdapter?.notifyDataSetChanged()

                total = if (type == "modify") {
                    (it.data.list[0].HGSL.toDouble() + it.data.list[0].BHGSL.toDouble()).toString()
                } else {
                    it.data.list[0].JYSL
                }

                v.etHgsl.text = it.data.list[0].HGSL
                v.etBhgsl.setText(it.data.list[0].BHGSL)

                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
                    list.addAll(it.data.BT)
                    println(list)
                    adapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
        vm.detailmodel.observe(this) { it ->
            if (it.data.list.size > 0) {
                v.tvWph.text = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvRwbh.text = it.data.list[0].RWBH
                v.etHgsl.text = it.data.list[0].JYSL

                djh = it.data.list[0].DJH

                total = it.data.list[0].JYSL
                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
                    list.addAll(it.data.BT.onEach {
                        it.PDJG = "2"
                    })
                    adapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
        vm.submitmodel.observe(this) {
            if (it.code == 1000) {
                Event.getInstance().post(EventMessage(EventCode.REFRESH))
                finish()
            }
        }
        vm.resultFile.observe(this) {
            postPhoto.add(it.list.toString())
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
                    .addFormDataPart("conmap", "lailiaojianyan")
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
        }

    }

    private fun takeCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //?????????
            if (mPicList.size >= MAX_SELECT_PIC_NUM) {
                ToastUtil.showToast(
                    mContext,
                    "???????????????" + MAX_SELECT_PIC_NUM + "??????"
                )
                return
            }
            //??????????????????
            ImageSelector.builder()
                .useCamera(true) // ????????????????????????
                .setSingle(false) //??????????????????
                .setViewImage(true) //??????????????????????????????,????????????true
                .setMaxSelectCount(MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //??????????????????????????????EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (mPicList.size >= MAX_SELECT_PIC_NUM) {
            ToastUtil.showToast(
                mContext,
                "???????????????" + MAX_SELECT_PIC_NUM + "??????"
            )
            return
        }
        //??????????????????
        ImageSelector.builder()
            .useCamera(true) // ????????????????????????
            .setSingle(false) //??????????????????
            .setViewImage(true) //??????????????????????????????,????????????true
            .setMaxSelectCount(MAX_SELECT_PIC_NUM - mPicList.size) // ??????????????????????????????????????????0?????????????????????
            .start(this, REQUEST_CODE) // ????????????
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        showAlertDialog("??????", "????????????", "??????????????????????????????") {
            val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
            intent.data = Uri.fromParts("package", packageName, null)
            startActivity(intent)
        }
    }

}
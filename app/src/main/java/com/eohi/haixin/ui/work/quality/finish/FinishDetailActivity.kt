package com.eohi.haixin.ui.work.quality.finish

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.App.Companion.postPhoto
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityFinishDetailBinding
import com.eohi.haixin.ui.plusimage.PlusImageActivity
import com.eohi.haixin.ui.work.adapter.ImageAdapter
import com.eohi.haixin.ui.work.adapter.ZjxmAdapter
import com.eohi.haixin.ui.work.model.BtBean
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.gone
import com.eohi.haixin.utils.Extensions.show
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.widget.clicks

class FinishDetailActivity : BaseActivity<FinishViewModel, ActivityFinishDetailBinding>() {

    private lateinit var adapter: ZjxmAdapter
    private lateinit var list: ArrayList<BtBean>
    var imgAdapter: ImageAdapter? = null
    var mPicList = ArrayList<String>()
    private var djh = ""

    companion object {
        var jydh = ""
        var type = ""
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        getGDList()

        adapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }
        }, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = adapter

    }

    private fun getGDList() {
        vm.getFinishCheckDetail(companyNo, jydh, djh)
    }

    private fun onTextResult(i: Int, s: String) {

    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("GDH")) {
            jydh = intent.getStringExtra("GDH")
        }
        if (intent.hasExtra("DJH")) {
            djh = intent.getStringExtra("DJH")
        }
        imgAdapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = imgAdapter
        imgAdapter?.itemClick {
            viewPluImg(it)
        }
    }

    override fun initVM() {
        vm.detailmodel.observe(this) { it ->

            if (it.data.BT.size > 0) {
                v.cardZjxm.show()
                list.clear()
                list.addAll(it.data.BT)
                adapter.notifyDataSetChanged()
            } else {
                v.cardZjxm.gone()
            }

            if (it.data.list.size > 0) {
                v.tvGdh.text = it.data.list[0].GDH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS

                v.tvHgsl.text = it.data.list[0].HGSL
                v.tvBhgsl.text = it.data.list[0].BHGSL

                if (it.data.list[0].JYJG == "1") {
                    v.tvPdjg.text = "合格"
                    v.llBlxx.gone()
                } else {
                    v.tvPdjg.text = "不合格"
                    v.llBlxx.show()
                    var str = ""
                    it.data.BLYY.onEach {
                        str += it.XXSM + ","
                    }
                    v.tvBlxx.text = str.substring(0, str.length - 1)
                }

                if (it.data.list[0].TPWJM != "") {
                    it.data.list[0].TPWJM.trim().split(",").forEach {
                        postPhoto.add(it)
                        mPicList.add("$apiurl/apidefine/image?filename=$it")
                    }
                }

                imgAdapter?.notifyDataSetChanged()

                v.tvJyms.text = it.data.list[0].JYMS

                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].JYSJ

            }
        }
    }

    //查看大图
    private fun viewPluImg(position: Int) {
        val intent =
            Intent(this, PlusImageActivity::class.java)
        intent.putStringArrayListExtra(
            PlusImageActivity.IMG_LIST,
            mPicList
        )
        intent.putExtra("type", "detail")
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

}
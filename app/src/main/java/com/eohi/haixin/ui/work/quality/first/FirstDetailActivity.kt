package com.eohi.haixin.ui.work.quality.first

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.eohi.haixin.App.Companion.postPhoto
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityFirstDetailBinding
import com.eohi.haixin.ui.plusimage.PlusImageActivity
import com.eohi.haixin.ui.work.adapter.ImageAdapter
import com.eohi.haixin.ui.work.adapter.ZjxmAdapter
import com.eohi.haixin.ui.work.model.BtBean
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.Extensions.gone
import com.eohi.haixin.utils.Extensions.show
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.widget.clicks

class FirstDetailActivity : BaseActivity<FirstCheckViewModel, ActivityFirstDetailBinding>() {

    private lateinit var hashMap: HashMap<String, String>
    private var gdh = ""
    private var rwbh = ""
    var mPicList = ArrayList<String>()
    var adapter: ImageAdapter? = null
    private lateinit var subAdapter: ZjxmAdapter
    private lateinit var list: ArrayList<BtBean>

    companion object {
        var type = "detail"
    }

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)

        list = ArrayList()
        hashMap = HashMap()

        subAdapter = ZjxmAdapter(this, list, fun(i: Int, b: Boolean) {
            if (b) {
                list[i].PDJG = "1"
            } else {
                list[i].PDJG = "2"
            }

        }, ::onTextResult)
        v.rc.layoutManager = LinearLayoutManager(this)
        v.rc.adapter = subAdapter

        initMap()
        vm.getDetail(hashMap)
    }

    private fun onTextResult(i: Int, s: String) {

    }

    private fun initMap() {
        hashMap["gsh"] = companyNo
        hashMap["gdh"] = gdh
        hashMap["rwbh"] = rwbh
    }

    override fun initClick() {
        v.ivBack clicks { finish() }
    }

    override fun initData() {
        if (intent.hasExtra("gdh")) {
            gdh = intent.getStringExtra("gdh")
        }
        if (intent.hasExtra("rwbh")) {
            rwbh = intent.getStringExtra("rwbh")
        }

        postPhoto.clear()

        adapter = ImageAdapter(this, mPicList)
        v.rcPhoto.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        v.rcPhoto.adapter = adapter
        adapter?.itemClick {
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
        intent.putExtra("type", "detail")
        intent.putExtra(PlusImageActivity.POSITION, position)
        startActivityForResult(intent, PlusImageActivity.REQUEST_CODE_MAIN)
    }

    override fun initVM() {
        vm.dataList.observe(this) { it ->
            if (it.data.list.size > 0) {

                v.tvGdh.text = gdh
                v.tvWph.text = it.data.list[0].WPH
                v.tvWpmc.text = it.data.list[0].WPMC
                v.tvGgms.text = it.data.list[0].GGMS
                v.tvRwbh.text = it.data.list[0].RWBH
                v.tvBhgsl.text = it.data.list[0].BHGSL
                v.tvCzy.text = it.data.list[0].JYYXM
                v.tvRq.text = it.data.list[0].SJSJ

                if (type == "detail" || type == "modify") {
                    v.tvHgsl.text = it.data.list[0].HGSL

                    if (it.data.list[0].TPWJM != "") {
                        it.data.list[0].TPWJM.trim().split(",").forEach {
                            postPhoto.add(it)
                            mPicList.add("$apiurl/apidefine/image?filename=$it")
                        }
                    }

                    adapter?.notifyDataSetChanged()
                } else {
                    v.tvHgsl.text = it.data.list[0].JYSL
                }
                v.tvJyms.text = it.data.list[0].SQMS

                if (it.data.list[0].JYJG == "1") {
                    v.tvPdjg.text = "??????"
                    v.tvCheckState.text = "??????"
                    v.tvCheckState.setTextColor(R.color.qualified.asColor())
                    v.llBlxx.gone()
                } else {
                    v.tvPdjg.text = "?????????"
                    v.tvCheckState.text = "?????????"
                    v.tvCheckState.setTextColor(R.color.unqualified.asColor())
                    v.llBlxx.show()
                }

                var str = ""
                it.data.BLYY.onEach {
                    str += it.XXSM + ","
                }
                v.tvBlxx.text = str.substring(0, str.length - 1)

                if (it.data.BT.size > 0) {
                    v.cardZjxm.show()
                    list.clear()

                    if (type == "detail" || type == "modify") {
                        list.addAll(it.data.BT)
                    } else {
                        list.addAll(it.data.BT.onEach {
                            it.PDJG = "2"
                        })
                    }

                    subAdapter.notifyDataSetChanged()
                } else {
                    v.cardZjxm.gone()
                }
            }
        }
    }

}
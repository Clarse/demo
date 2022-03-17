package com.eohi.haixin.ui.work.process.coordination

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.databinding.ActivityCoordinationStartBinding
import com.eohi.haixin.ui.work.adapter.ViewPagerAdapter
import com.eohi.haixin.ui.work.model.Item
import com.eohi.haixin.ui.work.model.WxkgSubmitModel
import com.eohi.haixin.ui.work.process.viewmodel.CoordinationViewModel
import com.eohi.haixin.utils.Constant
import com.eohi.haixin.utils.Constant.SCANACTION
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.utils.ToastUtil
import com.eohi.haixin.view.DialogList
import com.eohi.haixin.widget.clicks
import com.eohi.zxinglibrary.CaptureActivity
import com.eohi.zxinglibrary.Intents
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class CoordinationFinishedActivity :
    BaseActivity<CoordinationViewModel, ActivityCoordinationStartBinding>() {
    var dialog: DialogList? = null
    private var gysh = ""
    lateinit var infragment: CoordinationFinishedFragment
    lateinit var detailfragment: CoordinationFinishedDetailFragment

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)

        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnIndetail.isEnabled = true
            v.rlBtn.visibility = View.GONE
        }

        v.btnIn.setOnClickListener(View.OnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnIndetail.isEnabled = true
                v.viewpager.currentItem = 0
                v.rlBtn.visibility = View.GONE
            }
        })
        v.btnIndetail.setOnClickListener(View.OnClickListener {
            if (v.btnIndetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnIndetail.isEnabled = false
                v.viewpager.currentItem = 1
                v.rlBtn.visibility = View.VISIBLE
            }
        })


        infragment = CoordinationFinishedFragment()
        detailfragment = CoordinationFinishedDetailFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(infragment)
        fragmentList.add(detailfragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 2

        //object 匿名内部类
        v.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> if (v.btnIn.isEnabled) {
                        v.btnIn.isEnabled = false
                        v.btnIndetail.isEnabled = true
                        v.rlBtn.visibility = View.GONE
                    }
                    1 -> if (v.btnIndetail.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnIndetail.isEnabled = false
                        v.rlBtn.visibility = View.VISIBLE
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initClick() {
        v.btnGys.clicks {
            vm.getGysList(companyNo, "")
        }
        v.ivScan.clicks {
            checkCameraPermissions()
        }
        v.btnSubmit.clicks {
            val items = detailfragment.getList()
            if (items.isNullOrEmpty())
                return@clicks

            var itemlist = ArrayList<Item>()
            for (i in items.indices) {
                itemlist.add(Item(items[i].gxh, items[i].lzkkh, items[i].sybzs))
            }
            vm.submitWG(WxkgSubmitModel(gysh, itemlist, accout))

        }


    }

    override fun initData() {
        v.btnIn.text = "完工入库"
        v.btnIndetail.text = "入库明细"
        v.tvTitle.text = "工序外协完工"
    }

    override fun initVM() {
        vm.gyslist.observe(this, Observer {
            try {
                if (!it.isNullOrEmpty()) {
                    if (null == dialog) {
                        dialog = DialogList(mContext, this, "选择供应商", it)
                        dialog?.onSearch {
                            vm.getGysList(companyNo, it)
                        }
                        dialog?.onItemClick {
                            v.tvGys.text = it.gysmc
                            gysh = it.gysh
                        }
                    } else {
                        if (!dialog!!.isShowing) {
                            dialog?.clearData()
                        }
                        dialog?.updateList(it)
                    }

                    dialog?.show()

                }
            } catch (e: Exception) {
                ToastUtil.showToast(mContext, e.message)
            }

        })

        vm.wglist.observe(this, Observer {
            if (!it.isNullOrEmpty()) {
                infragment.setData(it[0])
                detailfragment.updateList(it[0])
                v.tvWpsl.text = detailfragment.itemSum().toString()
                v.tvLzks.text = detailfragment.getCount().toString()
            }
        })
        vm.subresult.observe(this, Observer {
            ToastUtil.showToast(mContext, "提交成功")
            detailfragment.clear()
            v.tvWpsl.text = "0"
            v.tvLzks.text = "0"
        })

    }

    @AfterPermissionGranted(Constant.RC_CAMERA)
    private fun checkCameraPermissions() {
        val perms = arrayOf(Manifest.permission.CAMERA)
        if (EasyPermissions.hasPermissions(this, *perms)) { //有权限
            val optionsCompat =
                ActivityOptionsCompat.makeCustomAnimation(this, R.anim.`in`, R.anim.out)
            val intent = Intent(this, CaptureActivity::class.java)
            intent.putExtra(Constant.KEY_TITLE, "扫码")
            intent.putExtra(Constant.KEY_IS_CONTINUOUS, Constant.isContinuousScan)
            ActivityCompat.startActivityForResult(
                this,
                intent,
                Constant.REQUEST_CODE_SCAN,
                optionsCompat.toBundle()
            )
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                this, getString(R.string.permission_camera), Constant.RC_CAMERA, *perms
            )
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
                val result = intent.getStringExtra("scannerdata").toString().trim { it <= ' ' }
                v.etLzk.setText(result)
                vm.getWgxx(result)
            }
        }
    }

    override fun onPause() {
        //取消广播注册
        unregisterReceiver(scanReceiver)
        super.onPause()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null) {
            when (requestCode) {
                Constant.REQUEST_CODE_SCAN -> {
                    val result = data.getStringExtra(Intents.Scan.RESULT)
                    v.etLzk.setText(result)
                    vm.getWgxx(result)
                }

            }
        }
    }

    override fun onBarcodeEvent(result: String) {
        v.etLzk.setText(result)
        vm.getWgxx(result)
    }

}
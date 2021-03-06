package com.eohi.haixin.ui.main

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.eohi.haixin.base.BaseFragment
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.FragmentMyBinding
import com.eohi.haixin.ui.main.model.Menu
import com.eohi.haixin.utils.KillSelfService
import com.eohi.haixin.utils.Preference

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/3/10 10:05
 */
class MyFragment : BaseFragment<BaseViewModel, FragmentMyBinding>() {
    private var accout by Preference("userid", "")
    private var username by Preference("username", "")
    private var companyname by Preference("companyname", "")
    private var firstmenus: ArrayList<Menu>? by Preference("menus", null)
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initVM() {
    }

    override fun initView() {
        v.tvAccount.text = accout
        v.tvUsername.text = username
        v.tvGc.text = companyname
    }

    override fun initClick() {
        v.btnOut.setOnClickListener {
            showDialog()
        }
    }

    override fun initData() {
    }

    override fun lazyLoadData() {
    }

    private fun showDialog() {
        AlertDialog.Builder(context!!)
            .setMessage("确定要退出登录")
            .setNegativeButton("取消") { dialog, which -> dialog.dismiss() }
            .setPositiveButton("确定") { dialog, which ->
                dialog.dismiss()
                accout = ""
                username = ""
                firstmenus = null
                context!!.startService(Intent(context, KillSelfService::class.java))
                android.os.Process.killProcess(android.os.Process.myPid())
            }
            .create().show()
    }

}
package com.eohi.haixin.ui.main.agv.production

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivitySurplusMaterialsBinding
import com.eohi.haixin.ui.work.adapter.ViewPagerAdapter
import com.eohi.haixin.utils.StatusBarUtil

class SurplusMaterialsActivity : BaseActivity<BaseViewModel, ActivitySurplusMaterialsBinding>() {
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, resources.getColor(R.color.white))
        StatusBarUtil.darkMode(this, true)
        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnDetail.isEnabled = true
        }

        v.btnIn.setOnClickListener(View.OnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnDetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        })
        v.btnDetail.setOnClickListener(View.OnClickListener {
            if (v.btnDetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        })


        val infragment = SurplusMaterialsFragment()
        val detailfragment = SurplusMaterialsDetailFragment()
        detailfragment.agvtype = intent.getStringExtra("type")
        when (intent.getStringExtra("type")) {
            "023" -> {
                v.tvTitle.text = "不合格退料"
                v.btnIn.text = "不合格退料"
            }
            "022" -> {
                v.tvTitle.text = "产线余料退至退料存储区"
                v.btnIn.text = "产线余料"
            }
        }
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
                        v.btnDetail.isEnabled = true
                    }
                    1 -> if (v.btnDetail.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnDetail.isEnabled = false
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

    }

    override fun initClick() {
    }

    override fun initData() {
    }

    override fun initVM() {
    }
}
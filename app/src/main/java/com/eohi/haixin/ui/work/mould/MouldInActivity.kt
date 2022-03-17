package com.eohi.haixin.ui.work.mould

import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivityMouldInBinding
import com.eohi.haixin.ui.work.adapter.ViewPagerAdapter
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.StatusBarUtil

class MouldInActivity : BaseActivity<BaseViewModel, ActivityMouldInBinding>() {

    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.ivBack.setOnClickListener { finish() }
        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnDetail.isEnabled = true
        }

        v.btnIn.setOnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnDetail.isEnabled = true
                v.viewpager.currentItem = 0
            }
        }
        v.btnDetail.setOnClickListener {
            if (v.btnDetail.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetail.isEnabled = false
                v.viewpager.currentItem = 1
            }
        }


        val infragment = MouldInFragment()
        val detailfragment = MouldInDetailFragment()
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(infragment)
        fragmentList.add(detailfragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 2

        //object 匿名内部类
        v.viewpager.addOnPageChangeListener(object : OnPageChangeListener {
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
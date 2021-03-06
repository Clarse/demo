package com.eohi.haixin.ui.work.process.merge

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivityCoordinationMergeBinding
import com.eohi.haixin.ui.work.adapter.ViewPagerAdapter
import com.eohi.haixin.utils.Extensions.asColor
import com.eohi.haixin.utils.StatusBarUtil
import com.eohi.haixin.widget.clicks

class CoordinationMergeActivity : BaseActivity<BaseViewModel, ActivityCoordinationMergeBinding>() {

    lateinit var mergefragment: MergeFragment
    lateinit var detailfragment: MergeDetailFragment
    lateinit var mergenewfragment: MergeNewFragment
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
        StatusBarUtil.setColor(this, R.color.white.asColor())
        StatusBarUtil.darkMode(this, true)
        v.ivBack.setOnClickListener { finish() }

        if (v.btnIn.isEnabled) {
            v.btnIn.isEnabled = false
            v.btnDetial.isEnabled = true
            v.btnNew.isEnabled = true
        }
        v.btnIn.setOnClickListener(View.OnClickListener {
            if (v.btnIn.isEnabled) {
                v.btnIn.isEnabled = false
                v.btnDetial.isEnabled = true
                v.btnNew.isEnabled = true
                v.viewpager.currentItem = 0
            }
        })
        v.btnDetial.clicks {
            if (v.btnDetial.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetial.isEnabled = false
                v.btnNew.isEnabled = true
                v.viewpager.currentItem = 1
            }
        }
        v.btnNew.clicks {
            if (v.btnNew.isEnabled) {
                v.btnIn.isEnabled = true
                v.btnDetial.isEnabled = true
                v.btnNew.isEnabled = false
                v.viewpager.currentItem = 2
            }
        }


    }

    override fun initClick() {
    }

    override fun initData() {
        mergefragment = MergeFragment()
        detailfragment = MergeDetailFragment()
        mergenewfragment = MergeNewFragment()

        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(mergefragment)
        fragmentList.add(detailfragment)
        fragmentList.add(mergenewfragment)
        val adapter = ViewPagerAdapter(supportFragmentManager, fragmentList)
        v.viewpager.adapter = adapter
        v.viewpager.offscreenPageLimit = 3

        //object ???????????????
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
                        v.btnDetial.isEnabled = true
                        v.btnNew.isEnabled = true
                    }
                    1 -> if (v.btnDetial.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnNew.isEnabled = true
                        v.btnDetial.isEnabled = false
                    }
                    2 -> if (v.btnNew.isEnabled) {
                        v.btnIn.isEnabled = true
                        v.btnNew.isEnabled = false
                        v.btnDetial.isEnabled = true
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    override fun initVM() {
    }

}
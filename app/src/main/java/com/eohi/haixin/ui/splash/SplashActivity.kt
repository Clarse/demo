package com.eohi.haixin.ui.splash

import android.R
import android.content.Intent
import android.graphics.Color
import android.os.Handler
import android.text.TextUtils
import android.view.View
import com.eohi.haixin.base.BaseActivity
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.databinding.ActivitySplashBinding
import com.eohi.haixin.ui.login.LoginActivity
import com.eohi.haixin.ui.main.MainActivity
import com.eohi.haixin.utils.Preference

class SplashActivity : BaseActivity<BaseViewModel, ActivitySplashBinding>() {
    private var accout by Preference("userid", "")
    override fun isNeedEventBus(): Boolean {
        return false
    }

    override fun initView() {
//        StatusBarUtil.hideBottomUIMenu(this)
//        StatusBarUtil.immersive(this)
//        StatusBarUtil.darkMode(this)

        val decorView = window.decorView
        decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT
        if (TextUtils.isEmpty(accout)) {
            Handler().postDelayed({
                startActivity(Intent(mContext, LoginActivity::class.java))
                overridePendingTransition(0, R.anim.fade_out)
                finish()
            }, 1000)

        } else {
            Handler().postDelayed({
                startActivity(Intent(mContext, MainActivity::class.java))
                overridePendingTransition(0, R.anim.fade_out)
                finish()
            }, 1000)
        }

    }

    override fun initClick() {
    }

    override fun initData() {

    }

    override fun initVM() {
    }


}
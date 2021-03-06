package com.eohi.haixin.base

import android.app.ProgressDialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.viewbinding.ViewBinding
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.event.EventCode
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.utils.LogUtil
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil
import com.honeywell.aidc.*
import com.honeywell.aidc.BarcodeReader.BarcodeListener
import com.honeywell.aidc.BarcodeReader.TriggerListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType


abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(),
    BarcodeListener, TriggerListener {
    lateinit var mContext: FragmentActivity
    lateinit var vm: VM
    lateinit var v: VB

    private var loadingDialog: ProgressDialog? = null

    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null

    companion object {
        var accout by Preference("userid", "")
        var username by Preference("username", "")
        var companyNo: String by Preference("companyNo", "")
        var companyname by Preference("companyname", "")
        var apiurl by Preference<String>("ApiUrl", "http://122.51.182.66:3019/")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initResources()

        //注意 type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clazz1)

        val clazz2 = type.actualTypeArguments[1] as Class<VB>
        val method = clazz2.getMethod("inflate", LayoutInflater::class.java)
        v = method.invoke(null, layoutInflater) as VB

        setContentView(v.root)
        if ("Honeywell" == android.os.Build.BRAND)
            initHoneyScan()
        mContext = this
        init()
        initData()
        initView()
        initClick()
        initVM()
        LogUtil.e(getClassName())
    }

    private fun initHoneyScan() {
        AidcManager.create(this) { aidcManager ->
            manager = aidcManager
            try {
                barcodeReader = manager?.createBarcodeReader()
                //设置扫描参数
                barcodeReader?.setProperty(
                    BarcodeReader.PROPERTY_CODE_128_ENABLED,
                    true
                ) //开启Code128码制
                barcodeReader?.setProperty(BarcodeReader.PROPERTY_EAN_13_ENABLED, false) //关闭EAN13码制
                barcodeReader?.setProperty(
                    BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                    BarcodeReader.TRIGGER_CONTROL_MODE_CLIENT_CONTROL
                ) //设置手动触发模式
                barcodeReader?.claim() //开启扫描功能
                //第二步：添加侧面扫描键事件监听和条码事件监听
                if (null != barcodeReader) {
                    barcodeReader?.addTriggerListener(this)
                    barcodeReader?.addBarcodeListener(this)
                }
            } catch (e: InvalidScannerNameException) {
            } catch (e: java.lang.Exception) {
            }
        }

    }


    /**
     * 防止系统字体影响到app的字体
     *
     * @return
     */
    open fun initResources(): Resources? {
        val res: Resources = super.getResources()
        val config = Configuration()
        config.setToDefaults()
        res.updateConfiguration(config, res.displayMetrics)
        return res
    }

    override fun onResume() {
        super.onResume()
        if ("Honeywell" == android.os.Build.BRAND) {
            if (barcodeReader != null) {
                try {
                    barcodeReader?.claim()
                } catch (e: ScannerUnavailableException) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if ("Honeywell" == android.os.Build.BRAND) {
            if (barcodeReader != null) {
                // release the scanner claim so we don't get any scanner
                // notifications while paused.
                barcodeReader?.release()
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        if (isNeedEventBus())
            EventBus.getDefault().unregister(this)
        if ("Honeywell" == android.os.Build.BRAND) {
            barcodeReader?.close()
            barcodeReader == null
            manager?.close()
        }

    }

    abstract fun isNeedEventBus(): Boolean

    //事件传递
    @Subscribe
    fun onEventMainThread(msg: EventMessage) {
        handleEvent(msg)
    }

    open fun getClassName(): String? {
        val className = "BaseActivity"
        try {
            return javaClass.name
        } catch (e: Exception) {
        }
        return className
    }

    abstract fun initView()

    abstract fun initClick()

    abstract fun initData()

    abstract fun initVM()

    private fun init() {
        if (isNeedEventBus())
            EventBus.getDefault().register(this)
        //loading
        vm.isShowLoading.observe(this) {
            if (it) showLoading() else dismissLoading()
        }
        //错误信息
        vm.errorData.observe(this) {
            if (it.show) ToastUtil.showToast(mContext, it.errMsg)
            errorResult(it)
        }
    }

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(this)
        }
        loadingDialog!!.show()
    }

    private fun dismissLoading() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * 消息、事件接收回调
     */
    open fun handleEvent(msg: EventMessage) {
        if (msg.code == EventCode.LOGIN_OUT) {
            finish()
        }
    }

    /**
     * 接口请求错误回调
     */
    open fun errorResult(errorResult: ErrorResult) {}

    /**
     * 解决Fragment中的onActivityResult()方法无响应问题。
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }


    override fun onBarcodeEvent(p0: BarcodeReadEvent) {
        if ("Honeywell" == android.os.Build.BRAND) {
            runOnUiThread {
                if (p0.barcodeData.isNotEmpty())
                    onBarcodeEvent(p0.barcodeData.trim { it <= ' ' })
            }

        }
    }

    open fun onBarcodeEvent(result: String) {}

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
    }

    override fun onTriggerEvent(event: TriggerStateChangeEvent) {
        if ("Honeywell" == android.os.Build.BRAND) {
            try {
                // only handle trigger presses
                // turn on/off aimer, illumination and decoding
                barcodeReader?.aim(event.state)
                barcodeReader?.light(event.state)
                barcodeReader?.decode(event.state)
            } catch (e: ScannerNotClaimedException) {
                e.printStackTrace()
            } catch (e: ScannerUnavailableException) {
                e.printStackTrace()
            }
        }
    }

}
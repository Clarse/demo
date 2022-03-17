package com.eohi.haixin.base

import android.app.ProgressDialog
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.eohi.haixin.R
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.event.EventMessage
import com.eohi.haixin.utils.LogUtil
import com.eohi.haixin.utils.Preference
import com.eohi.haixin.utils.ToastUtil
import com.honeywell.aidc.*

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.lang.reflect.ParameterizedType


abstract class BaseFragment<VM : BaseViewModel, VB : ViewBinding> : Fragment(),
    BarcodeReader.BarcodeListener, BarcodeReader.TriggerListener {

    lateinit var mContext: FragmentActivity
    var contentView: View? = null
    lateinit var vm: VM
    lateinit var v: VB
    private var loadingDialog: ProgressDialog? = null

    private var mediaplayer: MediaPlayer? = null

    //Fragment的View加载完毕的标记
    private var isViewCreated = false

    //Fragment对用户可见的标记
    private var isUIVisible = false

    var isVisibleToUser = false

    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null

    companion object {
        var accout by Preference("userid", "")
        var username by Preference("username", "")
        var companyNo: String by Preference("companyNo", "")
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //注意 type.actualTypeArguments[0]=BaseViewModel，type.actualTypeArguments[1]=ViewBinding
        val type = javaClass.genericSuperclass as ParameterizedType
        val clazz1 = type.actualTypeArguments[0] as Class<VM>
        vm = ViewModelProvider(this).get(clazz1)

        val clazz2 = type.actualTypeArguments[1] as Class<VB>
        val method = clazz2.getMethod("inflate", LayoutInflater::class.java)
        v = method.invoke(null, layoutInflater) as VB
        mContext = context as AppCompatActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (null == contentView) {
            contentView = v.root
            init()
            initData()
            initView()
            initClick()
            initVM()
            LogUtil.e(getClassName())
        }

        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        lazyLoad()
    }

    fun initHoneyScan() {
        if ("Honeywell" == android.os.Build.BRAND) {
            AidcManager.create(activity) { aidcManager ->
                manager = aidcManager
                try {
                    barcodeReader = manager?.createBarcodeReader()
                    //设置扫描参数
                    barcodeReader?.setProperty(
                        BarcodeReader.PROPERTY_CODE_128_ENABLED,
                        true
                    ) //开启Code128码制
                    barcodeReader?.setProperty(
                        BarcodeReader.PROPERTY_EAN_13_ENABLED,
                        false
                    ) //关闭EAN13码制
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

    }


    private fun init() {
        if (isNeedEventBus())
            EventBus.getDefault().register(this)
        //loading
        vm.isShowLoading.observe(this, Observer {
            if (it) showLoading() else dismissLoding()
        })
        //错误信息
        vm.errorData.observe(this, Observer {
            if (it.show) ToastUtil.showToast(mContext, it.errMsg)
            errorResult(it)
        })
    }


    abstract fun isNeedEventBus(): Boolean

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

    //事件传递
    @Subscribe
    fun onEventMainThread(msg: EventMessage) {
        handleEvent(msg)
    }

    open fun getClassName(): String? {
        val className = "BaseFragment"
        try {
            return javaClass.name
        } catch (e: Exception) {
        }
        return className
    }

    abstract fun initVM()

    abstract fun initView()

    abstract fun initClick()

    abstract fun initData()

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true
            lazyLoad()
        } else {
            isUIVisible = false
        }
    }

    fun lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            lazyLoadData()
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false
            isUIVisible = false
        }
    }

    //需要懒加载的数据，重写此方法
    abstract fun lazyLoadData()

    private fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = ProgressDialog(mContext)
        }
        loadingDialog!!.show()
    }

    private fun dismissLoding() {
        loadingDialog?.dismiss()
        loadingDialog = null
    }

    /**
     * 消息、事件接收回调
     */
    open fun handleEvent(msg: EventMessage) {}

    /**
     * 接口请求错误回调
     */
    open fun errorResult(errorResult: ErrorResult) {
        mediaplayer = MediaPlayer.create(mContext, R.raw.beep)
        mediaplayer!!.isLooping = false
        mediaplayer!!.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        contentView = null
        mediaplayer?.stop()
        mediaplayer?.release() //释放资源
    }

    /**
     * fragment 中嵌套子的 fragment
     * 解决子Fragment中的onActivityResult()方法无响应问题。
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (childFragmentManager.fragments.isNotEmpty()) {
            val fragments = childFragmentManager.fragments
            fragments.forEach {
                it.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if ("Honeywell" == android.os.Build.BRAND) {
            try {
                barcodeReader?.claim()
            } catch (e: ScannerUnavailableException) {
                e.printStackTrace()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        if ("Honeywell" == android.os.Build.BRAND) {
            // release the scanner claim so we don't get any scanner
            // notifications while paused.
            barcodeReader?.release()
        }

    }


    override fun onBarcodeEvent(p0: BarcodeReadEvent) {
        if ("Honeywell" == android.os.Build.BRAND) {
            activity?.runOnUiThread {
                if (p0.barcodeData.isNotEmpty())
                    onBarcodeResult(p0.barcodeData.trim { it <= ' ' })
            }
        }
    }

    open fun onBarcodeResult(result: String) {
    }

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

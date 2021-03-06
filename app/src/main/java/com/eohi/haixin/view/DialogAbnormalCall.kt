package com.eohi.haixin.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.eohi.haixin.R
import com.eohi.haixin.widget.clicks
import kotlinx.android.synthetic.main.dialog_abnormal_deal.*

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/1/20 13:38
 */
class DialogAbnormalCall(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    constructor(context: Context) : this(context, R.style.MyDialog)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_abnormal_deal)
        window!!.setGravity(Gravity.CENTER)
        window!!.setWindowAnimations(R.style.anim_pop_checkaddshelf)
        window!!.decorView.setPadding(0, 0, 0, 0)
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = lp
        btn_cancle.clicks { dismiss() }
        btn_ok.clicks {
            onOkClick?.apply {
                this(et_ms.text.toString())
                dismiss()
            }
        }
    }

    fun setOnOkClick(onOkClick: ((String) -> Unit)?) {
        this.onOkClick = onOkClick
    }

    private var onOkClick: ((String) -> Unit)? = null


}
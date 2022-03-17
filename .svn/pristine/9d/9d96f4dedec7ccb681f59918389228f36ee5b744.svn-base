package com.eohi.haixin.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.eohi.haixin.R
import com.eohi.haixin.base.BaseActivity.Companion.username
import com.eohi.haixin.utils.DateUtil
import com.eohi.haixin.widget.clicks
import kotlinx.android.synthetic.main.dialog_cutter.*

class DialogCutter(context: Context, themeResId: Int) : Dialog(context, themeResId) {

    private lateinit var mMyListener: MyListener
    private var mContext: Context? = null
    private lateinit var tv_ghsj: TextView
    private lateinit var tv_ghr: TextView

    constructor(
        context: Context,
        listener: MyListener
    ) : this(
        context,
        R.style.MyDialog
    ) {
        mContext = context
        mMyListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_cutter)
        findViewById<ImageView>(R.id.iv_close) clicks {
            dismiss()
        }
        findViewById<Button>(R.id.btn_ok) clicks {
            if (TextUtils.isEmpty(tv_ph.text.toString())) {
                Toast.makeText(context, "请输入批号", Toast.LENGTH_SHORT).show()
            } else {
                mMyListener.refreshActivity(tv_ph.text.toString())
            }
        }
        findViewById<Button>(R.id.btn_cancel) clicks {
            dismiss()
        }
        tv_ghsj = findViewById(R.id.tv_ghsj)
        tv_ghsj.text = DateUtil.audioTime
        tv_ghr = findViewById(R.id.tv_ghr)
        tv_ghr.text = username
    }

    interface MyListener {
        fun refreshActivity(ph: String)
    }

}
package com.eohi.haixin.view

import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.eohi.haixin.R
import com.eohi.haixin.widget.clicks

/**
 *@author: YH
 *@date: 2022/2/16
 *@desc:
 */
class DialogLzk(context: Context) : AlertDialog(context) {

    lateinit var tvLzkkh: TextView
    lateinit var tvJdgx: TextView
    lateinit var etJdsl: EditText
    lateinit var btnOk: Button
    lateinit var btnCancel: Button
    private lateinit var positiveListener: PositiveListener

    constructor(context: Context, listener: PositiveListener) : this(context) {
        positiveListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        setContentView(R.layout.dialog_lzk_jd)
        initView()
    }

    private fun initView() {
        tvLzkkh = findViewById(R.id.tv_lzkkh)!!
        tvJdgx = findViewById(R.id.tv_jdgx)!!
        etJdsl = findViewById(R.id.et_jdsl)!!
        btnOk = findViewById(R.id.btn_ok)!!
        btnCancel = findViewById(R.id.btn_cancel)!!

        btnCancel clicks {
            dismiss()
        }

        btnOk clicks {
            positiveListener.positiveClick(etJdsl.text.toString().toInt())
            dismiss()
        }

    }

    fun setLzkkh(s: String) {
        tvLzkkh.text = s
    }

    fun setJdgx(s: String) {
        tvJdgx.text = s
    }

    fun setJdsl(s: String) {
        etJdsl.setText(s)
    }

    interface PositiveListener {
        fun positiveClick(jdsl: Int)
    }

}
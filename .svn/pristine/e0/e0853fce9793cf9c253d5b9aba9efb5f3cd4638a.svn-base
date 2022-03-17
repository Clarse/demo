package com.eohi.haixin.ui.work.tooling.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.SubmitResult
import com.eohi.haixin.ui.work.tooling.model.CutterReplaceListBean
import com.eohi.haixin.ui.work.tooling.model.CutterReplacePostBean

class CutterReplaceViewModel : BaseViewModel() {
    val scanResult = MutableLiveData<ArrayList<CutterReplaceListBean>>()
    val postResult = MutableLiveData<ArrayList<SubmitResult>>()

    fun getCutterReplaceList(gsh: String, sbbh: String) {
        launchList(
            { httpUtil.getCutterReplaceList(gsh, sbbh) },
            scanResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun postCutterReplace(model: CutterReplacePostBean) {
        launchList(
            { httpUtil.postCutterReplace(model) },
            postResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

}
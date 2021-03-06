package com.eohi.haixin.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.*
import com.eohi.haixin.ui.work.process.card.LzkJdModel

class CirculationCardViewModel : BaseViewModel() {

    var lzkxxResult = MutableLiveData<ArrayList<LzkxxModel>>()
    var postResult = MutableLiveData<ArrayList<SubmitResult>>()
    var clxhResult = MutableLiveData<ArrayList<CLXHModel>>()
    var subListResult = MutableLiveData<ArrayList<LZKSubListModel>>()
    var gxResult = MutableLiveData<ArrayList<GxResultModel>>()
    var lzkxqResult = MutableLiveData<ArrayList<LzkxqModel>>()
    var jdResult = MutableLiveData<ArrayList<SubmitResult>>()

//    fun getGx(cardno: String) {
//        launchList(
//            { httpUtil.getGXResult(cardno) },
//            gxResult,
//            isShowLoading = true,
//            isShowError = true,
//            successCode = 200
//        )
//    }
//
//    fun getSubList(cardno: String) {
//        launchList(
//            { httpUtil.getSubList(cardno) },
//            subListResult,
//            isShowLoading = true,
//            isShowError = true,
//            successCode = 200
//        )
//    }
//
//    fun getCLXH(cardno: String) {
//        launchList(
//            { httpUtil.getCLXH(cardno) },
//            clxhResult,
//            isShowLoading = true,
//            isShowError = true,
//            successCode = 200
//        )
//    }

    fun jd(lzkJdModel: LzkJdModel) {
        launchList({ httpUtil.jd(lzkJdModel) }, jdResult)
    }

    fun post(lzkPostModel: LzkPostModel) {
        launchList(
            { httpUtil.postLzk(lzkPostModel) },
            postResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getLzkxx(cardno: String) {
        launchList(
            { httpUtil.getLzkxx(cardno) },
            lzkxxResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 200
        )
    }

    fun getLzkxq(gsh: String, lzkkh: String) {
        launchList(
            { httpUtil.getLzkxq(gsh, lzkkh) },
            lzkxqResult,
            isShowLoading = true,
            isShowError = true,
            successCode = 1000
        )
    }

}
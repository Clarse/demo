package com.eohi.haixin.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.MaterialCardInfoModel
import com.eohi.haixin.ui.work.model.RemovalReasonModel
import com.eohi.haixin.ui.work.model.RemovalSubModel
import com.eohi.haixin.ui.work.model.SubmitResult

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/7/5 13:28
 */
class MaterialViewModel : BaseViewModel() {
    var yylist = MutableLiveData<ArrayList<RemovalReasonModel>>()
    var ckxx = MutableLiveData<ArrayList<MaterialCardInfoModel>>()
    var newckxx = MutableLiveData<ArrayList<MaterialCardInfoModel>>()
    var submitresult = MutableLiveData<ArrayList<SubmitResult>>()

    //拆卡原因
    fun getCkyy() {
        launchList({ httpUtil.getRemovalReason() }, yylist, isShowLoading = true, successCode = 200)
    }

    //原拆卡信息
    fun getCkxx(clkno: String, userid: String) {
        launchList(
            { httpUtil.getCkxx(clkno, userid) },
            ckxx,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getNewCKxx(clkno: String, userid: String) {
        launchList(
            { httpUtil.getCkxx(clkno, userid) },
            newckxx,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun submit(model: RemovalSubModel) {
        launchList(
            { httpUtil.submitRemobal(model) },
            submitresult,
            isShowLoading = true,
            successCode = 200
        )
    }


}
package com.eohi.haixin.ui.work.abnormal.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.AbnormalCallModel
import com.eohi.haixin.ui.work.model.AbnormalCallSubmitModel
import com.eohi.haixin.ui.work.model.SubmitResult

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2022/1/19 16:09
 */
class AbnormalCallViewModel : BaseViewModel() {
    var abnormallist = MutableLiveData<ArrayList<AbnormalCallModel>>()
    var result = MutableLiveData<ArrayList<SubmitResult>>()
    fun getAbnormalList(gsh: String, page: Int, pagesize: Int) {
        launchList({ httpUtil.getAbnormalCallList(gsh, page, pagesize) }, abnormallist, true)
    }

    fun submit(model: AbnormalCallSubmitModel) {
        launchList({ httpUtil.subAbnormaolCall(model) }, result, true)
    }

}
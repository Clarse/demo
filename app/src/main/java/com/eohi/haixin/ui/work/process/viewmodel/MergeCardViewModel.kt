package com.eohi.haixin.ui.work.process.viewmodel

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.CardInfoModel
import com.eohi.haixin.ui.work.model.CardSubResultModel
import com.eohi.haixin.ui.work.model.CradMergeSubModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/28 9:57
 */
class MergeCardViewModel : BaseViewModel() {
    var cardinfo = MutableLiveData<ArrayList<CardInfoModel>>()
    var subresult = MutableLiveData<ArrayList<CardSubResultModel>>()
    fun getCardInfo(gsh: String, lzkh: String) {
        launchList({ httpUtil.getCardInfo(gsh, lzkh) }, cardinfo, true)
    }

    fun submitMerge(model: CradMergeSubModel) {
        launchList({ httpUtil.submitMergeCard(model) }, subresult, true)
    }

}
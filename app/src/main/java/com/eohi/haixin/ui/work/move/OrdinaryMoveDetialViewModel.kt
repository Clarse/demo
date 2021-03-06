package com.eohi.haixin.ui.work.move

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.api.error.ErrorUtil
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.response.BaseResModel
import com.eohi.haixin.ui.work.model.InstorageModel
import com.eohi.haixin.ui.work.model.SubmitResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/20 15:47
 */
class OrdinaryMoveDetialViewModel : BaseViewModel() {
    var wjresult = MutableLiveData<BaseResModel<SubmitResult>>()

    //移库提交
    fun submit(dj: String, model: InstorageModel) {
        viewModelScope.launch {
            try {
                showLoading()
                val resultmodel = withContext(Dispatchers.IO) { httpUtil.getOrderNo(dj) }
                if (resultmodel.code == 1000) {
                    model.swh = resultmodel.data!!.list!![0].DJH!!
                    val rkresult = withContext(Dispatchers.IO) { httpUtil.inStorage(model) }
                    wjresult.value = rkresult
                } else {
                    showError(ErrorResult(resultmodel.code, resultmodel.msg, true))
                }
            } catch (e: Exception) {
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }

        }
    }

}
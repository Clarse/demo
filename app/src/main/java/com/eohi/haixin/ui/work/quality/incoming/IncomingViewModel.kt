package com.eohi.haixin.ui.work.quality.incoming

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.eohi.haixin.api.error.ErrorResult
import com.eohi.haixin.api.error.ErrorUtil
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.response.BaseResModel
import com.eohi.haixin.ui.work.model.*
import com.eohi.haixin.utils.LogUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class IncomingViewModel : BaseViewModel() {

    var incominglist = MutableLiveData<ArrayList<IncomingListModel>>()
    var detailmodel = MutableLiveData<CommonDetailModel>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var submitmodel = MutableLiveData<BaseResModel<SubmitResult>>()
    var incomingDetail = MutableLiveData<CommonDetailModel>()

    var deleteInComing = MutableLiveData<DeleteResult>()

    fun delete(rwdh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteIncoming(rwdh) }
            if (result.code == 200) {//请求成功
                deleteInComing.value = result.data?.list!![0]
            } else {
                LogUtil.e("请求错误>>" + result.msg)
                showError(ErrorResult(result.code, result.msg, true))
            }
        }
    }

    fun modify(model: CommonPostModel) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.modifyIncoming(model)
            }
            submitmodel.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun getDetail(hashMap: HashMap<String, String>) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getIncomingDetail(hashMap)
            }
            incomingDetail.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun submit(model: CommonPostModel) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.submitInspection(model)
            }
            submitmodel.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }

    }

    fun getResult(file: MultipartBody) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) { httpUtil.fileUpload(file) }
                if (result.code == 200) {//请求成功
                    resultFile.value = result.data
                } else {
                    LogUtil.e("请求错误>>" + result.msg)
                    showError(ErrorResult(result.code, result.msg, true))
                }
            } catch (e: Throwable) {//接口请求失败
                LogUtil.e("请求异常>>" + e.message)
                val errorResult = ErrorUtil.getError(e)
                errorResult.show = true
                showError(errorResult)
            } finally {//请求结束
                dismissLoading()
            }
        }
    }

    fun getDetailModel(hashMap: HashMap<String, String>) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getIncomingTaskDetail(hashMap)
            }
            detailmodel.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun getIncomingList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getIncomingList(hashMap) },
            incominglist,
            isShowLoading = true
        )
    }

    fun getIncomingHistoryList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getIncomingHistoryList(hashMap) },
            incominglist,
            isShowLoading = true
        )
    }

}
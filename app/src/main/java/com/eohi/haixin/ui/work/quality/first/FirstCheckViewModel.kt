package com.eohi.haixin.ui.work.quality.first

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

class FirstCheckViewModel : BaseViewModel() {

    var firstCheckList = MutableLiveData<ArrayList<FirstCheckListResult>>()
    var resultFile = MutableLiveData<FileUploadResult.DataBean>()
    var dataList = MutableLiveData<CommonDetailModel>()
    var firstCheckPostResult = MutableLiveData<BaseResModel<SubmitResult>>()
    var deleteFirstCheck = MutableLiveData<DeleteResult>()
    var blxxList = MutableLiveData<ArrayList<BlxxBean>>()

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxList)
    }

    fun delete(rwdh: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { httpUtil.deleteFirstCheck(rwdh) }
            if (result.code == 200) {//请求成功
                deleteFirstCheck.value = result.data?.list!![0]
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
                httpUtil.modifyFirstCheck(model)
            }
            firstCheckPostResult.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun getDetail(hashMap: HashMap<String, String>) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.firstCheckDetail(hashMap)
            }
            dataList.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun postCheckFirst(commonPostModel: CommonPostModel) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.postFirstCheck(commonPostModel)
            }
            firstCheckPostResult.value = result
            dismissLoading()
            showError(ErrorResult(result.code, result.msg, true))
        }
    }

    fun getDataList(hashMap: HashMap<String, String>) {
        viewModelScope.launch {
            showLoading()
            val result = withContext(Dispatchers.IO) {
                httpUtil.getFirstCheckData(hashMap)
            }
            dataList.value = result
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

    fun getFirstCheckList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getFirstCheckList(hashMap) },
            firstCheckList,
            isShowLoading = true,
            isShowError = true
        )
    }

    fun getFirstCheckResultList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getFirstCheckResultList(hashMap) },
            firstCheckList,
            isShowLoading = true,
            isShowError = true
        )
    }

}
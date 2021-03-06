package com.eohi.haixin.ui.work.purchasein

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.main.model.ItemInfo
import com.eohi.haixin.ui.main.model.KwModel
import com.eohi.haixin.ui.main.model.WarehouseInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/13 10:14
 */
class FinishingInViewModel : BaseViewModel() {

    var whlist = MutableLiveData<ArrayList<WarehouseInfo>>()

    var kwlist = MutableLiveData<ArrayList<KwModel>>()

    var iteminfo = MutableLiveData<ArrayList<ItemInfo>>()

    //获取库仓库列表
    fun getCklist(isShowLoading: Boolean = true) {
        launchList({ httpUtil.getWarehouseInfo() }, whlist, isShowLoading)
    }

    //根据仓库获取库位
    fun getKwlist(ckh: String?, isShowLoading: Boolean = true) {
        launchList({ httpUtil.getKwList(ckh) }, kwlist, isShowLoading)
    }

    fun getItemInfo(map: ArrayMap<String?, String?>, isShowLoading: Boolean = true) {
        launchList({ httpUtil.getItemInfo(map) }, iteminfo, isShowLoading)
    }


}
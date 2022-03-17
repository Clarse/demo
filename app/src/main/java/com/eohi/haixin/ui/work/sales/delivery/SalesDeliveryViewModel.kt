package com.eohi.haixin.ui.work.sales.delivery

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.main.model.KwModel
import com.eohi.haixin.ui.main.model.WarehouseInfo
import com.eohi.haixin.ui.work.model.PickingWpxxModel

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/5/19 10:35
 */
class SalesDeliveryViewModel : BaseViewModel() {
    var whlist = MutableLiveData<ArrayList<WarehouseInfo>>()

    var kwlist = MutableLiveData<ArrayList<KwModel>>()

    var itemlist = MutableLiveData<ArrayList<PickingWpxxModel>>()

    //获取库仓库列表
    fun getCklist(isShowLoading: Boolean = true) {
        launchList({ httpUtil.getWarehouseInfo() }, whlist, isShowLoading)
    }

    //根据仓库获取库位
    fun getKwlist(ckh: String?, isShowLoading: Boolean = true) {
        launchList({ httpUtil.getKwList(ckh) }, kwlist, isShowLoading)
    }

    fun getItemInfo(tmh: String, ckh: String, kwh: String) {
        launchList({ httpUtil.getMoveItemInfo(tmh, ckh, kwh) }, itemlist, true)
    }


}
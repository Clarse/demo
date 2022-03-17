package com.eohi.haixin.ui.main.agv

import android.util.ArrayMap
import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.main.model.ItemInfo

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/3 9:48
 */
class AgvInViewModel : BaseViewModel() {
    var iteminfo = MutableLiveData<ArrayList<ItemInfo>>()

    fun getItemInfo(map: ArrayMap<String?, String?>, isShowLoading: Boolean = true) {
        launchList({ httpUtil.getItemInfo(map) }, iteminfo, isShowLoading)
    }

}
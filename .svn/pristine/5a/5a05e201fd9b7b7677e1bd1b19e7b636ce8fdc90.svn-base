package com.eohi.haixin.ui.work.adapter

import android.app.Activity
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutImageItemBinding
import com.eohi.haixin.ui.work.model.ImageViewModel
import com.eohi.haixin.widget.clicks

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/24 17:20
 */
class ImageViewAdapter(mContext: Activity, listDatas: ArrayList<ImageViewModel>) :
    BaseAdapter<LayoutImageItemBinding, ImageViewModel>(mContext, listDatas) {
    override fun convert(v: LayoutImageItemBinding, t: ImageViewModel, position: Int) {
        v.ivIcon.setImageResource(t.icon)
        v.tvText.text = t.str
        v.llLayout.clicks {
            onItemClick?.let {
                it(t.icon)
            }
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

    fun onNewItemClick(itemClick: (Int) -> Unit) {
        this.onItemClick = itemClick
    }


}
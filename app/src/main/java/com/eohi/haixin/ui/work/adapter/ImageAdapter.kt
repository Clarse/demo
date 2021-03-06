package com.eohi.haixin.ui.work.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.eohi.haixin.base.BaseAdapter
import com.eohi.haixin.databinding.LayoutPhotoItemBinding
import com.eohi.haixin.ui.work.quality.delivery.DeliveryCheckActivity
import com.eohi.haixin.ui.work.quality.delivery.DeliveryDetailActivity
import com.eohi.haixin.ui.work.quality.finish.FinishCheckActivity
import com.eohi.haixin.ui.work.quality.finish.FinishDetailActivity
import com.eohi.haixin.ui.work.quality.first.FirstCheckActivity
import com.eohi.haixin.ui.work.quality.first.FirstDetailActivity
import com.eohi.haixin.ui.work.quality.incoming.IncomingCheckActivity
import com.eohi.haixin.ui.work.quality.incoming.IncomingDetailActivity
import com.eohi.haixin.ui.work.quality.process.ProcessCheckActivity
import com.eohi.haixin.ui.work.quality.process.ProcessDetailActivity
import java.io.File

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/6/8 16:00
 */
class ImageAdapter(context: AppCompatActivity, listDatas: ArrayList<String>) :
    BaseAdapter<LayoutPhotoItemBinding, String>(
        context,
        listDatas,
        RecyclerView.LayoutParams.WRAP_CONTENT
    ) {
    @Suppress("MISSING_DEPENDENCY_CLASS")
    override fun convert(v: LayoutPhotoItemBinding, t: String, position: Int) {
        if (mContext.javaClass == IncomingDetailActivity::class.java || mContext.javaClass == FirstDetailActivity::class.java
            || mContext.javaClass == ProcessDetailActivity::class.java || mContext.javaClass == FinishDetailActivity::class.java
            || mContext.javaClass == DeliveryDetailActivity::class.java
        ) {
            Glide.with(mContext).load(t).into(v.ivItem)
        } else if ((mContext.javaClass == IncomingCheckActivity::class.java && IncomingCheckActivity.type == "modify") ||
            (mContext.javaClass == FirstCheckActivity::class.java && FirstCheckActivity.type == "modify") ||
            (mContext.javaClass == ProcessCheckActivity::class.java && ProcessCheckActivity.type == "modify") ||
            (mContext.javaClass == FinishCheckActivity::class.java && FinishCheckActivity.type == "modify") ||
            (mContext.javaClass == DeliveryCheckActivity::class.java && DeliveryCheckActivity.type == "modify")
        ) {
            Glide.with(mContext).load(t).into(v.ivItem)
        } else {
            Glide.with(mContext).load(File(t)).into(v.ivItem)
        }
    }

}
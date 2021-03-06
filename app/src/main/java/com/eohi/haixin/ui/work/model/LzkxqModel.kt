package com.eohi.haixin.ui.work.model

data class LzkxqModel(
    val LZKKH: String, //流转卡号
    val WPH: String, //物品号
    val WPMC: String, //物品名称
    val GG: String, //规格描述
    val TH: String, //图号
    val BZS: String, //需求数量
    val RWDH: String,
    val SCPH: String,
    val ZXBGGX: String,
    val items: ArrayList<LZKSubListModel>
)

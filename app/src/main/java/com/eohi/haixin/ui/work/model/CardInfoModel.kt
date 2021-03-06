package com.eohi.haixin.ui.work.model

data class CardInfoModel(
    val BGSL: Int,
    var BZS: Int,
    var HKSL: Int, //合卡数量
    val CLLH: String,
    val DjLsh: Int,
    val GGMS: String,
    val JLDW: String,
    val LZKKH: String,
    val RCLLH: String,
    val RWDH: String,
    val SCPH: String,
    val TH: String,
    val TSZTMS: String,
    val WPH: String,
    val WPMC: String,
    val items: List<GXItem>
)

data class GXItem(
    val GXH: String,
    val GXMS: String
)
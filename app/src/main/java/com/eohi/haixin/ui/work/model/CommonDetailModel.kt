package com.eohi.haixin.ui.work.model

data class CommonDetailModel(
    var code: Int,
    var msg: String,
    var count: Int,
    var `data`: DataBean
)

data class DataBean(
    var list: ArrayList<ListBean>,
    var BT: ArrayList<BtBean>,
    var BLYY: ArrayList<BlxxBean>,
    var count: Int
)

data class ListBean(
    val RWBH: String,
    val SCGDH: String,
    val WPH: String,
    val WPMC: String,
    val GGMS: String,
    val JYSL: String,
    val HGSL: String,
    val BHGSL: String,
    val SQMS: String,
    val JYYXM: String,
    val JYSJ: String,
    val JYJG: String,
    val CX: String,
    val SCRWDH: String,
    val JYGX: String,
    val JYMS: String,
    val GDH: String,
    val JYDH: String,
    val FHDH: String,
    val DJH: String,
    val TPWJM: String,
    val SJSJ: String
)

data class BtBean(
    var XMBM: String,
    var XMMC: String,
    var JYBZ: String,
    var BZZ: String,
    var JYGJ: String,
    var JYZ: String,
    var PDJG: String
)

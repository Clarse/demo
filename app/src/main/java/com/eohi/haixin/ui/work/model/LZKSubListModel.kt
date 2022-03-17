package com.eohi.haixin.ui.work.model

data class LZKSubListModel(
    val GXH: String,
    val GXMS: String,
    val BHGSL: Int,
    val HGSL: Int,
    val JYHGSL: Int,
    val JYBHGSL: Int,
    val SCRID: String,
    val SCRXM: String,
    val BC: String,
    val BGRQ: String,
    val BGSL: Int,
    val JYRQ: String,
    val ZJRYID: String,
    val ZJRY: String,
    val BZ: String,
    val JDSL: Int,//接单数量
    val DQBZNSL: Int,//编组内数量
    val DQGXLJBGSL: Int,//当前工序累计报工数量
    val BZS: Int,//标准编组数
    val JDRID: String,//接单人id
    val JDRXM: String,//接单人姓名
    val JDSJ: String,//接单时间
    val JGDYMC: String//加工单元名称
)

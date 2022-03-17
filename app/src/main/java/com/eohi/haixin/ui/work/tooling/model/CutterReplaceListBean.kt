package com.eohi.haixin.ui.work.tooling.model

data class CutterReplaceListBean(
    val SBBH: String,
    val SBMC: String,
    val SBXH: String,
    val items: ArrayList<Items>
)

data class Items(
    val DJBH: String,
    val DJMC: String,
    val GGXH: String,
    val PH: String,
    val BZSMSYCS: String,
    val YWGSL: String,
    val GHSJ: String,
    val GHRYHH: String,
    val GHRMC: String
)

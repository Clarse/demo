package com.eohi.haixin.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/28 15:55
 */
data class CardSubResultModel(
    val CLLH: String,
    val GGMS: String,
    val JLDW: String,
    val SCSL: Int,
    val LZKLX: String,
    val RCLLH: String,
    val RWDH: String,
    val SCPH: String,
    val TH: String,
    val TSZTMS: String,
    val WPH: String,
    val WPMC: String,
    val XLZKKH: String,
    val items: List<ResultItem>
)

data class ResultItem(
    val GXH: String,
    val GXMS: String
)
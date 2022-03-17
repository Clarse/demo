package com.eohi.haixin.ui.work.model

/**
 * @author zhaoli.Wang
 * @description:
 * @date :2021/12/28 15:50
 */
data class CradMergeSubModel(
    val bzs: Int,
    val cllh: String,
    val gsh: String,
    val hbyhh: String,
    val hbyhm: String,
    val items: List<CardItem>,
    val lzklx: String,
    val rcllh: String,
    val rwdh: String,
    val tsztms: String
)

data class CardItem(
    val bzsl: Int,
    val hklzkkh: String,
    val hklzklsh: Int,
    val hksl: Int
)
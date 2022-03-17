package com.eohi.haixin.ui.work.model

data class UnQualifiedPostModel(
    var gsh: String,//公司号
    var jgdybh: String,//加工单元编号
    var jgdymc: String,//加工单元名称
    var lzkkh: String, //流转卡卡号
    var bhgzsl: Int, //不合格总数量
    var gxbh: String,//工序编号
    var gxmc: String,//工序名称
    var cjrid: String, //创建人id
    var cjr: String, //创建人
    var scrid: String,//生产人id
    var scr: String, //生产人
    var sbbh: String,//设备编号
    var sbmc: String, //设备名称
    var zjr: String, //质检人
    var zjrid: String, //质检人id
    var gxtxh: String, //工序体序号
    var hgsl: String,//合格数量
    var bz: String,//备注
    var items: ArrayList<Items>
) {
    constructor() : this(
        "", "", "", "", 0, "", "", "", "", "", "", "", "", "", "", "", "", "", ArrayList()
    )
}

data class Items(
    var blbm: String,
    var blyy: String,
    var blsl: Int,
    var scrid: String,
    var scr: String
) {
    constructor() : this("", "", 0, "", "")
}

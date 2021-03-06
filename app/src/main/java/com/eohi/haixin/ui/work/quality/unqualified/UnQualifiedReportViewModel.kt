package com.eohi.haixin.ui.work.quality.unqualified

import androidx.lifecycle.MutableLiveData
import com.eohi.haixin.base.BaseViewModel
import com.eohi.haixin.ui.work.model.*

class UnQualifiedReportViewModel : BaseViewModel() {

    val lzkDetailResult = MutableLiveData<ArrayList<LzkDetailResult>>()
    val blxxResult = MutableLiveData<ArrayList<BlxxBean>>()
    val gxResult = MutableLiveData<ArrayList<GxModel>>()
    val personResult = MutableLiveData<ArrayList<PersonModel>>()
    val zjryResult = MutableLiveData<ArrayList<PersonModel>>()
    val postResult = MutableLiveData<ArrayList<SubmitResult>>()
    var sblist = MutableLiveData<ArrayList<EquipmentsModel>>()
    var equipmentList = MutableLiveData<ArrayList<EquipmentsModel>>()
    var jgdylist = MutableLiveData<ArrayList<ProcessingUnitModel>>()
    var lzkbg = MutableLiveData<ArrayList<LzkBgModel>>()

    //扫码得到生产人员
    var person = MutableLiveData<ArrayList<PersonModel>>()
    var perPerson = MutableLiveData<ArrayList<PersonModel>>()

    fun getLzkbg(gsh: String, lzkkh: String, gxbh: String) {
        launchList({ httpUtil.getLzkBg(gsh, lzkkh, gxbh) }, lzkbg)
    }

    fun getJgdy(cardno: String, gxno: String, equno: String) {
        if (equno.isEmpty()) {
            launchList(
                { httpUtil.getJgdysb(cardno, gxno) },
                jgdylist,
                isShowLoading = true,
                successCode = 200
            )
        } else {
            launchList(
                { httpUtil.getJgdy(cardno, gxno, equno) },
                jgdylist,
                isShowLoading = true,
                successCode = 200
            )
        }
    }

    fun getAllEquipmentList(hashMap: HashMap<String, String>) {
        launchList(
            { httpUtil.getAllEquipments(hashMap) },
            equipmentList,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getSblist(companycode: String, userid: String, scxbh: String) {
        launchList(
            { httpUtil.getNewSBxx(companycode, userid, scxbh) },
            sblist,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun getLzkDetail(cardno: String) {
        launchList({ httpUtil.getLzkDetail(cardno) }, lzkDetailResult, true, successCode = 200)
    }

    fun getBlxx() {
        launchList({ httpUtil.getBlxx() }, blxxResult, true)
    }

    fun getGx(cardno: String) {
        launchList({ httpUtil.getGxAll(cardno, "") }, gxResult, true, successCode = 200)
    }

    //选择人员
    fun getPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            personResult,
            true,
            successCode = 200
        )
    }

    fun getZjry(gsh: String, username: String) {
        launchList(
            { httpUtil.getJYRY(gsh, username) },
            zjryResult,
            true,
            successCode = 200
        )
    }

    //查询人员-生产人员
    fun findPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            person,
            isShowLoading = true,
            successCode = 200
        )
    }

    //查询检验人员
    fun findJYRY(gsh: String, username: String) {
        launchList(
            { httpUtil.getJYRY(gsh, username) },
            person,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun findPerPerson(gsh: String, username: String) {
        launchList(
            { httpUtil.getPersonnelInfo(gsh, username) },
            perPerson,
            isShowLoading = true,
            successCode = 200
        )
    }

    fun post(unQualifiedPostModel: UnQualifiedPostModel) {
        launchList({ httpUtil.unQualifiedPost(unQualifiedPostModel) }, postResult, true)
    }

}
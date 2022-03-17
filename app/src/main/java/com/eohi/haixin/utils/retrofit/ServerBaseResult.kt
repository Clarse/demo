package com.eohi.haixin.utils.retrofit

class ServerBaseResult<E> {
    var code = 0
    var msg: String? = null
    var data: E? = null
        private set

    fun setData(data: E) {
        this.data = data
    }

}
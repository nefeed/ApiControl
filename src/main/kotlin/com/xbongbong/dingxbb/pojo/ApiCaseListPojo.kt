package com.xbongbong.dingxbb.pojo

import java.io.Serializable

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-14
 * Time: 14:22
 */
class ApiCaseListPojo : Serializable {

    var page = 1
    var pageSize = 20
    var expectedStateCodeSort: String = ""
    var testResultSort: String = ""
    var durationTimeSort: String = ""
    var updateTimeSort: String = ""

    companion object {

        private const val serialVersionUID = -1L
    }
}
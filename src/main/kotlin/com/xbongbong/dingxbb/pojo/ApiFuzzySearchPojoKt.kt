package com.xbongbong.dingxbb.pojo

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-06-12
 * Time: 14:15
 */
data class ApiFuzzySearchPojoKt(var page: Int = 1,
                                var pageSize: Int = 20,
                                var version: String,
                                var module: String,
                                var apiNameLike: String,
                                var urlLike: String,
                                var authorNameLike: String,
                                var updateTimeStart: Int = 0,
                                var updateTimeEnd: Int = 0)
package com.xbongbong.dingxbb.util

import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFCellStyle
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.springframework.stereotype.Service



/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc: Excel 导出工具类
 * Date: 2017-07-19
 * Time: 22:50
 */
@Service
class ExcelUtil {

    fun getHSSFWorkbook(sheetName: String, title: Array<String>, values: Array<Array<String>>, wb: HSSFWorkbook?): HSSFWorkbook {
        var wb = wb
        // 第一步，创建一个webbook，对应一个Excel文件
        if (wb == null) {
            wb = HSSFWorkbook()
        }
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet
        val sheet = wb.createSheet(sheetName)
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
        var row = sheet.createRow(0)
        // 第四步，创建单元格，并设置值表头 设置表头居中
        val style = wb.createCellStyle()
        style.alignment = HSSFCellStyle.ALIGN_CENTER // 创建一个居中格式

        var cell: HSSFCell? = null
        //创建标题
        for (i in title.indices) {
            cell = row.createCell(i)
            cell!!.setCellValue(title[i])
            cell.setCellStyle(style)
        }
        //创建内容
        for (i in values.indices) {
            row = sheet.createRow(i + 1)
            for (j in 0..values[i].size - 1) {
                row.createCell(j).setCellValue(values[i][j])
            }
        }

        return wb
    }
}
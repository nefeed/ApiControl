package com.xbongbong.dingxbb.controller;

import com.xbongbong.dingxbb.util.ExcelUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * User: 章华隽
 * E-mail: nefeed@163.com
 * Desc:
 * Date: 2017-07-19
 * Time: 23:02
 */
@Controller
@RequestMapping(value="/excel/*")
public class ExcelController {

    @Autowired
    private ExcelUtil excelUtil;

    @RequestMapping(value = "exportfeedback")
    @ResponseBody
    public void exportFeedBack(HttpServletResponse response,
                                    @RequestParam(value="query", required=false) String searchText,
                                    @RequestParam(value="type", required=false) String strType,
                                    @RequestParam(value="startDate", required=false) String startDate,
                                    @RequestParam(value="endDate", required=false) String endDate){

        String fileName = "反馈明细"+System.currentTimeMillis()+".xls"; //文件名
        String sheetName = "反馈明细";//sheet名

        String []title = new String[]{"Id","导航图标","反馈类型","内容","联系方式","应用Id","应用版本","反馈时间"};//标题

        // TODO 获取需要导出到Excel的对象队列
        List<Object> list = new ArrayList<>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String [][]values = new String[list.size()][];
        for(int i=0;i<list.size();i++){
            values[i] = new String[title.length];
            //将对象内容转换成string
            Object obj = list.get(i);
            // TODO 插入Excel列数据
            values[i][0] = obj.toString();

        }

        HSSFWorkbook wb = excelUtil.getHSSFWorkbook(sheetName, title, values, null);

        //将文件存到指定位置
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
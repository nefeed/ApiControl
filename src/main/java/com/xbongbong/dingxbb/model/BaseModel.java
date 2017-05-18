package com.xbongbong.dingxbb.model;

import com.xbongbong.util.CommentUtil;
import com.xbongbong.util.PageHelper;

import java.util.List;
import java.util.Map;

/**
 * User: Gavin
 * E-mail: GavinChangCN@163.com
 * Desc:
 * Date: 2017-05-16
 * Time: 17:35
 */
public class BaseModel {
    protected static final String TAG = "BaseModel";

    /**
     * 生成pageHelper
     *
     * @return PageHelper
     */
    protected static PageHelper getPageHelper(Map<String, Object> param,
                                              Integer pageSize, IModel iModel) {
        Integer rowCounts = iModel.getEntitysCount(param);
        Integer page = (Integer) param.get("page");
        if (page == null || page < 1)
            page = 1;

        param.remove("page");
        String pageUriStr = CommentUtil.mapToUrl(param);
        param.put("page", page);

        //记录总数放入param
        param.put("total", rowCounts);

        PageHelper pageHelper = new PageHelper(page, pageSize);
        pageHelper.setRowsCount(rowCounts);
        pageHelper.setPageUriStr(pageUriStr);

        return pageHelper;
    }

    /**
     * 获取entityList
     *
     * @return list
     */
    public List<?> getEntityList(Map<String, Object> param,
                                 PageHelper pageHelper, IModel iModel) {
        Integer pageNum = pageHelper.getPageSize();
        Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

        param.put("pageNum", pageNum);
        param.put("start", start);
        return iModel.findEntitys(param);
    }

}

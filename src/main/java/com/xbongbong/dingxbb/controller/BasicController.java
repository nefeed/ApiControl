package com.xbongbong.dingxbb.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xbongbong.dingxbb.helper.ConfigConstant;
import com.xbongbong.dingxbb.helper.JedisPoolHelper;
import com.xbongbong.dingxbb.model.IModel;
import com.xbongbong.util.HttpUtil;
import com.xbongbong.util.PageHelper;
import com.xbongbong.util.StringUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
public abstract class BasicController {

    private static final Logger LOG = LogManager.getLogger(BasicController.class);

    public static final int SESSIONCODE_VALIDATE_TIME = 7 * 86400;  //sessionCode有效期为7天

    //项目缓存键前缀
    public static final String cachePrefix = ConfigConstant.getProjectName() + ":";

    protected static final int MONTHSECONDS = 31 * 24 * 3600;
    protected static final int WEEKSECONDS = 7 * 24 * 3600;

    //redis缓存连接池
    protected JedisPool pool = JedisPoolHelper.getInstance().getJedisPool();

    /**
     * 验证登录令牌
     */
//	protected boolean checkToken(HttpServletRequest request,HttpServletResponse response) {
//		
//		Integer nowTime = DateUtil.getInt();
//		
//		String data = request.getParameter("data");
//		String token = request.getParameter("token");
//		System.out.println("come to checkToken(request).....");
//		System.out.println("data:"+data);
//		System.out.println("token:"+token);
//		
//		try {
//			if (StringUtil.isEmpty(data) || StringUtil.isEmpty(token)) {
//				returnJsonData(request, response, 8, "请登入","");return false;
//			}
//			//data = URLEncodeUtils.decodeURL(data);
//			
//			JSONObject dataJson = JSONObject.parseObject(data);
//			// 通过json对象获取所需参数
//			Integer staffId = dataJson.getInteger("staffId");
//			// 使用mycat后的索引参数
//			Integer companyId = dataJson.getInteger("companyId");
//			
//			if(staffId == null || staffId < 0) {
//				returnJsonData(request, response, 8, "请登入","");return false;
//			}
//			
//			StaffSessionEntity staffSessionEntity = null;
//
//			//先从缓存中取数据 redis缓存连接实体
////			Jedis jedis = pool.getResource();
////			
////			try { 	//	TODO
////				String staffSessionKey = cachePrefix + "staffSession:" + staffId;
////				if(jedis.exists(staffSessionKey.getBytes())) {
////					staffSessionEntity = (StaffSessionEntity)SerializeUtil.unserialize(jedis.get(staffSessionKey.getBytes()));
////				}
////			} finally {
////			   /// 释放jedis实体
////			   if (null != jedis) { 
////				   jedis.close();
////			   }
////			}
//		
//			//从数据库中取
////			if(staffSessionEntity == null) {
//				StaffEntity staffEntity;
//				if(companyId != null && companyId > 0) {
//					staffEntity = staffModel.getByKey(staffId, companyId);
//				} else {
//					staffEntity = staffModel.getByKeyWithoutCid(staffId);
//					companyId = staffEntity.getCompanyId();
//				}
//				
//				if(staffEntity == null || staffEntity.getId() == null){
//					returnJsonData(request, response, 14, "用户名密码错误", "");return false;
//				}
//				
//				Integer userId = staffEntity.getUserId();
//				
//				Map<String,Object> param = new HashMap<String,Object>();
//				
//				//mycat路由
//				CommentUtil.addToMap(param, "companyId", companyId);
//				
//				CommentUtil.addToMap(param, "userId", userId);
//				CommentUtil.addToMap(param, "staffId", staffId);
//				List<StaffSessionEntity> staffSessionList = staffSessionModel.findEntitys(param);
//				
//				if (staffSessionList == null || staffSessionList.size() < 1) {
//					
//					returnJsonData(request, response, 8, "请登入","");return false;
//				}else {
//					staffSessionEntity = staffSessionList.get(0);
//					if(nowTime - staffSessionEntity.getEndTime() > 0){
//						returnJsonData(request, response, 8, "会话已过期，请重新登入","");return false;
//					}
//				}
////			}
//				request.setAttribute("staffEntity",staffEntity);
//			
//			String sessionCode = staffSessionEntity.getSessionCode();
//			if (MD5Util.md5(data + sessionCode).equalsIgnoreCase(token)) {
//				System.out.println("pass by not encodeURL");
//				return true;
//			}
//			
//			//把data整个encode之后处理一次,兼容整体encode的情况
//			data=URLEncodeUtils.encodeURL(data); 
////			System.out.println("encode data="+data);
//			if(MD5Util.md5(data+sessionCode).equalsIgnoreCase(token)){
//				System.out.println("pass by encodeURL");
//				return true;
//			}
//
//			data=data.replaceAll("\\+", "%20");//ios encode的结果是没有+
////			System.out.println("encode data2="+data);
////			System.out.println("encode my token2="+MD5Util.md5(data + sessionCode));
//			if(MD5Util.md5(data+sessionCode).equalsIgnoreCase(token)){
//				System.out.println("pass by encodeURL(replace +)");
//				return true;
//			}
//			
//			System.out.println("checkToken(request) not pass ........................");
//		
//		} catch (Exception e) {
//			//调试错误
//			LOG.error(e.getLocalizedMessage());
//			returnJsonData(request, response, 8, "请登入","");return false;
//		}
//
//		returnJsonData(request, response, 8, "请登入","");return false;
//	}
    protected PageHelper getPageHelper(Map<String, Object> param, IModel iModel) {
        Integer rowCounts = iModel.getEntitysCount(param);
        Integer page = (Integer) param.get("page");

        PageHelper pageHelper = new PageHelper(page);
        pageHelper.setRowsCount(rowCounts);
        return pageHelper;
    }

    protected PageHelper getPageHelper(Map<String, Object> param, IModel iModel, Integer pageSize) {
        Integer rowCounts = iModel.getEntitysCount(param);
        Integer page = (Integer) param.get("page");

        PageHelper pageHelper = new PageHelper(page, pageSize);
        pageHelper.setRowsCount(rowCounts);
        return pageHelper;
    }

    protected List<?> getEntityList(Map<String, Object> param, PageHelper pageHelper, IModel iModel) {
        Integer pageNum = pageHelper.getPageSize();
        Integer start = (pageHelper.getCurrentPageNum() - 1) * pageNum;

        param.put("pageNum", pageNum);
        param.put("start", start);
        return iModel.findEntitys(param);
    }

    protected JSONArray getJSONArray(String str) {
        if (!StringUtil.isEmpty(str) && str.startsWith("[") && str.endsWith("]")) {
            return JSON.parseArray(str);
        } else {
            return new JSONArray();
        }
    }

    /**
     * data参数验证方法
     */
    protected JSONObject validateData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        String data = request.getParameter("data");
        if (StringUtil.isEmpty(data)) {
            returnJsonData(request, response, 7, "data对象不存在！", "");
            return new JSONObject();
        }
        JSONObject dataJson = null;
        try {
            dataJson = JSONObject.parseObject(data);
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage());
            returnJsonData(request, response, 2, "json数据解析失败", "");
            return new JSONObject();
        }

        return dataJson;
    }

    /**
     * 接口返回参数方法
     */
    protected void returnSuccessJsonData(HttpServletRequest request, HttpServletResponse response, Object ret) {

        this.returnJsonData(request, response, 0, "操作成功", ret);
    }

    /**
     * 接口返回参数方法
     */
    protected void returnJsonData(HttpServletRequest request, HttpServletResponse response, Integer errorcode, String msg, Object ret) {
        response.addHeader("content-type", "application/json;charset=utf-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("errorcode", errorcode);
        param.put("msg", msg);
        param.put("ret", ret);
        try {
            HttpUtil.jsonOut(request, response, param);
        } catch (IOException e) {
            LOG.error(e.getLocalizedMessage());
        }
    }

}

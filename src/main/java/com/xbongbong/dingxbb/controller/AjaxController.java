package com.xbongbong.dingxbb.controller;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xbongbong.util.DigestUtil;
import com.xbongbong.util.HttpUtil;
import com.xbongbong.util.MD5Util;
import com.xbongbong.util.StringUtil;

@Controller
@RequestMapping("/ajax")
public class AjaxController extends BasicController {
	
	@RequestMapping("/md5")
	public void md5(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap)
			throws Exception {
		
		String code = request.getParameter("code");		
		modelMap.put("md5", MD5Util.md5(code));
		HttpUtil.ajaxOut(request, response, modelMap);
	}
	
	@RequestMapping("/sha256")
	public void sha256(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap)
			throws Exception {
		
		String code = request.getParameter("code");		
		modelMap.put("sha256", DigestUtil.Encrypt(code, "SHA-256"));
		HttpUtil.ajaxOut(request, response, modelMap);
	}
	
	@RequestMapping("/md5/center")
	public void indexCenter(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap)
			throws Exception {
		
		String code = request.getParameter("code");	
		
//		if(request.getMethod().equalsIgnoreCase("GET")){
//			if(!StringUtil.isEmpty(depNameStr)){
//				depName = new String(depNameStr.getBytes("ISO8859-1"),"UTF-8");
//			}
//		}else{
//			if(!StringUtil.isEmpty(depNameStr)){
//				depName = new String(depNameStr.getBytes("ISO8859-1"),"UTF-8");
//				if(depName.contains("?")){
//					depName = depNameStr;
//				}
//			}
//		}
		
		if(!StringUtil.isEmpty(code)){
			code = new String(code.getBytes("ISO8859-1"),"UTF-8");
		}
		modelMap.put("md5", MD5Util.md5(code));
		HttpUtil.ajaxOut(request, response, modelMap);
	}
}

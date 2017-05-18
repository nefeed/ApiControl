package com.xbongbong.dingxbb.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xbongbong.util.DateUtil;
import com.xbongbong.util.HttpUtil;
import com.xbongbong.util.NetAddressUtil;

@Controller
public class StatusController  {
	
	public static final String deployTime = DateUtil.getString(); //启动时间
	public static final String deployIP = NetAddressUtil.getHostAddress();  //部署服务器IP
	 
	
	@RequestMapping("/sys/status")
	public String sysStatus(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap) throws IOException {
		 
		modelMap.put("startTime",deployTime);
		modelMap.put("IP", deployIP);
		HttpUtil.jsonOut(request, response, modelMap);
		return null;
	}
}

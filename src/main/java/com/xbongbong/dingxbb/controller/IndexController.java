package com.xbongbong.dingxbb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class IndexController {
	
	@RequestMapping("/xbbInterfaceDebugging")
	public String index(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap)throws Exception {
		
		modelMap.put("pageIndex", "index");
		return "index";
	}
	
	@RequestMapping("/doc/sign")
	public String sign(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> modelMap)throws Exception {
		
		modelMap.put("pageIndex", "sign");
		return "sign";
	}

	/**
	 * 新建|编辑 接口
	 *
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/item")
	public String item(HttpServletRequest request,
					   HttpServletResponse response, Map<String, Object> modelMap)throws Exception {

		modelMap.put("pageIndex", "item");
		return "item";
	}

	/**
	 * 接口列表
	 *
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/list")
	public String list(HttpServletRequest request,
					   HttpServletResponse response, Map<String, Object> modelMap)throws Exception {

		modelMap.put("pageIndex", "list");
		return "list";
	}

	/**
	 * 测试用例列表
	 *
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/case/list")
	public String caseList(HttpServletRequest request,
					   HttpServletResponse response, Map<String, Object> modelMap)throws Exception {

		modelMap.put("pageIndex", "case_list");
		return "case_list";
	}

	/**
	 * 测试用例编辑
	 *
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/case/item")
	public String caseItem(HttpServletRequest request,
						   HttpServletResponse response, Map<String, Object> modelMap)throws Exception {

		modelMap.put("pageIndex", "case_item");
		return "case_item";
	}
}

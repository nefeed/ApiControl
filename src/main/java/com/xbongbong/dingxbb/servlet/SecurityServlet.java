package com.xbongbong.dingxbb.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;



public class SecurityServlet extends HttpServlet implements Filter {
	

	private static final long serialVersionUID = 7473287911651262413L;


	public void doFilter(ServletRequest arg0, ServletResponse arg1,
			FilterChain arg2) throws IOException, ServletException {
		
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpSession session = request.getSession(true);
		
		String url = request.getRequestURI();
		session.setAttribute("currentUri", url);
				
		arg2.doFilter(arg0, arg1);
		return;
	}
	

	public void init(FilterConfig arg0) throws ServletException {
	}
}
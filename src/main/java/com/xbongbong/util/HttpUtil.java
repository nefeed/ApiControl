package com.xbongbong.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
 
public class HttpUtil {
	private static final Logger LOG = LogManager.getLogger(HttpUtil.class);
	
	public static void ajaxOut(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> resultMap)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String callback = request.getParameter("callback");
		
		if (callback == null) {
			callback = "data";
		}
		PrintWriter out = response.getWriter();
		String rs = JsonUtil.toJson(resultMap);
		out.write(callback + "(" + rs + ");");// 账户没有设定uid
	}
	
	/**
	 * 输出json
	 * @param request
	 * @param response
	 * @param resultMap
	 * @throws IOException
	 */
	public static String jsonOut(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> resultMap)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String rs = JsonUtil.toJson(resultMap);
		out.write(rs);// 账户没有设定uid
		return null;
	}
	
	/**
	 * 好像服务器返回的json格式，客户端还是要eval，弃用
	 * @param request
	 * @param response
	 * @param resultMap
	 * @throws IOException
	 * @throws JSONException
	 */
	public static void ajaxOutJSONObject(HttpServletRequest request,
			HttpServletResponse response, Map<String, Object> resultMap)
			throws IOException, JSONException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String callback = request.getParameter("callback");
		LOG.debug("in ajaxout callback=" + callback);
		PrintWriter out = response.getWriter();
	//	String rs = JsonUtil.toJson(resultMap);
		JSONObject rj = new JSONObject(resultMap);
		out.write(callback + "(" + rj + ");");// 账户没有设定uid
	}
	
	/**
	 * ajax返回json格式数据
	 * @param request
	 * @param response
	 * @param str
	 * @throws IOException
	 */
	public static void ajaxOut(HttpServletRequest request,
			HttpServletResponse response, String str)
			throws IOException {
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		String callback = request.getParameter("callback");
		LOG.debug("in ajaxout callback=" + callback);
		PrintWriter out = response.getWriter();
		String rs = JsonUtil.toJson(str);
		out.write(callback + "(" + rs + ");");
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String requestType = request.getHeader("X-Requested-With");
		if (requestType != null && requestType.equals("XMLHttpRequest")) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String postHttps(String url, String params) {
        StringBuffer bufferRes = null;
        try {
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象  
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL urlGet = new URL(url);
            HttpsURLConnection http = (HttpsURLConnection) urlGet.openConnection();
            // 连接超时
            http.setConnectTimeout(25000);
            // 读取超时 --服务器响应比较慢，增大时间
            http.setReadTimeout(25000);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setSSLSocketFactory(ssf);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();

            OutputStream out = http.getOutputStream();
            out.write(params.getBytes("UTF-8"));
            out.flush();
            out.close();

            InputStream in = http.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String valueString = null;
            bufferRes = new StringBuffer();
            while ((valueString = read.readLine()) != null){
                bufferRes.append(valueString);
            }
            in.close();
            if (http != null) {
                // 关闭连接
                http.disconnect();
            }
            return bufferRes.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	

	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String getHttps(String url, Map<String, Object> params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = url + CommentUtil.mapToUrl(params);
			URL realUrl = new URL(urlName);
			
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager()
			{
			        public java.security.cert.X509Certificate[] getAcceptedIssuers()
			        {
			                return null;
			        }
			        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
			        {
			        }
			 
			        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
			        {
			        }
			}};
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			

			HostnameVerifier hv = new HostnameVerifier()
			{
			      public boolean verify(String urlHostName, SSLSession session)
			      {
			             System.out.println("Warning: URL Host: "+urlHostName+" vs. "+session.getPeerHost());
			             return true;
			      }
			};
			
//			HttpsURLConnection.setDefaultHostnameVerifier(hv);

			// 打开和URL之间的连接
			HttpsURLConnection conn = (HttpsURLConnection)realUrl.openConnection();
			conn.setHostnameVerifier(hv);
			conn.setSSLSocketFactory(sc.getSocketFactory());
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				result += "\n" + line;
			}
		} catch (Exception e) {
			LOG.error("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
	
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param apiUrl
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String get(String apiUrl, Map<String, Object> params) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = apiUrl + CommentUtil.mapToUrl(params);
			URL url = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = url.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line;
			while ((line = in.readLine()) != null) {
				if("".equals(result)) {
					result = line;
				} else {
					result += "\n" + line;
				}			
			}
			
		} catch (Exception e) {
			LOG.error("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	} 
	
	/**
	 * 向指定URL发送GET方法的请求
	 * 
	 * @param apiUrl
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
	 * @return URL所代表远程资源的响应
	 */
	public static String get(String apiUrl, Map<String, Object> params, String charSet) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlName = apiUrl + CommentUtil.mapToUrl(params);
			URL url = new URL(urlName);
			// 打开和URL之间的连接
			URLConnection conn = url.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
			// 建立实际的连接
			conn.connect();
			// 获取所有响应头字段
//			Map<String, List<String>> map = conn.getHeaderFields();
//			// 遍历所有的响应头字段
//			for (String key : map.keySet()) {
//				System.out.println(key + "--->" + map.get(key));
//			}
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream(), charSet));
			String line;
			while ((line = in.readLine()) != null) {
				if("".equals(result)) {
					result = line;
				} else {
					result += "\n" + line;
				}			
			}
			
		} catch (Exception e) {
			LOG.error("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	} 
	
	public static String getString(InputStream in){
		 BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(in,"gb2312"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}  
	        StringBuffer resBuffer = new StringBuffer();  
	        String resTemp = "";  
	        try {
				while((resTemp = br.readLine()) != null){  
				    resBuffer.append(resTemp);  
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
			}  
	        String response = resBuffer.toString(); 
	        return response;
	}
	
	/**
	 * android : 所有android设备
	 * mac os : iphone ipad
	 * windows phone:Nokia等windows系统的手机
	 */
	public static boolean   isMobileDevice(String requestHeader) {
		String[] deviceArray = new String[]{"android","iphone","ipad","ipod","windows phone"};
		if(requestHeader == null)
			return false;
		requestHeader = requestHeader.toLowerCase();
		for(int i=0;i<deviceArray.length;i++){
			if(requestHeader.indexOf(deviceArray[i])>0){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取当前完整url，包括参数部分
	 * @param request
	 * @return
	 */
	public static String getCurrentUrlStr(HttpServletRequest request) {
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();
		if (StringUtil.isEmpty(queryString)) {
			return url;
		}
		return url + "?" + queryString;
	}
	
	/**
	 * 获取域名和端口
	 * @param request
	 * @return
	 */
	public static String getBaseUrl(HttpServletRequest request) {
		
		String basePath = request.getScheme()+"://"+request.getServerName();
		if(request.getServerPort() != 80){
			basePath+= ":"+request.getServerPort();
		}
		basePath += request.getContextPath();

		return basePath;
	}
	
}
//证书管理
class MyX509TrustManager implements X509TrustManager {

    public X509Certificate[] getAcceptedIssuers() {
        return null;  
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
}


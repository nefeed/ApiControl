package com.xbongbong.util;

public class DeHtmlTag {

	public static String delTags(String html){
		String strHtml = html.replaceAll( ".*?(.*?)<\\/p>", "$1"); //读出body内里所有内容
//		strHtml = strHtml.replaceAll("</?[^/?(br)|(p)][^><]*>","");//保留br标签和p标签
		strHtml = strHtml.replaceAll("</?[a-zA-Z]+[^><]*>","");
		return strHtml;
	}
}

package com.xbongbong.util;

import java.text.DecimalFormat;

public class MoneyFormat {
	
	/**
	 * 钱币格式  123,456,789.12
	 * @param doub
	 * @return
	 */
	public static String  formateDouble (Object object){
		
       DecimalFormat decimalFormat = new DecimalFormat("###0.00");//格式化设置  
	   return decimalFormat.format(object);
	}
	
	public static String  formateDoubleWithComma (Object object){
		
       DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//格式化设置  
	   return decimalFormat.format(object);
	}
	public static void main(String[] args) {
		Double d = 12241452535.0/10000;
		System.out.println(MoneyFormat.formateDoubleWithComma(d));
	}
}

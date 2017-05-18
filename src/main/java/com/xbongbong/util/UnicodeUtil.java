package com.xbongbong.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * java中文转为unicode码，unicode转为中文
 * 
 * @author Administrator
 * 
 */
public class UnicodeUtil {
	public static void main(String[] args) {
		String encoding = gbEncoding("功能介绍:将unicode字符串转为汉字 输入参数:源unicode字符串 输出参数:转换后的字符串");
		System.out.println(encoding);
		String decoeing = decodeUnicode(encoding);
		System.out.println(decoeing);
		
		String ori = "&#33457;&#20799;.jpg";
		System.out.println(decodeDecimalUnicode(ori));
	}

	public static String gbEncoding(final String gbString) {
		char[] utfBytes = gbString.toCharArray();
		StringBuffer buffer = new StringBuffer();
		for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
			String hexB = Integer.toHexString(utfBytes[byteIndex]);
			if (hexB.length() <= 2) {
				hexB = "00" + hexB;
			}
			buffer.append("\\u" + hexB);
//			buffer.append("" + hexB);
		}
		return buffer.substring(0);
	}

	/**
	 * unicode 转换成 中文
	 * 
	 * @author fanhui 2007-3-15
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString) {
		char aChar;
		int len = theString.length();
		StringBuffer outBuffer = new StringBuffer(len);
		for (int x = 0; x < len;) {
			aChar = theString.charAt(x++);
			if (aChar == '\\') {
				aChar = theString.charAt(x++);
				if (aChar == 'u') {
					int value = 0;
					for (int i = 0; i < 4; i++) {
						aChar = theString.charAt(x++);
						switch (aChar) {
						case '0':
						case '1':
						case '2':
						case '3':
						case '4':
						case '5':
						case '6':
						case '7':
						case '8':
						case '9':
							value = (value << 4) + aChar - '0';
							break;
						case 'a':
						case 'b':
						case 'c':
						case 'd':
						case 'e':
						case 'f':
							value = (value << 4) + 10 + aChar - 'a';
							break;
						case 'A':
						case 'B':
						case 'C':
						case 'D':
						case 'E':
						case 'F':
							value = (value << 4) + 10 + aChar - 'A';
							break;
						default:
							throw new IllegalArgumentException(
									"Malformed      encoding.");
						}
					}
					outBuffer.append((char) value);
				} else {
					if (aChar == 't') {
						aChar = '\t';
					} else if (aChar == 'r') {
						aChar = '\r';
					} else if (aChar == 'n') {
						aChar = '\n';
					} else if (aChar == 'f') {
						aChar = '\f';
					}
					outBuffer.append(aChar);
				}
			} else {
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}
	
	 /**
	  * 将十进制形式的Unicode编码转换为字符，例如   36215->北 （&#36215;）
	  * @param codePoints
	  * @return
	  */
	 public static String fromCharCode(int... codePoints) { 
	   
	  StringBuilder builder = new StringBuilder(codePoints.length);  
	  for (int codePoint : codePoints){       
	   builder.append(Character.toChars(codePoint));   
	   }    
	         return builder.toString();
	   } 
	 
	 /**
	  * 将&#36215;&#39134; 转化为 只含有整数值的数组   result[0]=36215,result[1]=39134 
	  * @param unicodeStr
	  * @return
	  */
	 public static int[] removeUnicodeFlag(String unicodeStr){
	  String regex = "&#(\\d+);";
	  int result[]  = new int[unicodeStr.split(";").length];
	  Pattern p = Pattern.compile(regex);
	  Matcher ma = p.matcher(unicodeStr);
	  int i = 0;
	  while (ma.find()) {
	   result[i++] = Integer.parseInt(ma.group(1));
	  }
	  return result;
	 }
	 
	 public static String decodeDecimalUnicode(String unicodeStr) {
		 String regex = "&#(\\d+);";
		 Pattern p = Pattern.compile(regex);
		 Matcher ma = p.matcher(unicodeStr);
		 while (ma.find()) {
//			 System.out.println(ma.group(0));
//			 System.out.println(fromCharCode(Integer.parseInt(ma.group(1))));
			 unicodeStr = unicodeStr.replaceFirst(ma.group(0), fromCharCode(Integer.parseInt(ma.group(1))));
		 }
		 
		 return unicodeStr;
	 }
}
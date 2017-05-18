package com.xbongbong.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

/**
 *	Create by mabaobao-web-archetype
 *
 */
public class MD5Util {
	private static Logger	LOG			= Logger.getLogger("debugAppender");
	private static Logger	DATABASE	= Logger.getLogger("DATABASE");
	
	/**
	 * md5
	 * @param plainText
	 * @return
	 */
	public static String md5(String plainText) {
		String result = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();
			
			int i;
			
			StringBuffer buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
			result = buf.toString();// 32位的加密
		} catch (NoSuchAlgorithmException e) {
			LOG.error("md5 fail：", e);
			DATABASE.error("md5 fail：", e);
		}
		return result;
	}
	
	/**
	 * 对给定的字符串进行加密
	 * @param s
	 * @return 加密后的16进制的字符串
	 */
	public final static String EncoderByMd5(String s) {
		if (LOG.isInfoEnabled()) {
			LOG.info("encrypt md5：" + s);
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
				'e', 'f' };
		try {
			byte[] strTemp = s.getBytes();
			// 使用MD5创建MessageDigest对象
			MessageDigest mdTemp = MessageDigest.getInstance("MD5");
			mdTemp.update(strTemp);
			byte[] md = mdTemp.digest();
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte b = md[i];
				str[k++] = hexDigits[b >> 4 & 0xf];
				str[k++] = hexDigits[b & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			LOG.error("encrypt md5 fail：" + s, e);
			DATABASE.error("encrypt md5 fail：" + s, e);
			return null;
		}
	}
	
	/**
	 * chck password
	 * @param newStr
	 *        no encrypt password
	 * @param oldMD5Str
	 * 
	 * @return
	 */
	public final static boolean checkMD5(String newStr, String oldMD5Str) {
		String temp = EncoderByMd5(newStr);
		if (temp != null && temp.equals(oldMD5Str)) {
			return true;
		} else {
			return false;
		}
	}
	 
    /**
     *
     * @param plainText
     *            明文
     * @return 32位密文
     */
    public static String encryption_32(String plainText) {
        String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;
    }

	 public static void main(String[] args) {

//		 String test= "{\n  \"addTimeClient\" : \"1399725992\",\n  \"level\" : \"\",\n  \"city\" : \"杭州市\",\n  \"textString\" : \"\",\n  \"memo\" : \"\",\n  \"userId\" : \"4\",\n  \"name\" : \"\",\n  \"fromUser\" : \"0\",\n  \"type\" : \"location\",\n  \"district\" : \"\",\n  \"target\" : \"0\",\n  \"latitude\" : \"30.187057\",\n  \"operator\" : \"0\",\n  \"startTime\" : \"\",\n  \"classification\" : \"\",\n  \"longitude\" : \"120.138962\",\n  \"address\" : \"\",\n  \"country\" : \"中国\",\n  \"province\" : \"浙江省\",\n  \"endTime\" : \"\",\n  \"memoStr\" : \"\",\n  \"description\" : \"\",\n  \"status\" : \"0\",\n  \"outline\" : \"\"\n}";
//		 test="%7B%0A++%22addTimeClient%22+%3A+%221399726716%22%2C%0A++%22level%22+%3A+%22%22%2C%0A++%22city%22+%3A+%22%E6%9D%AD%E5%B7%9E%E5%B8%82%22%2C%0A++%22textString%22+%3A+%22%22%2C%0A++%22memo%22+%3A+%22%22%2C%0A++%22userId%22+%3A+%224%22%2C%0A++%22name%22+%3A+%22%22%2C%0A++%22fromUser%22+%3A+%220%22%2C%0A++%22type%22+%3A+%22location%22%2C%0A++%22district%22+%3A+%22%22%2C%0A++%22target%22+%3A+%220%22%2C%0A++%22latitude%22+%3A+%2230.187027%22%2C%0A++%22operator%22+%3A+%220%22%2C%0A++%22startTime%22+%3A+%22%22%2C%0A++%22classification%22+%3A+%22%22%2C%0A++%22longitude%22+%3A+%22120.139267%22%2C%0A++%22address%22+%3A+%22%22%2C%0A++%22country%22+%3A+%22%E4%B8%AD%E5%9B%BD%22%2C%0A++%22province%22+%3A+%22%E6%B5%99%E6%B1%9F%E7%9C%81%22%2C%0A++%22endTime%22+%3A+%22%22%2C%0A++%22memoStr%22+%3A+%22%22%2C%0A++%22description%22+%3A+%22%22%2C%0A++%22status%22+%3A+%220%22%2C%0A++%22outline%22+%3A+%22%22%0A%7D";
//		 test="%7B%0A%20%20%22addTimeClient%22%20%3A%20%221399730096%22%2C%0A%20%20%22level%22%20%3A%20%22%22%2C%0A%20%20%22city%22%20%3A%20%22%E6%9D%AD%E5%B7%9E%22%2C%0A%20%20%22textString%22%20%3A%20%22%22%2C%0A%20%20%22memo%22%20%3A%20%22%22%2C%0A%20%20%22userId%22%20%3A%20%224%22%2C%0A%20%20%22name%22%20%3A%20%22%22%2C%0A%20%20%22fromUser%22%20%3A%20%220%22%2C%0A%20%20%22type%22%20%3A%20%22location%22%2C%0A%20%20%22district%22%20%3A%20%22%22%2C%0A%20%20%22latitude%22%20%3A%20%220.000000%22%2C%0A%20%20%22target%22%20%3A%20%220%22%2C%0A%20%20%22operator%22%20%3A%20%220%22%2C%0A%20%20%22startTime%22%20%3A%20%22%22%2C%0A%20%20%22classification%22%20%3A%20%22%22%2C%0A%20%20%22longitude%22%20%3A%20%220.000000%22%2C%0A%20%20%22address%22%20%3A%20%22%22%2C%0A%20%20%22country%22%20%3A%20%22%E4%B8%AD%E5%9B%BD%22%2C%0A%20%20%22province%22%20%3A%20%22%E6%B5%99%E6%B1%9F%22%2C%0A%20%20%22endTime%22%20%3A%20%22%22%2C%0A%20%20%22memoStr%22%20%3A%20%22%22%2C%0A%20%20%22description%22%20%3A%20%22%22%2C%0A%20%20%22status%22%20%3A%20%220%22%2C%0A%20%20%22outline%22%20%3A%20%22%22%0A%7D";
//		 
//		 test+="cb75ab30cff9e09b24e6bda33e0c7b1f";
//		// test = 
//		// System.out.println(md5(URLEncodeUtils.decodeURL(test)));
//		 System.out.println(md5(URLEncodeUtils.decodeURL(URLEncodeUtils.decodeURL(test))));
//		 System.out.println(md5(test));
//		 
//		 
//		 test= URLEncodeUtils.encodeURL(test);
//		 System.out.println(md5(test));
//		 
//		 test= URLEncodeUtils.encodeURL(test);
//		 System.out.println(md5(test));
		 
		 System.out.println(MD5Util.encryption_32("xbb@2015"));
		 
	 }
}

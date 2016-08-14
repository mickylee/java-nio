package com.mickyli.java.nio.util;

import java.io.UnsupportedEncodingException;

/**
 * 按字节数截取字符串
 * 一个中文长度为2，如果从中间截取会出现乱码
 * @author liqian
 *
 */
public class SubStrUtils {

	/**
	 * 向前截取
	 * @param str
	 * @param subSLength
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String subStrForward(String str, int subSLength)    
            throws UnsupportedEncodingException{   
        if (str == null)    
            return "";    
        else{   
            int tempSubLength = subSLength;//截取字节数  
            String subStr = str.substring(0, str.length()<subSLength ? str.length() : subSLength);//截取的子串    
            int subStrByetsL = subStr.getBytes("GBK").length;//截取子串的字节长度   
            //int subStrByetsL = subStr.getBytes().length;//截取子串的字节长度   
            // 说明截取的字符串中包含有汉字    
            while (subStrByetsL > tempSubLength){    
                int subSLengthTemp = --subSLength;  
                subStr = str.substring(0, subSLengthTemp>str.length() ? str.length() : subSLengthTemp);    
                subStrByetsL = subStr.getBytes("GBK").length;  
                //subStrByetsL = subStr.getBytes().length;  
            }    
            return subStr;   
        }  
    }  
	/**
	 * 向后截取
	 * @param str
	 * @param start
	 * @param end
	 * @return
	 * @throws UnsupportedEncodingException
	 * 将字符编码GBK改为UTF-8，则每个中文长度按3个字符计算
	 */
	public static String subStrBackword(String str, int start, int end)  
            throws UnsupportedEncodingException{  
       
	     if (str == null)  return null;  
	     String chinese = "[\u0391-\uFFE5]";  
	     byte[] b = str.getBytes("UTF-8");  
	       
	     String temp = new String(b, start, end);  
	     String last = getLastStr(temp);  
	     while(!last.matches(chinese)){  
	         temp = new String(b, start, ++end);  
	         last = getLastStr(temp);  
	     }  
	
	     return new String(b, start, end);  
    }  


	public static String getLastStr(String temp) {
		return temp.substring(temp.length() - 1);
	}
	
	public static String getByteStr(String str, int start, int end) throws UnsupportedEncodingException{  
	     byte[] b = str.getBytes("UTF-8");  
	       
	     return new String(b, start, end);  
	 }  
	
}

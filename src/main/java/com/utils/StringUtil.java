package com.utils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��12��
 */
public class StringUtil {

	public static char cs[] = new char[36];
	static {
		int index = 0;
		for (char i = 'a'; i <= 'z'; i++) {
			cs[(int)index++] = i;
		}
		
		for (char i = '0'; i <= '9'; i++) {
			cs[(int)index++] = i;
		}
	}
	
	/**
	 * �ж�ԭ�ַ����Ƿ�û��  ������Ҳ��
	 */
	public static boolean isEmpty(String str) {
		return (str==null || 0 == str.trim().length());
	}
	
	/**
	 * �ж�ԭ�ַ����Ƿ���ֵ ������Ҳ��ûֵ
	 */
	public static boolean hasText(String str) {
		return str != null && 0 < str.trim().length();
	}
	
	/**
	 * ������ɳ���N����ĸ
	 */
	public static String randomChar(int n) {
		Random random = new Random();
		String str = "";
		for (int i = 0; i < n; i++) {
			char c = (char) ('a' + random.nextInt(26));
			str += c;
		}
		return str;
	}
	
	/**
	 * ������ɴ�������ĸ���ַ���
	 */
	public static String randomCharAndNumber(int n) {
		StringBuilder sb = new StringBuilder();
		
		Random random = new Random();
		
		for (int i = 0; i < n; i++) {
			sb.append(cs[random.nextInt(36)]);
		}
		
		return sb.toString();
	}
	
	/**
	 * ��ȡ�ļ����Ƶ���չ��
	 */
	public static String getFileSuffix(String fileName) {
		int indexOf = fileName.lastIndexOf('.');
		
		if(indexOf == -1) {
			return "";
		}
		
		return fileName.substring(fileName.lastIndexOf('.'));
	}
	
	/**
	 * �ж��Ƿ�Ϊ�ַ���
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+";
		return str.matches(reg);
	}
	
	/**
	 * ��֤����
	 */
	public static boolean isEmail(String str) {
		String reg = "^[0-9a-zA-Z]+@[0-9a-zA-Z]+\\.[a-z]{2,3}";
		return str.matches(reg);
	}
	
	public static boolean isEmail2(String str) {
		String regEx1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$"; 
        Pattern p = Pattern.compile(regEx1);
        Matcher m = p.matcher(str);
        return m.find();
	}
	
	/**
	 * У���ַ����Ƿ�Ϊ��ȷ�ĵ绰����
	 */
	public  static boolean isMobile(String mobile) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		boolean isMatch = m.matches();
		return isMatch;
	}
	
	/**
	 * ����Ҫ�ķ��Ž����滻
	 */
	public static String toHtml(String src) {
		String dst = src.replace("\r\n", "\n");
		
		dst = dst.replace("\n", "</p><p>");
		
		dst = "<p>" + dst + "</p>";
		
		dst = dst.replace("\r", "<br/>");
		
		return dst;
	}
}

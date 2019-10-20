package com.utils;

import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @作者：lzy
 * @时间：2019年10月12日
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
	
	public static void main(String[] args) {
		boolean empty = isEmpty("1");
		System.out.println(empty);
		boolean hasText = hasText("1");
		System.out.println(hasText);
		
		String randomChar = randomChar(10);
		System.out.println("随机生成的十个字母："+randomChar);
		
		String randomCharAndNumber = randomCharAndNumber(10);
		System.out.println("随机生成的字符串："+randomCharAndNumber);
		
		String fileSuffix = getFileSuffix("关机.vbs");
		System.out.println("文件后缀名："+fileSuffix);
		
		boolean number = isNumber("10.11541");
		System.out.println("是否是数字："+number);
		
		boolean email = isEmail("wasd@qq.com");
		System.out.println(email);
		
	}
	
	/**
	 * 百分比计算
	 *
	 */
	public static String percent(Integer num,Integer total) {
		// 创建一个数值格式化对象
		NumberFormat numberFormat = NumberFormat.getInstance();
		// 设置精确到小数点后0位   
		numberFormat.setMaximumFractionDigits(0);
		String result = numberFormat.format((float)num/(float)total*100);
		return result;
	}
	
	/**
	 * 判断原字符串是否为空  空引号也算
	 */
	public static boolean isEmpty(String str) {
		return (str==null || 0 == str.trim().length());
	}
	
	/**
	 * 判断原字符串是否有值 空引号也算没值
	 */
	public static boolean hasText(String str) {
		return str != null && 0 < str.trim().length();
	}
	
	/**
	 * 随机生成长度N的字母
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
	 * 随机生成带数字字母的字符串
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
	 * 获取文件名称的扩展名
	 */
	public static String getFileSuffix(String fileName) {
		int indexOf = fileName.lastIndexOf('.');
		
		if(indexOf == -1) {
			return "";
		}
		
		return fileName.substring(fileName.lastIndexOf('.'));
	}
	
	/**
	 * 判断是否为数字  包含小数
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+\\.?[0-9]*";
		return str.matches(reg);
	}
	
	/**
	 * 验证邮箱
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
	 * 校验字符串是否为正确的电话号码
	 */
	public  static boolean isMobile(String mobile) {
		String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobile);
		boolean isMatch = m.matches();
		return isMatch;
	}
	
	/**
	 * 将必要的符号进行替换
	 */
	public static String toHtml(String src) {
		String dst = src.replace("\r\n", "\n");
		
		dst = dst.replace("\n", "</p><p>");
		
		dst = "<p>" + dst + "</p>";
		
		dst = dst.replace("\r", "<br/>");
		
		return dst;
	}
	
}

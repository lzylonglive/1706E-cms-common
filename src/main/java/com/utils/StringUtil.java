package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
		return (str==null || "".equals(str.trim()));
	}
	
	/**
	 * 判断原字符串是否有值 空引号也算没值
	 */
	public static boolean hasText(String str) {
		return (str != null && "".equals(str.trim()));
	}
	
	/**
	 * 验证全为字母
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str) {
		String pattern = "^[a-zA-Z]+$";
		return str.matches(pattern);
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
	
	//获取n个随机中文字符串
	public static String getRandonCnStr(int n) {
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<n;i++) {
			sb.append(getOneCn());
		}
		return sb.toString();
		
	} 
	
	/**
	 * 随机获取一个中文字符
	 * @return
	 */
	private static String getOneCn(){
		
		String str = "";
        int hightPos; //
        int lowPos;
        Random random = new Random();

        hightPos = (176 + Math.abs(random.nextInt(39)));
        lowPos = (161 + Math.abs(random.nextInt(93)));

        byte[] b = new byte[2];
        b[0] = (Integer.valueOf(hightPos)).byteValue();
        b[1] = (Integer.valueOf(lowPos)).byteValue();

        try {
            str = new String(b, "GBK");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("错误");
        }

        return str;
	}
	
	/**
	 * 验证邮箱
	 */
	public static boolean isEmail(String str) {
		String reg = "^\\w+@\\w+\\.[a-zA-Z]{2,3}$";
		Pattern compile = Pattern.compile(reg);
		Matcher matcher = compile.matcher(str);
		return matcher.matches();
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
	
	/*
	* 方法功能：根据正则在字符串提取一段值，用于后面在url地址里提取ID值。
	* 例如在“http://news.cnstock.com/news,yw-201908-4413224.htm”把“4413224”提取出来。
	*/
	public static String getPlaceholderValue(String src, String regex){
		//TODO 实现代码
        Pattern pattern = Pattern.compile(regex);// 匹配的模式  
        Matcher m = pattern.matcher(src);  
        boolean find = m.find();
        if(find) {
        	String group = m.group(0);
        	 return group.substring(1,group.lastIndexOf('.'));
        }
        return "";
	}
	
	//判断Url地址
	public static boolean isUrl(String str) {
		 //转换为小写
       str = str.toLowerCase();
       String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https、http、ftp、rtsp、mms
               + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp的user@  
              + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP形式的URL- 例如：199.194.52.184  
                + "|" // 允许IP和DOMAIN（域名）
                + "([0-9a-z_!~*'()-]+\\.)*" // 域名- www.  
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // 二级域名  
               + "[a-z]{2,6})" // first level domain- .com or .museum  
               + "(:[0-9]{1,5})?" // 端口号最大为65535,5位数
               + "((/?)|" // a slash isn't required if there is no file name  
               + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
       return  str.matches(regex);
	}
	
	/*
	* 方法：生成唯一标签名，处理步骤：
	* 1、全部变成小写；
	* 2、清空两边的空格，把中间所有的空格替换成“-”；
	* 3、使用URLEncoder.encode()编码
	* 最后返回处理的结果。
	* 举例“Spring MVC”处理后为“spring-mvc”，“Spring Mvc”处理后也为“spring-mvc”
	*/
	public static String toUniqueTerm(String term) throws Exception{
	//TODO 实现代码
		term=term.toLowerCase();
		term=term.trim();
		term = term.replaceAll(" ", "-");
		return URLEncoder.encode(term,"UTF-8");
	}
	
}

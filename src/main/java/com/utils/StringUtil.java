package com.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.NumberFormat;
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
	
	public static void main(String[] args) {
		boolean empty = isEmpty("1");
		System.out.println(empty);
		boolean hasText = hasText("1");
		System.out.println(hasText);
		
		String randomChar = randomChar(10);
		System.out.println("������ɵ�ʮ����ĸ��"+randomChar);
		
		String randomCharAndNumber = randomCharAndNumber(10);
		System.out.println("������ɵ��ַ�����"+randomCharAndNumber);
		
		String fileSuffix = getFileSuffix("�ػ�.vbs");
		System.out.println("�ļ���׺����"+fileSuffix);
		
		boolean number = isNumber("10.11541");
		System.out.println("�Ƿ������֣�"+number);
		
		boolean email = isEmail("wasd@qq.com");
		System.out.println(email);
		
	}
	
	/**
	 * �ٷֱȼ���
	 *
	 */
	public static String percent(Integer num,Integer total) {
		// ����һ����ֵ��ʽ������
		NumberFormat numberFormat = NumberFormat.getInstance();
		// ���þ�ȷ��С�����0λ   
		numberFormat.setMaximumFractionDigits(0);
		String result = numberFormat.format((float)num/(float)total*100);
		return result;
	}
	
	/**
	 * �ж�ԭ�ַ����Ƿ�Ϊ��  ������Ҳ��
	 */
	public static boolean isEmpty(String str) {
		return (str==null || "".equals(str.trim()));
	}
	
	/**
	 * �ж�ԭ�ַ����Ƿ���ֵ ������Ҳ��ûֵ
	 */
	public static boolean hasText(String str) {
		return (str != null && "".equals(str.trim()));
	}
	
	/**
	 * ��֤ȫΪ��ĸ
	 * @param str
	 * @return
	 */
	public static boolean isLetter(String str) {
		String pattern = "^[a-zA-Z]+$";
		return str.matches(pattern);
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
	 * �ж��Ƿ�Ϊ����  ����С��
	 */
	public static boolean isNumber(String str) {
		String reg = "[0-9]+\\.?[0-9]*";
		return str.matches(reg);
	}
	
	//��ȡn����������ַ���
	public static String getRandonCnStr(int n) {
		
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<n;i++) {
			sb.append(getOneCn());
		}
		return sb.toString();
		
	} 
	
	/**
	 * �����ȡһ�������ַ�
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
            System.out.println("����");
        }

        return str;
	}
	
	/**
	 * ��֤����
	 */
	public static boolean isEmail(String str) {
		String reg = "^\\w+@\\w+\\.[a-zA-Z]{2,3}$";
		Pattern compile = Pattern.compile(reg);
		Matcher matcher = compile.matcher(str);
		return matcher.matches();
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
	
	/*
	* �������ܣ������������ַ�����ȡһ��ֵ�����ں�����url��ַ����ȡIDֵ��
	* �����ڡ�http://news.cnstock.com/news,yw-201908-4413224.htm���ѡ�4413224����ȡ������
	*/
	public static String getPlaceholderValue(String src, String regex){
		//TODO ʵ�ִ���
        Pattern pattern = Pattern.compile(regex);// ƥ���ģʽ  
        Matcher m = pattern.matcher(src);  
        boolean find = m.find();
        if(find) {
        	String group = m.group(0);
        	 return group.substring(1,group.lastIndexOf('.'));
        }
        return "";
	}
	
	//�ж�Url��ַ
	public static boolean isUrl(String str) {
		 //ת��ΪСд
       str = str.toLowerCase();
       String regex = "^((https|http|ftp|rtsp|mms)?://)"  //https��http��ftp��rtsp��mms
               + "?(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?" //ftp��user@  
              + "(([0-9]{1,3}\\.){3}[0-9]{1,3}" // IP��ʽ��URL- ���磺199.194.52.184  
                + "|" // ����IP��DOMAIN��������
                + "([0-9a-z_!~*'()-]+\\.)*" // ����- www.  
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\." // ��������  
               + "[a-z]{2,6})" // first level domain- .com or .museum  
               + "(:[0-9]{1,5})?" // �˿ں����Ϊ65535,5λ��
               + "((/?)|" // a slash isn't required if there is no file name  
               + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";  
       return  str.matches(regex);
	}
	
	/*
	* ����������Ψһ��ǩ���������裺
	* 1��ȫ�����Сд��
	* 2��������ߵĿո񣬰��м����еĿո��滻�ɡ�-����
	* 3��ʹ��URLEncoder.encode()����
	* ��󷵻ش���Ľ����
	* ������Spring MVC�������Ϊ��spring-mvc������Spring Mvc�������ҲΪ��spring-mvc��
	*/
	public static String toUniqueTerm(String term) throws Exception{
	//TODO ʵ�ִ���
		term=term.toLowerCase();
		term=term.trim();
		term = term.replaceAll(" ", "-");
		return URLEncoder.encode(term,"UTF-8");
	}
	
}

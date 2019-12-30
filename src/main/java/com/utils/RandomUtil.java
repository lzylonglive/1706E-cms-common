package com.utils;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

public class RandomUtil {

	// 方法1：返回min-max之间的随机整数（包含min和max值），例如返回1-3之间的随机数，那么返回1或2或3都是正确的，返回4就不对。 (5分)
		public static int random(int min, int max) {
			// 实例化随机数工具类
			Random r = new Random();
			int i = r.nextInt(max - min + 1) + min;
			return i;
		}

		// 方法2：在最小值min与最大值max之间截取subs个不重复的随机数。例如在1-10之间取3个不重复的随机数，那么[2,6,9]是对的，[3,5,5]则不对，因为5重复了。应用场景：在100篇文章中随机取10篇文章，月考可能会使用到。
		public static int[] subRandom(int min, int max, int subs) {
			// 存放随机数字.用来过滤重复数据
			HashSet<Integer> set = new HashSet<Integer>();

			// 声明目标数组.用来存放随机数
			int[] dest = new int[subs];
			
			//向set集合中添加随机数据,
			while (set.size() != subs) {
				int x = random(min, max);
				set.add(x);
			}
			//遍历set集合,存入目标数组
			int y = 0;
			for (int value : set) {
				dest[y] = value;
				y++;
			}

			return dest;
		}


		// 方法3：返回1个1-9,a-Z之间的随机字符。 (8分)
		public static char randomCharacter() {
			String str="123456789qwrtyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
			
			return str.charAt(random(0, str.length() -1));
		}

		
		// 方法4：返回参数length个字符串(5分)，方法内部要调用randomCharacter()方法 (4分)
		public static String randomString(int length) {
			String str="";
			for(int i =0;i<length; i++) {
				str += randomCharacter();
			}
			
			return str;
		}
		
		//给定时间 随机日期(字符串参数)
		public static Date randomDate(String stratDate,String endDate) {
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
			long date = 0L;
			try {
				Date d1 = st.parse(stratDate);
				Date d2 = st.parse(endDate);
				date = (long) (Math.random() * (d2.getTime() - d1.getTime() + 1) +d1.getTime());
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return new Date(date);
			
		}
		
		//给定时间 随机日期(日期参数)
		public static Date randomDate(Date stratDate,Date endDate) {
			SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
			String s1 = st.format(stratDate);
			String s2 = st.format(endDate);
			return randomDate(s1, s2);
		}
		
		
		//随机13开头的电话号
		public static String random13Phone() {
			long i = (long)(Math.ceil((Math.random()*(999999999-100000000.0) + 1)+100000000));
			String phone = i+"";
			return "13" + i;
		}
		
		/**
		 * 生成随机汉字
		 * @param num	数量
		 * @return
		 * @throws UnsupportedEncodingException 
		 */
		public static String randomChinese(int num) throws UnsupportedEncodingException {
			String str = "";
			Random random = new Random();
			
			for (int i = 0; i < num; i++) {
				int hightPos, lowPos; // 定义高低位
				hightPos = (176 + Math.abs(random.nextInt(39)));//获取高位值
				lowPos = (161 + Math.abs(random.nextInt(93)));//获取低位值
				
				byte[] b = new byte[2];
				b[0] = (new Integer(hightPos).byteValue());
				b[1] = (new Integer(lowPos).byteValue());
				
				str += new String(b, "GBk");//转成中文
			}

			return str;
		}
		
		
		/**
		 * 生成随机邮箱
		 * @param min	最小长度
		 * @param max	最大长度
		 * @return
		 */
		public static String randomEmail(int min, int max) {
			String prefix = "";

			String[] suffix = {"@qq.com", "@163.com", "@sian.com", "@gmail.com",
					"@sohu.com", "@hotmail.com", "@foxmail.com"};
			String str = "";
			
			int random = RandomUtil.random(min, max);
			
			for (int i = 0; i < max; i++) {
				if (i == random) {
					break;
				}
				str += randomCharacter();
			}
			
			int suffixStr = RandomUtil.random(0, suffix.length - 1);
			str += suffix[suffixStr];
					
			return str;
		}
	
}

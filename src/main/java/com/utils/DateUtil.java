package com.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��12��
 */
public class DateUtil {
	
	public static void main(String[] args) {
		Date date = new Date(101,5,10);
		int calAge = calAge(date);
		System.out.println(calAge);
		boolean today = isToday(date);
		System.out.println(today);
		boolean thisYear = isThisYear(date);
		System.out.println(thisYear);
		Date monthStart = getMonthStart(date);
		System.out.println(monthStart);
		Date monthEnd = getMonthEnd(date);
		System.out.println(monthEnd);
	}
	
	/**
	 * 
	 * @param birthday
	 * @return
	 */
	public static  int calAge(Date birthday) {
		 
		Calendar cal = Calendar.getInstance();  
		cal.setTime(birthday);
		//��ȡ����
		int birthYear = cal.get(Calendar.YEAR);
		int birthMonth = cal.get(Calendar.MONTH);
		int birthDate = cal.get(Calendar.DAY_OF_MONTH);
		
		// ��ȡ���죙
		cal.setTime(new Date());
		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int date = cal.get(Calendar.DAY_OF_MONTH);
		
		int age = year-birthYear;
		if(month<birthMonth) {
			age--;
		}else if (month==birthMonth){
			if(date<birthDate) {
				age--;
			}
		}
		return age;
	}
	
	/**
	 * ���ǲ��ǽ���
	 * @param date
	 * @return
	 */
	public static boolean isToday(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		String dateStr = sdf.format(date);
		String todayStr = sdf.format(new Date());
		return dateStr.equals(todayStr);
		
	}
	
	
	/**
	 * ���ǲ��ǽ���
	 * @param date
	 * @return
	 */
	public static boolean isThisYear(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
		
		String dateStr = sdf.format(date);
		String todayStr = sdf.format(new Date());
		
		return dateStr.equals(todayStr);
	}
	
	
	/** 
	 *  
	 * ���³�  ��ʱ����0.0.0
	 * @return
	 */
	public static Date getMonthStart(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.AM_PM,Calendar.AM);
		cal.set(Calendar.DAY_OF_MONTH,1);
		
		return cal.getTime();
	}
	
	/**
	 * ����ĩ
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.AM_PM,Calendar.AM);
		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		
		return cal.getTime();
	}
	
}

package com.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��16��
 */
public class MD5Util {

	/**
	 * ����
	 */
	private static String salString = "mymdfive";
	
	/**
	 * ��������м���
	 */
	public static String md5(String pwd) {
		return DigestUtils.md5Hex( pwd + salString );
	}
	
	/**
	 * ��������м���
	 * @param pwd
	 * @param salt
	 */
	public static String md5(String pwd,String salt) {
		return DigestUtils.md5Hex( pwd + salt );
	}
	
}

package com.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * @作者：lzy
 * @时间：2019年10月16日
 */
public class MD5Util {

	/**
	 * 加盐
	 */
	private static String salString = "mymdfive";
	
	/**
	 * 对密码进行加密
	 */
	public static String md5(String pwd) {
		return DigestUtils.md5Hex( pwd + salString );
	}
	
	/**
	 * 对密码进行加密
	 * @param pwd
	 * @param salt
	 */
	public static String md5(String pwd,String salt) {
		return DigestUtils.md5Hex( pwd + salt );
	}
	
}

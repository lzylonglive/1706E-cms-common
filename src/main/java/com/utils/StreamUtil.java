package com.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @作者：lzy
 * @时间：2019年10月12日
 */
public class StreamUtil {

	/**
	 * 关闭所有流
	 */
	public static void closeStream(Closeable ... streams) throws IOException {
		for (int i = 0; i < streams.length; i++) {
			streams[i].close();
		}
	}
}

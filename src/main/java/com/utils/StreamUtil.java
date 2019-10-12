package com.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��12��
 */
public class StreamUtil {

	/**
	 * �ر�������
	 */
	public static void closeStream(Closeable ... streams) throws IOException {
		for (int i = 0; i < streams.length; i++) {
			streams[i].close();
		}
	}
}

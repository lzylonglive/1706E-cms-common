package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��12��
 */
public class FileUtil {
	
	/**
	 * �ݹ�ɾ���ļ�
	 */
	public static void del(String path) {
		File file = new File(path);
		//����ļ�·���������˳�
		if(!file.exists()) {
			System.out.println("�����ڸ�·����"+path);
			return;
		}
		//����ļ�������ɾ��
		if(file.exists()) {
			System.out.println("ɾ���ļ���"+path);
			file.delete();
		}
		//������ļ��� ��ݹ��Լ������Լ�
		if(file.isDirectory()) {
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				String subFileName = path + "\\" + list[i];
				del(subFileName);
			}
			System.out.println("ɾ��Ŀ¼��"+path);
			file.delete();
		}
	}
	
	/**
	 * �ļ�����
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static void copy(String src,String dst) throws IOException {
		File fileSrc = new File(src);
		
		if(!fileSrc.exists() || !fileSrc.isFile()) {
			System.out.println("�ļ������ڣ����ܸ���");
			return;
		}
		
		File fileDst = new File(dst);
		
		if(fileDst.exists()) {
			System.out.println("�ļ��Ѵ��ڣ����ܸ���");
		}
		
		FileInputStream fis = new FileInputStream(fileSrc);
		FileOutputStream fos = new FileOutputStream(fileDst);
		
		byte be[] = new byte[1024];
		
		while(fis.read(be) != -1) {
			fos.write(be);
		}
		
		StreamUtil.closeStream(fis,fos);
	}
	
	/**
	 * һ��һ�ж�
	 */
	@SuppressWarnings("resource")
	public static String readFileByLine(String fileName) throws IOException {
		StringBuilder sb = new StringBuilder();
		
		File file = new File(fileName);
		
		FileInputStream fis = new FileInputStream(file);
		
		InputStreamReader reader = new InputStreamReader(fis);
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String str = null;
		
		while((str = bufferedReader.readLine()) != null) {
			sb.append(str).append("\r\n");//׷�ӻ���
		}
		
		StreamUtil.closeStream(fis);
		//����
		return sb.toString();
	}
	
	/**
	 * һ��һ�ж�  �ü��Ϸ�װ
	 */
	@SuppressWarnings("resource")
	public static List<String> readFile(String fileName) throws IOException {
		ArrayList<String> list = new ArrayList<>();
		
		File file = new File(fileName);
		
		FileInputStream fis = new FileInputStream(file);
		
		InputStreamReader reader = new InputStreamReader(fis,"utf-8");
		
		BufferedReader bufferedReader = new BufferedReader(reader);
		
		String str = null;
		
		while((str = bufferedReader.readLine()) != null) {
			list.add(str);
		}
		//����
		StreamUtil.closeStream(fis);
		
		return list;
	}
	
}

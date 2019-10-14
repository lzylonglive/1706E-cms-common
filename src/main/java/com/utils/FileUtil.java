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
 * @作者：lzy
 * @时间：2019年10月12日
 */
public class FileUtil {
	
	/**
	 * 递归删除文件
	 */
	public static void del(String path) {
		File file = new File(path);
		//如果文件路径不存在退出
		if(!file.exists()) {
			System.out.println("不存在该路径："+path);
			return;
		}
		//如果文件存在则删除
		if(file.exists()) {
			System.out.println("删除文件："+path);
			file.delete();
		}
		//如果是文件夹 则递归自己调用自己
		if(file.isDirectory()) {
			String[] list = file.list();
			for (int i = 0; i < list.length; i++) {
				String subFileName = path + "\\" + list[i];
				del(subFileName);
			}
			System.out.println("删除目录："+path);
			file.delete();
		}
	}
	
	/**
	 * 文件复制
	 * @throws IOException 
	 */
	@SuppressWarnings("resource")
	public static void copy(String src,String dst) throws IOException {
		File fileSrc = new File(src);
		
		if(!fileSrc.exists() || !fileSrc.isFile()) {
			System.out.println("文件不存在，不能复制");
			return;
		}
		
		File fileDst = new File(dst);
		
		if(fileDst.exists()) {
			System.out.println("文件已存在，不能复制");
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
	 * 一行一行读
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
			sb.append(str).append("\r\n");//追加换行
		}
		
		StreamUtil.closeStream(fis);
		//关流
		return sb.toString();
	}
	
	/**
	 * 一行一行读  用集合封装
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
		//关流
		StreamUtil.closeStream(fis);
		
		return list;
	}
	
}

package com.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import com.sun.javafx.geom.transform.GeneralTransform3D;

/**
 * @���ߣ�lzy
 * @ʱ�䣺2019��10��15��
 */
public class MyUtil {
	
	@Test
	public void map() {
		
		String str = "* Because TreeNodes are about twice the size of regular nodes, we\r\n" + 
				"     * use them only when bins contain enough nodes to warrant use\r\n" + 
				"     * (see TREEIFY_THRESHOLD). And when they become too small (due to\r\n" + 
				"     * removal or resizing) they are converted back to plain bins.  In\r\n" + 
				"     * usages with well-distributed user hashCodes, tree bins are\r\n" + 
				"     * rarely used.  Ideally, under random hashCodes, the frequency of\r\n" + 
				"     * nodes in bins follows a Poisson distribution\r\n" + 
				"     * (http://en.wikipedia.org/wiki/Poisson_distribution) with a\r\n" + 
				"     * parameter of about 0.5 on average for the default resizing\r\n" + 
				"     * threshold of 0.75, although with a large variance because of\r\n" + 
				"     * resizing granularity. Ignoring variance, the expected\r\n" + 
				"     * occurrences of list size k are (exp(-0.5) * pow(0.5, k) /\r\n" + 
				"     * factorial(k)). The first values are";
		
		//��ĸ���ִ���
		HashMap<String, Integer> map = new HashMap<>();
		
		for (int i = 0; i < str.length(); i++) {
			
			String strr = String.valueOf(str.charAt(i));
			
			Integer in = map.get(strr);
			
			if(in == null) {
				map.put(strr, 1);
			}else {
				map.put(strr, ++in);
			}
			
		}
		
		//��һ�ֱ���
		map.forEach((key,value)->{
			System.out.println("Key is "+key+",���ִ����ǣ�"+value+"  --��ĸ");
		});
		
		System.out.println("--------------------------------------------------------");
		
		//�ڶ��ֱ���
		Set<Entry<String, Integer>> entrySet = map.entrySet();
		
		for (Entry<String, Integer> entry : entrySet) {
			System.out.println("Key is "+entry.getKey()+",���ִ����ǣ�"+entry.getValue()+"  --��ĸ��");
		}
		
		System.out.println("========================================================");
		
		//���ʳ��ִ����Լ����ִ����������εĵ���
		Set<String> keySet = map.keySet();
		
		for (String key : keySet) {
			System.out.println("Key is "+key+",���ִ����ǣ�"+map.get(key));
		}
		
		String[] spl = str.split(" ");
		
		HashMap<String, Integer> m = new HashMap<>();
		
		for (String strs : spl) {
			
			Integer in = m.get(strs);
			
			if(in == null) {
				m.put(strs, 1);
			}else {
				m.put(strs, ++in);
			}
			
		}
		HashSet<String> hashSet = new HashSet<>();
		
		m.forEach((key,value)->{
			if(value > 3) {
				hashSet.add(key);
			}
		});
		
		hashSet.forEach(x->{
			System.out.println("Key is "+x+"  --����");
		});
		
	}
	
}

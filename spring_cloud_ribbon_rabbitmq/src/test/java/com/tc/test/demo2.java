package com.tc.test;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class demo2 {
	@Test
	public void main() throws Exception {

		FileWriter fileWriter = new FileWriter("d:\\id.txt");

		for (int i = 1; i < 40000; i++) {
			fileWriter.write("i am " + i + ",");
		}
		fileWriter.close();
	}

	@Test
	public void main2() throws Exception {
//		String[] split = getData().split(",");
//		List<String> list =  Arrays.asList(split);
		
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= 11; i++) {
			list.add(i);
		}
		int duan = 5;
		int count = list.size();
		int m = count % duan; // 求余数
		int pageCount = count / duan;
		int pointer = 0;

		for (int i = 1; i <= duan; i++) {
			int online = 0;
			if ((i == duan) && m > 0) {
				online = pointer + pageCount + m;
			} else {
				online = pointer + pageCount;
			}
			for (; pointer < online; pointer++) {
				System.out.println("第" + i + "页:" + list.get(pointer));
			}
		}

	}

	private String getData() {
		try {
			List<String> strings = Files.readAllLines(Paths.get("d:\\id.txt"));
			return strings.get(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}

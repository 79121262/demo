package com.tc.test.resign.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cai.tian on 2017/8/31.
 */
public class ResignUtils {
    private final Logger logger = LoggerFactory.getLogger(ResignUtils.class);
    private static final ConcurrentHashMap<String, String> db = new ConcurrentHashMap<>();

    public static List<Queue<String>> getQueues(Integer duan) {
        if (duan == null || duan == 0) return null;

        String[] split = getData().split(",");
        List<String> list = Arrays.asList(split);
        List<Queue<String>> queues = new ArrayList<>();
        Queue<String> queue = null;
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
            queue = new LinkedList<>();
            for (; pointer < online; pointer++) {
                queue.offer(String.valueOf(list.get(pointer)));
            }
            if (!queue.isEmpty())
                queues.add(queue);
        }
        return queues;
    }

    public static String getData() {
        try {
        	List<String> strings = Files.readAllLines(Paths.get("d:\\id.txt"));
            return strings.get(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void to() throws Exception {
        FileWriter fileWriter = new FileWriter("d:\\resault.txt");
        fileWriter.write(db.toString());
        fileWriter.close();
    }

    public static boolean count(String status) throws Exception {
        return db.containsValue(status);
    }

    public static void insertDb(String a, String status) throws Exception {
        db.put(a, status);
    }

    public static void updateDb(String a, String status) throws Exception {
        if (db.containsKey(a)) {
            db.put(a, status);
        }
    }
}

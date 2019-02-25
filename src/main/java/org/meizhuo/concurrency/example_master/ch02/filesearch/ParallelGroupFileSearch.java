package org.meizhuo.concurrency.example_master.ch02.filesearch;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @ProjectName: ConcurrencyTest
 * @Package: org.meizhuo.concurrency.example_master.ch02.filesearch
 * @ClassName: ${TYPE_NAME}
 * @Description:
 * @Author: Gangan
 * @CreateDate: 2019/2/25 13:39
 * @UpdateUser:
 * @UpdateDate: 2019/2/25 13:39
 * @UpdateRemark: The modified content
 * @Version: 1.0
 * <p>Copyright: Copyright (c) 2019</p>
 */
public class ParallelGroupFileSearch {

    public static void searchFile(File file, String fileName, Result parallelResult) {
        ConcurrentLinkedQueue<File> dir = new ConcurrentLinkedQueue<>();

        File[] files = file.listFiles();
        //将指定file下的所有目录添加进dir中
        for (File file1 : files) {
            if (file1.isDirectory()) {
                dir.add(file1);
            }
        }

        int processors = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[processors];
        ParallelGroupFileTask[] tasks = new ParallelGroupFileTask[processors];

        //创建线程并启动
        for (int i = 0; i < processors; i++) {
            tasks[i] = new ParallelGroupFileTask(fileName, dir,parallelResult);
            threads[i] = new Thread(tasks[i]);
            threads[i].start();
        }

        boolean finish = false;
        int numFinished = 0;

        while (!finish) {
            numFinished = 0;
            for (int i = 0; i < threads.length; i++) {
                if (threads[i].getState() == Thread.State.TERMINATED) {
                    numFinished++;
                    if (tasks[i].getFound()) {
                        finish = true;
                    }
                }
            }
            if (numFinished == threads.length) {
                finish=true;
            }
        }
        //表明已完成搜索
        if (numFinished != threads.length) {
            for (Thread thread : threads) {
                thread.interrupt();
            }
        }

    }

}
